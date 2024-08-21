package com.example.Pill_Mate_Backend.domain.sets;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
@Converter
public class StringSetConverter implements AttributeConverter<Set<String>, String> {

    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(Set<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return attribute.stream().collect(Collectors.joining(SPLIT_CHAR));
    }

    @Override
    public Set<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(Arrays.asList(dbData.split(SPLIT_CHAR)));
    }

}
