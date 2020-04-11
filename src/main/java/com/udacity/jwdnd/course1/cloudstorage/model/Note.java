package com.udacity.jwdnd.course1.cloudstorage.model;

public class Note {

    private Integer noteid;
    private String notetitle;
    private String notedescription;
    private User user;
    public Integer userid;

    // Constructors
    public Note(Integer noteid, String notetitle, String notedescription, User user, Integer userid){
        super();
        this.noteid = noteid;
        this.notetitle = notetitle;
        this.notedescription = notedescription;
        this.user = user;
        this.userid = userid;
    }
    public Note(){}

    public void setNoteId(Integer noteid){this.noteid=noteid;}
    public void setNoteTitle(String notetitle){this.notetitle=notetitle;}
    public void setNoteDescription(String notedescription){this.notedescription=notedescription;}
    public void setUser(User user){this.user=user;}
    public void setUserId(Integer userId){this.userid = this.user.getUserId();}

    public Integer getNoteId(){return this.noteid;}
    public String getNoteTitle(){return this.notetitle;}
    public String getNoteDescription(){return this.notedescription;}
    public User getUser(){return this.user;}
    public Integer getUserId(){return this.userid;}

}
