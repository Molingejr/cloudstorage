package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserRepository {
    String findAll = "SELECT * FROM USERS";
    String findByUsername = "SELECT * FROM USERS where username=#{username}";
    String findById = "SELECT * FROM USERS where userid=#{userid}";
    String insert = "INSERT into USERS(userid, username, salt, password, firstname, lastname) " +
            "VALUES (#{userid}, #{username}, #{salt}, #{password}, #{firstname}, #{lastname})";
    String update = "UPDATE USERS set(username=#{username}, salt=#{salt}, password=#{password}, firstname=#{firstname}, lastname=#{lastname})" +
            "WHERE userid=#{userid}";
    String delete = "DELETE from USERS WHERE userid=#{userid}";

    @Select(findAll)
    @Results({
            @Result(property = "userid", column = "userid"),
            @Result(property = "username", column = "username"),
            @Result(property = "salt", column = "salt"),
            @Result(property = "password", column = "password"),
            @Result(property = "firstname", column = "firstname"),
            @Result(property = "lastname", column = "lastname")
    })
    List<User> findAll();

    @Select(findByUsername)
    @Results({
            @Result(property = "userid", column = "userid"),
            @Result(property = "username", column = "username"),
            @Result(property = "salt", column = "salt"),
            @Result(property = "password", column = "password"),
            @Result(property = "firstname", column = "firstname"),
            @Result(property = "lastname", column = "lastname")
    })
    User findByUsername(String username);

    @Select(findById)
    @Results({
            @Result(property = "userid", column = "userid"),
            @Result(property = "username", column = "username"),
            @Result(property = "salt", column = "salt"),
            @Result(property = "password", column = "password"),
            @Result(property = "firstname", column = "firstname"),
            @Result(property = "lastname", column = "lastname")
    })
    User findById(Integer userid);

    @Insert(insert)
    @Options(useGeneratedKeys = true, keyProperty = "userid")
    void insert(User user);

    @Update(update)
    void update(User user);

    @Delete(delete)
    void delete(Integer userid);

}
