package service;

import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;

import java.time.LocalDate;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ServiceIncrementalTest {
    public static Service service;

    public static final String STUDENT_TEST_FILEPATH = "fisiere_test/studenti_test.xml";
    public static final String TEME_TEST_FILEPATH = "fisiere_test/teme_test.xml";
    public static final String NOTE_TEST_FILEPATH = "fisiere_test/note_test.xml";

    @BeforeAll
    public static void init() {
        StudentXMLRepo studentXMLRepo = new StudentXMLRepo(ServiceIncrementalTest.STUDENT_TEST_FILEPATH);
        TemaXMLRepo temaXMLRepo = new TemaXMLRepo(ServiceIncrementalTest.TEME_TEST_FILEPATH);
        NotaXMLRepo notaXMLRepo = new NotaXMLRepo(ServiceIncrementalTest.NOTE_TEST_FILEPATH);
        ServiceIncrementalTest.service = new Service(
                studentXMLRepo, new StudentValidator(),
                temaXMLRepo, new TemaValidator(),
                notaXMLRepo, new NotaValidator(studentXMLRepo, temaXMLRepo)
        );
//
//        Student student = service.addStudent(new Student("112", "ioana", 936, "siie2810@scs.ubbcluj.ro"));
//        Tema tema = service.addTema(new Tema( "valid_id", "vlad", 11, 5));
    }

    @Test
    public void addStudent_validData_created() {
        Student student = service.addStudent(new Student("113", "ioana", 936, "siie2810@scs.ubbcluj.ro"));

        assertNull(student);

        service.deleteStudent("113");
    }

    @Test
    public void addStudent_addAssignment_validData_created() {
        Tema tema = service.addTema(new Tema( "valid_id1", "vlad", 11, 5));
        Student student = service.addStudent(new Student("113", "ioana", 936, "siie2810@scs.ubbcluj.ro"));

        assertNull(tema);
        assertNull(student);

        service.deleteTema("valid_id1");
        service.deleteStudent("113");
    }

    @Test
    public void addStudent_addAssignment_addGrade_integration() {
        Student student = service.addStudent(new Student("112", "ioana", 936, "siie2810@scs.ubbcluj.ro"));
        Tema tema = service.addTema(new Tema( "valid_id", "vlad", 11, 5));

        Double nota = service.addNota(
                new Nota("prima nota", "112", "valid_id", 9, LocalDate.of(2021,11,21)),
                "numa profesorii sunt de 10");

        assertNull(nota);
        assertNull(tema);
        assertNull(student);
        assertEquals(1, StreamSupport.stream(service.getAllNote().spliterator(), false).count());

        service.deleteNota("prima nota");
        service.deleteTema("valid_id");
        service.deleteStudent("112");
    }

}
