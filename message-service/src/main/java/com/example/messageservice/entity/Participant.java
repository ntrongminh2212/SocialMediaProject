package com.example.messageservice.entity;

import com.example.messageservice.id.ParticipantId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Participant {
    @EmbeddedId
    private ParticipantId participantId;
    private String nickName;
    @CreationTimestamp
    private Date joinTime;
    @UpdateTimestamp
    private Date updateTime;
}
