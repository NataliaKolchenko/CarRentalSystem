package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.exception.BrandNotFoundException;
import com.example.CarRentalSystem.exception.ModelAlreadyExistsException;
import com.example.CarRentalSystem.exception.ModelNotFoundException;
import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.model.Model;
import com.example.CarRentalSystem.repository.model.ModelRepository;
import com.example.CarRentalSystem.service.interfaces.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class ModelServiceImp  implements ModelService {
    private final ModelRepository modelRepository;
    private final BrandServiceImp brandServiceImp;

    @Autowired
    public ModelServiceImp(ModelRepository modelRepository, BrandServiceImp brandServiceImp) {
        this.modelRepository = modelRepository;
        this.brandServiceImp = brandServiceImp;
    }

    @Override
    public Model createModel(Model model) {
        Model checkExistModel = modelRepository.getModelByName(model.getModelName());
        if(checkExistModel != null){
            throw  new ModelAlreadyExistsException("BrandName has to be unique");
        }
        Brand brand = brandServiceImp.getVehicleBrandById(model.getBrand().getId());
        Model newModel = new Model(model.getModelName(), brand);

        return modelRepository.createModel(newModel);
    }

    @Override
    public Model updateModel(Long modelId, String newModelName) {
        return null;
    }

    @Override
    public boolean deleteModelById(Long modelId) {
        return false;
    }

    @Override
    public Model getModelById(Long modelId) {
        return null;
    }

    @Override
    public Model getModelByName(String modelName) {
        Model model = modelRepository.getModelByName(modelName);
        if (model == null){
            throw new ModelNotFoundException("ModelName was not found");
        }
        return model;
    }

    @Override
    public List<Model> getAllModel() {
        return null;
    }
}
