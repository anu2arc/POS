package com.increff.pos.pojo;

import lombok.Getter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Getter
public class OrderPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private ZonedDateTime time;
    public void setId(Integer id) {
        this.id = id;
    }
    public void setTime() {
        this.time = ZonedDateTime.now();
    }
}
