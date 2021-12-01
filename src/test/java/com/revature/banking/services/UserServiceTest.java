package com.revature.banking.services;

import com.revature.banking.daos.AppUserDAO;
import com.revature.banking.daos.BankDAO;

import com.revature.banking.exceptions.InvalidRequestException;

import com.revature.banking.models.AppUser;

import com.revature.banking.orm.OrmMain;

import com.revature.banking.orm.models.AppUserORM;
import com.revature.banking.orm.models.BankAccountORM;
import com.revature.banking.orm.utils.CrudORM;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;




import static org.mockito.Mockito.*;

public class UserServiceTest {

    UserService sut;
    BankService but;
    AppUserDAO mockUserDAO;
    AppUserORM mockUserORM;
    BankAccountORM mockBankAccountORM;
    BankDAO mockBankDAO;
    CrudORM mockCrudORM;
    OrmMain mockORMMain;
    UserService mockUserService;


    @Before
    public void testCaseSetup() {
        mockUserDAO = mock(AppUserDAO.class);
        mockBankDAO = mock(BankDAO.class);
        mockCrudORM = mock(CrudORM.class);
        mockUserService = mock(UserService.class);
        mockUserORM = mock(AppUserORM.class);
        mockBankAccountORM = mock(BankAccountORM.class);
        mockORMMain = mock(OrmMain.class);
        sut = new UserService(mockUserDAO, mockCrudORM);
        but = new BankService(mockBankDAO, sut, mockCrudORM);
    }

    @After
    public void testCaseCleanUp() {
        sut = null;
    }


    @Test
    public void test_registerNewUser_returnsTrue_givenValidUser() {

        // Arrange
        AppUser validUser = new AppUser("valid", "valid", "valid", "valid", "valid");
        when(mockUserDAO.findUserByUsername(validUser.getUsername())).thenReturn(null);
        when(mockUserDAO.findUserByEmail(validUser.getEmail())).thenReturn(null);
        when(mockCrudORM.insertTable(validUser)).thenReturn(validUser);

        // Act
        boolean actualResult = sut.registerNewUser(validUser);

        // Assert
        Assert.assertTrue("Expected result to be true with valid user provided.", actualResult);
        verify(mockCrudORM, times(1)).insertTable(validUser);

    }


    @Test(expected = InvalidRequestException.class)
    public void test_registerNewUser_throwsInvalidRequestException_givenInvalidUser() {
        // Arrange
        AppUser InvalidUser = new AppUser("  ", "valid", "valid", "valid", "valid");
        when(mockUserDAO.findUserByUsername(InvalidUser.getUsername())).thenReturn(new AppUser());
        when(mockUserDAO.findUserByEmail(InvalidUser.getEmail())).thenReturn(new AppUser());
        when(mockCrudORM.insertTable(InvalidUser)).thenReturn(InvalidUser);
        // Act
        try {
            boolean actualResult = sut.registerNewUser(InvalidUser);
        } finally {
            // Assert
            verify(mockCrudORM, times(0)).insertTable(InvalidUser);
        }
    }


    @Test
    public void test_authenticateUser__returnsTrue_givenValidUsernameValidPassword() {
        // Arrange
        AppUser ValidUser = new AppUser("valid", "valid", "valid", "valid", "valid");
        String ValidUsername = "valid", ValidEmail = "valid";
        when(mockUserDAO.findUserByUsernameAndPassword(ValidUsername, ValidEmail)).thenReturn(new AppUser());
        // Act
        try {
            sut.authenticateUser(ValidUsername, ValidEmail);
        } finally {
            // Assert
        }
    }

    @Test(expected = InvalidRequestException.class)
    public void test_authenticateUser_throwsInvalidRequestException_givenInvalidUsernameOrInvalidPassword() {
        // Arrange
        AppUser ValidUser = new AppUser("valid", "valid", "valid", "valid", "valid");
        String invalidUsername = " ", invalidEmail = " ";
        when(mockUserDAO.findUserByUsernameAndPassword(invalidUsername, invalidEmail)).thenReturn(new AppUser());
        // Act
        try {
            sut.authenticateUser(invalidUsername, invalidEmail);
        } finally {
            // Assert
        }
    }


}

