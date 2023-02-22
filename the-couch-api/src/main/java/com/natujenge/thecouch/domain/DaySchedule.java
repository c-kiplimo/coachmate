package com.natujenge.thecouch.domain;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import java.util.List;

public class DaySchedule {
    private String startTime;
    private String endTime;

    @ElementCollection
    @CollectionTable(name = "schedule_breaks")
    private List<DayBreaks> breaks;


}
