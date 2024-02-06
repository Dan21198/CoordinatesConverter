package com.example.coordinatesconverter.fileConversions;

import com.example.coordinatesconverter.controller.CoordinatesFileController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CoordinatesFileControllerTest {

    @Autowired
    public CoordinatesFileController coordinatesFileController;

    @Test
    public void testProcessCoordinatesFile() throws IOException {
        String content = "49.8406814N, 18.2883317E\n" +
                "49.8406814S, 18.2883317W\n" +
                "49.8406814N, 18.2883317°E";
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", content.getBytes());

        List<String> result = coordinatesFileController.processCoordinatesFile(file);

        assertEquals(3, result.size());
        assertEquals("49.8406814°N,18.2883317°E", result.get(0));
        assertEquals("49.8406814°S,18.2883317°W", result.get(1));
        assertEquals("49.8406814°N,18.2883317°E", result.get(2));

    }

    @Test
    public void printTestProcessCoordinatesFileFromFile() throws IOException {
        String fileContent =
                "49.8406814N, 18.2883317E\n" +
                        "49.8406814°N, 18.2883317°E\n" +
                        "49.8406814N; 18.2883317E\n" +
                        "49.8406814N 18.2883317E\n" +
                        "49.8406814N	18.2883317E\n" +
                        "49.8406814N, 18.2883317°E\n" +
                        "49.8406814, 18.2883317E\n" +
                        "49.8406814N, 18.2883317\n" +
                        "49.8406814 N; 18.2883317 E\n" +
                        "49.8406814°N,18.2883317°E\n" +
                        "49.8406814N-18.2883317E\n" +
                        "49.8406814N--18.2883317E\n" +
                        "49.8406814N,-18.2883317E\n" +
                        "49.8406814 °N, 18.2883317 °E\n" +
                        "49.8406814° N, 18.2883317° E\n" +
                        "49.8406814N;	18.2883317E\n" +
                        "49.8406814 N,18.2883317E\n" +
                        "18.2883317E, 49.8406814N\n" +
                        "49.8406814N, 18.2883317E\n" +
                        "49.8406814N;18.2883317E;\n" +
                        "49.8406814N,18.2883317E,\n" +
                        "49.8406814N 18.2883317E\n" +
                        "49.8406814N.18.2883317E\n" +
                        "49.8406814N. 18.2883317E\n" +
                        "49.8406814N, 18.2883317E.\n" +
                        "49.8406814N		18.2883317E\n" +
                        "49,8406814N, 18,2883317E\n" +
                        "49,8406814N,18,2883317E\n" +
                        "49,8406814N; 18,2883317E\n" +
                        "49,8406814N;18,2883317E\n" +
                        "49,8406814N,18,2883317E,\n" +
                        "49,8406814 N, 18,2883317 E\n" +
                        "49,8406814 N 18,2883317 E\n" +
                        "N49.8406814, E18.2883317\n" +
                        "49.8406814N, E18.2883317\n" +
                        "N49.8406814, 18.2883317E\n" +
                        "N49.8406814°, 18.2883317°E\n" +
                        "N 49.8406814; E 18.2883317\n" +
                        "N 49.8406814 E 18.2883317\n" +
                        "49.8406814N18.2883317E\n" +
                        "49°50'26.45304\"N, 18°17'17.99412\"E\n" +
                        "49°50'26.45304\"N; 18°17'17.99412\"E\n" +
                        "49°50'26.45304\"N 18°17'17.99412\"E\n" +
                        "49°50'26.45304\"N18°17'17.99412\"E\n" +
                        "49°50'26,45304\"N,18°17'17,99412\"E\n" +
                        "49°50'26.45304N, 18°17'17.99412E\n" +
                        "49°50'26.45304\"N	18°17'17.99412\"E\n" +
                        "49°50'26.45304\"N, 18.2883317E\n" +
                        "49° 50' 26.45304\" N, 18° 17' 17.99412\" E\n" +
                        "49 °50'26.45304\"N, 18°17 '17.99412\"E\n" +
                        "49°50´26.45304\"N, 18°17´17.99412\"E\n" +
                        "49°50´26.45304“N, 18°17´17.99412“E\n" +
                        "49°50´26.45304´´N, 18°17´17.99412´´E\n" +
                        "49°50'26.45304\"N, 18°17'17.99412\"E\n" +
                        "49.8406814N, 18°17'17.99412\"E\n" +
                        "49°50.440884'N, 18°17.299902'E\n" +
                        "49°50'26.45304\"N, 18°17.299902'E\n" +
                        "49°50.440884'N, 18°17.299902'E";

        MockMultipartFile file = new MockMultipartFile("coordinates.txt", fileContent.getBytes());

        List<String> result = coordinatesFileController.processCoordinatesFile(file);

        for (String line : result) {
            System.out.println("Processed Coordinate: " + line);
        }
    }

    //@Test
    /*public void testProcessCoordinatesFileFromFile() throws IOException {
        String fileContent =
                "49.8406814N, 18.2883317E\n" +
                        "49.8406814°N, 18.2883317°E\n" +
                        "49.8406814N; 18.2883317E\n" +
                        "49.8406814N 18.2883317E\n" +
                        "49.8406814N	18.2883317E\n" +
                        "49.8406814N, 18.2883317°E\n" +
                        "49.8406814, 18.2883317E\n" +
                        "49.8406814N, 18.2883317\n" +
                        "49.8406814 N; 18.2883317 E\n" +
                        "49.8406814°N,18.2883317°E\n" +
                        "49.8406814N-18.2883317E\n" +
                        "49.8406814 °N, 18.2883317 °E\n" +
                        "49.8406814° N, 18.2883317° E\n" +
                        "49.8406814N;	18.2883317E\n" +
                        "49.8406814 N,18.2883317E\n" +
                        "18.2883317E, 49.8406814N\n" +
                        "49.8406814N, 18.2883317E\n" +
                        "49.8406814N;18.2883317E;\n" +
                        "49.8406814N,18.2883317E,\n" +
                        "49.8406814N 18.2883317E\n" +
                        "49.8406814N.18.2883317E\n" +
                        "49.8406814N. 18.2883317E\n" +
                        "49.8406814N, 18.2883317E.\n" +
                        "49.8406814N		18.2883317E\n" +
                        "49,8406814N, 18,2883317E\n" +
                        "49,8406814N,18,2883317E\n" +
                        "49,8406814N; 18,2883317E\n" +
                        "49,8406814N;18,2883317E\n" +
                        "49,8406814N,18,2883317E,\n" +
                        "49,8406814 N, 18,2883317 E\n" +
                        "49,8406814 N 18,2883317 E\n" +
                        "N49.8406814, E18.2883317\n" +
                        "49.8406814N, E18.2883317\n" +
                        "N49.8406814, 18.2883317E\n" +
                        "N49.8406814°, 18.2883317°E\n" +
                        "N 49.8406814; E 18.2883317\n" +
                        "N 49.8406814 E 18.2883317\n" +
                        "49.8406814N18.2883317E\n" +
                        "49°50'26.45304\"N, 18°17'17.99412\"E\n" +
                        "49°50'26.45304\"N; 18°17'17.99412\"E\n" +
                        "49°50'26.45304\"N 18°17'17.99412\"E\n" +
                        "49°50'26.45304\"N18°17'17.99412\"E\n" +
                        "49°50'26,45304\"N,18°17'17,99412\"E\n" +
                        "49°50'26.45304N, 18°17'17.99412E\n" +
                        "49°50'26.45304\"N	18°17'17.99412\"E\n" +
                        "49°50'26.45304\"N, 18.2883317E\n" +
                        "49° 50' 26.45304\" N, 18° 17' 17.99412\" E\n" +
                        "49 °50'26.45304\"N, 18°17 '17.99412\"E\n" +
                        "49°50´26.45304\"N, 18°17´17.99412\"E\n" +
                        "49°50´26.45304“N, 18°17´17.99412“E\n" +
                        "49°50´26.45304´´N, 18°17´17.99412´´E\n" +
                        "49°50'26.45304\"N, 18°17'17.99412\"E\n" +
                        "49.8406814N, 18°17'17.99412\"E\n" +
                        "49°50.440884'N, 18°17.299902'E\n" +
                        "49°50'26.45304\"N, 18°17.299902'E\n" +
                        "49°50.440884'N, 18°17.299902'E";

        MockMultipartFile file = new MockMultipartFile("coordinates.txt", fileContent.getBytes());

        List<String> result = coordinatesFileController.processCoordinatesFile(file);

        String[] expectedResults = {

        };

        assertEquals(expectedResults.length, result.size());

        for (int i = 0; i < expectedResults.length; i++) {
            assertEquals(expectedResults[i], result.get(i));
        }
    }*/
}