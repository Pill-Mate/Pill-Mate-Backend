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

    // Scrap 에러
    _NOT_FOUND_SCRAP(HttpStatus.NOT_FOUND, "SCRAP400", "스크랩이 존재하지 않습니다."),

    // User 에러
    _NOT_FOUND_USER(HttpStatus.NOT_FOUND, "USER400", "사용자가 존재하지 않습니다."),

    // News API 에러
    _NOT_FOUND_NEWS(HttpStatus.NOT_FOUND, "NEWS400", "검색된 뉴스가 존재하지 않습니다."),
    _UNAVAILABLE_NEWS_API(HttpStatus.SERVICE_UNAVAILABLE, "NEWS503", "Naver API 미작동, 관리자에게 문의 바랍니다.");

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