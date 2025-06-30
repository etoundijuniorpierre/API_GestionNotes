package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsRequest extends AbstractEntity {
    private List<Long> semesterIds;
    private List<Long> subjectIds;
    private List<Long> studentIds;
    private String groupBy; // "SEMESTER", "SUBJECT", "STUDENT", "MONTH"
    private String metricType;

}

