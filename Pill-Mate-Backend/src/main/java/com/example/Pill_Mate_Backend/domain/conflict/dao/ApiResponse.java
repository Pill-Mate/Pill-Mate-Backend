package com.example.Pill_Mate_Backend.domain.conflict.dao;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ApiResponse {

    private Header header;
    private Body body;

    // Getters and Setters

    public static class Header {
        @JsonProperty("resultCode")
        private String resultCode;

        @JsonProperty("resultMsg")
        private String resultMsg;

        // Getters and Setters
    }

    public static class Body {
        @JsonProperty("pageNo")
        private int pageNo;

        @JsonProperty("totalCount")
        private int totalCount;

        @JsonProperty("numOfRows")
        private int numOfRows;

        @JsonProperty("items")
        private List<Item> items;

        // Getters and Setters
    }

    public static class Item {
        @JsonProperty("DUR_SEQ")
        private String durSeq;

        @JsonProperty("TYPE_CODE")
        private String typeCode;

        @JsonProperty("TYPE_NAME")
        private String typeName;

        @JsonProperty("MIX")
        private String mix;

        @JsonProperty("INGR_CODE")
        private String ingrCode;

        @JsonProperty("INGR_KOR_NAME")
        private String ingrKorName;

        @JsonProperty("ITEM_NAME")
        private String itemName;

        // Other fields...

        @JsonProperty("PROHBT_CONTENT")
        private String prohbtContent;

        @JsonProperty("ITEM_PERMIT_DATE")
        private String itemPermitDate;

        // Add all fields in your JSON structure with @JsonProperty annotation
        // Getters and Setters
    }
}

