package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.enums.DocType;
import com.example.CarRentalSystem.exception.IncorrectUserDocTypeException;
import com.example.CarRentalSystem.exception.IncorrectVehicleDocTypeException;
import com.example.CarRentalSystem.exception.SubjectNotFoundException;
import com.example.CarRentalSystem.exception.error.ErrorMessage;
import com.example.CarRentalSystem.model.Doc;
import com.example.CarRentalSystem.repository.JpaDocsRepository;
import com.example.CarRentalSystem.service.interfaces.DocsService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class DocsServiceImp implements DocsService{
    public final JpaDocsRepository docsRepository;

    public DocsServiceImp(JpaDocsRepository docsRepository) {
        this.docsRepository = docsRepository;
    }

    @Override
    public Doc create(Doc doc) {
        if(doc.getUserId() != null){
            if(!DocType.USER_PASSPORT.equals(doc.getDocType()) || !DocType.DRIVER_LICENSE.equals(doc.getDocType())) {
                throw new IncorrectUserDocTypeException(ErrorMessage.INCORRECT_USER_DOC_TYPE);
            }
        }

        if(doc.getVehicleId() != null){
            if(!DocType.AUTO_PASSPORT.equals(doc.getDocType()) || !DocType.AUTO_REGISTRATION_CERTIFICATE.equals(doc.getDocType())) {
                throw new IncorrectVehicleDocTypeException(ErrorMessage.INCORRECT_VEHICLE_DOC_TYPE);
            }
        }

        return docsRepository.save(doc);
    }

    @Override
    public List<Doc> getAllDocsByUserId(Long userId) {
        List<Doc> docListByUserId = docsRepository.findByUserId(userId);
        return docListByUserId.isEmpty() ? Collections.emptyList() : docListByUserId;
    }

    @Override
    public List<Doc> getAllDocsByVehicleId(Long vehicleId) {
        List<Doc> docListByVehicleId = docsRepository.findByVehicleId(vehicleId);
        return docListByVehicleId.isEmpty() ? Collections.emptyList() : docListByVehicleId;
    }

    @Override
    public List<Doc> getAllDocs() {
        List<Doc> docList = docsRepository.findAll();
        return docList.isEmpty() ? Collections.emptyList() : docList;
    }

    @Override
    public Doc getById(Long id) {
        Optional<Doc> docOpt = docsRepository.findById(id);
        Doc doc = docOpt.orElseThrow(() -> new SubjectNotFoundException(ErrorMessage.DOC_ID_WAS_NOT_FOUND));
        return doc;
    }

    @Override
    public void deleteById(Long id) {
        if(!docsRepository.existsById(id)){
            throw new SubjectNotFoundException(ErrorMessage.DOC_ID_WAS_NOT_FOUND);
        }
        docsRepository.deleteById(id);
    }
}
