package com.example.coordinatesconverter.controller;

import com.example.coordinatesconverter.model.*;
import com.example.coordinatesconverter.service.converter.CoordinateConversionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/coordinates/convert")
@RequiredArgsConstructor
public class CoordinateController {
    private final CoordinateConversionService conversionService;

    @PostMapping("/dms-to-dd")
    public DDCoordinates convertDMSToDD(@RequestBody DMSCoordinates dmsCoordinates) {
        return conversionService.convertDMSToDD(dmsCoordinates);
    }

    @PostMapping("/dms-to-dm")
    public DMCoordinates convertDMSToDM(@RequestBody DMSCoordinates dmsCoordinates) {
        return conversionService.convertDMSToDM(dmsCoordinates);
    }

    @PostMapping("/dm-to-dd")
    public DDCoordinates convertDMToDD(@RequestBody DMCoordinates dmCoordinates) {
        return conversionService.convertDMToDD(dmCoordinates);
    }

    @PostMapping("/dm-to-dms")
    public DMSCoordinates convertDMToDMS(@RequestBody DMCoordinates dmCoordinates) {
        return conversionService.convertDMToDMS(dmCoordinates);
    }

    @PostMapping("/dd-to-dms")
    public DMSCoordinates convertDDToDMS(@RequestBody DDCoordinates ddCoordinates) {
        return conversionService.convertDDToDMS(ddCoordinates);
    }

    @PostMapping("/dd-to-dm")
    public DMCoordinates convertDDToDM(@RequestBody DDCoordinates ddCoordinates) {
        return conversionService.convertDDToDM(ddCoordinates);
    }
}

