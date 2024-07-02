package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {
    
    public Account createAccount(Account account) {
        try {
            Connection conn = ConnectionUtil.getConnection();
            String sql = "INSERT INTO account (username, password) VALUES(?, ?);";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                Account createAccount = new Account(rs.getInt("account_id"), account.getUsername(), account.getPassword());
                return createAccount;
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return null;

    }

    public Account getAccountByUsernameAndPassword(Account account) throws SQLException{
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * from account WHERE username = ? AND password = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, account.getUsername());
        ps.setString(2, account.getPassword());

        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            Account loginAccount = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            return loginAccount;
        } return null;

    }
}