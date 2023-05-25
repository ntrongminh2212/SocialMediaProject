package com.example.friendservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InviteFormDTO {
    private Long user_id;
    private List<ParticipantDTO> lstParticipantDTO;
}
