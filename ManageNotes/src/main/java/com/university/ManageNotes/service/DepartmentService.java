package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.DepartmentRequest;
import com.university.ManageNotes.dto.Response.DepartmentResponse;


public interface DepartmentService{

    DepartmentResponse getAllDepartments(Integer pageNumber,
                                               Integer pageSize,
                                               String sortBy, String sortOrder);
    DepartmentRequest createDepartment(DepartmentRequest request);

    DepartmentRequest updateDepartment(DepartmentRequest departmentRequest, Long departmentId);

    DepartmentRequest deleteDepartment(Long departmentId);
}

