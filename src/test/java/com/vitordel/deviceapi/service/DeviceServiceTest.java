package com.vitordel.deviceapi.service;

import com.vitordel.deviceapi.exception.DeviceInUseException;
import com.vitordel.deviceapi.exception.DeviceNotFoundException;
import com.vitordel.deviceapi.model.Device;
import com.vitordel.deviceapi.model.DeviceState;
import com.vitordel.deviceapi.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DeviceServiceTest {

    private DeviceRepository deviceRepository;
    private DeviceService deviceService;

    @BeforeEach
    void setUp() {
        deviceRepository = mock(DeviceRepository.class);
        deviceService = new DeviceService(deviceRepository);
    }

    @Test
    void shouldCreateDevice() {
        Device device = new Device();
        device.setName("Galaxy");
        device.setBrand("Samsung");
        device.setState(DeviceState.AVAILABLE);

        when(deviceRepository.save(device)).thenReturn(device);

        Device createdDevice = deviceService.createDevice(device);

        assertEquals("Galaxy", createdDevice.getName());
        verify(deviceRepository).save(device);
    }

    @Test
    void shouldGetDeviceById() {
        UUID id = UUID.randomUUID();
        Device device = new Device();
        device.setId(id);

        when(deviceRepository.findById(id)).thenReturn(Optional.of(device));

        Device deviceFound = deviceService.getDeviceById(id);
        assertEquals(id, deviceFound.getId());
    }

    @Test
    void shouldThrowIfDeviceNotFound() {
        UUID id = UUID.randomUUID();

        when(deviceRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(DeviceNotFoundException.class, () -> deviceService.getDeviceById(id));
    }

    @Test
    void shouldUpdateDeviceNameWhenNotInUse() {
        UUID id = UUID.randomUUID();
        Device existing = new Device();
        existing.setId(id);
        existing.setName("Galaxy");
        existing.setBrand("Samsung");
        existing.setState(DeviceState.AVAILABLE);

        Device update = new Device();
        update.setName("Iphone");
        update.setBrand("Apple");
        update.setState(DeviceState.AVAILABLE);

        when(deviceRepository.findById(id)).thenReturn(Optional.of(existing));
        when(deviceRepository.save(any(Device.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Device result = deviceService.updateDevice(id, update);

        assertEquals("Iphone", result.getName());
        assertEquals("Apple", result.getBrand());
    }

    @Test
    void shouldNotUpdateNameIfDeviceInUse() {
        UUID id = UUID.randomUUID();

        Device device = new Device();
        device.setId(id);
        device.setName("Galaxy");
        device.setBrand("Samsung");
        device.setState(DeviceState.IN_USE);

        Device updatedDevice = new Device();
        updatedDevice.setName("Iphone");
        updatedDevice.setBrand("Apple");

        when(deviceRepository.findById(id)).thenReturn(Optional.of(device));

        assertThrows(DeviceInUseException.class, () -> {
            deviceService.updateDevice(id, updatedDevice);
        });
    }

    @Test
    void shouldDeleteIfNotInUse() {
        UUID id = UUID.randomUUID();
        Device device = new Device();
        device.setId(id);
        device.setState(DeviceState.AVAILABLE);

        when(deviceRepository.findById(id)).thenReturn(Optional.of(device));

        deviceService.deleteDevice(id);

        verify(deviceRepository).delete(device);
    }

    @Test
    void shouldNotDeleteIfInUse() {
        UUID id = UUID.randomUUID();
        Device device = new Device();
        device.setId(id);
        device.setState(DeviceState.IN_USE);

        when(deviceRepository.findById(id)).thenReturn(Optional.of(device));

        assertThrows(IllegalStateException.class, () -> deviceService.deleteDevice(id));
        verify(deviceRepository, never()).delete(device);
    }
}
