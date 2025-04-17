package com.vitordel.deviceapi.service;

import com.vitordel.deviceapi.exception.DeviceInUseException;
import com.vitordel.deviceapi.exception.DeviceNotFoundException;
import com.vitordel.deviceapi.model.Device;
import com.vitordel.deviceapi.model.DeviceState;
import com.vitordel.deviceapi.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeviceServiceTest {

    private DeviceRepository deviceRepository;
    private DeviceService deviceService;

    private static final String BRAND_SAMSUNG = "Samsung";
    private static final String BRAND_APPLE = "Apple";
    private static final String NAME_GALAXY = "Galaxy";
    private static final String NAME_IPHONE = "Iphone";

    @BeforeEach
    void setUp() {
        deviceRepository = mock(DeviceRepository.class);
        deviceService = new DeviceService(deviceRepository);
    }

    // Tests for createDevice()
    @Test
    void shouldCreateDevice() {
        Device device = new Device();
        device.setName(NAME_GALAXY);
        device.setBrand(BRAND_SAMSUNG);
        device.setState(DeviceState.AVAILABLE);

        when(deviceRepository.save(any(Device.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Device createdDevice = deviceService.createDevice(device);

        assertEquals(NAME_GALAXY, createdDevice.getName());
        assertNotNull(createdDevice.getCreationTime());
        assertNotNull(createdDevice.getUpdateTime());
        verify(deviceRepository).save(device);
    }

    // Tests for getDeviceById()
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

    // Tests for getAllDevices()
    @Test
    void shouldReturnAllDevices() {
        when(deviceRepository.findAll()).thenReturn(List.of(new Device(), new Device()));
        List<Device> result = deviceService.getAllDevices();
        assertEquals(2, result.size());
        verify(deviceRepository).findAll();
    }

    // Tests for getDevicesByBrand()
    @Test
    void shouldReturnDevicesByBrand() {
        when(deviceRepository.findByBrandIgnoreCase(BRAND_SAMSUNG)).thenReturn(List.of(new Device(), new Device()));
        List<Device> result = deviceService.getDevicesByBrand(BRAND_SAMSUNG);
        assertEquals(2, result.size());
        verify(deviceRepository).findByBrandIgnoreCase(BRAND_SAMSUNG);
    }

    // Tests for getDevicesByState()
    @Test
    void shouldReturnDevicesByState() {
        when(deviceRepository.findByState(DeviceState.AVAILABLE)).thenReturn(List.of(new Device()));
        List<Device> result = deviceService.getDevicesByState(DeviceState.AVAILABLE);
        assertEquals(1, result.size());
        verify(deviceRepository).findByState(DeviceState.AVAILABLE);
    }

    // Tests for updateDevice()
    @Test
    void shouldUpdateDeviceWhenNotInUse() {
        UUID id = UUID.randomUUID();
        Device existing = new Device();
        existing.setId(id);
        existing.setName(NAME_GALAXY);
        existing.setBrand(BRAND_SAMSUNG);
        existing.setState(DeviceState.AVAILABLE);

        Device update = new Device();
        update.setName(NAME_IPHONE);
        update.setBrand(BRAND_APPLE);
        update.setState(DeviceState.AVAILABLE);

        when(deviceRepository.findById(id)).thenReturn(Optional.of(existing));
        when(deviceRepository.save(any(Device.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Device result = deviceService.updateDevice(id, update);

        assertEquals(NAME_IPHONE, result.getName());
        assertEquals(BRAND_APPLE, result.getBrand());
        assertEquals(DeviceState.AVAILABLE, result.getState());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingDevice() {
        UUID id = UUID.randomUUID();
        Device update = new Device();

        when(deviceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DeviceNotFoundException.class, () -> deviceService.updateDevice(id, update));
    }

    @Test
    void shouldNotUpdateNameIfDeviceInUse() {
        UUID id = UUID.randomUUID();

        Device existing = new Device();
        existing.setId(id);
        existing.setName(NAME_GALAXY);
        existing.setBrand(BRAND_SAMSUNG);
        existing.setState(DeviceState.IN_USE);

        Device updatedDevice = new Device();
        updatedDevice.setName(NAME_IPHONE);
        updatedDevice.setBrand(BRAND_APPLE);

        when(deviceRepository.findById(id)).thenReturn(Optional.of(existing));

        assertThrows(DeviceInUseException.class, () -> deviceService.updateDevice(id, updatedDevice));
    }

    // Tests for deleteDevice()
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

    @Test
    void shouldThrowWhenDeletingNonExistingDevice() {
        UUID id = UUID.randomUUID();
        when(deviceRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(DeviceNotFoundException.class, () -> deviceService.deleteDevice(id));
    }
}