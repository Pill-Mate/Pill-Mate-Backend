package com.example.Pill_Mate_Backend.register.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDTO<T> {   //java Generic이용 -> 어떤 원소타입의 리스트도 반환할 수 있도록함
    private String error;
    private List<T> data;
}
