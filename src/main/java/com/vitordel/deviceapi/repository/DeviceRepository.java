package com.vitordel.deviceapi.repository;

import com.vitordel.deviceapi.model.Device;
import com.vitordel.deviceapi.model.DeviceState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {

    List<Device> findByBrandIgnoreCase(String brand);

    List<Device> findByState(DeviceState state);
}
