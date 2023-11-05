package com.rahim.userservice.model;

import com.rahim.userservice.listener.AuditLogEntityListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditLogEntityListener.class)
@Table(name = "audit_log", schema = "rgts")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", nullable = false)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "action", length = 10)
    private String action;

    @Column(name = "change_timestamp", columnDefinition = "TIMESTAMPTZ(0)")
    private OffsetDateTime changeTimestamp;

    @Column(name = "old_data", length = 500)
    private String oldData;

    @Column(name = "new_data", length = 500)
    private String newData;

    public AuditLog(Integer userId, String action, OffsetDateTime changeTimestamp, String oldData, String newData) {
        this.userId = userId;
        this.action = action;
        this.changeTimestamp = changeTimestamp;
        this.oldData = oldData;
        this.newData = newData;
    }

}