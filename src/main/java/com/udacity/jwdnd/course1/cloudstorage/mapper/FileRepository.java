package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface FileRepository {
    String findByUserId = "SELECT * FROM FILES WHERE userid=#{userid}";
    String findByFileNameAndUserId = "SELECT * FROM FILES WHERE userid=#{userid} AND filename=#{filename}";
    String insert = "INSERT into FILES(fileid, filename, contenttype, filesize, userid, filedata) " +
                    "VALUES (#{fileid}, #{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})";
    String findByFileId = "SELECT * FROM FILES where fileid=#{fileid}";
    String update = "UPDATE FILES set filename=#{filename}, contenttype=#{contenttype}, filesize=#{filesize}, filedata=#{filedata} " +
                    "WHERE fileid=#{fileid}";
    String delete = "DELETE from FILES where fileid=#{fileid}";

    @Select(findByUserId)
    @Results({
            @Result(property = "fileid", column = "fileid"),
            @Result(property = "filename", column = "filename"),
            @Result(property = "contenttype", column = "contenttype"),
            @Result(property = "filesize", column = "filesize"),
            @Result(property = "userid", column = "userid"),
            @Result(property = "filedata", column = "filedata")
    })
    List<File> findByUserId(int userid);


    @Insert(insert)
    @Options(useGeneratedKeys = true, keyProperty = "fileid")
    void createFile(File files);

    @Select(findByFileId)
    @Results({
            @Result(property = "fileid", column = "fileid"),
            @Result(property = "filename", column = "filename"),
            @Result(property = "contenttype", column = "contenttype"),
            @Result(property = "filesize", column = "filesize"),
            @Result(property = "userid", column = "userid"),
            @Result(property = "filedata", column = "filedata")
    })
    File getFile(int fileid);

    @Select(findByFileNameAndUserId)
    @Results({
            @Result(property = "fileid", column = "fileid"),
            @Result(property = "filename", column = "filename"),
            @Result(property = "contenttype", column = "contenttype"),
            @Result(property = "filesize", column = "filesize"),
            @Result(property = "userid", column = "userid"),
            @Result(property = "filedata", column = "filedata")
    })
    File getFileByNameAndUserId(Integer userid, String filename);

    @Update(update)
    void updateFile(int fileid);

    @Delete(delete)
    void deleteFile(int fileid);
}
