package com.example.friendservice.idkey;

import com.example.friendservice.entity.User;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class FriendId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "source_id")
    private User sourceId;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private User targetId;
}
