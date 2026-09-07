package com.poc_ycyw.ycyw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

    Integer id;
    String content;
    Timestamp dateEnvoi;
    Integer senderId;
    String senderName;
    String senderEmail;
    Integer destinataire;
}
