package com.example.coordinatesconverter.model;

import lombok.Data;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "coordinates")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class CoordinatesXML {

    @XmlElement
    private String latitude;

    @XmlElement
    private String longitude;

}