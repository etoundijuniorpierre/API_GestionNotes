package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.dto.Request.SemesterRequest;
import com.university.ManageNotes.dto.Response.SemesterResponse;
import com.university.ManageNotes.exception.APIException;
import com.university.ManageNotes.exception.ResourceNotFoundException;
import com.university.ManageNotes.model.Semester;
import com.university.ManageNotes.repository.SemesterRepository;
import com.university.ManageNotes.service.SemesterService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {

    private final SemesterRepository semesterRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<SemesterResponse> getAllSemesters() {
        List<Semester> semesters = semesterRepository.findAll();
        
        return semesters.stream()
            .map(semester -> modelMapper.map(semester, SemesterResponse.class))
            .collect(Collectors.toList());
    }

    @Override
    public SemesterResponse getSemesterById(Long id) {
        Semester semester = semesterRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Semester", "id", id));
        
        return modelMapper.map(semester, SemesterResponse.class);
    }

    @Override
    @Transactional
    public SemesterRequest createSemester(SemesterRequest request) {
        // Map DTO to Entity
        Semester semester = modelMapper.map(request, Semester.class);
        
        // Validate semester name uniqueness
        if (semesterRepository.existsByName(request.getName())) {
            throw new APIException("Semester with name '" + request.getName() + "' already exists");
        }
        
        // Validate dates
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new APIException("Start date cannot be after end date");
        }
        
        // Business rule: Only one active semester at a time
        if (request.getActive() != null && request.getActive()) {
            semesterRepository.findByActiveTrue().ifPresent(activeSemester -> {
                throw new APIException("Another semester is already active. Deactivate it first.");
            });
        }
        
        // Validate maximum 2 semesters per academic year
        validateMaxSemestersPerYear(request.getStartDate());
        
        // Save entity and map to request DTO
        Semester savedSemester = semesterRepository.save(semester);
        return modelMapper.map(savedSemester, SemesterRequest.class);
    }

    @Override
    @Transactional
    public SemesterRequest updateSemester(Long id, SemesterRequest request) {
        Semester existingSemester = semesterRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Semester", "id", id));
        
        // Update fields
        if (request.getName() != null && !request.getName().equals(existingSemester.getName())) {
            if (semesterRepository.existsByName(request.getName())) {
                throw new APIException("Semester with name '" + request.getName() + "' already exists");
            }
            existingSemester.setName(request.getName());
        }
        
        if (request.getStartDate() != null) {
            existingSemester.setStartDate(request.getStartDate());
        }
        
        if (request.getEndDate() != null) {
            existingSemester.setEndDate(request.getEndDate());
        }
        
        // Validate dates
        if (existingSemester.getStartDate().isAfter(existingSemester.getEndDate())) {
            throw new APIException("Start date cannot be after end date");
        }
        
        // Handle active status change
        if (request.getActive() != null) {
            if (request.getActive() && !existingSemester.getActive()) {
                // Activating this semester - deactivate others
                semesterRepository.findByActiveTrue().ifPresent(activeSemester -> {
                    if (!activeSemester.getSemesterId().equals(id)) {
                        throw new APIException("Another semester is already active. Deactivate it first.");
                    }
                });
            }
            existingSemester.setActive(request.getActive());
        }
        
        // Save entity and map to request DTO
        Semester updatedSemester = semesterRepository.save(existingSemester);
        return modelMapper.map(updatedSemester, SemesterRequest.class);
    }

    @Override
    @Transactional
    public void deleteSemester(Long id) {
        Semester semester = semesterRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Semester", "id", id));
        
        // Prevent deletion of active semester
        if (semester.getActive()) {
            throw new APIException("Cannot delete an active semester. Deactivate it first.");
        }
        
        semesterRepository.delete(semester);
    }

    // Helper method to validate max 2 semesters per academic year
    private void validateMaxSemestersPerYear(LocalDate startDate) {
        int year = startDate.getYear();
        long semesterCount = semesterRepository.findAll().stream()
            .filter(s -> s.getStartDate().getYear() == year)
            .count();
            
        if (semesterCount >= 2) {
            throw new APIException("Maximum 2 semesters allowed per academic year (" + year + ")");
        }
    }
}