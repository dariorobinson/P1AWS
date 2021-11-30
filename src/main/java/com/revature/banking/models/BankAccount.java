package com.revature.banking.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.revature.banking.orm.annotation.ColumnInORM;
import com.revature.banking.orm.annotation.DataSourceORM;
import com.revature.banking.orm.annotation.NotIntoDabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties(ignoreUnknown = true)
@DataSourceORM(TableName = "bank_accounts", Schema = "banking")
public class BankAccount {

    @ColumnInORM(Constraint = "NOT NULL", PRIMARY = "Y")
    private String bank_account_id;
    @ColumnInORM(Constraint = "NOT NULL", Size = 255, UNIQUE = "Y", Check = "(account_name)::text <> ''::text")
    private String account_name;
    @ColumnInORM(Constraint = "NOT NULL", Size = 255, UNIQUE = "Y", Check = "(account_number)::text <> ''::text")
    private String account_number;
    @ColumnInORM(Constraint = "NOT NULL", Size = 255, Check = "(account_type)::text <> ''::text")
    private String account_type;
    @ColumnInORM(Constraint = "NOT NULL", DefaultValue = "0.00")
    private Double balance;
    @ColumnInORM(Constraint = "NOT NULL", Size = 255, FOREIGN = "app_users(user_id)", Check = "(creator_id)::text <> ''::text")
    private String creator_id;
    @NotIntoDabase
    @ColumnInORM(Constraint = "NOT NULL", DefaultValue = "LOCALTIMESTAMP")
    private LocalDateTime date_added;

    public BankAccount() {
    }

    public BankAccount(String accountName, String accountType) {
        this.account_name = accountName;
        this.account_type = accountType;
    }

    public BankAccount(String account_name, String account_type, String creator_id) {
        this.account_name = account_name;
        this.account_type = account_type;
        this.creator_id = creator_id;
    }

    public BankAccount(String account_name, String account_number, String account_type, Double balance, String creator_id) {
        this.account_name = account_name;
        this.account_number = account_number;
        this.account_type = account_type;
        this.balance = balance;
        this.creator_id = creator_id;
    }

    public BankAccount(String bank_account_id, String accountName, String account_number,
                       String accountType, double balance, String creator_id) {
        this(accountName, account_number, accountType, balance, creator_id);
        this.bank_account_id = bank_account_id;
        this.creator_id = creator_id;
    }

    public String getBank_account_id() {
        return bank_account_id;
    }

    public void setBank_account_id(String bank_account_id) {
        this.bank_account_id = bank_account_id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public LocalDateTime getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        System.out.println(date_added);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSS");
        this.date_added = LocalDateTime.parse(date_added, formatter);
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "bank_account_id='" + bank_account_id + '\'' +
                ", account_name='" + account_name + '\'' +
                ", account_number='" + account_number + '\'' +
                ", account_type='" + account_type + '\'' +
                ", balance=" + balance +
                ", creator_id=" + creator_id +
                ", date_added=" + date_added +
                '}';
    }
}
