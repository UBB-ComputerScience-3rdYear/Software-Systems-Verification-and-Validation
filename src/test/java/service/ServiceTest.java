package service;

import domain.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {
    public static Service service;

    @BeforeAll
    public static void init() {
        StudentXMLRepo studentXMLRepo = new StudentXMLRepo("fisiere_test/studenti_test.xml");
        TemaXMLRepo temaXMLRepo = new TemaXMLRepo("fisiere_test/teme_test.xml");
        NotaXMLRepo notaXMLRepo = new NotaXMLRepo("fisiere_test/note_test.xml");
        ServiceTest.service = new Service(
                studentXMLRepo, new StudentValidator(),
                temaXMLRepo, new TemaValidator(),
                notaXMLRepo, new NotaValidator(studentXMLRepo, temaXMLRepo)
        );
    }


    @Test
    @Order(1)
    public void addStudent_validInexistentID_Created() {
        service.addStudent(new Student("1", "vlad", 936, "rvie2810@scs.ubbcluj.ro"));

        assertEquals(1, Stream.of(service.getAllStudenti().spliterator()).count());
    }

    @Test
    @Order(2)
    public void addStudent_validExistentID_NotCreated() {
        //returns entity that we wanted to add
        service.addStudent(new Student("1", "vlad new", 936, "someemail@scs.ubbcluj.ro"));

        assertEquals(1, Stream.of(service.getAllStudenti().spliterator()).count());

        assertEquals("vlad", service.getAllStudenti().iterator().next().getNume());
    }

    @Test
    public void addStudent_invalidInexistentID_NotCreated(){
        try{
            service.addStudent(new Student("", "someone", 936, "sta"));
            fail("validation exception not thrown");
        }catch (ValidationException exception){

        }

    }

    @Test
    public void addStudent_invalidName_NotCreated(){
        try {
            service.addStudent(new Student("some_id", "", 936, "sta"));
            fail("validation exception not thrown");
        }catch (ValidationException exception){

        }
    }

    @Test
    public void addStudent_invalidGroup_NotCreated(){
        try {
            service.addStudent(new Student("some_id", "some name", -200, "sta"));
            fail("validation exception not thrown");
        }catch (ValidationException exception){

        }
    }

    @Test
    public void addStudent_invalidEmail_NotCreated(){
        try {
            service.addStudent(new Student("some_id", "some name", 936, ""));
            fail("validation exception not thrown");
        }catch (ValidationException exception){

        }
    }
}
