# ManageNotes - University Grade Management System

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17.5-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A comprehensive university grade management system built with Spring Boot, providing secure appRole-based access for administrators, teachers, and students to manage academic records, grades, and institutional data.

## 🚀 Features

### Core Functionality
- **Multi-appRole Authentication**: JWT-based authentication for Admin, Teacher, and Student roles
- **Grade Management**: Complete CRUD operations for student grades with validation
- **Academic Structure**: Department, subject, and semester management
- **Grade Claims**: Student-initiated grade dispute system with approval workflow
- **Reporting**: Comprehensive academic reports and transcripts
- **Grading Windows**: Time-controlled grade entry periods

### Role-Based Access Control
- **Admin**: Full system access, user management, department/subject creation
- **Teacher**: Grade entry, subject management (one subject per level), student grade viewing
- **Student**: Grade viewing, grade claim submission, transcript access

### Advanced Features
- **Real-time Validation**: Grade entry validation with grading window controls
- **Teacher Assignment Control**: One subject per teacher per academic level constraint
- **Audit Trail**: Complete tracking of grade changes and user actions
- **Bulk Operations**: Batch updates for semester and departments
- **Data Integrity**: Foreign key constraint handling with cascade operations
- **API Documentation**: Comprehensive Swagger/OpenAPI documentation

## 🏗️ Architecture

### Technology Stack
- **Backend**: Spring Boot 3.5.3, Spring Security, Spring Data JPA
- **Database**: PostgreSQL 17.5 with Flyway migrations
- **Authentication**: JWT tokens with appRole-based authorization
- **Documentation**: SpringDoc OpenAPI 3 (Swagger UI)
- **Build Tool**: Maven 3.9+
- **Java Version**: OpenJDK 21

### Database Schema
```
Users (id, username, password, appRole, email, first_name, last_name)
├── Students (id, matricule, level, cycle, speciality)
├── Departments (id, name, creation_date)
├── Subjects (id, name, code, credits, level, cycle, department_id, id_teacher)
│   └── CONSTRAINT uk_teacher_level UNIQUE (id_teacher, level)
├── Semesters (id, name, start_date, end_date, active)
├── Grades (id, student_id, subject_id, semester_id, value, type, period_type)
├── GradeClaims (id, grade_id, student_id, requested_score, status, cause)
└── GradingWindows (id, name, start_date, end_date, period_type, semester_id)
```

## 🛠️ Installation & Setup

### Prerequisites
- Java 21 or higher
- PostgreSQL 17.5+
- Maven 3.9+
- Git

### Database Setup
1. **Install PostgreSQL**:
   ```bash
   # Ubuntu/Debian
   sudo apt update && sudo apt install postgresql postgresql-contrib
   
   # macOS
   brew install postgresql
   ```

2. **Create Database**:
   ```sql
   CREATE DATABASE managerNotes;
   CREATE USER postgres WITH PASSWORD 'nathan';
   GRANT ALL PRIVILEGES ON DATABASE managerNotes TO postgres;
   ```

3. **Import Initial Schema**:
   ```bash
   psql -h localhost -U postgres -d managerNotes -f src/main/resources/managerNotes.sql
   ```

### Application Setup
1. **Clone Repository**:
   ```bash
   git clone <repository-url>
   cd ManageNotes
   ```

2. **Configure Database**:
   ```properties
   # src/main/resources/application.properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/managerNotes
   spring.datasource.username=postgres
   spring.datasource.password=nathan
   ```

3. **Build & Run**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Access Application**:
   - API Base URL: `http://localhost:3030`
   - Swagger UI: `http://localhost:3030/swagger-ui.html`
   - API Docs: `http://localhost:3030/api-docs`

## 🔐 Authentication

### Default Users
```json
{
  "admin": {
    "username": "admin",
    "password": "admin123",
    "appRole": "ADMIN"
  },
  "teacher": {
    "username": "prof.johnson",
    "password": "teacher123",
    "appRole": "TEACHER"
  },
  "student": {
    "username": "STU2024001",
    "password": "student123",
    "appRole": "STUDENT"
  }
}
```

### JWT Token Usage
```bash
# Login
curl -X POST http://localhost:3030/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Use token in requests
curl -X GET http://localhost:3030/api/students \
  -H "Authorization: Bearer <your-jwt-token>"
```

## 📚 API Documentation

### Core Endpoints

#### Authentication
- `POST /api/auth/signin` - User login
- `POST /api/auth/signup` - User registration (Admin only)
- `POST /api/auth/refresh` - Token refresh

#### User Management
- `GET /api/students` - List students (Teacher/Admin)
- `GET /api/teachers` - List teachers (Admin)
- `PUT /api/students/{id}` - Update student (Admin)
- `DELETE /api/users/{id}` - Delete user (Admin)

#### Academic Management
- `GET /api/departments` - List departments
- `POST /api/departments` - Create department (Admin)
- `GET /api/subjects` - List subjects
- `POST /api/subjects` - Create subject (Admin)
- `GET /api/semester` - List semester
- `PUT /api/semester` - Bulk update semester (Admin)

#### Grade Management
- `POST /api/grades` - Create grade (Teacher)
- `GET /api/grades/student/{id}` - Get student grades
- `PUT /api/grades/{id}` - Update grade (Teacher)
- `DELETE /api/grades/{id}` - Delete grade (Teacher)

#### Grade Claims
- `POST /api/grade-claims` - Submit grade claim (Student)
- `GET /api/grade-claims` - List grade claims
- `PUT /api/grade-claims/{id}/decision` - Approve/reject claim (Teacher)

### Request/Response Examples

#### Create Grade
```bash
curl -X POST http://localhost:3030/api/grades \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 13,
    "subjectId": 11,
    "semesterId": 1,
    "value": 15.5,
    "maxValue": 20,
    "type": "CC_1",
    "exam": "CC_1",
    "comments": "Good work",
    "enteredBy": 2
  }'
```

#### Submit Grade Claim
```bash
curl -X POST http://localhost:3030/api/grade-claims \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "gradeId": 1,
    "requestedScore": 18,
    "cause": "Calculation Error",
    "period": "CC_1",
    "description": "I believe there was an error in the calculation"
  }'
```

## 🏫 Academic Structure

### Student Levels
- `LEVEL1` - First Year
- `LEVEL2` - Second Year  
- `LEVEL3` - Third Year
- `LEVEL4` - Fourth Year (Master's)
- `LEVEL5` - Fifth Year (Master's)

### Academic Cycles
- `BACHELOR` - Undergraduate (Levels 1-3)
- `MASTER` - Graduate (Levels 4-5)
- `PHD` - Doctoral

### Grading Periods
- `CC_1` - Continuous Assessment 1
- `CC_2` - Continuous Assessment 2
- `SN_1` - Session Normale 1 (Midterm)
- `SN_2` - Session Normale 2 (Final)

### Semester Configuration
- **Semester 1**: September 8, 2025 - February 23, 2026
- **Semester 2**: March 15, 2026 - June 2, 2026

## 🔧 Configuration

### Environment Variables
```bash
# Database
DB_PASSWORD=nathan
DB_URL=jdbc:postgresql://localhost:5432/managerNotes

# JWT
JWT_SECRET=R2RThdHu3OKhHvJ3QgUnkQsVv8Af+Jsw9A9+1oSx6nE=
JWT_EXPIRATION=86400000

# Email (Optional)
EMAIL_USERNAME=your-email@gmail.com
EMAIL_PASSWORD=your-app-password
```

### Application Properties
```properties
# Server Configuration
server.port=3030

# Database Configuration
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/managerNotes}
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD:nathan}
spring.jpa.hibernate.ddl-auto=update

# Security Configuration
app.jwtSecret=${JWT_SECRET}
app.jwtExpirationMs=${JWT_EXPIRATION:86400000}

# File Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

## 🧪 Testing

### Run Tests
```bash
mvn test
```

### API Testing with Postman
1. Import the Swagger JSON from `http://localhost:3030/api-docs`
2. Set up environment variables for base URL and JWT token
3. Test authentication flow and appRole-based access

### Manual Testing Scenarios
1. **Admin Workflow**: Create departments, subjects, manage users
2. **Teacher Workflow**: Enter grades, manage grade claims
3. **Student Workflow**: View grades, submit grade claims

## 🚀 Deployment

### Production Configuration
```properties
# Production Database
spring.datasource.url=jdbc:postgresql://prod-db:5432/managernotes
spring.jpa.hibernate.ddl-auto=validate

# Security
spring.security.enabled=true
logging.level.com.university.ManageNotes.security=WARN

# Performance
spring.jpa.show-sql=false
logging.level.org.hibernate.SQL=WARN
```

### Docker Deployment
```dockerfile
FROM openjdk:21-jdk-slim
COPY target/ManageNotes-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 3030
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Database Migration
```bash
# Run Flyway migrations
mvn flyway:migrate

# Validate schema
mvn flyway:validate
```

## 🤝 Contributing

### Development Guidelines
1. Follow Spring Boot best practices
2. Use functional programming where applicable
3. Implement proper error handling
4. Add comprehensive JavaDoc comments
5. Write unit and integration tests

### Code Style
- Use Lombok for boilerplate code reduction
- Follow RESTful API conventions
- Implement proper validation with Bean Validation
- Use MapStruct for entity-DTO mapping

### Pull Request Process
1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

### Common Issues

#### Database Connection Issues
```bash
# Check PostgreSQL status
sudo systemctl status postgresql

# Reset database password
sudo -u postgres psql -c "ALTER USER postgres PASSWORD 'nathan';"
```

#### JWT Token Expiration
- Default expiration: 24 hours
- Refresh tokens using `/api/auth/refresh` endpoint

#### Grade Entry Validation
- Ensure grading windows are active
- Verify teacher has permission for the subject
- Check student enrollment in the subject

### Getting Help
- Check the [Issues](../../issues) page for known problems
- Review API documentation at `/swagger-ui.html`
- Contact the development team

## 📊 Monitoring & Logging

### Application Metrics
- Spring Boot Actuator endpoints available at `/actuator`
- Health check: `GET /actuator/health`
- Application info: `GET /actuator/info`

### Logging Configuration
```properties
# Logging levels
logging.level.com.university.ManageNotes=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

---

**Version**: 0.0.1-SNAPSHOT  
**Last Updated**: August 2025  
**Maintainer**: K48 Development Team