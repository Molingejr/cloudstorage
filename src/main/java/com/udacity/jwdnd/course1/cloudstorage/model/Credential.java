package com.udacity.jwdnd.course1.cloudstorage.model;

public class Credential {
    private Integer credentialid;

    private String url;
    private String username;
    private String key;
    private String password;

    private User user;
    private Integer userid;

    // Constructors
    public Credential(){

    }

    public Credential(Integer credentialid, String url, String username, String key, String password,
                      User user, Integer userid){
        this.credentialid = credentialid;
        this.url = url;
        this.username = username;
        this.key = key;
        this.password = password;
        this.user = user;
        this.userid = userid;
    }

    public void setCredentialid(Integer credentialid){this.credentialid=credentialid;}
    public void setUrl(String url){this.url=url;}
    public void setUsername(String username){this.username=username;}
    public void setKey(String key){this.key=key;}
    public void setPassword(String password) {
        this.password = password;
    }
    public void setUserid(Integer userid){this.userid = userid;}
    public void setUser(User user){this.user=user;}

    public Integer getCredentialid(){return this.credentialid;}
    public String getUrl() {
        return this.url;
    }
    public String getUsername() {
        return username;
    }
    public String getKey() {
        return this.key;
    }
    public String getPassword() {
        return this.password;
    }
    public Integer getUserid(){return this.userid;}
    public User getUser() {
        return this.user;
    }
}

