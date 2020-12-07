package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import socialnetwork.domain.Message;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.MasterService;
import socialnetwork.service.MessageService;

public class ReplyController {
    private MasterService service;
    private Stage senderStage;
    private Utilizator sender;
    private Message message;

    @FXML
    Button sendReplyButton;
    @FXML
    TextArea replyTextArea;
    public void initialize(){
    }

    public void setService(MasterService service, Stage senderStage, Utilizator sender, Message message) {
        this.service = service;
        this.senderStage = senderStage;
        this.sender = sender;
        this.message = message;
    }

    public void handleSendReplyButton(ActionEvent actionEvent) {
        String content = replyTextArea.getText();
        replyTextArea.setText("");
        Message reply = this.service.replyToAMessage(MessageService.getMaxID(), this.message.getId(), sender.getId(), content);
        if(reply == null)
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Reply sent", "Reply sent!");
        else
            MessageAlert.showErrorMessage(null, "Reply not sent!");
    }
}
