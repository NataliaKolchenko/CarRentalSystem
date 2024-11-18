package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.exception.SubjectAlreadyExistsException;
import com.example.CarRentalSystem.exception.SubjectNotFoundException;
import com.example.CarRentalSystem.exception.error.ErrorMessage;
import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.model.Model;
import com.example.CarRentalSystem.repository.JpaModelRepository;
import com.example.CarRentalSystem.service.interfaces.ModelService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ModelServiceImp  implements ModelService {
    private final JpaModelRepository modelRepository;
    private final BrandServiceImp brandServiceImp;
    public ModelServiceImp(JpaModelRepository modelRepository, BrandServiceImp brandServiceImp) {
        this.modelRepository = modelRepository;
        this.brandServiceImp = brandServiceImp;
    }

    @Override
    public Model create(Model model) {
        Model checkExistModel = modelRepository.findByModelName(model.getModelName());
        if(checkExistModel != null){
            throw  new SubjectAlreadyExistsException(ErrorMessage.MODEL_NAME_IS_ALREADY_EXIST);
        }
        Brand brand = brandServiceImp.getById(model.getBrand().getId());
        Model newModel = new Model(model.getModelName(), brand);

        return modelRepository.save(newModel);
    }

    @Override
    public Model update(Long modelId, String newModelName) {
        Model model = getById(modelId);
        if(modelRepository.findByModelName(newModelName) != null){
            throw new SubjectAlreadyExistsException(ErrorMessage.MODEL_NAME_IS_ALREADY_EXIST);
        }
        model.setModelName(newModelName);
        return modelRepository.save(model);
    }

    @Override
    public void deleteById(Long modelId) {
        if(!modelRepository.existsById(modelId)){
            throw new SubjectNotFoundException(ErrorMessage.MODEL_ID_WAS_NOT_FOUND);
        }
        modelRepository.deleteById(modelId);
    }

    @Override
    public Model getById(Long modelId) {
        Optional<Model> modelOpt = modelRepository.findById(modelId);
        return modelOpt.orElseThrow(() -> new SubjectNotFoundException(ErrorMessage.MODEL_ID_WAS_NOT_FOUND));
    }

    @Override
    public Model getByName(String modelName) {
        Model model = modelRepository.findByModelName(modelName);
        if (model == null){
            throw new SubjectNotFoundException(ErrorMessage.MODEL_NAME_WAS_NOT_FOUND);
        }
        return model;
    }

    @Override
    public List<Model> getAllModels() {
        List<Model> modelList = modelRepository.findAll();
        return modelList.isEmpty() ? Collections.emptyList() : modelList;
    }

    public boolean existsByBrandId(Long brandId){
        List<Model> modelList= modelRepository.findByBrandId(brandId);
        if (modelList.isEmpty()) {
            return false;
        } else {
            return true;
        }

    }
}
