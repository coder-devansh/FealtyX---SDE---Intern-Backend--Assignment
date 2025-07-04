// StudentController.java
package com.engineeringdigest.Demo.Controller;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.engineeringdigest.Demo.entity.ErrorResponse;
import com.engineeringdigest.Demo.entity.Student;
import com.engineeringdigest.Demo.service.OllamaService;
import com.engineeringdigest.Demo.service.StudentService;

import java.util.List;
import java.util.Optional;
import java.util.*;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
      private final StudentService studentService;
      @Autowired
    private final OllamaService ollamaService;

    public StudentController(StudentService studentService, OllamaService ollamaService) {
        this.studentService = studentService;
        this.ollamaService = ollamaService;
    }
      @GetMapping("/{id}/summary")
    public ResponseEntity<?> getStudentSummary(@PathVariable int id) {
        Optional<Student> studentOpt = studentService.getStudentById(id);

        if (studentOpt.isEmpty()) {
            return ResponseEntity.status(404).body(new ErrorResponse("Student not found", 404));
        }

        Student student = studentOpt.get();

        String prompt = "You are a resume summary generator. Given a student object, generate a short, professional summary in a single paragraph, highlighting their name, education, skills, and key projects. Do not use bullet points or formatting — just plain paragraph text." +
                "Name: " + student.getName() + "\n" +
                "Email: " + student.getEmail() + "\n" +
                "Education: " + student.getEducation() + "\n" +
                "Skills: " + String.join(", ", student.getSkills());

        try {
            
            String summary = ollamaService.generateSummary(prompt);
            return ResponseEntity.ok(Map.of("summary", summary));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Failed to generate summary", 500));
        }
    }

  

    @GetMapping
    public ResponseEntity<?> getAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Failed to fetch students", 500));
        }
    }
    // use for getStudent

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable int id) {
        try {
        Optional<Student> studentOpt = studentService.getStudentById(id);
        if (studentOpt.isPresent()) {
            return ResponseEntity.ok(studentOpt.get());
        } else {
            return ResponseEntity.status(404).body(new ErrorResponse("Student not found", 404));
        }
    } catch (Exception e) {
        return ResponseEntity.status(500).body(new ErrorResponse("Error fetching student", 500));
    }
    }
    // createStudent
    @PostMapping
    public ResponseEntity<?> createStudent(@Valid @RequestBody Student student) {
        try {
             if (studentService.existsByEmail(student.getEmail())) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("Email already exists", 400));
    }
            
            Student created = studentService.addStudent(student);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Failed to add student", 500));
        }
    }
    // update student
    @PutMapping("/{id}")

   public ResponseEntity<?> updateStudent(@PathVariable int id, @Valid @RequestBody Student student) {
    Optional<Student> existingStudent = studentService.getStudentById(id);

    if (existingStudent.isPresent()) {
        student.setId(id); // Make sure the ID stays the same
        Optional updatedStudent = studentService.updateStudent(id, student);
        return ResponseEntity.ok(updatedStudent);
    } else {
        return ResponseEntity.status(404).body(new ErrorResponse("Student not found", 404));
    }
}
  // delete student
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable int id) {
        if (studentService.deleteStudent(id)) {
            return ResponseEntity.ok("Student deleted successfully.");
        } else {
            return ResponseEntity.status(404).body(new ErrorResponse("Student not found", 404));
        }
    }
}
