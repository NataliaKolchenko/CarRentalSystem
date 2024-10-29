package com.example.CarRentalSystem.service.interfaces;

import com.example.CarRentalSystem.model.Model;

import java.util.List;

public interface ModelService {
    Model create(Model newModel);
    Model update(Long modelId, String newModelName);
    void deleteById(Long modelId);
    Model getById(Long modelId);
    Model getByName(String modelName);
    List<Model> getAllModels();
}
