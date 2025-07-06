package ru.yandex.practicum.plus_smart_home_tech.analyzer.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "actions")
@SecondaryTable(name = "scenario_actions", pkJoinColumns = @PrimaryKeyJoinColumn(name = "action_id"))
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = Integer.MAX_VALUE)
    private ActionType type;

    @Column(name = "value")
    private Integer value;

    @ManyToOne
    @JoinColumn(name = "scenario_id", table = "scenario_actions")
    private Scenario scenario;

    @ManyToOne
    @JoinColumn(name = "sensor_id", table = "scenario_actions")
    private Sensor sensor;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Action action = (Action) o;
        return getId() != null && Objects.equals(getId(), action.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}