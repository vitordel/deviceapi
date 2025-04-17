package com.vitordel.deviceapi.service;

import com.vitordel.deviceapi.exception.DeviceInUseException;
import com.vitordel.deviceapi.exception.DeviceNotFoundException;
import com.vitordel.deviceapi.model.Device;
import com.vitordel.deviceapi.model.DeviceState;
import com.vitordel.deviceapi.repository.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Device getDeviceById(UUID id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));
    }

    public List<Device> getDevicesByBrand(String brand) {
        return deviceRepository.findByBrandIgnoreCase(brand);
    }

    public List<Device> getDevicesByState(DeviceState state) {
        return deviceRepository.findByState(state);
    }

    public Device createDevice(Device device) {
        device.setCreationTime(LocalDateTime.now());
        device.setUpdateTime(LocalDateTime.now());

        if (device.getState() == null) {
            device.setState(DeviceState.AVAILABLE);
        }

        return deviceRepository.save(device);
    }

    @Transactional
    public Device updateDevice(UUID id, Device device) {
        Optional<Device> existingDevice = deviceRepository.findById(id);

        if (existingDevice.isPresent()) {
            Device deviceToUpdate = existingDevice.get();

            if (deviceToUpdate.getState() == DeviceState.IN_USE) {
                if(!device.getName().equals(deviceToUpdate.getName())) {
                    throw new DeviceInUseException("Device name cannot be updated as the device is in use.");
                }
                if(!device.getBrand().equals(deviceToUpdate.getBrand())) {
                    throw new DeviceInUseException("Device brand cannot be updated as the device is in use.");
                }
            }

            deviceToUpdate.setName(device.getName());
            deviceToUpdate.setBrand(device.getBrand());
            deviceToUpdate.setState(device.getState());
            deviceToUpdate.setUpdateTime(LocalDateTime.now());

            return deviceRepository.save(deviceToUpdate);
        }

        throw new DeviceNotFoundException("Device not found with id: " + id);
    }

    @Transactional
    public void deleteDevice(UUID id) {
        Optional<Device> device = deviceRepository.findById(id);

        if (device.isPresent()) {
            if (device.get().getState() == DeviceState.IN_USE) {
                throw new IllegalStateException("Device cannot be deleted as the device is in use");
            }

            deviceRepository.delete(device.get());
        } else {
            throw new IllegalArgumentException("Device not found with id: " + id);
        }
    }
}
