package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteRepository {

    String findByUserId = "SELECT * FROM NOTES WHERE userid=#{userid}";
    String findByNoteId = "SELECT * FROM NOTES WHERE noteid=#{noteid}";
    String insert = "INSERT into NOTES(noteid, notetitle, notedescription, userid) " +
                    "VALUES (#{noteid}, #{notetitle}, #{notedescription}, #{userid})";
    String update = "UPDATE NOTES SET notetitle=#{notetitle}, notedescription=#{notedescription} " +
                    " WHERE noteid=#{noteid}";
    String delete = "DELETE from NOTES WHERE noteid=#{noteid}";

    @Select(findByUserId)
    @Results({
            @Result(property = "noteid", column = "noteid"),
            @Result(property = "notetitle", column = "notetitle"),
            @Result(property = "notedescription", column = "notedescription"),
            @Result(property = "userid", column = "userid")
    })
    List<Note> findByUserId(Integer userid);

    @Select(findByNoteId)
    @Results({
            @Result(property = "noteid", column = "noteid"),
            @Result(property = "notetitle", column = "notetitle"),
            @Result(property = "notedescription", column = "notedescription"),
            @Result(property = "userid", column = "userid")
    })
    Note getNote(Integer noteid);

    @Insert(insert)
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    void createNote(Note note);

    @Update(update)
    void updateNote(Note note);

    @Delete(delete)
    void deleteNote(Integer noteId);
}
