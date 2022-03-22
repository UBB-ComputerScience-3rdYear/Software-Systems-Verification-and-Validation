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



    //testcase 3
    @Test
    @Order(1)
    public void addStudent_validInexistentID_Created() {
        Student student = service.addStudent(new Student("112", "ioana", 936, "siie2810@scs.ubbcluj.ro"));

        assertNull(student);
        assertEquals(1, Stream.of(service.getAllStudenti().spliterator()).count());
    }

    //testcase 4
    @Test
    @Order(2)
    public void addStudent_validExistentID_NotCreated() {
        //returns entity that we wanted to add
        Student student = service.addStudent(new Student("112", "maria", 936, "someemail@scs.ubbcluj.ro"));

        assertEquals("ioana", student.getNume());
        assertEquals(1, Stream.of(service.getAllStudenti().spliterator()).count());
    }

    //testcase1
    @Test
    public void addStudent_invalidEmptyID_NotCreated(){
        try{
            service.addStudent(new Student("", "someone", 936, "sta"));
            fail("validation exception not thrown");
        }catch (ValidationException exception){

        }
    }

    //testcase2
    @Test
    public void addStudent_invalidNullID_NotCreated(){
        try{
            service.addStudent(new Student(null, "someone", 936, "sta"));
            fail("validation exception not thrown");
        }catch (ValidationException exception){

        }
    }

    //testcase5
    @Test
    public void addStudent_invalidEmptyName_NotCreated(){
        try {
            service.addStudent(new Student("some_id", "", 936, "sta"));
            fail("validation exception not thrown");
        }catch (ValidationException exception){

        }
    }

    //testcase6
    @Test
    public void addStudent_invalidNullName_NotCreated(){
        try {
            service.addStudent(new Student("some_id", null, 936, "sta"));
            fail("validation exception not thrown");
        }catch (ValidationException exception){

        }
    }

    //testcase7
    @Test
    public void addStudent_validName_Created(){
        //nume "ana"
        //verificare ca la testcase 3 & 4
    }

    //testcase8
    @Test
    public void addStudent_validPositiveGroup_Created(){
        //group 0
        //group 1
        //verificare ca la testcase 3 & 4

    }

    //testcase9
    @Test
    public void addStudent_invalidNegativeGroup_NotCreated(){
        try {
            service.addStudent(new Student("some_id", "some name", -1, "sta"));
            fail("validation exception not thrown");
        }catch (ValidationException exception){

        }
    }


    //testcase 12
    @Test
    public void addStudent_invalidEmptyEmail_NotCreated(){
        try {
            service.addStudent(new Student("some_id", "some name", 936, ""));
            fail("validation exception not thrown");
        }catch (ValidationException exception){

        }
    }

    //testcase 13
    @Test
    public void addStudent_invalidNullEmail_NotCreated(){
        try {
            service.addStudent(new Student("some_id", "some name", 936, ""));
            fail("validation exception not thrown");
        }catch (ValidationException exception){

        }
    }

    //testcase 14
    @Test
    public void addStudent_validEmail_Created(){
        //email "ana"
        //verificare ca la testcase 3 & 4

    }
}
