package com.example.CarRentalSystem;

import com.example.CarRentalSystem.enums.VehicleDocType;
import com.example.CarRentalSystem.model.Vehicle;
import com.example.CarRentalSystem.model.VehicleDoc;
import com.example.CarRentalSystem.model.VehicleType;
import com.example.CarRentalSystem.repository.JpaVehicleDocRepository;
import com.example.CarRentalSystem.service.VehicleDocServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VehicleDocServiceImpTest {
    @InjectMocks
    private VehicleDocServiceImp docService;
    @Mock
    private JpaVehicleDocRepository docRepository;

    @Test
    public void testCreate_NewVehicleDoc_Successfully(){
        VehicleDoc doc = new VehicleDoc(
                VehicleDocType.AUTO_PASSPORT,
                "https://example.com/",
                new Vehicle()
        );


    }
}
