package com.example.coordinatesconverter.service.converter;

import com.example.coordinatesconverter.exception.CoordinateConversionException;
import com.example.coordinatesconverter.model.DDCoordinates;
import com.example.coordinatesconverter.model.DMCoordinates;
import com.example.coordinatesconverter.model.DMSCoordinates;
import org.springframework.stereotype.Service;

@Service
public class CoordinateConversionServiceImpl implements CoordinateConversionService {

    @Override
    public DDCoordinates convertDMSToDD(DMSCoordinates dmsCoordinates) {
        validateDMS(dmsCoordinates);
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
        validateDMS(dmsCoordinates);
        double latDegrees = dmsCoordinates.getLatDegrees();
        double latMinutes = dmsCoordinates.getLatMinutes() + dmsCoordinates.getLatSeconds() / 60;

        double lonDegrees = dmsCoordinates.getLonDegrees();
        double lonMinutes = dmsCoordinates.getLonMinutes() + dmsCoordinates.getLonSeconds() / 60;

        return new DMCoordinates(latDegrees, latMinutes, lonDegrees, lonMinutes);
    }

    @Override
    public DDCoordinates convertDMToDD(DMCoordinates dmCoordinates) {
        validateDM(dmCoordinates);
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
        validateDM(dmCoordinates);
        double latDegrees = dmCoordinates.getLatDegrees();
        double latMinutes = dmCoordinates.getLatMinutes();
        double lonDegrees = dmCoordinates.getLonDegrees();
        double lonMinutes = dmCoordinates.getLonMinutes();

        double latSeconds = (latMinutes % 1) * 60;
        double lonSeconds = (lonMinutes % 1) * 60;

        latMinutes = Math.floor(latMinutes);
        lonMinutes = Math.floor(lonMinutes);

        return new DMSCoordinates(latDegrees, latMinutes, latSeconds, lonDegrees, lonMinutes, lonSeconds);
    }

    @Override
    public DMCoordinates convertDDToDM(DDCoordinates ddCoordinates) {
        validateDD(ddCoordinates);
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
        validateDD(ddCoordinates);
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

    private void validateDMS(DMSCoordinates dmsCoordinates) {
        validateLatitude(dmsCoordinates.getLatDegrees(), "degrees");
        validateLongitude(dmsCoordinates.getLonDegrees(), "degrees");
        validateMinutesAndSeconds(dmsCoordinates.getLatMinutes(), "Latitude minutes");
        validateMinutesAndSeconds(dmsCoordinates.getLonMinutes(), "Longitude minutes");
        validateMinutesAndSeconds(dmsCoordinates.getLatSeconds(), "Latitude seconds");
        validateMinutesAndSeconds(dmsCoordinates.getLonSeconds(), "Longitude seconds");
    }

    private void validateDM(DMCoordinates dmCoordinates) {
        validateLatitude(dmCoordinates.getLatDegrees(), "degrees");
        validateLongitude(dmCoordinates.getLonDegrees(), "degrees");
        validateMinutesAndSeconds(dmCoordinates.getLatMinutes(), "Latitude minutes");
        validateMinutesAndSeconds(dmCoordinates.getLonMinutes(), "Longitude minutes");
    }

    private void validateDD(DDCoordinates ddCoordinates) {
        validateLatitude(ddCoordinates.getLatitude(), "value");
        validateLongitude(ddCoordinates.getLongitude(), "value");
    }

    private void validateLatitude(double latitude, String valueType) {
        if (latitude < -90 || latitude > 90) {
            throw new CoordinateConversionException("Invalid latitude " + valueType + ": "
                    + latitude + ". Must be between -90 and 90 degrees.");
        }
    }

    private void validateLongitude(double longitude, String valueType) {
        if (longitude < -180 || longitude > 180) {
            throw new CoordinateConversionException("Invalid longitude " + valueType + ": "
                    + longitude + ". Must be between -180 and 180 degrees.");
        }
    }

    private void validateMinutesAndSeconds(double value, String name) {
        if (value < 0 || value >= 60) {
            throw new CoordinateConversionException("Invalid " + name + ": "
                    + value + ". Must be between 0 and 59.");
        }
    }
}