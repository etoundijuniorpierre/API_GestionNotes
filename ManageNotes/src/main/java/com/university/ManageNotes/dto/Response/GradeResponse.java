package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.AbstractEntity;
import com.university.ManageNotes.model.GradeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class GradeResponse extends AbstractEntity {


    private Long studentId;
    private String studentName;
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private Long semesterId;
    private String semesterName;
    private Double value;
    private Double coefficient;
    private GradeType gradeType;
    private String comments;
    private Long enteredBy;
    private String enteredByName;




}
