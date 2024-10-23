package com.example.CarRentalSystem.service.interfaces;

import com.example.CarRentalSystem.model.Doc;

import java.util.List;

public interface DocsService {
    Doc create(Doc doc);
    List<Doc> getAllDocsByUserId(Long userId);
    List<Doc> getAllDocsByVehicleId(Long vehicleId);
    List<Doc> getAllDocs();
    Doc getById(Long id);
    void deleteById(Long id);
}
