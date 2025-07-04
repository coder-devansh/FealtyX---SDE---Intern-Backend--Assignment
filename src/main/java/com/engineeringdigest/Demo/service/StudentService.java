// StudentService.java
package com.engineeringdigest.Demo.service;
import org.springframework.stereotype.Service;

import com.engineeringdigest.Demo.entity.Student;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StudentService {

    private final Map<Integer, Student> studentMap = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);
   // used to get all students
    public List<Student> getAllStudents() {
        return new ArrayList<>(studentMap.values());
    }
    // user
    public Optional<Student> getStudentById(int id) {
        return Optional.ofNullable(studentMap.get(id));
    }
    // used to add the student

    public Student addStudent(Student student) {
        int id = idCounter.getAndIncrement();
        student.setId(id);
        studentMap.put(id, student);
        return student;
    }

    public Optional<Student> updateStudent(int id, Student updatedStudent) {
        if (!studentMap.containsKey(id)) return Optional.empty();
        updatedStudent.setId(id);
        studentMap.put(id, updatedStudent);
        return Optional.of(updatedStudent);
    }
    public boolean existsByEmail(String email) {
    return studentMap.values().stream()
            .anyMatch(s -> s.getEmail().equalsIgnoreCase(email));
}

    public boolean deleteStudent(int id) {
        return studentMap.remove(id) != null;
    }
}
