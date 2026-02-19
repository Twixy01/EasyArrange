package org.example.backend.Model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@Table(name = "staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "start_duty")
    private Instant startDuty;

    @Column(name = "end_duty")
    private Instant endDuty;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getStartDuty() {
        return startDuty;
    }

    public void setStartDuty(Instant startDuty) {
        this.startDuty = startDuty;
    }

    public Instant getEndDuty() {
        return endDuty;
    }

    public void setEndDuty(Instant endDuty) {
        this.endDuty = endDuty;
    }

}