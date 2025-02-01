package com.example.Pill_Mate_Backend.domain.conflict.dto;

public record PhoneAddresses(
        String pharmacyName,
        String pharmacyAddress,
        String pharmacyPhoneNumber,

        String hospitalName,
        String hospitalAddress,
        String hospitalPhoneNumber
) {
}
