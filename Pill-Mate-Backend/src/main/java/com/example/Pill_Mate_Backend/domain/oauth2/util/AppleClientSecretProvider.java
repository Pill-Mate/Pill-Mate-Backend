package com.example.Pill_Mate_Backend.domain.oauth2.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

public class AppleClientSecretProvider {
    private final AppleProps props;
    public AppleClientSecretProvider(AppleProps props) { this.props = props; }

    public String issueClientSecret(long validDays) {
        try {
            RSAPrivateKey key = loadPrivateKey(props.privateKeyPem());
            var header = new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(props.keyId()).type(JOSEObjectType.JWT).build();
            var now = Instant.now();
            var claims = new JWTClaimsSet.Builder()
                    .issuer(props.teamId())
                    .subject(props.clientId())
                    .audience("https://appleid.apple.com")
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(now.plusSeconds(validDays * 86400)))
                    .build();
            var signed = new SignedJWT(header, claims);
            signed.sign(new RSASSASigner(key));
            return signed.serialize();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private RSAPrivateKey loadPrivateKey(String pem) throws Exception {
        String c = pem.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replaceAll("\\s", "");
        byte[] der = Base64.getDecoder().decode(c);
        var spec = new PKCS8EncodedKeySpec(der);
        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
    }
}