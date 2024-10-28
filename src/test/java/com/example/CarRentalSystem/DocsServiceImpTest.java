package com.example.CarRentalSystem;

import com.example.CarRentalSystem.enums.DocType;
import com.example.CarRentalSystem.model.UserDoc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DocsServiceImpTest {
    @InjectMocks
    private DocsServiceImp docsService;
    @Mock
    private JpaDocsRepository docsRepository;

    @Test
    public void testCreate_NewDoc_Successfully(){
        UserDoc userDoc = UserDoc.builder()
                .docType(DocType.AUTO_PASSPORT)
                .link("https://docs.google.com/")
                .build();

        when(docsRepository.save(userDoc)).thenReturn(userDoc);
        UserDoc result = docsService.create(userDoc);

        assertAll(
                () -> assertNotNull(userDoc),
                () -> assertEquals(result, userDoc),

                () -> verify(docsRepository).save(any(UserDoc.class))
        );
    }


    public void testCreate_HasUserId_Successfully(){


    }



    @Test
    public void testCreate_HasUserId_ThrowsException()
        //DocType.other
}
