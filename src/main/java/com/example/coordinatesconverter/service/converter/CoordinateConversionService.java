package com.example.coordinatesconverter.service.converter;

import com.example.coordinatesconverter.model.DDCoordinates;
import com.example.coordinatesconverter.model.DMCoordinates;
import com.example.coordinatesconverter.model.DMSCoordinates;

public interface CoordinateConversionService {
    DDCoordinates convertDMSToDD(DMSCoordinates dmsCoordinates);

    DMCoordinates convertDMSToDM(DMSCoordinates dmsCoordinates);

    DDCoordinates convertDMToDD(DMCoordinates dmCoordinates);

    DMSCoordinates convertDMToDMS(DMCoordinates dmCoordinates);

    DMCoordinates convertDDToDM(DDCoordinates ddCoordinates);

    DMSCoordinates convertDDToDMS(DDCoordinates ddCoordinates);

}
