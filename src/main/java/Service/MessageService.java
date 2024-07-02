package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.*;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public Message createMessage(Message message) throws Exception {
        if (message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
            return null;
        }
        if (!messageDAO.checkPostedBy(message.getPosted_by())) {
            return null;
        }
        return messageDAO.createMessage(message);
    }

    public ArrayList<Message> getAllMessages() throws Exception {
        ArrayList<Message> allMessage = messageDAO.getAllMessages();
        return allMessage;
    }

    public Message getMessageById(int message_id) throws Exception {
        Message message = messageDAO.getMessageById(message_id);
        if (message == null) {
            return null;
        } else {
            return message;
        }
    }


    public Message deleteMessage(int message_id) throws Exception {
        Message messageToBeDeleted = messageDAO.getMessageById(message_id);
        if (messageToBeDeleted != null) {
            messageDAO.deleteMessage(message_id);
            return messageToBeDeleted;
        } 
        return null;
    }

    public Message updateMessage(int message_id, String newMessageText) throws Exception {
        Message existingMessage = messageDAO.getMessageById(message_id);
        if (existingMessage == null) {
            return null;
        }
        if (newMessageText.isBlank() || newMessageText.length() > 255) {
            return null;
        } 
        
        return messageDAO.updateMessage(message_id, newMessageText);
        
    }

    public ArrayList<Message> getMessagesByUserId(int userId) throws Exception {
        ArrayList<Message> userMessageList = messageDAO.getMessagesByUserId(userId);
        return userMessageList;
    }
}
