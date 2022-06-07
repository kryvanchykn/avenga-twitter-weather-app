package com.example.avengatwitterweatherapp.repository.impl;

import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.repository.RocketStrikeRepositoryCustom;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.HashSet;


@Repository
@Transactional(readOnly = true)
public class RocketStrikeRepositoryImpl implements RocketStrikeRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public HashSet<RocketStrike> findSortedRocketStrikeByStrikeDateBetween(String sinceDate, String untilDate) {
        Query query = entityManager.createNativeQuery("SELECT * FROM rocket_strikes " +
                "WHERE strike_date >= ? AND strike_date <= ?", RocketStrike.class);
        query.setParameter(1, sinceDate);
        query.setParameter(2, untilDate);

        return new HashSet<RocketStrike>(query.getResultList());

    }
}
