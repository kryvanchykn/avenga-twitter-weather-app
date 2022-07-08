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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.ASC_ORDER;
import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.SORT_BY_REGION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RocketStrikeServiceTest {
    private static final Region REGION = new Region(1, "Львівська область", "Львівщина", "Lviv", "Lviv");
    private static final RocketStrike RECENT_ROCKET_STRIKE = new RocketStrike(1, REGION, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
    private static final LocalDateTime UNTIL_DATE = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    private static final LocalDateTime SINCE_DATE = UNTIL_DATE.minusDays(1);

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
        when(regionService.getAllRegions()).thenReturn(List.of(REGION));
        when(rocketStrikeRepository.findRocketStrikeByStrikeDateBetweenAndRegion(
                any(LocalDateTime.class), any(LocalDateTime.class), any(Region.class)))
                .thenReturn(List.of(RECENT_ROCKET_STRIKE));

        List<RocketStrike> list = rocketStrikeService.getRecentRocketStrikes(SORT_BY_REGION, ASC_ORDER);

        verify(rocketStrikeRepository, times(2)).findRocketStrikeByStrikeDateBetweenAndRegion(
                any(LocalDateTime.class), any(LocalDateTime.class), any(Region.class));
        verify(rocketStrikeRepository, times(2)).findAll();
        assertEquals(List.of(RECENT_ROCKET_STRIKE), list);
    }

    @Test
    public void getFilteredRocketStrikesTest() {
        RocketStrike rocketStrike = new RocketStrike(1, REGION, LocalDateTime.of(2022, 6, 25, 0, 0, 0));

        LocalDateTime sinceDate = LocalDateTime.of(2022, 6, 20, 0, 0, 0);
        LocalDateTime untilDate = LocalDateTime.of(2022, 6, 28, 0, 0, 0);

        when(rocketStrikeRepository.findRocketStrikeByStrikeDateBetweenAndRegion(sinceDate, untilDate, REGION))
                .thenReturn(List.of(rocketStrike));

        List<RocketStrike> list = rocketStrikeService.getFilteredRocketStrikes(sinceDate, untilDate,
                List.of(REGION), SORT_BY_REGION, ASC_ORDER);

        verify(rocketStrikeRepository, times(2)).findRocketStrikeByStrikeDateBetweenAndRegion(sinceDate, untilDate, REGION);
        verify(rocketStrikeRepository, times(2)).findAll();
        assertEquals(List.of(rocketStrike), list);
    }

    @Test
    public void getFilteredRocketStrikesThrowBadDateRangeExceptionTest() {
        assertThrows(BadDateRangeException.class, () -> rocketStrikeService.getFilteredRocketStrikes(
                UNTIL_DATE, SINCE_DATE, List.of(REGION), SORT_BY_REGION, ASC_ORDER));
    }

    @Test
    public void getFilteredRocketStrikesThrowNoSelectedRegionsExceptionTest() {
        assertThrows(NoSelectedRegionsException.class, () -> rocketStrikeService.getFilteredRocketStrikes(
                SINCE_DATE, UNTIL_DATE, null, SORT_BY_REGION, ASC_ORDER));
    }

    @Test
    public void getRocketStrikeByIdThrowsRocketStrikeNotFoundExceptionTest() {
        assertThrows(RocketStrikeNotFoundException.class, () -> rocketStrikeService.getRocketStrikeById(anyInt()));
    }

    @Test
    public void getRocketStrikeByIdTest() {
        when(rocketStrikeRepository.findById(REGION.getId())).thenReturn(Optional.of(RECENT_ROCKET_STRIKE));
        assertEquals(rocketStrikeService.getRocketStrikeById(REGION.getId()), RECENT_ROCKET_STRIKE);
    }

    @Test
    public void getFilteredRocketStrikeDTOTest() {
        RocketStrikeDto rocketStrikeDto = Mockito.spy(new RocketStrikeDto());
        List<Long> checkedRegionsId = List.of(1L);

        rocketStrikeDto.setSinceDate(SINCE_DATE);
        rocketStrikeDto.setUntilDate(UNTIL_DATE);
        rocketStrikeDto.setCheckedRegionsId(checkedRegionsId);
        rocketStrikeDto.setSortField(SORT_BY_REGION);
        rocketStrikeDto.setSortDir(ASC_ORDER);

        when(regionService.getRegionsById(anyList())).thenReturn(List.of(REGION));
        when(rocketStrikeRepository.findRocketStrikeByStrikeDateBetweenAndRegion(
                any(LocalDateTime.class), any(LocalDateTime.class), any(Region.class)))
                .thenReturn(List.of(RECENT_ROCKET_STRIKE));

        List<RocketStrike> list = rocketStrikeService.getFilteredRocketStrikes(rocketStrikeDto);

        assertEquals(List.of(RECENT_ROCKET_STRIKE), list);
    }


}

