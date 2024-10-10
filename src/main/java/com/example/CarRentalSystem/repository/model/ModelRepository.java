package com.example.CarRentalSystem.repository.model;

import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.model.Model;

import java.util.List;

public interface ModelRepository {
    Model createModel (Model newModel);
    Brand updateModel (Model updatedModel);
    boolean deleteModelById(Long modelId);
    Model getModelById(Long modelId);
    Model getModelByName(String modelName);
    List<Model> getAllModel();
}
