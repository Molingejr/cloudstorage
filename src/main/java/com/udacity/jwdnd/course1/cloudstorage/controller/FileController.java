package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileRepository;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.io.ByteArrayResource;


import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

@Controller
public class FileController {

    @Autowired private FileRepository fileRepository;
    @Autowired private UserRepository userRepository;

    @GetMapping("/files")
    @ResponseBody
    public List<File> getNote(Integer userid){
        return fileRepository.findByUserId(userid);
    }

    @GetMapping("/file/view/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") Integer fileId){
        File file = fileRepository.getFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new ByteArrayResource(file.getFiledata()));
    }

    @PostMapping("/file/upload")
    public String addFile(HttpSession session, Authentication authentication,
                          @RequestParam("fileUpload") MultipartFile fileUpload){

        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        String filename = StringUtils.cleanPath(Objects.requireNonNull(fileUpload.getOriginalFilename()));
        String contenttype = fileUpload.getContentType();

        if (filename.equals("")){
            session.setAttribute("error_message",  "Please Provide a file ");
            return "redirect:/result?error";
        }

        File existingFile = fileRepository.getFileByNameAndUserId(user.getUserId(), filename);
        if (existingFile != null){
            session.setAttribute("error_message",  "Sorry! the filename {" + filename + "} already exist. try another one");
            return "redirect:/result?error";
        }
        File files = new File();

        files.setFilename(filename);
        files.setContenttype(contenttype);
        files.setUser(user);
        files.setUserid(user.getUserId());
        try{
            files.setFiledata(fileUpload.getBytes());
            files.setFilesize(String.valueOf(fileUpload.getSize()));

            fileRepository.createFile(files);

        } catch (Exception e){
            session.setAttribute("error_message", e.getMessage());
            return "redirect:/result?error";
        }

        return "redirect:/result?success";
    }

    @RequestMapping(value = "/file/{fileId}", method = {RequestMethod.GET, RequestMethod.DELETE})
    public String deleteFile(HttpSession session, @PathVariable("fileId") String fileId){
        try {
            int fileid = Integer.parseInt(fileId);
            fileRepository.deleteFile(fileid);
            return "redirect:/result?success";
        }catch (Exception e){
            session.setAttribute("error_message", e.getMessage());
            return "redirect:/result?error";
        }

    }
}
