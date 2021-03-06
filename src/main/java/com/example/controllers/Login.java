package com.example.controllers;

/**
 * Created by nitin on 10/21/2016.
 */
import com.example.model.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.omg.CORBA.Request;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.web.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.*;

//@JsonSerialize
@RestController
// produces= MediaType.APPLICATION_JSON_VALUE
public class Login{

    @RequestMapping(path="/hello",method = RequestMethod.POST,consumes= MediaType.APPLICATION_JSON_VALUE,produces= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Access checkUser(@RequestBody User user, HttpSession userSession,HttpServletRequest request) {
        Access access = new Access();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/showyourskillz", "root", "nitin4192");
            PreparedStatement pst = conn.prepareStatement("Select user_name,password from user where user_name=? and password=?");
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getPassword());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                access.setAllow("yes");
                userSession = request.getSession();
                userSession.setAttribute("username",user.getUsername());
                rs.close();
                return access;
            } else {
                access.setAllow("no");
                rs.close();
                return access;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        //if (user.getUsername().equals("nitin")){
        //  access.setAllow("yes");
        //return access;
        //}
        //else{
        //  access.setAllow("no");
        //return access;
        //}

        return access;
    }
    @RequestMapping(path="/login",method = RequestMethod.GET,produces= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody User sendUser(HttpSession userSession, HttpServletRequest request) {

        User user = new User();
        //if(request.getSession(false)== null) {
            user.setUsername((String) userSession.getAttribute("username"));
            return user;
        //}
        //else
        //{
          //  user.setUsername("no");
            //return user;
        //}
        //return user;
    }
    @RequestMapping(path="/logout",method = RequestMethod.GET)
    public void logOut(HttpSession userSession, HttpServletRequest request) {

        if(userSession != null){
            userSession.invalidate();
        }
    }
}
