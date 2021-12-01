package com.revature.banking.services;

import com.revature.banking.daos.AppUserDAO;
import com.revature.banking.exceptions.AuthenticationException;
import com.revature.banking.exceptions.InvalidRequestException;
import com.revature.banking.exceptions.ResourcePersistenceException;
import com.revature.banking.models.AppUser;
import com.revature.banking.orm.utils.CrudORM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserService {

    private final AppUserDAO userDAO;
    private final CrudORM crudORM;

    public UserService(AppUserDAO userDAO, CrudORM crudORM) {
        this.userDAO = userDAO;
        this.crudORM = crudORM;
    }

    public boolean registerNewUser(AppUser newUser) {

        if (!isUserValid(newUser)) {
            throw new InvalidRequestException("Invalid user data provided!");
        }
        // username
        Map<String, Map<String, String>> whereOderBy = new HashMap<>();

        Map<String, String> where = new HashMap<>();
        where.put("username", newUser.getUsername());
        whereOderBy.put("where", where);

        boolean usernameAvailable = true;
        List<AppUser> appUserList = crudORM.readTable(newUser, whereOderBy, AppUser.class);
        for (AppUser appUser : appUserList) {
            System.out.println(appUser);
            usernameAvailable = false;
            break;
        }
        // email
        whereOderBy = new HashMap<>();

        where = new HashMap<>();
        where.put("email", newUser.getEmail());
        whereOderBy.put("where", where);

        boolean emailAvailable = true;
        appUserList = crudORM.readTable(newUser, whereOderBy, AppUser.class);
        for (AppUser appUserORM : appUserList) {
            System.out.println(appUserORM);
            emailAvailable = false;
            break;
        }

        // ---
        if (!usernameAvailable || !emailAvailable) {
            String msg = "The values provided for the following fields are already taken by other users:";
            if (!usernameAvailable) msg = msg + "\n\t- username";
            if (!emailAvailable) msg = msg + "\n\t- email";
            throw new ResourcePersistenceException(msg);
        }

//        AppUser registeredUser = userDAO.save(newUser);

        newUser.setUser_id(UUID.randomUUID().toString());
        AppUser registeredUser = crudORM.insertTable(newUser);

        if (registeredUser == null) {
            throw new ResourcePersistenceException("The user could not be persisted to the datasource!");
        }


        return true;

    }

    public AppUser authenticateUser(String username, String password) {

        if (username == null || username.trim().equals("") || password == null || password.trim().equals("")) {
            throw new InvalidRequestException("Invalid credential values provided!");
        }

        AppUser authenticatedUser = userDAO.findUserByUsernameAndPassword(username, password);

        if (authenticatedUser == null) {
            throw new AuthenticationException();
        }

        return authenticatedUser;

    }

    public boolean isUserValid(AppUser user) {
        if (user == null) return false;
        if (user.getFirst_name() == null || user.getFirst_name().trim().equals("")) return false;
        if (user.getLast_name() == null || user.getLast_name().trim().equals("")) return false;
        if (user.getEmail() == null || user.getEmail().trim().equals("")) return false;
        if (user.getUsername() == null || user.getUsername().trim().equals("")) return false;
        return user.getPassword() != null && !user.getPassword().trim().equals("");
    }


}
