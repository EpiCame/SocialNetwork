package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import socialnetwork.domain.Entity;
import socialnetwork.domain.Message;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.MasterService;
import socialnetwork.service.MessageService;

import java.util.List;
import java.util.stream.Collectors;

public class MessageController {
    private MasterService service;
    private Stage senderStage;
    private List<Utilizator> users;
    private Utilizator senderUser;

    @FXML
    TextArea messageTextArea;
    @FXML
    Button sendButton;
    @FXML
    Label toLabel;

    public void initialize(){

    }


    public void setService(MasterService service, Stage senderStage, List<Utilizator> users, Utilizator senderUser) {
        this.service=service;
        this.senderStage = senderStage;
        this.users = users;
        this.senderUser = senderUser;
        StringBuilder to = new StringBuilder();
        for(Utilizator user : users)
            to.append(user.getFirstName()).append(" ");
        toLabel.setText("To: " + to.toString());
    }

    public void handleSendButton(ActionEvent actionEvent) {
        Long messageID = MessageService.getMaxID();
        List<Long> toList = this.users.stream().map(Entity::getId).collect(Collectors.toList());
        String message = messageTextArea.getText();
        Message msg = this.service.sendAMessage(messageID, this.senderUser.getId(), toList, message);
        if(msg == null)
            MessageAlert.showMessage(null , Alert.AlertType.INFORMATION, "Sent", "Message Sent!");
        else
            MessageAlert.showErrorMessage(null , "Message not sent!");
    }
}
