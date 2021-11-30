package com.revature.banking.web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.revature.banking.exceptions.AuthenticationException;
import com.revature.banking.exceptions.InvalidRequestException;
import com.revature.banking.models.AppUser;
import com.revature.banking.services.UserService;
import com.revature.banking.web.dtos.Credentials;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthServlet extends HttpServlet {

    private final UserService userService;
    private final ObjectMapper mapper;

    public AuthServlet(UserService userService, ObjectMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    // login
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            System.out.println("Initializing application-- autho");
            Credentials creds = mapper.readValue(req.getInputStream(), Credentials.class);

            System.out.println(creds);

            AppUser authUser = userService.authenticateUser(creds.getUsername(), creds.getPassword());

            // adds a Cookie to the response that is stored within Tomcat
            // and is used to identify the requester in future requests
            HttpSession httpSession = req.getSession();
            httpSession.setAttribute("authUser", authUser);


            resp.setStatus(204); // success, but nothing to return (NO_CONTENT)
        } catch (InvalidRequestException | UnrecognizedPropertyException e) {
            resp.setStatus(400); // user made a bad request
        } catch (AuthenticationException e) {
            resp.setStatus(401); // user provided incorrect credentials
        } catch (Exception e) {
            e.printStackTrace(); // for dev purposes only, to be deleted before push to prod
            resp.setStatus(500);
        }

    }

    // logout
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate(); // invalidates the session associated with this request (logging the user out)
    }
}
