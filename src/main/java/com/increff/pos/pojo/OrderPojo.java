package com.increff.pos.pojo;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.ZonedDateTime;

@Entity
@Getter
public class OrderPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private ZonedDateTime time;
    public void setId(Integer id) {
        this.id = id;
    }
    public void setTime() {
        this.time = ZonedDateTime.now();
    }
}
