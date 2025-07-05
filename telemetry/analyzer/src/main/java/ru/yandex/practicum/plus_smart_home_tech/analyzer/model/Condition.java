package ru.yandex.practicum.plus_smart_home_tech.analyzer.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "conditions")
public class Condition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = Integer.MAX_VALUE)
    private ConditionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation", length = Integer.MAX_VALUE)
    private ConditionOperation operation;

    @Column(name = "value")
    private Integer value;
}