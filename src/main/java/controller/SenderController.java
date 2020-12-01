package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.MasterService;



public class SenderController {
    private MasterService service;
    private Stage senderStage;
    private Utilizator user;

    @FXML
    Button sendButton;
    @FXML
    TextField userIDText;

    public void setService(MasterService service, Stage senderStage,Utilizator user){
        this.service=service;
        this.senderStage = senderStage;
        this.user = user;
    }


    public void initialize(){}

    public void handleSendButton(ActionEvent actionEvent) {
        try {
            Long requestID = this.service.getMaxRequestID();
            Long senderID = this.user.getId();
            Long userID = Long.parseLong(userIDText.getText());
            userIDText.setText("");
            FriendRequest request = this.service.sendFriendRequest(requestID,senderID,userID);
            if(request != null)
                MessageAlert.showErrorMessage(null, "Invalid data!");
            else
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Sent", "Request sent!");
        }
        catch(NumberFormatException e){
            MessageAlert.showErrorMessage(null, "Invalid data!");
        }
    }
}
