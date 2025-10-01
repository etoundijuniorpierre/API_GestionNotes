package com.university.ManageNotes.controller;

import com.university.ManageNotes.config.AppConstant;
import com.university.ManageNotes.dto.Request.DepartmentRequest;
import com.university.ManageNotes.dto.Response.DepartmentResponse;
import com.university.ManageNotes.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Department Management", description = "CRUD operations and department-specific functionality")
public class DepartmentController {
    
    private final DepartmentService departmentService;

    @PostMapping("/admin/department")
    @Operation(summary = "Create department context", description = "This endpoint is used to create a new department.")
    public ResponseEntity<DepartmentRequest> createDepartement(@RequestBody DepartmentRequest request) {
        return new ResponseEntity<>(departmentService.createDepartment(request),
                HttpStatus.CREATED);
    }

    @GetMapping("/admin/department")
    @Operation(summary = "Get All department", description = "This endpoint retrieves all the departments in a paginated format")
    public ResponseEntity<DepartmentResponse> getAllDepartment(@RequestParam(name = "pageNumber",
                                                           defaultValue = AppConstant.PAGE_NUMBER,
                                                           required = false) Integer pageNumber,
                                               @RequestParam(name = "pageSize",
                                                       defaultValue = AppConstant.PAGE_SIZE,
                                                       required = false) Integer pageSize,
                                               @RequestParam(name = "sortBy",
                                                       defaultValue = AppConstant.SORT_DEPARTMENT_BY,
                                                       required = false) String sortBy,
                                               @RequestParam(name = "sortOrder",
                                                       defaultValue = AppConstant.SORT_DIR,
                                                       required = false) String sortOrder) {
        return new ResponseEntity<>(departmentService.getAllDepartments(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    @PutMapping("/admin/department/{departmentId}")
    @Operation(summary = "Update a department information", description = "This endpoint update the department from a departmentId")
    public ResponseEntity<DepartmentRequest> updateDepartment(@Valid @RequestBody DepartmentRequest departmentRequest,
                                                          @PathVariable Long departmentId) {
        DepartmentRequest savedDepartement = departmentService.updateDepartment(departmentRequest, departmentId);
        return new ResponseEntity<>(savedDepartement, HttpStatus.OK);
    }

    @DeleteMapping("/admin/department/{departmentId}")
    @Operation(summary = "Delete a department information", description = "This endpoint delete the department from a departmentId")
    public ResponseEntity<DepartmentRequest> deleteDepartment(@PathVariable Long departmentId){
        DepartmentRequest deletedDepartment = departmentService.deleteDepartment(departmentId);
        return new ResponseEntity<>(deletedDepartment, HttpStatus.OK);
    }
}
