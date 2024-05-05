package com.rahim.common.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@DynamicInsert
@Table(name = "hz_map_data")
public class HzMapData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hz_map_id", nullable = false)
    private Integer id;

    @Column(name = "hz_map_name", nullable = false)
    private String mapName;

    @Column(name = "hz_map_key")
    private String mapKey;

    @Column(name = "hz_map_value", nullable = false)
    private String mapValue;

    @Column(name = "hz_map_updated_at")
    private OffsetDateTime updatedAt;

}