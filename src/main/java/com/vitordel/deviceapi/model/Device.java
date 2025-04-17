package com.vitordel.deviceapi.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "device")
public class Device {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceState state = DeviceState.AVAILABLE;

    @Column(name = "created_at", nullable = false ,updatable = false)
    private LocalDateTime creationTime;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updateTime;

    public Device() {}

    public Device(String name, String brand, DeviceState state) {
        this.name = name;
        this.brand = brand;
        this.state = state;
        this.creationTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    // Getter e Setter para name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter e Setter para brand
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    // Getter e Setter para state
    public DeviceState getState() {
        return state;
    }

    public void setState(DeviceState state) {
        this.state = state;
    }

    // Getter e Setter para creationTime
    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        if (this.creationTime == null) {
            this.creationTime = creationTime;
        }
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        if (this.updateTime == null) {
            this.updateTime = updateTime;
        }
    }

    public void updateUpdateTime() {
        this.updateTime = LocalDateTime.now(); // Atualiza o timestamp de updateTime
    }

}
