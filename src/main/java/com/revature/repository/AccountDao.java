package com.revature.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.revature.model.Account;
import com.revature.exception.DuplicateUsernameException;
import com.revature.exception.PasswordTooShortException;
import com.revature.service.ConnectionUtil;



public class AccountDao {


  public List<Account> findAll() {
    try (Connection conn = ConnectionUtil.connect()) {
      String sql = "select * from Accounts order by id";
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      List<Account> list = new ArrayList<>();
      while (rs.next()) {
        try {
          list.add(new Account(rs.getString(1), rs.getString(2), rs.getString(3), 0));
        } catch (DuplicateUsernameException | PasswordTooShortException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      return list;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Account findByString(String s) {
    try (Connection conn = ConnectionUtil.connect()) {
      String sql = "select * from accounts where username = ?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, s);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        try {
          return new Account(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4));
        } catch (DuplicateUsernameException | PasswordTooShortException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }


  public Account insert(Account t) {
    try (Connection conn = ConnectionUtil.connect()) {
      String sql = "insert into accounts (username, password, name, balance) values (?,?,?,?)";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, t.getUsername());
      ps.setString(2, t.getPassword());
      ps.setString(3, t.getName());
      ps.setDouble(4, t.getBalance());
      ps.execute();
      return findByString(t.getName());
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }


  public boolean update(Account t, double d) {
    try (Connection conn = ConnectionUtil.connect()) {
      String sql = "update accounts set balance = ? where username = ?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setDouble(1, d);
      ps.setString(2, t.getUsername());
      ps.execute();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }
}
