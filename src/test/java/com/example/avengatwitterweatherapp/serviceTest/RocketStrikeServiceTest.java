package com.example.avengatwitterweatherapp.serviceTest;

import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.repository.RocketStrikeRepository;
import com.example.avengatwitterweatherapp.service.RocketStrikeService;
import com.example.avengatwitterweatherapp.service.impl.RocketStrikeServiceImpl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.ASC_ORDER;
import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.SORT_BY_REGION;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RocketStrikeServiceTest {
    private static final Logger log = LogManager.getLogger(RocketStrikeServiceImpl.class);

    @Autowired
    private RocketStrikeService rocketStrikeService;
    @MockBean
    private RocketStrikeRepository rocketStrikeRepository;

    public RocketStrikeServiceTest() {
    }

    @Test
    public void getRecentRocketStrikesTest() {
        Region region1 = new Region(1, "Львівщина", "Львівська область", "Lviv", "Lviv");
        Region region2 = new Region(2, "Волинь", "Волинська область", "Lutsk", "Lutsk");

        RocketStrike rocketStrike1 = new RocketStrike(1, region1, LocalDateTime.now());
        RocketStrike rocketStrike2 = new RocketStrike(2, region2, LocalDateTime.now().minusDays(1));

        rocketStrikeRepository.saveAll(List.of(rocketStrike1, rocketStrike2));
        verify(rocketStrikeRepository, times(1)).saveAll(any());

//        assertEquals(List.of(rocketStrike1), rocketStrikeService.getRecentRocketStrikes(SORT_BY_REGION, ASC_ORDER));
    }

    @Test
    public void getFilteredRocketStrikesTest() {
        Region region1 = new Region(1, "Львівщина", "Львівська область", "Lviv", "Lviv");
        RocketStrike rocketStrike1 = new RocketStrike(1, region1, LocalDateTime.of(2022, 2, 24, 0, 0, 0));

        LocalDateTime sinceDate = LocalDateTime.of(2022, 2, 20, 0, 0, 0);
        LocalDateTime untilDate = LocalDateTime.of(2022, 2, 26, 0, 0, 0);

        when(rocketStrikeRepository.findRocketStrikeByStrikeDateBetweenAndRegion(sinceDate, untilDate, region1))
                .thenReturn(List.of(rocketStrike1));

        assertEquals(List.of(rocketStrike1), rocketStrikeService.getRocketStrikesFromDB(sinceDate,
                untilDate, List.of(region1), SORT_BY_REGION, ASC_ORDER));
    }


}

