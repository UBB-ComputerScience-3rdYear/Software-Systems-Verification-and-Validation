package service;

import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.jupiter.api.*;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import java.io.File;
import java.time.LocalDate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;



public class ServiceTestIntegration {
    public static Service service;

    public static final String STUDENT_TEST_FILEPATH = "fisiere_test/studenti_test.xml";
    public static final String TEME_TEST_FILEPATH = "fisiere_test/teme_test.xml";
    public static final String NOTE_TEST_FILEPATH = "fisiere_test/note_test.xml";

    @BeforeAll
    public static void init() {
        StudentXMLRepo studentXMLRepo = new StudentXMLRepo(ServiceTestIntegration.STUDENT_TEST_FILEPATH);
        TemaXMLRepo temaXMLRepo = new TemaXMLRepo(ServiceTestIntegration.TEME_TEST_FILEPATH);
        NotaXMLRepo notaXMLRepo = new NotaXMLRepo(ServiceTestIntegration.NOTE_TEST_FILEPATH);
        ServiceTestIntegration.service = new Service(
                studentXMLRepo, new StudentValidator(),
                temaXMLRepo, new TemaValidator(),
                notaXMLRepo, new NotaValidator(studentXMLRepo, temaXMLRepo)
        );

        Student student = service.addStudent(new Student("112", "ioana", 936, "siie2810@scs.ubbcluj.ro"));
        Tema tema = service.addTema(new Tema( "valid_id", "vlad", 11, 5));
    }

    @AfterAll
    public static void cleanup() {
        File studentFile = new File(ServiceTestIntegration.STUDENT_TEST_FILEPATH);
        File temeFile = new File(ServiceTestIntegration.TEME_TEST_FILEPATH);
        File noteFile = new File(ServiceTestIntegration.NOTE_TEST_FILEPATH);

        studentFile.delete();
        temeFile.delete();
        noteFile.delete();
    }

    @Test
    public void addStudent_validInexistingId_Created() {
        Student student = service.addStudent(new Student("113", "ioana", 936, "siie2810@scs.ubbcluj.ro"));

        assertNull(student);

        service.deleteStudent("113");
    }

    @Test
    public void addTema_validTema_added() {
        Tema tema = service.addTema(new Tema( "valid_id1", "vlad", 11, 5));

        assertNull(tema);

        service.deleteTema("valid_id1");
    }

    @Test
    public void addGrade_validGrade_gradedAdded() {
        Double nota = service.addNota(
                new Nota("prima nota", "112", "valid_id", 9, LocalDate.of(2021,11,21)),
                "numa profesorii sunt de 10");

        assertNull(nota);
        assertEquals(1, StreamSupport.stream(service.getAllNote().spliterator(), false).count());

        service.deleteNota("prima nota");
        service.deleteTema("valid_id");
        service.deleteStudent("112");
    }

    @Test
    public void integration_test() {
        Student student = service.addStudent(new Student("112", "ioana", 936, "siie2810@scs.ubbcluj.ro"));
        Tema tema = service.addTema(new Tema( "valid_id", "vlad", 11, 5));

        Double nota = service.addNota(
                new Nota("prima nota", "112", "valid_id", 9, LocalDate.of(2021,11,21)),
                "numa profesorii sunt de 10");

        assertNull(nota);
        assertEquals(1, StreamSupport.stream(service.getAllNote().spliterator(), false).count());

        service.deleteNota("prima nota");
        service.deleteTema("valid_id");
        service.deleteStudent("112");
    }
}
