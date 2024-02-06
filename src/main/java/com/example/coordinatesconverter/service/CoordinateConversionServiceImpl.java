package com.example.coordinatesconverter.service;

import com.example.coordinatesconverter.model.DDCoordinates;
import com.example.coordinatesconverter.model.DMCoordinates;
import com.example.coordinatesconverter.model.DMSCoordinates;
import org.springframework.stereotype.Service;

@Service
public class CoordinateConversionServiceImpl implements CoordinateConversionService {

    @Override
    public DDCoordinates convertDMSToDD(DMSCoordinates dmsCoordinates) {
        double latDegrees = dmsCoordinates.getLatDegrees();
        double latMinutes = dmsCoordinates.getLatMinutes();
        double latSeconds = dmsCoordinates.getLatSeconds();

        double lonDegrees = dmsCoordinates.getLonDegrees();
        double lonMinutes = dmsCoordinates.getLonMinutes();
        double lonSeconds = dmsCoordinates.getLonSeconds();

        double decimalLatDegrees = latDegrees + latMinutes / 60 + latSeconds / 3600;
        double decimalLonDegrees = lonDegrees + lonMinutes / 60 + lonSeconds / 3600;

        return new DDCoordinates(decimalLatDegrees, decimalLonDegrees);
    }

    @Override
    public DMCoordinates convertDMSToDM(DMSCoordinates dmsCoordinates) {
        double latDegrees = dmsCoordinates.getLatDegrees();
        double latMinutes = dmsCoordinates.getLatMinutes() + dmsCoordinates.getLatSeconds() / 60;

        double lonDegrees = dmsCoordinates.getLonDegrees();
        double lonMinutes = dmsCoordinates.getLonMinutes() + dmsCoordinates.getLonSeconds() / 60;

        return new DMCoordinates(latDegrees, latMinutes, lonDegrees, lonMinutes);
    }

    @Override
    public DDCoordinates convertDMToDD(DMCoordinates dmCoordinates) {
        double latDegrees = dmCoordinates.getLatDegrees();
        double latMinutes = dmCoordinates.getLatMinutes();
        double lonDegrees = dmCoordinates.getLonDegrees();
        double lonMinutes = dmCoordinates.getLonMinutes();

        double decimalLatDegrees = latDegrees + latMinutes / 60;
        double decimalLonDegrees = lonDegrees + lonMinutes / 60;

        return new DDCoordinates(decimalLatDegrees, decimalLonDegrees);
    }

    @Override
    public DMSCoordinates convertDMToDMS(DMCoordinates dmCoordinates) {
        double latDegrees = dmCoordinates.getLatDegrees();
        double latMinutes = dmCoordinates.getLatMinutes();
        double lonDegrees = dmCoordinates.getLonDegrees();
        double lonMinutes = dmCoordinates.getLonMinutes();

        return new DMSCoordinates(latDegrees, latMinutes, 0, lonDegrees, lonMinutes, 0);
    }

    @Override
    public DMCoordinates convertDDToDM(DDCoordinates ddCoordinates) {
        double latDegrees = ddCoordinates.getLatitude();
        int latDegreesInt = (int) latDegrees;
        double latMinutes = Math.abs((latDegrees - latDegreesInt) * 60);

        double lonDegrees = ddCoordinates.getLongitude();
        int lonDegreesInt = (int) lonDegrees;
        double lonMinutes = Math.abs((lonDegrees - lonDegreesInt) * 60);

        return new DMCoordinates(latDegreesInt, latMinutes, lonDegreesInt, lonMinutes);
    }

    @Override
    public DMSCoordinates convertDDToDMS(DDCoordinates ddCoordinates) {
        double latDegrees = ddCoordinates.getLatitude();
        double lonDegrees = ddCoordinates.getLongitude();

        int latDegreesInt = (int) latDegrees;
        double latMinutes = Math.abs((latDegrees - latDegreesInt) * 60);
        double latSeconds = (latMinutes - (int) latMinutes) * 60;

        int lonDegreesInt = (int) lonDegrees;
        double lonMinutes = Math.abs((lonDegrees - lonDegreesInt) * 60);
        double lonSeconds = (lonMinutes - (int) lonMinutes) * 60;

        return new DMSCoordinates(latDegreesInt, latMinutes, latSeconds, lonDegreesInt, lonMinutes, lonSeconds);
    }

}