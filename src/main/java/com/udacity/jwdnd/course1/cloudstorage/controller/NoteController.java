package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteRepository;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class NoteController {
    @Autowired private NoteRepository noteRepository;
    @Autowired private UserRepository userRepository;

    @GetMapping("/notes")
    @ResponseBody
    public List<Note> getNote(Integer userid){
        return noteRepository.findByUserId(userid);
    }

    @PostMapping("/notes")
    public String addAndEditNote(Authentication authentication, HttpSession session,
                                 @RequestParam Integer noteId,
                                 @RequestParam String noteTitle,
                                 @RequestParam String noteDescription){

        if (noteId == null){
            // Create new Note
            String username = authentication.getName();
            Note note = new Note();
            try{

                User user = userRepository.findByUsername(username);
                note.setNoteTitle(noteTitle);
                note.setNoteDescription(noteDescription);
                note.setUser(user);
                note.setUserId(user.getUserId());
                noteRepository.createNote(note);
            } catch (Exception e){
                session.setAttribute("error_message", e.getMessage());
                return "redirect:/result?error";
            }
        }else {
            // Edit existing note
            System.out.println("The note id is " + noteId);
            Note note = noteRepository.getNote(noteId);
            note.setNoteTitle(noteTitle);
            note.setNoteDescription(noteDescription);
            try{
                noteRepository.updateNote(note);
            } catch(Exception e){
                session.setAttribute("error_message", e.getMessage());
                return "redirect:/result?error";
            }
        }

        return "redirect:/result?success";
    }

    @RequestMapping(value = "/notes/{noteId}", method = {RequestMethod.GET, RequestMethod.DELETE})
    public String deleteFile(HttpSession session, @PathVariable("noteId") String noteId) {
        try {
            int noteid = Integer.parseInt(noteId);
            noteRepository.deleteNote(noteid);
            return "redirect:/result?success";
        }catch (Exception e){
            session.setAttribute("error_message", e.getMessage());
            return "redirect:/result?error";
        }
    }
}
