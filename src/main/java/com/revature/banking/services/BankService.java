package com.revature.banking.services;

import com.revature.banking.daos.BankDAO;
import com.revature.banking.exceptions.NotEnoughBalanceException;
import com.revature.banking.exceptions.ResourcePersistenceException;
import com.revature.banking.models.BankAccount;
import com.revature.banking.models.BankTransaction;
import com.revature.banking.orm.utils.CrudORM;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BankService {

    private final BankDAO bankDAO;
    private final UserService userService;
    private final CrudORM crudORM;

    public BankService(BankDAO bankDAO, UserService userService, CrudORM crudORM) {
        this.bankDAO = bankDAO;
        this.userService = userService;
        this.crudORM = crudORM;
    }

    public boolean isBankAccountValid(BankAccount bankAccount) {
        if (bankAccount == null) return false;
        if (bankAccount.getAccount_name() == null || bankAccount.getAccount_name().trim().equals("")) return false;
        if (bankAccount.getAccount_type() == null || bankAccount.getAccount_type().trim().equals("")) return false;
        return true;
    }

    public boolean openBankAccount(BankAccount bankAccount) {
//        BankAccount registeredBankAccount = bankDAO.save(bankAccount);

        bankAccount.setBank_account_id(UUID.randomUUID().toString());
        BankAccount registeredBankAccount = crudORM.insertTable(bankAccount);

        if (registeredBankAccount == null) {
            throw new ResourcePersistenceException("The bank account could not be persisted to the datasource!");
        }

        return true;
    }

    public List<BankAccount> getBankAccountsByUserId() {
        return getBankAccountsByUserId("Me");
    }

    public List<BankAccount> getBankAccountsByUserId(String Others_or_Me) {

        List<BankAccount> registeredBankAccounts = bankDAO.findBankAccountsByUserId("userId", Others_or_Me);

        if (registeredBankAccounts == null) {
            throw new ResourcePersistenceException("The bank account could not be persisted to the datasource!");
        }

        return registeredBankAccounts;
    }


    public List<BankAccount> getBankAccountsByOthersThanBankAccountId(String bankAccount_from_this) {

        List<BankAccount> registeredBankAccounts = bankDAO.findBankAccountsByOthersThanBankAccountId(bankAccount_from_this);

        if (registeredBankAccounts == null) {
            throw new ResourcePersistenceException("The bank account could not be persisted to the datasource!");
        }

        return registeredBankAccounts;
    }

    public boolean update(BankAccount bankAccount, Map<String, Map<String, String>> whereOderBy) {

        bankAccount = crudORM.updateTable(bankAccount, whereOderBy);
        System.out.println(bankAccount);

        if (bankAccount == null) {
            throw new ResourcePersistenceException("The transaction could not be persisted to the datasource!");
        }

        return true;
    }

    public boolean delete(BankAccount bankAccount, Map<String, Map<String, String>> whereOderBy) {

        bankAccount = crudORM.deletTable(bankAccount, whereOderBy);
        System.out.println(bankAccount);

        if (bankAccount == null) {
            throw new ResourcePersistenceException("The transaction could not be persisted to the datasource!");
        }

        return true;
    }

    public BankTransaction transact(BankTransaction bankTransaction) {

        // withdraw, transfer
        if (bankTransaction.getBankAccount_From().getBalance() + bankTransaction.getAmount() < 0) {
            throw new NotEnoughBalanceException();
        }

        bankTransaction = bankDAO.transact(bankTransaction);

        if (bankTransaction == null) {
            throw new ResourcePersistenceException("The transaction could not be persisted to the datasource!");
        }

        return bankTransaction;
    }

    public List<BankTransaction> getTransactionsByUserAccountId(BankAccount bankAccount) {

        List<BankTransaction> bankTransactions = bankDAO.findTransactionsByUserAccountId(bankAccount.getBank_account_id());

        if (bankTransactions == null) {
            throw new ResourcePersistenceException("The transactions could not be persisted to the datasource!");
        }

        return bankTransactions;
    }

    public double deposit(BankAccount bankAccount, double amount) {

        return 0;
    }

    public double withdraw(BankAccount bankAccount, double amount) {

        return 0;
    }

    public BankAccount[] viewBalances() {

        return null;
    }
}
