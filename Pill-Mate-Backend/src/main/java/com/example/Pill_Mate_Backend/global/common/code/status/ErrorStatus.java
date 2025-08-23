package com.example.Pill_Mate_Backend.global.common.code.status;


import com.example.Pill_Mate_Backend.global.common.code.BaseErrorCode;
import com.example.Pill_Mate_Backend.global.common.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 기본 에러
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),


    // User 에러
    _NOT_FOUND_USER(HttpStatus.NOT_FOUND, "USER400", "사용자가 존재하지 않습니다."),
    _USER_NOT_IN_DB(HttpStatus.UNAUTHORIZED, "USERNOTINDB401", "DB에 사용자가 없습니다"),
    //jwt 만료 에러
    _EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWTEXPIRED401", "만료된 JWT 토큰입니다."), //HttpStatus.UNAUTHORIZED -> 401에러
    GOOGLE_REQUEST_TOKEN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GOOGLE_REQUEST_TOKEN_ERROR", "Failed to process Google request token"),
    _EXPIRED_REFRESH_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "REFRESHEXPIRED401", "만료된 REFRESH JWT 토큰입니다."),

    //medicine 에러
    _MEDICINE_NOT_FOUND(HttpStatus.NOT_FOUND, "MEDICINE400", "약물이 존재하지 않습니다."),
    //users요소 불충분 에러
    _USERS_ELEMENT_LACK(HttpStatus.BAD_REQUEST, "USERSLACK400", "USERS 생성을 위한 요소가 불충분합니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder().message(message).code(code).isSuccess(false).build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}