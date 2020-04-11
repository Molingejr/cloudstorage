package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialRepository {
    String findByUserId = "SELECT * FROM CREDENTIALS WHERE userid=#{userid}";
    String findById = "SELECT * FROM CREDENTIALS" +
                      " WHERE credentialid=#{credentialid}";
    String insert = "INSERT into CREDENTIALS(credentialid, url, username, `key`, password, userid) " +
                    "VALUES (#{credentialid}, #{url}, #{username}, #{key}, #{password}, #{userid})";
    String update = "UPDATE CREDENTIALS SET url=#{url}, username=#{username}, password=#{password} " +
                    "WHERE credentialid=#{credentialid}";
    String delete = "DELETE from CREDENTIALS WHERE credentialid=#{credentialid}";

    @Select(findByUserId)
    @Results({
            @Result(property = "credentialid", column = "credentialid"),
            @Result(property = "url", column = "url"),
            @Result(property = "username", column = "username"),
            @Result(property = "key", column = "key"),
            @Result(property = "password", column = "password"),
            @Result(property = "userid", column = "userid")
    })
    List<Credential> findByUserId(Integer userid);

    @Select(findById)
    @Results({
            @Result(property = "credentialid", column = "credentialid"),
            @Result(property = "url", column = "url"),
            @Result(property = "username", column = "username"),
            @Result(property = "key", column = "key"),
            @Result(property = "password", column = "password"),
            @Result(property = "userid", column = "userid")
    })
    Credential findById(Integer credentialid);

    @Insert(insert)
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    void createCredentials(Credential credentials);

    @Update(update)
    void updateCredential(Credential credentials);

    @Delete(delete)
    void deleteCredential(Integer credentialid);
}
