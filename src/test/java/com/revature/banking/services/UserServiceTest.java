package com.revature.banking.services;

import com.revature.banking.daos.AppUserDAO;
import com.revature.banking.exceptions.InvalidRequestException;
import com.revature.banking.exceptions.ResourcePersistenceException;
import com.revature.banking.models.AppUser;
import com.revature.banking.orm.utils.CrudORM;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static org.mockito.Mockito.*;

public class UserServiceTest {

    UserService sut;
    AppUserDAO mockUserDAO;
    CrudORM mockCrudORM;

    @Before
    public void testCaseSetup() {
        mockUserDAO = mock(AppUserDAO.class);
        mockCrudORM = mock(CrudORM.class);
        sut = new UserService(mockUserDAO, mockCrudORM);
    }

    @After
    public void testCaseCleanUp() {
        sut = null;
    }

    @Test
    public void test_isUserValid_returnsTrue_givenValidUser() {
        // AAA: Arrange, Act, Assert.

        AppUser validUser = new AppUser("valid", "valid", "valid", "valid", "valid");

        boolean actualResult = sut.isUserValid(validUser);

        Assert.assertTrue("Expected user to be considered valid", actualResult);
    }


    @Test
    public void test_isUserValid_returnsFalse_givenUserWithInvalidEmail() {
        AppUser invalidUser_1 = new AppUser("valid", "valid", null, "valid", "valid");
        AppUser invalidUser_2 = new AppUser("valid", "valid", "", "valid", "valid");
        AppUser invalidUser_3 = new AppUser("valid", "valid", "   ", "valid", "valid");

        boolean actualResult_1 = sut.isUserValid(invalidUser_1);
        boolean actualResult_2 = sut.isUserValid(invalidUser_2);
        boolean actualResult_3 = sut.isUserValid(invalidUser_3);

        Assert.assertFalse("Expected user to be considered false.", actualResult_1);
        Assert.assertFalse("Expected user to be considered false.", actualResult_2);
        Assert.assertFalse("Expected user to be considered false.", actualResult_3);

    }

    @Test
    public void test_isUserValid_returnsFalse_givenUserWithInvalidUsername() {
        AppUser invalidUser_1 = new AppUser("valid", "valid", "valid", null, "valid");
        AppUser invalidUser_2 = new AppUser("valid", "valid", "valid", "", "valid");
        AppUser invalidUser_3 = new AppUser("valid", "valid", "valid", "   ", "valid");

        boolean actualResult_1 = sut.isUserValid(invalidUser_1);
        boolean actualResult_2 = sut.isUserValid(invalidUser_2);
        boolean actualResult_3 = sut.isUserValid(invalidUser_3);

        Assert.assertFalse("Expected user to be considered false.", actualResult_1);
        Assert.assertFalse("Expected user to be considered false.", actualResult_2);
        Assert.assertFalse("Expected user to be considered false.", actualResult_3);

    }

    /* @Test
    public void test_registerNewUser_returnsTrue_givenValidUser() {

        AppUser validUser = new AppUser("valid", "valid", "valid", "valid", "valid");
        when(mockUserDAO.findUserByUsername(validUser.getUsername())).thenReturn(null);
        when(mockUserDAO.findUserByEmail(validUser.getEmail())).thenReturn(null);
        when(mockUserDAO.save(validUser)).thenReturn(validUser);

        boolean actualResult = sut.registerNewUser(validUser);

        Assert.assertTrue("Expected result to be true with valid user provided.", actualResult);
        verify(mockUserDAO, times(1)).save(validUser);
    }

     */

   /*  @Test
    public void test_loginExistingUser_returnsTrue_givenValidUser() {

        AppUser validUser = new AppUser("valid", "valid", "valid", "valid", "valid");
        when(mockUserDAO.findUserByUsernameAndPassword(validUser.getUsername(), validUser.getPassword())).thenReturn(validUser);
        when(mockUserDAO.save(validUser)).thenReturn(validUser);

        boolean actualResult = sut.isUserValid(validUser);

        Assert.assertTrue("Expected result to be true with valid user provided.", actualResult);
        verify(mockUserDAO, times(1)).save(validUser);
    }
*/
    @Test(expected = ResourcePersistenceException.class)
    public void test_registerNewUser_throwsResourcePersistenceException_givenValidUserWithTakenUsername() {

        AppUser validUser = new AppUser("valid", "valid", "valid", "valid", "valid");
        when(mockUserDAO.findUserByUsername(validUser.getUsername())).thenReturn(new AppUser());
        when(mockUserDAO.findUserByEmail(validUser.getEmail())).thenReturn(null);
        when(mockUserDAO.save(validUser)).thenReturn(validUser);

        try {
            boolean actualResult = sut.registerNewUser(validUser);
        } finally {
            verify(mockUserDAO, times(0)).save(validUser);
        }

    }

     /* @Test(expected = InvalidRequestException.class)
    public void test_registerNewUser_throwsInvalidRequestException_givenInvalidUser() {

        AppUser validUser = new AppUser("valid", "valid", "valid", "valid", "valid");
        when(mockUserDAO.findUserByUsername(validUser.getUsername())).thenReturn(new AppUser());
        when(mockUserDAO.findUserByEmail(validUser.getEmail())).thenReturn(null);
        when(mockUserDAO.save(validUser)).thenReturn(validUser);

        try {
            boolean actualResult = sut.registerNewUser(validUser);
        } finally {
            verify(mockUserDAO, times(0)).save(validUser);
        }
        */


}

