package com.udacity.jwdnd.course1.cloudstorage.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserRepository;
import com.udacity.jwdnd.course1.cloudstorage.services.HashService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.Base64;

@Controller
public class SignupController {

    @Autowired private UserRepository userRepository;
    @Autowired private HashService hashService;

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String addUser(HttpSession session,
                          @RequestParam String firstName,
                          @RequestParam String lastName,
                          @RequestParam String username,
                          @RequestParam String password ){

        User exitingUser = userRepository.findByUsername(username);
        if (exitingUser != null){
            session.setAttribute("error_message",  "Sorry! the username {" + username + "} already exist. try another one");
            return "redirect:/signup?error";
        }
        // new user
        User user = new User();
        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setUsername(username);

        // Create random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);

        user.setSalt(encodedSalt);
        String hashedPassword = hashService.getHashedValue(password, encodedSalt);
        user.setPassword(hashedPassword);

        try{
            userRepository.insert(user);
        }
        catch(Exception e){
            session.setAttribute("error_message",  e.getMessage());
            return "redirect:/signup?error";
        }

        return "redirect:/signup?success";

    }

}
