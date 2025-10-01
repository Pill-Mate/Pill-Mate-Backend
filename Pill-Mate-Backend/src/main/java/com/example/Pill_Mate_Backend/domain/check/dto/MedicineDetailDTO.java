package com.example.Pill_Mate_Backend.domain.check.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
public interface MedicineDetailDTO {
    String getMedicineName();    // item_name
    String getMedicineImage();   // item_image
    String getClassName();       // class_name
    String getUserMethod();      // use_method_qesitm
    String getEfficacy();        // efcy_qesitm
    String getCaution();         // type_name
    String getSideEffect();      // atpn_qesitm
    String getStorage();         // deposit_method
    String getEntpName();        // entp_name
}