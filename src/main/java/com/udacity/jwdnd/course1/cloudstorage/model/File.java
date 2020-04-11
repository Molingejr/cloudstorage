package com.udacity.jwdnd.course1.cloudstorage.model;

public class File {
    private Integer fileid;
    private String filename;
    private String contenttype;
    private String filesize;
    private byte[] filedata;
    private User user;
    private Integer userid;

    // Constructors
    public File() {

    }

    public File(Integer fileid, String filename, String contenttype, String filesize,
                 byte[] filedata, User user, Integer userid){
        this.fileid =  fileid;
        this.filename = filename;
        this.contenttype = contenttype;
        this.filesize = filesize;
        this.filedata = filedata;
        this.user = user;
        this.userid = userid;
    }

    public void setFileId(Integer fileid){this.fileid=fileid;}
    public void setFilename(String filename){this.filename=filename;}
    public void setContenttype(String contenttype){this.contenttype=contenttype;}
    public void setFilesize(String filesize){this.filesize=filesize;}
    public void setFiledata(byte[] filedata) {
        this.filedata = filedata;
    }
    public void setUser(User user){this.user=user;}
    public void setUserid(Integer userid){this.userid=userid;}

    public Integer getFileId(){return this.fileid;}
    public String getFilename() {
        return this.filename;
    }
    public String getContenttype() {
        return this.contenttype;
    }
    public String getFilesize() {
        return this.filesize;
    }
    public byte[] getFiledata() {
        return this.filedata;
    }
    public User getUser() {
        return this.user;
    }
    public Integer getUserid(){return this.userid;}
}
