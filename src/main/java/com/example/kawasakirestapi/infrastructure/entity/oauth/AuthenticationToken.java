package com.example.kawasakirestapi.infrastructure.entity.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * トークンテーブルのエンティティ
 *
 * @author kawasakiryosuke
 */
@Entity
@Table(name = "token")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationToken implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private Long userId;

    @Column
    private String userName;

    @Column
    private String authToken;

    @Column
    private LocalDateTime deadLine = LocalDateTime.now().plusMinutes(30);

    @Column
    private LocalDateTime createAt;

    public boolean isExpired() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(getDeadLine());
    }

    @PrePersist
    public void setCreateAt() {
        createAt = LocalDateTime.now();
    }

}
