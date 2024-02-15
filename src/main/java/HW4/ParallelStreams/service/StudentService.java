package HW4.ParallelStreams.service;


import HW4.ParallelStreams.model.Faculty;
import HW4.ParallelStreams.model.Student;
import HW4.ParallelStreams.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    public Student find(long id) {
        Student student = studentRepository.findById(id).orElse(null);
        logger.info("Was invoked method for find student");
        if (student == null) {
            logger.error("There is no student with id = {}", id);
        }

        return student;
    }


    public Student changeStudentInfo(Student student) {
        logger.info("Was invoked method for change student info");
        return studentRepository.save(student);

    }

    public void removeStudent(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        studentRepository.deleteById(id);
        logger.info("Was invoked method for remove student");
        if (student == null) {
            logger.error("There is no student with id = {}", id);
        }
    }

    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method for getAllStudents");
        return studentRepository.findAll();
    }

    public Collection<Student> findStudentByAge(int age) {
        logger.info("Was invoked method for findStudentByAge");
        Collection<Student> student = studentRepository.findStudentByAge(age);
        if (student == null) {
            logger.error("There is no student with age = {}", age);
        }

        return student;
    }

    public Collection<Student> findByAgeBetween(int min, int max) {
        logger.info("Was invoked method for findByAgeBetween");
        return studentRepository.findByAgeBetweenIgnoreCase(min, max);
    }

    public Faculty findFacultyByStudent(long id) {
        logger.info("Was invoked method for findFacultyByStudent");
        return studentRepository.getReferenceById(id).getFaculty();
    }

    public List<String> getStudentsStartingWithA() {
        List<Student> students = studentRepository.findAll();

        List<String> namesStartingWithA = students.stream()
                .map(Student::getName)
                .filter(name -> name != null && name.startsWith("A"))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());

        return namesStartingWithA;


    }

    public Double getAverageAge() {
        List<Student> students = studentRepository.findAll();

        double averageAge = students.stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
        return averageAge;

    }

    public Integer returnNumber() {

        int sum = Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, (a, b) -> a + b);
        return sum;
    }
}
