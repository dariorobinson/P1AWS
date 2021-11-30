package com.revature.banking.web.servlets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.banking.models.AppUser;
import com.revature.banking.models.BankAccount;
import com.revature.banking.services.BankService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BankServlet extends HttpServlet {

    private final BankService bankService;
    private final ObjectMapper mapper;

    public BankServlet(BankService bankService, ObjectMapper mapper) {
        this.bankService = bankService;
        this.mapper = mapper;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("beginning of doPost..... bank");

        AppUser authUser = (AppUser) req.getSession().getAttribute("authUser");
        if (authUser == null) {
            System.out.println("User login first!");
            resp.setStatus(400); // client error; BAD REQUEST
            return;
        }
        System.out.println(authUser.toString());

        try {
            System.out.println("type = " + req.getParameter("type"));
            boolean wasRegistered = false;
            BankAccount bankAccount = mapper.readValue(req.getInputStream(), BankAccount.class);
            System.out.println(bankAccount);;
            if (req.getParameter("type").equals("create")) {
                bankAccount.setCreator_id(authUser.getUser_id());
                System.out.println(bankAccount.toString());
                wasRegistered = bankService.openBankAccount(bankAccount);
            } else if (req.getParameter("type").equals("update")) {
                String update_column = req.getParameter("col");

                System.out.println("col = " + req.getParameter("col"));

                Map<String, Map<String, String>> whereOderBy = new HashMap<>();

                Map<String, String> cols = new HashMap<>();
                cols.put(update_column, "valid-update");
                whereOderBy.put("cols", cols);

                Map<String, String> where = new HashMap<>();
                where.put("bank_account_id", bankAccount.getBank_account_id());
                whereOderBy.put("where", where);

                System.out.println(whereOderBy);

                wasRegistered = bankService.update(bankAccount, whereOderBy);
            } else if (req.getParameter("type").equals("delete")) {
                Map<String, Map<String, String>> whereOderBy = new HashMap<>();

                Map<String, String> where = new HashMap<>();
                where.put("bank_account_id", bankAccount.getBank_account_id());
                whereOderBy.put("where", where);

                System.out.println(whereOderBy);

                wasRegistered = bankService.delete(bankAccount, whereOderBy);
            }

            if (wasRegistered) {
                System.out.println("Bank successfully persisted!");
                resp.setStatus(201);
            } else {
                System.out.println("Could not persist bank! Check logs.");
                resp.setStatus(500); // server error
            }
        } catch (JsonParseException e) {
            resp.setStatus(400); // client error; BAD REQUEST
            e.printStackTrace();
        }
    }
}
