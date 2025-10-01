package com.example.Pill_Mate_Backend.domain.oauth2.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
public class AppleClientSecretProvider {
    private final AppleProps props;
    public AppleClientSecretProvider(AppleProps props) { this.props = props; }

    public String issueClientSecret(long validDays) {
        try {
            ECPrivateKey key = loadEcPrivateKeyFromPath(props.keyPath());
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(props.keyId()).type(JOSEObjectType.JWT).build();
            Instant now = Instant.now();
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .issuer(props.teamId())
                    .subject(props.clientId())
                    .audience("https://appleid.apple.com")
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(now.plusSeconds(validDays * 86400)))
                    .build();
            SignedJWT signed = new SignedJWT(header, claims);
            signed.sign(new ECDSASigner(key));
            return signed.serialize();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private ECPrivateKey loadEcPrivateKeyFromPath(String path) throws Exception {
        if (path == null || path.isBlank()) throw new IllegalStateException("Apple p8 path is empty");
        if (path.startsWith("~")) path = System.getProperty("user.home") + path.substring(1);
        String pem = Files.readString(Path.of(path));
        String c = pem.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replaceAll("\\s", "");
        byte[] der = Base64.getDecoder().decode(c);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(der);
        return (ECPrivateKey) KeyFactory.getInstance("EC").generatePrivate(spec);
    }
}