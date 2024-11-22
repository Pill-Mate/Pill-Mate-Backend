package com.example.Pill_Mate_Backend.global.common.exception.handler;

import com.example.Pill_Mate_Backend.global.common.code.BaseErrorCode;
import com.example.Pill_Mate_Backend.global.common.exception.GeneralException;

public class UserHandler extends GeneralException {
    public UserHandler(BaseErrorCode code) {
        super(code);
    }
}
