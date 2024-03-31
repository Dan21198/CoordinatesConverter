package com.example.coordinatesconverter.fileConversions;

import com.example.coordinatesconverter.exception.CoordinateConversionException;
import com.example.coordinatesconverter.model.DDCoordinates;
import com.example.coordinatesconverter.model.DMCoordinates;
import com.example.coordinatesconverter.model.DMSCoordinates;
import com.example.coordinatesconverter.service.converter.CoordinateConversionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class CoordinateConversionServiceImplTest {
    private CoordinateConversionServiceImpl service;

    @BeforeEach
    public void setUp() {
        service = new CoordinateConversionServiceImpl();
    }

    @Test
    public void convertDMSToDD_ValidCoordinates_ReturnsDDCoordinates() {
        DMSCoordinates dmsCoordinates = new DMSCoordinates(40, 30, 20, -70
                , 50, 30);
        DDCoordinates expected = new DDCoordinates(40.505556, -69.158333);
        DDCoordinates actual = service.convertDMSToDD(dmsCoordinates);
        Assertions.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void convertDMSToDD_InvalidLatitude_ThrowsException() {
        DMSCoordinates dmsCoordinates = new DMSCoordinates(91, 0, 0, 0
                , 0, 0);
        Assertions.assertThrows(CoordinateConversionException.class, () -> service.convertDMSToDD(dmsCoordinates));
    }

    @Test
    public void convertDMSToDM_ValidCoordinates_ReturnsDMCoordinates() {
        DMSCoordinates dmsCoordinates = new DMSCoordinates(40, 30, 20, -70
                , 50, 30);
        DMCoordinates expected = new DMCoordinates(40, 30.333333, -70, 50.5);
        DMCoordinates actual = service.convertDMSToDM(dmsCoordinates);
        Assertions.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void convertDMToDD_ValidCoordinates_ReturnsDDCoordinates() {
        DMCoordinates dmCoordinates = new DMCoordinates(40, 30, -70, 50);
        DDCoordinates expected = new DDCoordinates(40.5, -69.16667);
        DDCoordinates actual = service.convertDMToDD(dmCoordinates);
        Assertions.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void convertDMToDMS_ValidCoordinates_ReturnsDMSCoordinates() {
        DMCoordinates dmCoordinates = new DMCoordinates(40, 30.5, -70, 50.5);
        DMSCoordinates expected = new DMSCoordinates(40, 30, 30, -70
                , 50, 30);
        DMSCoordinates actual = service.convertDMToDMS(dmCoordinates);
        Assertions.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void convertDDToDM_ValidCoordinates_ReturnsDMCoordinates() {
        DDCoordinates ddCoordinates = new DDCoordinates(40.5, -70.8333333);
        DMCoordinates expected = new DMCoordinates(40, 30, -70, 50);
        DMCoordinates actual = service.convertDDToDM(ddCoordinates);
        Assertions.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void convertDDToDMS_ValidCoordinates_ReturnsDMSCoordinates() {
        DDCoordinates ddCoordinates = new DDCoordinates(40.5050, -70.8435);
        DMSCoordinates expected = new DMSCoordinates(40, 30, 18, -70
                , 50, 36.6);
        DMSCoordinates actual = service.convertDDToDMS(ddCoordinates);
        Assertions.assertEquals(expected.toString(), actual.toString());
    }
}
