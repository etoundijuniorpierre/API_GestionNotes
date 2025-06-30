package com.university.ManageNotes.service;

import com.university.ManageNotes.model.*;
import com.university.ManageNotes.repository.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private AuthService authService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public byte[] generateStudentTranscript(Long studentId, Long semesterId) throws IOException {
        Students student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Grades> grades;
        String reportTitle;

        if (semesterId != null) {
            Semesters semester = semesterRepository.findById(semesterId)
                    .orElseThrow(() -> new RuntimeException("Semester not found"));
            grades = gradeRepository.findByStudentAndSemesters(student, semester);
            reportTitle = "Semester Transcript - " + semester.getName();
        } else {
            grades = gradeRepository.findByStudent(student);
            reportTitle = "Complete Academic Transcript";
        }

        return generatePDFReport(student, grades, reportTitle);
    }

    @PreAuthorize("hasRole('TEACHER')")
    public byte[] generateSubjectGradesReport(Long subjectId) throws IOException {
        Users currentUser = authService.getCurrentUser();

        // Verify teacher teaches this subject
        List<Grades> allGrades = gradeRepository.findGradesEnteredByTeacher(currentUser.getId());
        List<Grades> grades = allGrades.stream()
                .filter(grade -> grade.getSubject().getId().equals(subjectId))
                .collect(Collectors.toList());
        if (grades.isEmpty()) {
            throw new RuntimeException("No grades found for this subject or unauthorized access");
        }

        String subjectName = grades.get(0).getSubject().getName();
        String reportTitle = "Subject Grades Report - " + subjectName;

        return generateSubjectPDFReport(grades, reportTitle);
    }

    private byte[] generatePDFReport(Students student, List<Grades> grades, String reportTitle) throws IOException {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float margin = 50;
                float yStart = page.getMediaBox().getHeight() - margin;
                float tableTop = yStart - 100;
                float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
                float yPosition = tableTop;
                float rowHeight = 20f;

                // Header
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.newLineAtOffset(margin, yStart);
                contentStream.showText("UNIVERSITY GRADE MANAGEMENT SYSTEM");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.newLineAtOffset(margin, yStart - 30);
                contentStream.showText(reportTitle);
                contentStream.endText();

                // Student Info
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(margin, yStart - 60);
                contentStream.showText("Student: " + student.getFirstName() + " " + student.getLastName());
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yStart - 75);
                contentStream.showText("Student Number: " + student.getStudentNumber());
                contentStream.endText();

                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yStart - 90);
                contentStream.showText("Level: " + student.getLevel());
                contentStream.endText();

                // Table Headers
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Subject");
                contentStream.newLineAtOffset(120, 0);
                contentStream.showText("Grade");
                contentStream.newLineAtOffset(60, 0);
                contentStream.showText("Coefficient");
                contentStream.newLineAtOffset(80, 0);
                contentStream.showText("Type");
                contentStream.newLineAtOffset(80, 0);
                contentStream.showText("Semester");
                contentStream.endText();

                yPosition -= rowHeight;

                // Table Data
                Map<Subject, List<Grades>> gradesBySubject = grades.stream()
                        .collect(Collectors.groupingBy(Grades::getSubject));

                double totalGpa = 0;
                double totalCredits = 0;

                for (Map.Entry<Subject, List<Grades>> entry : gradesBySubject.entrySet()) {
                    Subject subject = entry.getKey();
                    List<Grades> subjectGrades = entry.getValue();

                    double subjectAverage = calculateWeightedAverage(subjectGrades);
                    totalGpa += subjectAverage * subject.getCredits().doubleValue();
                    totalCredits += subject.getCredits().doubleValue();

                    for (Grades grade : subjectGrades) {
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA, 9);
                        contentStream.newLineAtOffset(margin, yPosition);
                        contentStream.showText(truncateText(subject.getName(), 15));
                        contentStream.newLineAtOffset(120, 0);
                        contentStream.showText(String.format("%.2f", grade.getValue()));
                        contentStream.newLineAtOffset(60, 0);
                        contentStream.showText(String.format("%.1f", grade.getCoefficient()));
                        contentStream.newLineAtOffset(80, 0);
                        contentStream.showText(grade.getGradeType().name());
                        contentStream.newLineAtOffset(80, 0);
                        contentStream.showText(truncateText(grade.getSemesters().getName(), 12));
                        contentStream.endText();

                        yPosition -= rowHeight;

                        // Add new page if needed
                        if (yPosition < margin) {
                            contentStream.close();
                            page = new PDPage();
                            document.addPage(page);
                            contentStream = new PDPageContentStream(document, page);
                            yPosition = yStart - margin;
                        }
                    }

                    // Subject average
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 9);
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText("Subject Average:");
                    contentStream.newLineAtOffset(120, 0);
                    contentStream.showText(String.format("%.2f", subjectAverage));
                    contentStream.endText();

                    yPosition -= rowHeight * 1.5f;
                }

                // Overall GPA
                double overallGpa = totalCredits > 0 ? totalGpa / totalCredits : 0;
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(margin, yPosition - 20);
                contentStream.showText("Overall GPA: " + String.format("%.2f", overallGpa));
                contentStream.endText();

                // Footer
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 8);
                contentStream.newLineAtOffset(margin, 50);
                contentStream.showText("Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                contentStream.endText();
            }

            document.save(out);
            return out.toByteArray();
        }
    }

    private byte[] generateSubjectPDFReport(List<Grades> grades, String reportTitle) throws IOException {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float margin = 50;
                float yStart = page.getMediaBox().getHeight() - margin;
                float yPosition = yStart - 100;
                float rowHeight = 20f;

                // Header
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.newLineAtOffset(margin, yStart);
                contentStream.showText("SUBJECT GRADES REPORT");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.newLineAtOffset(margin, yStart - 30);
                contentStream.showText(reportTitle);
                contentStream.endText();

                // Table Headers
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Student Name");
                contentStream.newLineAtOffset(150, 0);
                contentStream.showText("Grade");
                contentStream.newLineAtOffset(60, 0);
                contentStream.showText("Type");
                contentStream.newLineAtOffset(80, 0);
                contentStream.showText("Comments");
                contentStream.endText();

                yPosition -= rowHeight;

                // Data
                for (Grades grade : grades) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 9);
                    contentStream.newLineAtOffset(margin, yPosition);
                    String studentName = grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName();
                    contentStream.showText(truncateText(studentName, 20));
                    contentStream.newLineAtOffset(150, 0);
                    contentStream.showText(String.format("%.2f", grade.getValue()));
                    contentStream.newLineAtOffset(60, 0);
                    contentStream.showText(grade.getGradeType().name());
                    contentStream.newLineAtOffset(80, 0);
                    contentStream.showText(truncateText(grade.getComments() != null ? grade.getComments() : "", 25));
                    contentStream.endText();

                    yPosition -= rowHeight;

                    // Add new page if needed
                    if (yPosition < margin) {
                        contentStream.close();
                        page = new PDPage();
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        yPosition = yStart - margin;
                    }
                }
            }

            document.save(out);
            return out.toByteArray();
        }
    }

    private double calculateWeightedAverage(List<Grades> grades) {
        if (grades.isEmpty()) return 0;

        double totalWeightedSum = 0;
        double totalCoefficients = 0;

        for (Grades grade : grades) {
            totalWeightedSum += grade.getValue() * grade.getCoefficient();
            totalCoefficients += grade.getCoefficient();
        }

        return totalCoefficients > 0 ? totalWeightedSum / totalCoefficients : 0;
    }

    private String truncateText(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }
}
