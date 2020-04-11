package com.udacity.jwdnd.course1.cloudstorage.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(){ return "login"; }

    @PostMapping("/login?logout")
    public String logout(){
        return "redirect:/login?logout";
    }

    @GetMapping("/logout")
    public String logoutView(){
        return "redirect:/login?logout";
    }
}
