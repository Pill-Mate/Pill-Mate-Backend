package com.example.Pill_Mate_Backend.global.common.exception;

import com.example.Pill_Mate_Backend.global.common.code.BaseErrorCode;
import com.example.Pill_Mate_Backend.global.common.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public GeneralException(BaseErrorCode code) {
        super(code.getMessage()); // ✅ 이 한 줄이 로그에 메시지를 출력하게 해줍니다!
        this.code = code;
    }
    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }

}