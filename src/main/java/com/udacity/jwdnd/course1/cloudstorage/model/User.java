package com.udacity.jwdnd.course1.cloudstorage.model;

public class User {
    private Integer userid;

    private String username;

    private String salt;
    private String password;
    private String firstname;
    private String lastname;

    public User(Integer userid, String username, String salt, String password, String firstname, String lastname){
        super();
        this.userid = userid;
        this.username = username;
        this.salt = salt;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public User(){

    }

    public void setUserid(Integer id) {this.userid = id;}
    public void setUsername(String username) {this.username = username;}
    public void setSalt(String salt){this.salt = salt;}
    public void setPassword(String password){this.password=password;}
    public void setFirstname(String firstname){this.firstname=firstname;}
    public void setLastname(String lastname){this.lastname=lastname;}

    public Integer getUserId(){return this.userid;}
    public String getUsername(){return this.username;}
    public String getSalt(){return this.salt;}
    public String getPassword(){return this.password;}
    public String getFirstname(){return this.firstname;}
    public String getLastname(){return this.lastname;}
}
