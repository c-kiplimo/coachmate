package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.DaysOfTheWeek;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.repository.DaysOfTheWeekRepository;
import com.natujenge.thecouch.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
public class DaysOfTheWeekService {
    private final DaysOfTheWeekRepository daysOfTheWeekRepository;
    private final UserRepository userRepository;

    public DaysOfTheWeekService(DaysOfTheWeekRepository daysOfTheWeekRepository, UserRepository userRepository) {
        this.daysOfTheWeekRepository = daysOfTheWeekRepository;
        this.userRepository = userRepository;
    }

    public List<DaysOfTheWeek> getDaysOfTheWeek(Long coachId) {
        log.info("Getting days of the week");
        //find all by coach and if null insert
        // find coach by id
        User coach = userRepository.findById(coachId).orElseThrow(() -> new IllegalArgumentException("Coach not found"));

        if (coach == null) {
            log.warn("Coach with id {} not found", coachId);
            throw new IllegalArgumentException("Coach not found");
        }
        List<DaysOfTheWeek> daysOfTheWeek = daysOfTheWeekRepository.findAllByCoach(coach);

        if (daysOfTheWeek.isEmpty()) {
            //insert days of the week
            DaysOfTheWeek[] daysOfTheWeeks = new DaysOfTheWeek[7];
            daysOfTheWeeks[0] = new DaysOfTheWeek();
            daysOfTheWeeks[0].setDay("SUNDAY");
            daysOfTheWeeks[0].setAvailable(false);
            daysOfTheWeeks[0].setCoach(coach);

            daysOfTheWeeks[1] = new DaysOfTheWeek();
            daysOfTheWeeks[1].setDay("MONDAY");
            daysOfTheWeeks[1].setAvailable(true);
            daysOfTheWeeks[1].setCoach(coach);

            daysOfTheWeeks[2] = new DaysOfTheWeek();
            daysOfTheWeeks[2].setDay("TUESDAY");
            daysOfTheWeeks[2].setAvailable(true);
            daysOfTheWeeks[2].setCoach(coach);

            daysOfTheWeeks[3] = new DaysOfTheWeek();
            daysOfTheWeeks[3].setDay("WEDNESDAY");
            daysOfTheWeeks[3].setAvailable(true);
            daysOfTheWeeks[3].setCoach(coach);

            daysOfTheWeeks[4] = new DaysOfTheWeek();
            daysOfTheWeeks[4].setDay("THURSDAY");
            daysOfTheWeeks[4].setAvailable(true);
            daysOfTheWeeks[4].setCoach(coach);

            daysOfTheWeeks[5] = new DaysOfTheWeek();
            daysOfTheWeeks[5].setDay("FRIDAY");
            daysOfTheWeeks[5].setAvailable(true);
            daysOfTheWeeks[5].setCoach(coach);

            daysOfTheWeeks[6] = new DaysOfTheWeek();
            daysOfTheWeeks[6].setDay("SATURDAY");
            daysOfTheWeeks[6].setAvailable(false);
            daysOfTheWeeks[6].setCoach(coach);

            for (DaysOfTheWeek day : daysOfTheWeeks) {
                insertDaysOfTheWeek(day);
            }

            daysOfTheWeek = daysOfTheWeekRepository.findAllByCoach(coach);

        }
        return daysOfTheWeek;
    }



  public void insertDaysOfTheWeek(DaysOfTheWeek day) {
        log.info("Inserting days of the week into the database");

        DaysOfTheWeek daysOfTheWeek = new DaysOfTheWeek();
        daysOfTheWeek.setAvailable(day.isAvailable());
        daysOfTheWeek.setDay(day.getDay());
        daysOfTheWeek.setCoach(day.getCoach());

        daysOfTheWeekRepository.save(daysOfTheWeek);
    }


    public DaysOfTheWeek updateDayOfTheWeek(Long id, DaysOfTheWeek daysOfTheWeek) {
        //find by id
        DaysOfTheWeek day = daysOfTheWeekRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Day not found"));
        if (day != null) {
            day.setAvailable(daysOfTheWeek.isAvailable());
            return daysOfTheWeekRepository.save(day);
        }
        return null;
    }

    public DaysOfTheWeek getDaysOfTheWeekById(Long id) {
        return daysOfTheWeekRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Day not found"));
    }
}
