package com.revature.banking.services;

import com.revature.banking.daos.AppUserDAO;
import com.revature.banking.daos.BankDAO;
import com.revature.banking.exceptions.ResourcePersistenceException;
import com.revature.banking.models.BankAccount;
import com.revature.banking.orm.OrmMain;
import com.revature.banking.orm.models.AppUserORM;
import com.revature.banking.orm.models.BankAccountORM;
import com.revature.banking.orm.utils.CrudORM;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class BankServiceTest {

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
    public void test_openBankAccount_returnsTrue_givenValidUser() {

        BankAccount validBankAccount = new BankAccount("valid", "valid");
        when(mockCrudORM.insertTable(validBankAccount)).thenReturn(validBankAccount);

        boolean actualResult = but.openBankAccount(validBankAccount);

        Assert.assertTrue("Expected result to be true with valid bank account provided.", actualResult);
        verify(mockCrudORM, times(1)).insertTable(validBankAccount);
    }

    @Test(expected = ResourcePersistenceException.class)
    public void test_openBankAccount_throwsResourcePersistenceException_givenValidBankAccount() {

        BankAccount validBankAccount = new BankAccount("valid", "valid");
        when(mockCrudORM.insertTable(validBankAccount)).thenReturn(null);

        try {
            boolean actualResult = but.openBankAccount(validBankAccount);
        } finally {
            verify(mockCrudORM, times(1)).insertTable(validBankAccount);
        }
    }

    @Test
    public void test_updateBankAccount_returnsTrue_givenValidUser() {

        BankAccount validBankAccount = new BankAccount("valid", "valid");
        validBankAccount.setBank_account_id("testid");

        Map<String, Map<String, String>> whereOderBy = new HashMap<>();

        Map<String, String> cols = new HashMap<>();
        cols.put("account_name", "valid-222");
        whereOderBy.put("cols", cols);

        Map<String, String> where = new HashMap<>();
        where.put("account_type", "valid");
        whereOderBy.put("where", where);

        when(mockCrudORM.updateTable(validBankAccount, whereOderBy)).thenReturn(validBankAccount);

        boolean actualResult = but.update(validBankAccount, whereOderBy);

        Assert.assertTrue("Expected user bank account to be updated", actualResult);
        verify(mockCrudORM, times(1)).updateTable(validBankAccount, whereOderBy);



    }

    @Test(expected = ResourcePersistenceException.class)
    public void test_updateBankAccount_throwsResourcePersistenceException_givenInvalidBankAccount() {

        BankAccount invalidBankAccount = new BankAccount(null, "valid");
        when(mockCrudORM.insertTable(invalidBankAccount)).thenReturn(invalidBankAccount);

        Map<String, Map<String, String>> whereOderBy = new HashMap<>();

        Map<String, String> cols = new HashMap<>();
        cols.put("accountName", null);
        whereOderBy.put("cols", cols);

        Map<String, String> where = new HashMap<>();
        where.put("accountType", "valid");
        whereOderBy.put("where", where);


        try {
            boolean actualResult = but.update(invalidBankAccount, whereOderBy);
        } finally {
            verify(mockCrudORM, times(1)).updateTable(invalidBankAccount, whereOderBy);
        }
    }

    @Test
    public void test_deleteBankAccount_returnsTrue_givenValidUser() {

        BankAccount validBankAccount = new BankAccount("valid", "valid");
        validBankAccount.setBank_account_id("valid");

        Map<String, Map<String, String>> whereOderBy = new HashMap<>();

        Map<String, String> where = new HashMap<>();
        where.put("account_type", "valid");
        whereOderBy.put("where", where);

        when(mockCrudORM.deletTable(validBankAccount, whereOderBy)).thenReturn(validBankAccount);

        boolean actualResult = but.delete(validBankAccount, whereOderBy);

        Assert.assertTrue("Expected result to be true with valid bank account provided.", actualResult);
        verify(mockCrudORM, times(1)).deletTable(validBankAccount, whereOderBy);
    }

    @Test(expected = ResourcePersistenceException.class)
    public void test_deleteBankAccount_throwsResourcePersistenceException_givenInvalidBankAccount() {

        BankAccount invalidBankAccount = new BankAccount("valid", "valid");
        when(mockCrudORM.insertTable(invalidBankAccount)).thenReturn(invalidBankAccount);

        Map<String, Map<String, String>> whereOderBy = new HashMap<>();

        Map<String, String> cols = new HashMap<>();
        cols.put("accountName", null);
        whereOderBy.put("cols", cols);

        Map<String, String> where = new HashMap<>();
        where.put("accountType", "valid");
        whereOderBy.put("where", where);

        try {
            boolean actualResult = but.delete(invalidBankAccount, whereOderBy);
        } finally {
            verify(mockCrudORM, times(1)).deletTable(invalidBankAccount, whereOderBy);
        }
    }


}
