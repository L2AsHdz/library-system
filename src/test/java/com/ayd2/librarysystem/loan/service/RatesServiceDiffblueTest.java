package com.ayd2.librarysystem.loan.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ayd2.librarysystem.book.model.BookModel;
import com.ayd2.librarysystem.career.model.CareerModel;
import com.ayd2.librarysystem.loan.model.LoanModel;
import com.ayd2.librarysystem.loan.repository.LoanRepository;
import com.ayd2.librarysystem.user.model.StudentModel;
import com.ayd2.librarysystem.user.model.enums.Rol;
import com.ayd2.librarysystem.user.repository.StudentRepository;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {RatesService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class RatesServiceDiffblueTest {
    @MockBean
    private LoanRepository loanRepository;

    @Autowired
    private RatesService ratesService;

    @MockBean
    private StudentRepository studentRepository;

    /**
     * Method under test: {@link RatesService#priceCalculation()}
     */
    @Test
    void testPriceCalculation() {
        // Arrange
        when(loanRepository.findAllByStatus(Mockito.<String>any())).thenReturn(new ArrayList<>());

        // Act
        ratesService.priceCalculation();

        // Assert that nothing has changed
        verify(loanRepository).findAllByStatus(eq("ACTIVE"));
    }

    /**
     * Method under test: {@link RatesService#priceCalculation()}
     */
    @Test
    void testPriceCalculation2() {
        // Arrange
        BookModel bookModel = new BookModel();
        bookModel.setAuthor("JaneDoe");
        bookModel.setId(1L);
        bookModel.setPublicationDate(LocalDate.of(1970, 1, 1));
        bookModel.setPublisher("ACTIVE");
        bookModel.setStock(1L);
        bookModel.setTitle("Dr");

        CareerModel careerModel = new CareerModel();
        careerModel.setId(1L);
        careerModel.setName("ACTIVE");

        StudentModel studentModel = new StudentModel();
        studentModel.setAcademicNumber(1L);
        studentModel.setBirthDate(LocalDate.of(1970, 1, 1));
        studentModel.setCareerModel(careerModel);
        studentModel.setEmail("jane.doe@example.org");
        studentModel.setFullName("Dr Jane Doe");
        studentModel.setId(1L);
        studentModel.setIsSanctioned(true);
        studentModel.setPassword("iloveyou");
        studentModel.setStatus((short) 1);
        studentModel.setUserRole(Rol.ADMIN);
        studentModel.setUsername("janedoe");

        LoanModel loanModel = new LoanModel();
        loanModel.setBookModel(bookModel);
        loanModel.setId(1L);
        loanModel.setLoanArrears(10.0d);
        loanModel.setLoanDate(LocalDate.of(1970, 1, 1));
        loanModel.setLoanPrice(10.0d);
        loanModel.setReturnDate(LocalDate.of(1970, 1, 1));
        loanModel.setStatus("ACTIVE");
        loanModel.setStudentModel(studentModel);

        ArrayList<LoanModel> loanModelList = new ArrayList<>();
        loanModelList.add(loanModel);
        when(loanRepository.findAllByStatus(Mockito.<String>any())).thenReturn(loanModelList);

        // Act
        ratesService.priceCalculation();

        // Assert
        verify(loanRepository).findAllByStatus(eq("ACTIVE"));
    }

    /**
     * Method under test: {@link RatesService#priceCalculation()}
     */
    @Test
    void testPriceCalculation3() {
        // Arrange
        BookModel bookModel = new BookModel();
        bookModel.setAuthor("JaneDoe");
        bookModel.setId(1L);
        bookModel.setPublicationDate(LocalDate.of(1970, 1, 1));
        bookModel.setPublisher("ACTIVE");
        bookModel.setStock(1L);
        bookModel.setTitle("Dr");

        CareerModel careerModel = new CareerModel();
        careerModel.setId(1L);
        careerModel.setName("ACTIVE");

        StudentModel studentModel = new StudentModel();
        studentModel.setAcademicNumber(1L);
        studentModel.setBirthDate(LocalDate.of(1970, 1, 1));
        studentModel.setCareerModel(careerModel);
        studentModel.setEmail("jane.doe@example.org");
        studentModel.setFullName("Dr Jane Doe");
        studentModel.setId(1L);
        studentModel.setIsSanctioned(true);
        studentModel.setPassword("iloveyou");
        studentModel.setStatus((short) 1);
        studentModel.setUserRole(Rol.ADMIN);
        studentModel.setUsername("janedoe");

        LoanModel loanModel = new LoanModel();
        loanModel.setBookModel(bookModel);
        loanModel.setId(1L);
        loanModel.setLoanArrears(10.0d);
        loanModel.setLoanDate(LocalDate.of(1970, 1, 1));
        loanModel.setLoanPrice(10.0d);
        loanModel.setReturnDate(LocalDate.of(1970, 1, 1));
        loanModel.setStatus("ACTIVE");
        loanModel.setStudentModel(studentModel);

        BookModel bookModel2 = new BookModel();
        bookModel2.setAuthor("ACTIVE");
        bookModel2.setId(2L);
        bookModel2.setPublicationDate(LocalDate.of(1970, 1, 1));
        bookModel2.setPublisher("Publisher");
        bookModel2.setStock(0L);
        bookModel2.setTitle("Mr");

        CareerModel careerModel2 = new CareerModel();
        careerModel2.setId(2L);
        careerModel2.setName("Name");

        StudentModel studentModel2 = new StudentModel();
        studentModel2.setAcademicNumber(0L);
        studentModel2.setBirthDate(LocalDate.of(1970, 1, 1));
        studentModel2.setCareerModel(careerModel2);
        studentModel2.setEmail("john.smith@example.org");
        studentModel2.setFullName("Mr John Smith");
        studentModel2.setId(2L);
        studentModel2.setIsSanctioned(false);
        studentModel2.setPassword("ACTIVE");
        studentModel2.setStatus((short) 0);
        studentModel2.setUserRole(Rol.STUDENT);
        studentModel2.setUsername("ACTIVE");

        LoanModel loanModel2 = new LoanModel();
        loanModel2.setBookModel(bookModel2);
        loanModel2.setId(2L);
        loanModel2.setLoanArrears(10.0d);
        loanModel2.setLoanDate(LocalDate.of(1970, 1, 1));
        loanModel2.setLoanPrice(10.0d);
        loanModel2.setReturnDate(LocalDate.of(1970, 1, 1));
        loanModel2.setStatus("Status");
        loanModel2.setStudentModel(studentModel2);

        ArrayList<LoanModel> loanModelList = new ArrayList<>();
        loanModelList.add(loanModel2);
        loanModelList.add(loanModel);
        when(loanRepository.findAllByStatus(Mockito.<String>any())).thenReturn(loanModelList);

        // Act
        ratesService.priceCalculation();

        // Assert
        verify(loanRepository).findAllByStatus(eq("ACTIVE"));
    }

    /**
     * Method under test: {@link RatesService#arrearCalculation()}
     */
    @Test
    void testArrearCalculation() {
        // Arrange
        when(loanRepository.findAllByStatus(Mockito.<String>any())).thenReturn(new ArrayList<>());

        // Act
        ratesService.arrearCalculation();

        // Assert that nothing has changed
        verify(loanRepository).findAllByStatus(eq("ARREARS"));
    }

    /**
     * Method under test: {@link RatesService#arrearCalculation()}
     */
    @Test
    void testArrearCalculation4() {
        // Arrange
        BookModel bookModel = new BookModel();
        bookModel.setAuthor("JaneDoe");
        bookModel.setId(1L);
        bookModel.setPublicationDate(LocalDate.of(1970, 1, 1));
        bookModel.setPublisher("ARREARS");
        bookModel.setStock(1L);
        bookModel.setTitle("Dr");

        CareerModel careerModel = new CareerModel();
        careerModel.setId(1L);
        careerModel.setName("ARREARS");

        StudentModel studentModel = new StudentModel();
        studentModel.setAcademicNumber(1L);
        studentModel.setBirthDate(LocalDate.of(1970, 1, 1));
        studentModel.setCareerModel(careerModel);
        studentModel.setEmail("jane.doe@example.org");
        studentModel.setFullName("Dr Jane Doe");
        studentModel.setId(1L);
        studentModel.setIsSanctioned(true);
        studentModel.setPassword("iloveyou");
        studentModel.setStatus((short) 1);
        studentModel.setUserRole(Rol.ADMIN);
        studentModel.setUsername("janedoe");
        LoanModel loanModel = mock(LoanModel.class);
        when(loanModel.getLoanArrears()).thenReturn(10.0d);
        when(loanModel.getReturnDate()).thenReturn(LocalDate.now());
        doNothing().when(loanModel).setBookModel(Mockito.<BookModel>any());
        doNothing().when(loanModel).setId(Mockito.<Long>any());
        doNothing().when(loanModel).setLoanArrears(Mockito.<Double>any());
        doNothing().when(loanModel).setLoanDate(Mockito.<LocalDate>any());
        doNothing().when(loanModel).setLoanPrice(Mockito.<Double>any());
        doNothing().when(loanModel).setReturnDate(Mockito.<LocalDate>any());
        doNothing().when(loanModel).setStatus(Mockito.<String>any());
        doNothing().when(loanModel).setStudentModel(Mockito.<StudentModel>any());
        loanModel.setBookModel(bookModel);
        loanModel.setId(1L);
        loanModel.setLoanArrears(10.0d);
        loanModel.setLoanDate(LocalDate.of(1970, 1, 1));
        loanModel.setLoanPrice(10.0d);
        loanModel.setReturnDate(LocalDate.of(1970, 1, 1));
        loanModel.setStatus("ARREARS");
        loanModel.setStudentModel(studentModel);

        ArrayList<LoanModel> loanModelList = new ArrayList<>();
        loanModelList.add(loanModel);
        when(loanRepository.findAllByStatus(Mockito.<String>any())).thenReturn(loanModelList);

        // Act
        ratesService.arrearCalculation();

        // Assert that nothing has changed
        verify(loanModel).getLoanArrears();
        verify(loanModel).getReturnDate();
        verify(loanModel).setBookModel(isA(BookModel.class));
        verify(loanModel).setId(eq(1L));
        verify(loanModel, atLeast(1)).setLoanArrears(Mockito.<Double>any());
        verify(loanModel).setLoanDate(isA(LocalDate.class));
        verify(loanModel).setLoanPrice(eq(10.0d));
        verify(loanModel).setReturnDate(isA(LocalDate.class));
        verify(loanModel).setStatus(eq("ARREARS"));
        verify(loanModel).setStudentModel(isA(StudentModel.class));
        verify(loanRepository).findAllByStatus(eq("ARREARS"));
    }
}
