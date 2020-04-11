package com.udacity.jwdnd.course1.cloudstorage.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileRepository;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteRepository;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialRepository;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserRepository;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HomeController {

    @Autowired private FileRepository filesRepository;
    @Autowired private NoteRepository notesRepository;
    @Autowired private CredentialRepository credentialsRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private EncryptionService encryptionService;

    @GetMapping("/home")
    public String homePage(Authentication authentication, HttpSession session, Model model){
        try{
            String username = authentication.getName();
            User user = userRepository.findByUsername(username);
            List<File> files = filesRepository.findByUserId(user.getUserId());
            List<Note> notes = notesRepository.findByUserId(user.getUserId());
            List<Credential> creds = credentialsRepository.findByUserId(user.getUserId());

            model.addAttribute("filesData", files);
            model.addAttribute("notesData", notes);
            model.addAttribute("credentialsData", creds);
            model.addAttribute("encryptionService", encryptionService);

            return "home";
        }catch(Exception e){
            session.setAttribute("error_message", e.getMessage() != null ? e.getMessage() : "Fail to load data.");
            return "redirect:/result?error";
        }

    }

    @GetMapping("/result")
    public String result(){ return "result"; }

    @GetMapping("/")
    public String index(){ return "redirect:/home"; }

}
