package com.revature.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
/////"jdbc:postgresql://jdbcdb.croogizlluy5.us-east-1.rds.amazonaws.com:5432/jdbcdb";
    private static String url=System.getenv("url");
    private static String username=System.getenv("username");
    private static String password=System.getenv("password");
    
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
