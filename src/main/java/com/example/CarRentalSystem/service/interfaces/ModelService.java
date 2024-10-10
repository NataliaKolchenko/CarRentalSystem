package com.example.CarRentalSystem.service.interfaces;

import com.example.CarRentalSystem.model.Model;

import java.util.List;

public interface ModelService {
    Model createModel (String modelName, Long brandId);
    Model updateModel (Long modelId, String newModelName);
    boolean deleteModelById(Long modelId);
    Model getModelById(Long modelId);
    Model getModelByName(String modelName);
    List<Model> getAllModel();
}
