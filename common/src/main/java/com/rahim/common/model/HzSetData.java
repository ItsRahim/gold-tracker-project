package com.rahim.common.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.time.Instant;

@Getter
@Setter
@Entity
@DynamicInsert
@NoArgsConstructor
@Table(name = "hz_set_data")
public class HzSetData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hz_set_id", nullable = false)
    private Integer id;

    @Column(name = "hz_set_name", nullable = false)
    private String setName;

    @Column(name = "hz_set_value", nullable = false)
    private Object setValue;

    @Column(name = "hz_set_created")
    private Instant createdAt;

    public HzSetData(String setName, Object setValue) {
        this.setName = setName;
        this.setValue = setValue;
    }
}