package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);


        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{messageId}", this::getMessageByIdHandler);
        app.delete("/messages/{messageId}", this::deleteMessageHandler);
        app.patch("/messages/{messageId}", this::updateMessageHandler);
        app.get("/accounts/{accountId}/messages", this::getMessagesByUserHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registerHandler(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Account account = objectMapper.readValue(context.body(), Account.class);
        Account registeredAccount = accountService.registerAccount(account);

        if (registeredAccount != null) {
            context.json(objectMapper.writeValueAsString(registeredAccount));
            context.status(200);

        } else {
            context.status(400);

        }
    }

    private void loginHandler(Context ctx) throws JsonMappingException, JsonProcessingException, Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Account account = objectMapper.readValue(ctx.body(), Account.class);
        Account loggedInAccount = accountService.loginAccount(account);

        if (loggedInAccount == null) {
            ctx.status(401);
        } else {
            ctx.status(200);
            ctx.json(objectMapper.writeValueAsString(loggedInAccount));
        }

    }

    private void createMessageHandler(Context context) throws JsonMappingException, JsonProcessingException, Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(context.body(), Message.class);
        Message createdMessage = messageService.createMessage(message);
        if (createdMessage == null) {
            context.status(400);
        } else {
            context.status(200);
            context.json(createdMessage);
        }
    }

    private void getAllMessagesHandler(Context context) {
        try {
            context.json(messageService.getAllMessages()).status(200);
        } catch (Exception e) {
            context.status(400).result(e.getMessage());
        }
    }

    private void getMessageByIdHandler(Context context) throws JsonMappingException, JsonProcessingException, Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("messageId"));
        Message message = messageService.getMessageById(message_id);
        if (message == null) {
            context.status(200);
        } else {
            context.status(200);
            context.json(objectMapper.writeValueAsString(message));
        }
    }

    private void deleteMessageHandler(Context context) throws JsonMappingException, JsonProcessingException, Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("messageId"));
        Message message = messageService.deleteMessage(message_id);

        if (message == null) {
            context.status(200);
        } else {
            context.status(200);
            context.json(objectMapper.writeValueAsString(message));
        }
        
    }

    private void updateMessageHandler(Context context) throws JsonMappingException, JsonProcessingException, Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("messageId"));
        String newMessageText = objectMapper.readTree(context.body()).get("message_text").asText();
        Message updatedMessage = messageService.updateMessage(message_id, newMessageText);
        
        if (updatedMessage == null) {
            context.status(400);
        } else {
            context.status(200).json(updatedMessage);
        }
    }

    private void getMessagesByUserHandler(Context context) throws JsonMappingException, JsonProcessingException, Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        int account_id = Integer.parseInt(context.pathParam("accountId"));
        ArrayList<Message> userMessageList = messageService.getMessagesByUserId(account_id);

        context.status(200);
        context.json(objectMapper.writeValueAsString(userMessageList));
    }
}