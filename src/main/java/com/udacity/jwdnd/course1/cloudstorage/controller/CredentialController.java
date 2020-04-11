package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialRepository;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserRepository;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncoderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class CredentialController {

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private EncoderService encoderService;

    @PostMapping("/credentials")
    public String addAndUpdateCredential(HttpSession session, Authentication authentication,
                                 @RequestParam("credentialId") Integer credentialId,
                                 @RequestParam String url,
                                 @RequestParam String username,
                                 @RequestParam String password){

        if (credentialId == null){
            String userName = authentication.getName();
            User user = userRepository.findByUsername(userName);
            Credential credential = new Credential();

            String encodedKey = encoderService.encodeValue();
            credential.setKey(encodedKey);
            String encryptedPassword = encryptionService.encryptValue(password, encodedKey);

            credential.setPassword(encryptedPassword);
            credential.setUrl(url);
            credential.setUsername(username);
            credential.setUserid(user.getUserId());
            credential.setUser(user);
            credentialRepository.createCredentials(credential);

        }else {
            Credential credential = credentialRepository.findById(credentialId);
            credential.setUrl(url);
            credential.setUsername(username);
            String encryptedPassword = encryptionService.encryptValue(password, credential.getKey());
            credential.setPassword(encryptedPassword);
            try{
                credentialRepository.updateCredential(credential);
            } catch (Exception e){
                session.setAttribute("error_message", e.getMessage());
                return "redirect:/result?error";
            }
        }

        return "redirect:/result?success";
    }

    @GetMapping("/credentials/show")
    public String showCredential(){
        return "redirect:/result?success";
    }

    @RequestMapping(value = "/credentials/delete/{credentialId}", method = {RequestMethod.GET, RequestMethod.DELETE})
    public String deleteCredential(@PathVariable("credentialId") String credentialId){
        int credentialid = Integer.parseInt(credentialId);
        credentialRepository.deleteCredential(credentialid);
        return "redirect:/result?success";
    }
}
