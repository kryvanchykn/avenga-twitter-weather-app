package com.example.avengatwitterweatherapp.serviceTest;

import com.example.avengatwitterweatherapp.dto.RocketStrikeDto;
import com.example.avengatwitterweatherapp.exceptions.BadDateRangeException;
import com.example.avengatwitterweatherapp.exceptions.NoSelectedRegionsException;
import com.example.avengatwitterweatherapp.exceptions.RocketStrikeNotFoundException;
import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.repository.RocketStrikeRepository;
import com.example.avengatwitterweatherapp.service.impl.RegionServiceImpl;
import com.example.avengatwitterweatherapp.service.impl.RocketStrikeServiceImpl;
import com.example.avengatwitterweatherapp.service.impl.TwitterServiceImpl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.ASC_ORDER;
import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.SORT_BY_REGION;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RocketStrikeServiceTest {
    private static final Logger log = LogManager.getLogger(RocketStrikeServiceTest.class);

    @InjectMocks
    private RocketStrikeServiceImpl rocketStrikeService;
    @Mock
    private TwitterServiceImpl twitterService;
    @Mock
    private RegionServiceImpl regionService;
    @Mock
    private RocketStrikeRepository rocketStrikeRepository;

    @Test
    public void getRecentRocketStrikesTest() {
        Region region1 = new Region(1, "Львівщина", "Львівська область", "Lviv", "Lviv");
        RocketStrike rocketStrike1 = new RocketStrike(1, region1, LocalDateTime.now());

        when(regionService.getAllRegions()).thenReturn(List.of(region1));
        when(rocketStrikeRepository.findRocketStrikeByStrikeDateBetweenAndRegion(
                any(LocalDateTime.class), any(LocalDateTime.class), any(Region.class)))
                .thenReturn(List.of(rocketStrike1));

        List<RocketStrike> list =  rocketStrikeService.getRecentRocketStrikes(SORT_BY_REGION, ASC_ORDER);

        verify(rocketStrikeRepository, times(2)).findRocketStrikeByStrikeDateBetweenAndRegion(
                any(LocalDateTime.class), any(LocalDateTime.class), any(Region.class));
        verify(rocketStrikeRepository, times(2)).findAll();
        assertEquals(List.of(rocketStrike1), list);
    }

    @Test
    public void getFilteredRocketStrikesTest() {
        Region region1 = new Region(1, "Львівщина", "Львівська область", "Lviv", "Lviv");
        RocketStrike rocketStrike1 = new RocketStrike(1, region1, LocalDateTime.of(2022, 6, 25, 0, 0, 0));

        LocalDateTime sinceDate = LocalDateTime.of(2022, 6, 20, 0, 0, 0);
        LocalDateTime untilDate = LocalDateTime.of(2022, 6, 28, 0, 0, 0);

        when(rocketStrikeRepository.findRocketStrikeByStrikeDateBetweenAndRegion(sinceDate, untilDate, region1))
                .thenReturn(List.of(rocketStrike1));

        List<RocketStrike> list =  rocketStrikeService.getFilteredRocketStrikes(sinceDate, untilDate,
                List.of(region1), SORT_BY_REGION, ASC_ORDER);

        verify(rocketStrikeRepository, times(2)).findRocketStrikeByStrikeDateBetweenAndRegion(sinceDate, untilDate, region1);
        verify(rocketStrikeRepository, times(2)).findAll();
        assertEquals(List.of(rocketStrike1), list);
    }

    @Test
    public void getFilteredRocketStrikesThrowBadDateRangeExceptionTest() {
        Region region1 = new Region(1, "Львівщина", "Львівська область", "Lviv", "Lviv");

        LocalDateTime sinceDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime untilDate = sinceDate.minusDays(1);

        assertThrows(BadDateRangeException.class, () -> rocketStrikeService.getFilteredRocketStrikes(
                sinceDate, untilDate, List.of(region1), SORT_BY_REGION, ASC_ORDER));
    }

    @Test
    public void getFilteredRocketStrikesThrowNoSelectedRegionsExceptionTest() {
        LocalDateTime untilDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime sinceDate = untilDate.minusDays(1);

        assertThrows(NoSelectedRegionsException.class, () -> rocketStrikeService.getFilteredRocketStrikes(
                sinceDate, untilDate, null, SORT_BY_REGION, ASC_ORDER));
    }

    @Test
    public void getRocketStrikeByIdThrowsRocketStrikeNotFoundExceptionTest() {
        assertThrows(RocketStrikeNotFoundException.class, () -> rocketStrikeService.getRocketStrikeById(anyInt()));
    }

    @Test
    public void getRocketStrikeByIdTest() {
        Region region1 = new Region(1, "Львівщина", "Львівська область", "Lviv", "Lviv");
        RocketStrike rocketStrike1 = new RocketStrike(1, region1, LocalDateTime.now());

        when(rocketStrikeRepository.findById(region1.getId())).thenReturn(Optional.of(rocketStrike1));
        assertEquals(rocketStrikeService.getRocketStrikeById(region1.getId()), rocketStrike1);
    }

    @Test
    public void getFilteredRocketStrikeDTOTest() {
        RocketStrikeDto rocketStrikeDto = Mockito.spy(new RocketStrikeDto());

        LocalDateTime untilDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime sinceDate = untilDate.minusDays(1);
        List<Long> checkedRegionsId = List.of(1L);

        rocketStrikeDto.setSinceDate(sinceDate);
        rocketStrikeDto.setUntilDate(untilDate);
        rocketStrikeDto.setCheckedRegionsId(checkedRegionsId);
        rocketStrikeDto.setSortField(SORT_BY_REGION);
        rocketStrikeDto.setSortDir(ASC_ORDER);

        Region region1 = new Region(1, "Львівщина", "Львівська область", "Lviv", "Lviv");
        RocketStrike rocketStrike1 = new RocketStrike(1, region1, sinceDate);

        when(regionService.getRegionsById(anyList())).thenReturn(List.of(region1));
        when(rocketStrikeRepository.findRocketStrikeByStrikeDateBetweenAndRegion(
                any(LocalDateTime.class), any(LocalDateTime.class), any(Region.class)))
                .thenReturn(List.of(rocketStrike1));

        List<RocketStrike> list =  rocketStrikeService.getFilteredRocketStrikes(rocketStrikeDto);

        assertEquals(List.of(rocketStrike1), list);
    }




}

