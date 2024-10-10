package com.example.CarRentalSystem.repository.model;

import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.model.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ModelRepositoryImp implements ModelRepository{
    private final JpaModelRepository jpaModelRepository;

    @Override
    public Model createModel(Model newModel) {
        Model model = new Model(newModel.getModelName(), newModel.getBrand());
        return jpaModelRepository.save(model);
    }

    @Override
    public Brand updateModel(Model updatedModel) {
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
        return jpaModelRepository.findByModelName(modelName);
    }

    @Override
    public List<Model> getAllModel() {
        return null;
    }
}
