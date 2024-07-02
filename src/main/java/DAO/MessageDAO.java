package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.*;

public class MessageDAO {
    
    public Boolean checkPostedBy(int existingId) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * from account WHERE account_id = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, existingId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return true;
        }
        return false;

    }
    
    public Message createMessage(Message message) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) values(?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, message.getPosted_by());
        ps.setString(2, message.getMessage_text());
        ps.setLong(3, message.getTime_posted_epoch());
        ps.executeUpdate();

        ResultSet id_key = ps.getGeneratedKeys();
        if (id_key.next()) {
            int generated_message_id = (int) id_key.getLong(1);
            Message createdMessage = new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            return createdMessage;
        }
        return null;
    }

    public ArrayList<Message> getAllMessages() throws SQLException {
        ArrayList<Message> allMessage = new ArrayList<Message>();
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Message message = new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")
            );
            allMessage.add(message);
        }
        return allMessage;
    }

    public Message getMessageById(int message_id) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE message_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, message_id);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            return new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
            );
        }
        return null;
    }


    public void deleteMessage(int message_id) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "DELETE FROM message WHERE message_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, message_id);
        ps.executeUpdate();
    }

    public Message updateMessage(int message_id, String newMessageText) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, newMessageText);
        ps.setInt(2, message_id);
        

        int rowsUpdated = ps.executeUpdate();
        if (rowsUpdated == 0) {
            return null;
        }
        
        return getMessageById(message_id);


    }

    public ArrayList<Message> getMessagesByUserId(int userId) throws SQLException {
        ArrayList<Message> AllMessagesByUser = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT *  from message WHERE posted_by = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

            Message msg = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            AllMessagesByUser.add(msg);

        }
        return AllMessagesByUser;
    }
}
