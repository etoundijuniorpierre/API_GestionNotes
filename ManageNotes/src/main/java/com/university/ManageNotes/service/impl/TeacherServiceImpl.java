package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.dto.Request.TeacherRequest;
import com.university.ManageNotes.dto.Response.TeacherResponse;
import com.university.ManageNotes.exception.ResourceNotFoundException;
import com.university.ManageNotes.model.Teacher;
import com.university.ManageNotes.model.TeachingLevel;
import com.university.ManageNotes.repository.TeacherRepository;
import com.university.ManageNotes.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;

    private final ModelMapper modelMapper;

    @Override
    public TeacherRequest updateTeacher(Long teacherId, TeacherRequest request) {
        Teacher teacher = modelMapper.map(request, Teacher.class);

        Teacher teacherFromDb = this.teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", teacherId));

        // Update Teacher entity
        teacherFromDb.setFirstName(teacher.getFirstName());
        teacherFromDb.setLastName(teacher.getLastName());
        teacherFromDb.setEmail(teacher.getEmail());
        teacherFromDb.setUsername(teacher.getUsername());
        teacherFromDb.setDepartment(teacher.getDepartment());
        teacherFromDb.setPhoneNumber(teacher.getPhoneNumber());
        teacherFromDb.setTeachingLevel(teacher.getTeachingLevel());
        teacherFromDb.setRoles(teacher.getRoles());
        teacherFromDb.setIsActive(teacher.getIsActive());
        return modelMapper.map(teacherRepository.save(teacherFromDb), TeacherRequest.class);
    }

    @Override
    public TeacherResponse teacherProfile(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Teacher teacher = teacherRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", userDetails.getId()));

        TeacherRequest request = modelMapper.map(teacher, TeacherRequest.class);

        TeacherResponse response = new TeacherResponse();

        response.setFirstName(request.getFirstName());
        response.setLastName(request.getLastName());
        response.setEmail(request.getEmail());
        response.setUsername(request.getUsername());
        response.setDepartment(request.getDepartment());
        response.setPhoneNumber(request.getPhoneNumber());
        response.setTeachingLevel((Set<TeachingLevel>) request.getTeachingLevel());
        response.setIsActive(teacher.getIsActive());
        return response;
    }
}
