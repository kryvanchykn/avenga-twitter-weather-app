package com.example.avengatwitterweatherapp.serviceTest;

import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.repository.RegionRepository;
import com.example.avengatwitterweatherapp.service.RegionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegionServiceTest {
    @Autowired
    private RegionService regionService;

    @MockBean
    private RegionRepository regionRepository;


    @Test
    public void getAllRegionsTest() {
        when(regionRepository.findAll()).thenReturn(Stream
                .of(new Region(1, "Львівщина", "Львівська область", "Lviv", "Lviv"),
                    new Region(2, "Волинь", "Волинська область", "Lutsk", "Lutsk"))
                .collect(Collectors.toList()));
        assertEquals(2, regionService.getAllRegions().size());
    }

    @Test
    public void getRegionsByIdTest() {
        List<Long> idList = List.of(1L, 3L);
        when(regionRepository.findAll()).thenReturn(Stream
                .of(new Region(1, "Львівщина", "Львівська область", "Lviv", "Lviv"),
                    new Region(2, "Волинь", "Волинська область", "Lutsk", "Lutsk"),
                    new Region(3, "Івано-Франківщина", "Івано-Франківська область", "Ivanofrankovsk", "Ivano-Frankivsk"))
                .collect(Collectors.toList()));
        assertEquals(2, regionService.getRegionsById(idList).size());
    }
}
