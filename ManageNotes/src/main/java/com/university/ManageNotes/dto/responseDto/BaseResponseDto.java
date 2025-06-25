package com.university.ManageNotes.dto.responseDto;

import lombok.Data;

import java.util.Date;

@Data
public abstract class BaseResponseDto {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
}
