package com.example.Pill_Mate_Backend.domain.oauth2.jwt;

import com.example.Pill_Mate_Backend.CommonEntity.Users;
import com.example.Pill_Mate_Backend.domain.mypage.repository.UsersRepository;
import com.example.Pill_Mate_Backend.domain.oauth2.service.JwtService;
import com.example.Pill_Mate_Backend.global.common.code.ErrorReasonDTO;
import com.example.Pill_Mate_Backend.global.common.code.status.ErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService, UsersRepository usersRepository) {
        this.jwtService = jwtService;
        this.usersRepository = usersRepository;
    }

    private UsersRepository usersRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        //api 경로에 대한 요청은 인증을 요구하지 않음
        if (requestURI.startsWith("/api/v1/auth/reissue")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authorizationHeader);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if (jwtService.validateToken(token)) {
                String email = jwtService.extractEmail(token);
                System.out.println("Extracted Email: " + email);

                Optional<Users> optionalUser = usersRepository.findByEmail(email);
                if(!optionalUser.isPresent()){//db 삭제시
                    System.out.println("DB에 사용자가 없음: "+email);
                    //throw new GeneralException(ErrorStatus._USER_NOT_IN_DB);

                    ErrorReasonDTO error = ErrorStatus._USER_NOT_IN_DB.getReasonHttpStatus();
                    response.setStatus(error.httpStatus().value());
                    response.setContentType("application/json;charset=UTF-8");

                    ObjectMapper objectMapper = new ObjectMapper();
                    response.getWriter().write(objectMapper.writeValueAsString(error));
                    return;
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else {
                //System.out.println("Invalid JWT Token");
                //throw new GeneralException(ErrorStatus._EXPIRED_JWT_TOKEN);
                System.out.println("Invalid JWT Token");

                ErrorReasonDTO error = ErrorStatus._EXPIRED_JWT_TOKEN.getReasonHttpStatus();
                response.setStatus(error.httpStatus().value());
                response.setContentType("application/json;charset=UTF-8");

                ObjectMapper objectMapper = new ObjectMapper();
                response.getWriter().write(objectMapper.writeValueAsString(error));
                return;
            }
        } else {
            System.out.println("Authorization header is missing or invalid");
        }
        filterChain.doFilter(request, response);
    }
}
