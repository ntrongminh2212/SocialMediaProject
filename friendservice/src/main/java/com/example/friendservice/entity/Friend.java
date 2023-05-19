package com.example.friendservice.entity;

import com.example.friendservice.idkey.FriendId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Friend {
    @EmbeddedId
    private FriendId friendId;
    @CreationTimestamp
    private Date sentDate;
    @Column(columnDefinition = "boolean default false")
    private boolean isAccepted = false;
    @Column(columnDefinition = "boolean default false")
    private boolean isBlock = false;
}
























