package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.dto.Request.SubjectRequest;
import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.exception.APIException;
import com.university.ManageNotes.exception.ResourceNotFoundException;
import com.university.ManageNotes.model.Subject;
import com.university.ManageNotes.model.Teacher;
import com.university.ManageNotes.repository.SubjectRepository;
import com.university.ManageNotes.repository.TeacherRepository;
import com.university.ManageNotes.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;

    @Override
    public SubjectResponse getAllSubjects(Integer pageNumber,
                                   Integer pageSize,
                                   String sortBy, String sortOrder){
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page subjectPage = this.subjectRepository.findAll(pageable);

        List<Subject> subjectList = subjectPage.getContent();

        if(subjectList.isEmpty()){
            throw new APIException("No subjects found");
        }

        List<SubjectRequest> subjectRequests = subjectList.stream()
                .map(sub -> modelMapper.map(sub, SubjectRequest.class))
                .toList();

        SubjectResponse subjectResponse = new SubjectResponse();

        subjectResponse.setContent(subjectRequests);
        subjectResponse.setPageNumber(subjectPage.getNumber());
        subjectResponse.setPageSize(subjectPage.getSize());
        subjectResponse.setTotalElements(subjectPage.getTotalElements());
        subjectResponse.setTotalPages(subjectPage.getTotalPages());
        subjectResponse.setLastPage(subjectPage.isLast());
        return subjectResponse;
    }

    @Override
    public SubjectResponse getAllSubjectsByTeacher(Long teacherId,
                                                         Integer pageNumber,
                                                         Integer pageSize,
                                                         String sortBy, String sortOrder){
        Teacher teacherDb = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", teacherId));

        List<Subject> subjectByTeacherDb = subjectRepository.findSubjectByTeacher_Email(teacherDb.getEmail());

        if(subjectByTeacherDb.isEmpty()){
            throw new APIException("No Subjects were found for this teacher with email " + teacherDb.getFirstName());
        }

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page subjectPage = this.subjectRepository.findAll(pageable);

        List<Subject> subjectList = subjectPage.getContent();

        if(subjectList.isEmpty()){
            throw new APIException("No subjects found");
        }

        List<SubjectRequest> subjectRequests = subjectList.stream()
                .map(sub -> modelMapper.map(sub, SubjectRequest.class))
                .toList();

        SubjectResponse subjectResponse = new SubjectResponse();

        subjectResponse.setContent(subjectRequests);
        subjectResponse.setPageNumber(subjectPage.getNumber());
        subjectResponse.setPageSize(subjectPage.getSize());
        subjectResponse.setTotalElements(subjectPage.getTotalElements());
        subjectResponse.setTotalPages(subjectPage.getTotalPages());
        subjectResponse.setLastPage(subjectPage.isLast());
        return subjectResponse;
    }

    @Override
    public SubjectRequest createSubject(SubjectRequest request) {
        Subject subject = modelMapper.map(request, Subject.class);

        Subject subjectFromDb = this.subjectRepository.findBySubjectCode(subject.getSubjectCode())
                .orElseThrow(() -> new ResourceNotFoundException("Subject", "code", request.getSubjectCode()));

        if(subjectFromDb != null) {
            throw new APIException("Subject with code " + subject.getSubjectCode() + " already exists");
        }

        // Check if teacher is already assigned to a subject at this level
        if (subject.getTeacher().getId() != null &&
                subjectRepository.existsByTeacherIdAndSubjectsLevel(subjectFromDb.getTeacher().getId(),
                        subjectFromDb.getSubjectsLevel())) {
            throw  new APIException("Teacher is already assigned to a subject at this level");
        }
        return modelMapper.map(subjectRepository.save(subject), SubjectRequest.class);
    }

    @Override
    @Transactional
    public SubjectRequest updateSubject(Long subjectId, SubjectRequest request) {
        Subject subjectFromDb = this.subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject", "id", subjectId));

        Subject subject = modelMapper.map(subjectFromDb, Subject.class);

        subjectFromDb.setSubjectName(subject.getSubjectName());
        subjectFromDb.setSubjectCode(subject.getSubjectCode());
        subjectFromDb.setCredits((subject.getCredits() == null) ? null : (subject.getCredits()));
        subjectFromDb.setDescription(request.getDescription());

        // Set level and cycle
        if (subject.getSubjectsLevel() != null) {
            subjectFromDb.setSubjectsLevel(subject.getSubjectsLevel());
        }

        if (subject.getStudentcycle() != null) {
            subjectFromDb.setStudentcycle(subject.getStudentcycle());
        }

        // Handle teacher assignment
        if (subject.getTeacher().getId() != null) {
            subjectFromDb.setTeacher(subject.getTeacher());
        }

        if (subject.getSemester() != null) {
            subjectFromDb.setSemester(subject.getSemester());
        }

        if (subject.getDepartment() != null) {
            subjectFromDb.setDepartment(subject.getDepartment());
        }

        return modelMapper.map(subjectRepository.save(subjectFromDb), SubjectRequest.class);
    }

    @Override
    public SubjectRequest deleteSubject(Long id) {
        Subject subjectFoundDb = this.subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject", "id", id));

        this.subjectRepository.delete(subjectFoundDb);
        return modelMapper.map(subjectFoundDb, SubjectRequest.class);
    }
}
