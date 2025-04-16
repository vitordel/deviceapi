package com.vitordel.deviceapi.controller;

import com.vitordel.deviceapi.model.Device;
import com.vitordel.deviceapi.model.DeviceState;
import com.vitordel.deviceapi.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    public ResponseEntity<Device> createDevice(@RequestBody Device device) {
        Device createdDevice = deviceService.createDevice(device);
        return ResponseEntity.ok(createdDevice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable UUID id, @RequestBody Device device) {
        Device updatedDevice = deviceService.updateDevice(id, device);
        return ResponseEntity.ok(updatedDevice);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable UUID id) {
        Device device = deviceService.getDeviceById(id);
        return ResponseEntity.ok(device);
    }

    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices() {
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Device>> getDevicesByBrand(@PathVariable String brand) {
        return ResponseEntity.ok(deviceService.getDevicesByBrand(brand));
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<Device>> getDevicesByState(@PathVariable DeviceState state) {
        return ResponseEntity.ok(deviceService.getDevicesByState(state));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Device> deleteDevice(@PathVariable UUID id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
