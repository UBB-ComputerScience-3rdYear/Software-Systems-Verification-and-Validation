package service;

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
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceTest {
    public static Service service;

    public static final String STUDENT_TEST_FILEPATH = "fisiere_test/studenti_test.xml";
    public static final String TEME_TEST_FILEPATH = "fisiere_test/teme_test.xml";
    public static final String NOTE_TEST_FILEPATH = "fisiere_test/note_test.xml";

    @BeforeAll
    public static void init() {
        StudentXMLRepo studentXMLRepo = new StudentXMLRepo(ServiceTest.STUDENT_TEST_FILEPATH);
        TemaXMLRepo temaXMLRepo = new TemaXMLRepo(ServiceTest.TEME_TEST_FILEPATH);
        NotaXMLRepo notaXMLRepo = new NotaXMLRepo(ServiceTest.NOTE_TEST_FILEPATH);
        ServiceTest.service = new Service(
                studentXMLRepo, new StudentValidator(),
                temaXMLRepo, new TemaValidator(),
                notaXMLRepo, new NotaValidator(studentXMLRepo, temaXMLRepo)
        );
    }

    @AfterAll
    public static void cleanup() {
        File studentFile = new File(ServiceTest.STUDENT_TEST_FILEPATH);
        File temeFile = new File(ServiceTest.TEME_TEST_FILEPATH);
        File noteFile = new File(ServiceTest.NOTE_TEST_FILEPATH);

        studentFile.delete();
        temeFile.delete();
        noteFile.delete();
    }


    //testcase 3
    @Test
    @Order(1)
    public void addStudent_validInexistentID_Created() {
        Student student = service.addStudent(new Student("112", "ioana", 936, "siie2810@scs.ubbcluj.ro"));

        assertNull(student);
        assertEquals(1, Stream.of(service.getAllStudenti().spliterator()).count());

        service.deleteStudent("112");
    }

    //testcase 4
    @Test
    @Order(2)
    public void addStudent_validExistentID_NotCreated() {
        //returns entity that we wanted to add
        Student first = service.addStudent(new Student("112", "ioana", 936, "siie2810@scs.ubbcluj.ro"));
        Student second = service.addStudent(new Student("112", "maria", 936, "someemail@scs.ubbcluj.ro"));

        assertEquals("ioana", second.getNume());
        assertEquals(1, Stream.of(service.getAllStudenti().spliterator()).count());

        service.deleteStudent("112");
    }

    //testcase1
    @Test
    public void addStudent_invalidEmptyID_NotCreated() {
        try {
            service.addStudent(new Student("", "someone", 936, "sta"));
            fail("validation exception not thrown");
        } catch (ValidationException exception) {

        }
    }

    //testcase2
    @Test
    public void addStudent_invalidNullID_NotCreated() {
        try {
            service.addStudent(new Student(null, "someone", 936, "sta"));
            fail("validation exception not thrown");
        } catch (ValidationException exception) {

        }
    }

    //testcase5
    @Test
    public void addStudent_invalidEmptyName_NotCreated() {
        try {
            service.addStudent(new Student("some_id", "", 936, "sta"));
            fail("validation exception not thrown");
        } catch (ValidationException exception) {

        }
    }

    //testcase6
    @Test
    public void addStudent_invalidNullName_NotCreated() {
        try {
            service.addStudent(new Student("some_id", null, 936, "sta"));
            fail("validation exception not thrown");
        } catch (ValidationException exception) {

        }
    }

    //testcase7
    @Test
    @Order(3)
    public void addStudent_validName_Created() {
        //nume "ana"
        //verificare ca la testcase 3 & 4
        Student student = service.addStudent(
                new Student("random_id", "ana", 936, "ana@scs.cluj.ro"));


        assertNull(student);
        assertEquals(1, StreamSupport.stream(service.getAllStudenti().spliterator(), false).count());

        service.deleteStudent("random_id");
    }

    //testcase8
    @Test
    public void addStudent_validPositiveGroup_Created() {
        //group 0
        //group 1
        //verificare ca la testcase 3 & 4
        Student student0 = service.addStudent(new Student( "valid_id0", "vlad", 0, "some@sscs.cluj.ro"));
        Student student1 = service.addStudent(new Student( "valid_id1", "vlad", 1, "some@sscs.cluj.ro"));

        assertNull(student0);
        assertNull(student1);
        assertEquals(2, StreamSupport.stream(service.getAllStudenti().spliterator(), false).count());

        service.deleteStudent("valid_id0");
        service.deleteStudent("valid_id1");

    }

    //testcase9
    @Test
    public void addStudent_invalidNegativeGroup_NotCreated() {
        try {
            service.addStudent(new Student("some_id", "some name", -1, "sta"));
            fail("validation exception not thrown");
        } catch (ValidationException exception) {

        }
    }


    //testcase 12
    @Test
    public void addStudent_invalidEmptyEmail_NotCreated() {
        try {
            service.addStudent(new Student("some_id", "some name", 936, ""));
            fail("validation exception not thrown");
        } catch (ValidationException exception) {

        }
    }

    //testcase 13
    @Test
    public void addStudent_invalidNullEmail_NotCreated() {
        try {
            service.addStudent(new Student("some_id", "some name", 936, ""));
            fail("validation exception not thrown");
        } catch (ValidationException exception) {

        }
    }

    //testcase 14
    @Test
    public void addStudent_validEmail_Created() {
        //email "ana"
        //verificare ca la testcase 3 & 4
        Student student = service.addStudent(new Student( "valid_id", "vlad", 0, "ana"));

        assertNull(student);
        assertEquals(1, StreamSupport.stream(service.getAllStudenti().spliterator(), false).count());

        service.deleteStudent("valid_id");
    }


    //WHITE BOX - addTema
    @Test
    public void addTema_invalidNullID_Exception() {
        String INVALID_NULL_ID_MESSAGE = "Numar tema invalid!";
        Tema testTema = new Tema(null, "ceva descriere", 13, 12);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> service.addTema(testTema));

        assertEquals(INVALID_NULL_ID_MESSAGE, exception.getMessage());
    }

    @Test
    public void addTema_invalidEmptyID_Exception() {
        String INVALID_NULL_ID_MESSAGE = "Numar tema invalid!";
        Tema testTema = new Tema("", "", 13, 12);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> service.addTema(testTema));

        assertEquals(INVALID_NULL_ID_MESSAGE, exception.getMessage());
    }


    @Test
    public void addTema_invalidEmptyDescription_Exception() {
        String INVALID_DESCRIPTION_MESSAGE = "Descriere invalida!";
        Tema testTema = new Tema("vlad_sad", "", 13, 12);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> service.addTema(testTema));

        assertEquals(INVALID_DESCRIPTION_MESSAGE, exception.getMessage());
    }

    @Test
    public void addTema_outOfBoundsDeadline_Exception() {
        String INVALID_DEADLINE_MESSAGE = "Deadlineul trebuie sa fie intre 1-14.";
        Tema testTema = new Tema("vlad_sad", "ceva descriere", 0, 12);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> service.addTema(testTema));

        assertEquals(INVALID_DEADLINE_MESSAGE, exception.getMessage());
    }

    @Test
    public void addTema_outOfBoundsPrimire_Exception() {
        String INVALID_PRIMIRE_MESSAGE = "Saptamana primirii trebuie sa fie intre 1-14.";
        Tema testTema = new Tema("vlad_sad", "ceva descriere", 10, 122);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> service.addTema(testTema));

        assertEquals(INVALID_PRIMIRE_MESSAGE, exception.getMessage());
    }

    @Test
    public void addTema_deadlineSoonerThanPrimire_Exception() {
        String INVALID_DEADLINE_TO_PRIMIRE_MESSAGE = "Saptamana deadline trebuie sa fie mai mare ca saptamana primirii";
        Tema testTema = new Tema("vlad_sad", "ceva descriere", 10, 11);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> service.addTema(testTema));

        assertEquals(INVALID_DEADLINE_TO_PRIMIRE_MESSAGE, exception.getMessage());
    }

    @Test
    public void addTema_validTema_added() {
        Tema tema = service.addTema(new Tema( "valid_id", "vlad", 11, 5));

        assertNull(tema);
        assertEquals(1, StreamSupport.stream(service.getAllTeme().spliterator(), false).count());

        service.deleteTema("valid_id");
    }

    @Test
    public void addTema_duplicateTema_notAdded() {
        Tema tema = service.addTema(new Tema( "valid_id", "vlad", 11, 5));
        Tema tema2 = service.addTema(new Tema( "valid_id", "vlad_rares", 11, 5));

        assertNull(tema);
        assertNotNull(tema2);
        assertEquals(1, StreamSupport.stream(service.getAllTeme().spliterator(), false).count());

        service.deleteTema("valid_id");
    }


}
