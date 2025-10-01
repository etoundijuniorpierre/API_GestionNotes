# ManageNotes API Documentation

**Base URL**: `http://localhost:3030/api`

## Authentication Endpoints

### 1. User Login
- **URL**: `POST /auth/login`
- **Description**: Authenticate user and get JWT token
- **Request Body**: `LoginRequest`
- **Response**: `LoginResponse`
- **Parameters**: None

### 2. User Registration (Admin Only)
- **URL**: `POST /auth/admin/register`
- **Description**: Admin registers new users (students/teachers)
- **Request Body**: `SignupRequest`
- **Response**: `MessageResponse`
- **Parameters**: None
- **Authorization**: Admin role required

### 3. Get Admin Profile
- **URL**: `GET /auth/admin/profile`
- **Description**: Get current admin profile information
- **Request Body**: None
- **Response**: `UserResponse`
- **Parameters**: None
- **Authorization**: Admin role required

### 4. Change Password
- **URL**: `POST /auth/password`
- **Description**: Update user password
- **Request Body**: `PasswordChangeRequest`
- **Response**: `MessageResponse`
- **Parameters**: None
- **Authorization**: Authenticated user required

### 5. User Logout
- **URL**: `POST /auth/logout`
- **Description**: Log out current user
- **Request Body**: None
- **Response**: `MessageResponse`
- **Parameters**: None

## Department Management (Admin Only)

### 6. Create Department
- **URL**: `POST /admin/department`
- **Description**: Create new department
- **Request Body**: `DepartmentRequest`
- **Response**: `DepartmentRequest`
- **Parameters**: None

### 7. Get All Departments
- **URL**: `GET /admin/department`
- **Description**: Retrieve all departments with pagination
- **Request Body**: None
- **Response**: `DepartmentResponse`
- **Parameters**: 
  - `pageNumber` (optional, default: 0)
  - `pageSize` (optional, default: 50)
  - `sortBy` (optional, default: departmentName)
  - `sortOrder` (optional, default: asc)

### 8. Update Department
- **URL**: `PUT /admin/department/{departmentId}`
- **Description**: Update department information
- **Request Body**: `DepartmentRequest`
- **Response**: `DepartmentRequest`
- **Parameters**: `departmentId` (path parameter)

### 9. Delete Department
- **URL**: `DELETE /admin/department/{departmentId}`
- **Description**: Delete department
- **Request Body**: None
- **Response**: `DepartmentRequest`
- **Parameters**: `departmentId` (path parameter)

## Grade Management

### 10. Create Grade (Teacher)
- **URL**: `POST /teacher/grade`
- **Description**: Teacher creates new grade entry
- **Request Body**: `GradeRequest`
- **Response**: `GradeRequest`
- **Parameters**: None
- **Authorization**: Teacher role required

### 11. Update Grade (Teacher)
- **URL**: `PUT /teacher/grade/{gradeId}`
- **Description**: Teacher updates existing grade
- **Request Body**: `GradeRequest`
- **Response**: `GradeRequest`
- **Parameters**: `gradeId` (path parameter)
- **Authorization**: Teacher role required

### 12. Delete Grade (Teacher)
- **URL**: `DELETE /teacher/grade/{gradeId}`
- **Description**: Teacher deletes grade entry
- **Request Body**: None
- **Response**: `MessageResponse`
- **Parameters**: `gradeId` (path parameter)
- **Authorization**: Teacher role required

## Revendication Management

### 13. Create Revendication (Student)
- **URL**: `POST /student/revendication`
- **Description**: Student submits grade revendication request
- **Request Body**: `RevendicationRequest`
- **Response**: `RevendicationRequest`
- **Parameters**: None
- **Authorization**: Student role required

### 14. Get Teacher Revendications
- **URL**: `GET /teacher/revendications`
- **Description**: Teacher views pending revendications
- **Request Body**: None
- **Response**: `RevendicationResponse`
- **Parameters**: 
  - `pageNumber` (optional, default: 0)
  - `pageSize` (optional, default: 50)
  - `sortBy` (optional, default: revendicationId)
  - `sortOrder` (optional, default: asc)
- **Authorization**: Teacher role required

### 15. Approve Revendication (Teacher)
- **URL**: `POST /teacher/revendication/{id}/approve`
- **Description**: Teacher approves grade revendication
- **Request Body**: None
- **Response**: `MessageResponse`
- **Parameters**: 
  - `id` (path parameter)
  - `comment` (optional query parameter)
- **Authorization**: Teacher role required

### 16. Reject Revendication (Teacher)
- **URL**: `POST /teacher/revendication/{id}/reject`
- **Description**: Teacher rejects grade revendication
- **Request Body**: None
- **Response**: `MessageResponse`
- **Parameters**: 
  - `id` (path parameter)
  - `reason` (optional query parameter)
- **Authorization**: Teacher role required

## Revendication Period Management

### 17. Get All Revendication Periods
- **URL**: `GET /revendication-period`
- **Description**: Retrieve all revendication periods
- **Request Body**: None
- **Response**: `List<RevendicationPeriodResponse>`
- **Parameters**: None

### 18. Create Revendication Period (Admin)
- **URL**: `POST /admin/revendication-period`
- **Description**: Admin creates new revendication period
- **Request Body**: `RevendicationPeriodRequest`
- **Response**: `RevendicationPeriodRequest`
- **Parameters**: None
- **Authorization**: Admin role required

### 19. Update Revendication Period (Admin)
- **URL**: `PUT /admin/revendication-period/{id}`
- **Description**: Admin updates revendication period
- **Request Body**: `RevendicationPeriodRequest`
- **Response**: `RevendicationPeriodRequest`
- **Parameters**: `id` (path parameter)
- **Authorization**: Admin role required

### 20. Delete Revendication Period (Admin)
- **URL**: `DELETE /admin/revendication-period/{id}`
- **Description**: Admin deletes revendication period
- **Request Body**: None
- **Response**: `MessageResponse`
- **Parameters**: `id` (path parameter)
- **Authorization**: Admin role required

### 21. Get Active Revendication Periods
- **URL**: `GET /revendication-period/active`
- **Description**: Retrieve all active revendication periods
- **Request Body**: None
- **Response**: `List<RevendicationPeriodResponse>`
- **Parameters**: None

## Semester Management

### 22. Get All Semesters
- **URL**: `GET /semesters`
- **Description**: Retrieve all semesters
- **Request Body**: None
- **Response**: `List<SemesterResponse>`
- **Parameters**: None

### 23. Create Semester (Admin)
- **URL**: `POST /admin/semester`
- **Description**: Admin creates new semester
- **Request Body**: `SemesterRequest`
- **Response**: `SemesterRequest`
- **Parameters**: None
- **Authorization**: Admin role required

### 24. Update Semester (Admin)
- **URL**: `PUT /admin/semester/{id}`
- **Description**: Admin updates semester
- **Request Body**: `SemesterRequest`
- **Response**: `SemesterRequest`
- **Parameters**: `id` (path parameter)
- **Authorization**: Admin role required

### 25. Delete Semester (Admin)
- **URL**: `DELETE /admin/semester/{id}`
- **Description**: Admin deletes semester
- **Request Body**: None
- **Response**: `MessageResponse`
- **Parameters**: `id` (path parameter)
- **Authorization**: Admin role required

## Student Management

### 26. Update Student (Admin)
- **URL**: `PUT /admin/student/{id}`
- **Description**: Admin updates student information
- **Request Body**: `StudentRequest`
- **Response**: `StudentRequest`
- **Parameters**: `id` (path parameter)
- **Authorization**: Admin role required

### 27. Get Student Profile
- **URL**: `GET /profile`
- **Description**: Get current student profile
- **Request Body**: None
- **Response**: `StudentResponse`
- **Parameters**: None
- **Authorization**: Student role required

### 28. Get Student Grades
- **URL**: `GET /student/{studentId}`
- **Description**: Get grades for specific student
- **Request Body**: None
- **Response**: `StudentResponse`
- **Parameters**: 
  - `studentId` (path parameter)
  - `semesterId` (optional query parameter)

### 29. Get Student Revendications
- **URL**: `GET /student/{studentId}/revendications`
- **Description**: Get student's revendication history
- **Request Body**: None
- **Response**: `List<RevendicationResponse>`
- **Parameters**: `studentId` (path parameter)

## Subject Management

### 30. Get All Subjects (Admin)
- **URL**: `GET /admin/subjects`
- **Description**: Admin views all subjects with pagination
- **Request Body**: None
- **Response**: `SubjectResponse`
- **Parameters**: 
  - `pageNumber` (optional, default: 0)
  - `pageSize` (optional, default: 50)
  - `sortBy` (optional, default: subjectId)
  - `sortOrder` (optional, default: asc)
- **Authorization**: Admin role required

### 31. Create Subject (Admin)
- **URL**: `POST /admin/subject`
- **Description**: Admin creates new subject
- **Request Body**: `SubjectRequest`
- **Response**: `SubjectRequest`
- **Parameters**: None
- **Authorization**: Admin role required

### 32. Update Subject (Admin)
- **URL**: `PUT /admin/subject/{id}`
- **Description**: Admin updates subject
- **Request Body**: `SubjectRequest`
- **Response**: `SubjectRequest`
- **Parameters**: `id` (path parameter)
- **Authorization**: Admin role required

### 33. Delete Subject (Admin)
- **URL**: `DELETE /admin/subject/{id}`
- **Description**: Admin deletes subject
- **Request Body**: None
- **Response**: `SubjectRequest`
- **Parameters**: `id` (path parameter)
- **Authorization**: Admin role required

### 34. Get Teacher Subjects
- **URL**: `GET /teacher/subject`
- **Description**: Get subjects assigned to current teacher
- **Request Body**: None
- **Response**: `SubjectResponse`
- **Parameters**: 
  - `pageNumber` (optional, default: 0)
  - `pageSize` (optional, default: 50)
  - `sortBy` (optional, default: subjectId)
  - `sortOrder` (optional, default: asc)
- **Authorization**: Teacher role required

## Teacher Management

### 35. Update Teacher (Admin)
- **URL**: `PUT /admin/teacher/{id}`
- **Description**: Admin updates teacher information
- **Request Body**: `TeacherRequest`
- **Response**: `TeacherRequest`
- **Parameters**: `id` (path parameter)
- **Authorization**: Admin role required

### 36. Get Teacher Profile
- **URL**: `GET /profile`
- **Description**: Get current teacher profile
- **Request Body**: None
- **Response**: `TeacherResponse`
- **Parameters**: None
- **Authorization**: Teacher role required

### 37. Get Teacher Grades
- **URL**: `GET /teacher/my-grades`
- **Description**: Get all grades entered by current teacher
- **Request Body**: None
- **Response**: `List<GradeResponse>`
- **Parameters**: None
- **Authorization**: Teacher role required

## Transcript Management

### 38. Get Student Transcript
- **URL**: `GET /student/transcript`
- **Description**: Student retrieves their academic transcript
- **Request Body**: None
- **Response**: `TranscriptResponse`
- **Parameters**: None
- **Authorization**: Student role required

## DTO Structures

### Request DTOs

#### `LoginRequest`
```json
{
  "username": "string (3-50 chars, required)",
  "password": "string (5-100 chars, required)"
}
```

#### `SignupRequest`
```json
{
  "username": "string",
  "email": "string",
  "password": "string",
  "firstName": "string",
  "lastName": "string",
  "role": "Set<Roles>",
  "level": "TeachingLevel (for students)",
  "matricule": "string (for students)",
  "speciality": "string (for students)",
  "cycle": "StudentCycle (for students)",
  "dateOfBirth": "LocalDate (for students)",
  "placeOfBirth": "string (for students)",
  "levels": "List<TeachingLevel> (for teachers)",
  "department": "Department (for teachers)",
  "phone": "string (for teachers)",
  "subjects": "List<Subject> (for teachers)"
}
```

#### `PasswordChangeRequest`
```json
{
  "newPassword": "string (6-100 chars, required)",
  "confirmPassword": "string (required)"
}
```

#### `DepartmentRequest`
```json
{
  "departmentName": "string",
  "subjectIds": "Set<Long>"
}
```

#### `GradeRequest`
```json
{
  "studentId": "Long (required)",
  "subjectId": "Long (required)",
  "examId": "Long (required)",
  "semesterId": "Long (required)",
  "ccScore": "Double (0-30)",
  "snScore": "Double (0-70)",
  "comments": "string (5-255 chars)",
  "assessmentType": "AssessmentType (required)"
}
```

#### `RevendicationRequest`
```json
{
  "period": "Exam",
  "student": "Student",
  "grade": "Grades",
  "semester": "Semester",
  "requestedScore": "Double",
  "description": "string"
}
```

#### `RevendicationPeriodRequest`
```json
{
  "examId": "Long (required)",
  "startDate": "LocalDate (required)",
  "endDate": "LocalDate (required)",
  "color": "string",
  "isActive": "Boolean (default: false)"
}
```

#### `SemesterRequest`
```json
{
  "name": "string (5+ chars, required)",
  "startDate": "LocalDate (required)",
  "endDate": "LocalDate (required)",
  "active": "Boolean (default: true)"
}
```

#### `StudentRequest`
```json
{
  "username": "string",
  "password": "string",
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "studentLevel": "TeachingLevel",
  "cycle": "StudentCycle",
  "matricule": "string",
  "speciality": "string",
  "dateOfBirth": "LocalDate",
  "placeOfBirth": "string"
}
```

#### `SubjectRequest`
```json
{
  "subjectCode": "string",
  "credits": "BigDecimal",
  "description": "string",
  "teacherId": "Long",
  "subjectsLevel": "List<TeachingLevel>",
  "Studentcycle": "StudentCycle",
  "semesterId": "Long",
  "departmentId": "Long"
}
```

#### `TeacherRequest`
```json
{
  "username": "string",
  "password": "string",
  "firstName": "string",
  "lastName": "string",
  "phoneNumber": "string",
  "email": "string",
  "subjects": "List<Subject>",
  "department": "Department",
  "teachingLevel": "List<TeachingLevel>",
  "appRole": "Roles",
  "isActive": "Boolean"
}
```

#### `TranscriptRequest`
```json
{
  "student": "Student",
  "semester": "Semester",
  "format": "string (PDF)",
  "includeComments": "Boolean (default: true)",
  "facultyName": "string",
  "academicYear": "string"
}
```

### Response DTOs

#### `LoginResponse`
```json
{
  "id": "Long",
  "username": "string",
  "roles": "List<string>",
  "createdDate": "Instant",
  "lastModifiedDate": "Instant"
}
```

#### `MessageResponse`
```json
{
  "message": "string"
}
```

#### `UserResponse`
```json
{
  "userId": "Long",
  "username": "string",
  "email": "string",
  "firstName": "string",
  "lastName": "string",
  "roles": "Roles",
  "isActive": "Boolean",
  "createdDate": "Instant",
  "lastModifiedDate": "Instant"
}
```

#### `DepartmentResponse`
```json
{
  "departmentId": "Long",
  "departmentName": "string",
  "departmentSubjects": "Set<SubjectResponse>",
  "createdDate": "Instant",
  "lastModifiedDate": "Instant",
  "content": "List<DepartmentRequest>",
  "subjects": "Set<SubjectResponse>",
  "pageNumber": "Integer",
  "pageSize": "Integer",
  "totalElements": "Long",
  "totalPages": "Integer",
  "lastPage": "Boolean"
}
```

#### `GradeResponse`
```json
{
  "gradeId": "Long",
  "score": "Double",
  "maxValue": "Double",
  "comments": "string",
  "student": "StudentRequest",
  "subject": "SubjectRequest",
  "examiner": "Teacher",
  "semester": "SemesterRequest",
  "exam": "AssessmentType",
  "revendication": "List<RevendicationRequest>",
  "hasPassed": "Boolean",
  "gpa": "Double",
  "content": "GradeRequest",
  "createdDate": "Instant",
  "lastModifiedDate": "Instant"
}
```

#### `RevendicationResponse`
```json
{
  "revendicationId": "Long",
  "student": "StudentResponse",
  "grade": "GradeResponse",
  "semester": "SemesterResponse",
  "requestedScore": "Double",
  "description": "string",
  "teacherComment": "string",
  "status": "RequestStatus",
  "createdDate": "Instant",
  "lastModifiedDate": "Instant",
  "content": "List<RevendicationRequest>",
  "pageNumber": "Integer",
  "pageSize": "Integer",
  "totalElements": "Long",
  "totalPages": "Integer",
  "lastPage": "Boolean"
}
```

#### `RevendicationPeriodResponse`
```json
{
  "revendicationPeriodId": "Long",
  "exam": "ExamResponse",
  "semester": "SemesterResponse",
  "startDate": "LocalDate",
  "endDate": "LocalDate",
  "color": "string",
  "isActive": "Boolean",
  "createdDate": "Instant",
  "lastModifiedDate": "Instant",
  "content": "List<RevendicationPeriodResponse>",
  "pageNumber": "Integer",
  "pageSize": "Integer",
  "totalElements": "Long",
  "totalPages": "Integer",
  "lastPage": "Boolean"
}
```

#### `SemesterResponse`
```json
{
  "semesterId": "Long",
  "name": "string",
  "startDate": "LocalDate",
  "endDate": "LocalDate",
  "active": "Boolean",
  "createdDate": "Instant",
  "lastModifiedDate": "Instant",
  "subjects": "Set<SubjectResponse>",
  "grades": "Set<GradeResponse>",
  "content": "List<SemesterResponse>",
  "pageNumber": "Integer",
  "pageSize": "Integer",
  "totalElements": "Long",
  "totalPages": "Integer",
  "lastPage": "Boolean"
}
```

#### `StudentResponse`
```json
{
  "id": "Long",
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "studentLevel": "TeachingLevel",
  "cycle": "StudentCycle",
  "matricule": "string",
  "speciality": "string",
  "dateOfBirth": "LocalDate",
  "placeOfBirth": "string",
  "grades": "List<GradeResponse>",
  "createdDate": "Instant",
  "lastModifiedDate": "Instant",
  "isActive": "Boolean",
  "semesterId": "Long",
  "content": "List<StudentRequest>",
  "pageNumber": "Integer",
  "pageSize": "Integer",
  "totalElements": "Long",
  "totalPages": "Integer",
  "lastPage": "Boolean"
}
```

#### `SubjectResponse`
```json
{
  "subjectId": "Long",
  "subjectCode": "string",
  "credits": "BigDecimal",
  "description": "string",
  "teacher": "TeacherResponse",
  "subjectsLevel": "List<TeachingLevel>",
  "Studentcycle": "StudentCycle",
  "semester": "SemesterResponse",
  "department": "DepartmentResponse",
  "content": "List<SubjectRequest>",
  "pageNumber": "Integer",
  "pageSize": "Integer",
  "totalElements": "Long",
  "totalPages": "Integer",
  "lastPage": "Boolean",
  "createdDate": "Instant",
  "lastModifiedDate": "Instant"
}
```

#### `TeacherResponse`
```json
{
  "teacherId": "Long",
  "username": "string",
  "firstName": "string",
  "lastName": "string",
  "phoneNumber": "string",
  "email": "string",
  "subjects": "List<SubjectResponse>",
  "department": "Department",
  "teachingLevel": "Set<TeachingLevel>",
  "createdDate": "Instant",
  "lastModifiedDate": "Instant",
  "appRole": "Roles",
  "isActive": "Boolean"
}
```

#### `TranscriptResponse`
```json
{
  "transcriptId": "Long",
  "studentFirstName": "string",
  "studentLastName": "string",
  "studentMatricule": "string",
  "subjectResults": "List<SubjectResponse>",
  "status": "TranscriptStatus",
  "studentLevel": "TeachingLevel",
  "studentCycle": "StudentCycle",
  "semesterName": "string",
  "studentGrades": "List<GradeResponse>",
  "annualAverage": "Double",
  "pdfPath": "string",
  "creditsEarned": "Integer",
  "totalCreditsRequired": "Integer",
  "semester1Credits": "Integer",
  "semester2Credits": "Integer",
  "semester1Average": "Double",
  "semester2Average": "Double",
  "facultyName": "string",
  "academicYear": "string",
  "createdDate": "Instant",
  "lastModifiedDate": "Instant"
}
```

#### `ExamResponse`
```json
{
  "examPeriodId": "Long",
  "assessmentType": "AssessmentType"
}
```

## Authentication Notes
- All endpoints require JWT token in Authorization header: `Bearer <token>`
- Role-based access control enforced on admin/teacher/student specific endpoints
- Token expires after 24 hours by default