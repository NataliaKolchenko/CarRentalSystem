package com.example.CarRentalSystem.repository.model;

import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.model.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    public Optional<Model> getModelById(Long modelId) {

        return jpaModelRepository.findById(modelId);
    }

    @Override
    public Model getModelByName(String modelName) {
        return jpaModelRepository.findByModelName(modelName);
    }

    @Override
    public List<Model> getAllModels() {
        return jpaModelRepository.findAll();
    }
}
