package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import socialnetwork.domain.Message;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.MasterService;
import utils.events.MessageChangeEvent;
import utils.observer.Observer;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ConversationController implements Observer<MessageChangeEvent> {
    private MasterService service;
    private Utilizator user;
    private Utilizator otherUser;
    private Stage senderStage;
    private ObservableList<Message> firstStrings = FXCollections.observableArrayList();
    private ObservableList<Message> secondStrings = FXCollections.observableArrayList();

    @FXML
    Label firstUserLabel;
    @FXML
    Label secondUserLabel;
    @FXML
    ListView<Message> firstListView;
    @FXML
    ListView<Message> secondListView;
    @FXML
    Button firstReplyButton;
    @FXML
    Button secondReplyButton;



    public void initialize(){

    }

    public void initmodel(){
        firstStrings.clear();
        secondStrings.clear();
        firstUserLabel.setText("To "+ this.user.getFirstName());
        secondUserLabel.setText("To "+ this.otherUser.getFirstName());
        List<Message> conv = this.service.getConversation(this.user.getId(), this.otherUser.getId());
        conv = conv.stream().sorted(Comparator.comparing(Message::getDate)).collect(Collectors.toList());
        for(Message msg : conv)
        {
            if(msg.getFrom() == this.otherUser)
                firstStrings.add(msg);
            else
                secondStrings.add(msg);
        }
        firstListView.getItems().setAll(firstStrings);
        secondListView.getItems().setAll(secondStrings);
    }

    public void setService(MasterService service, Utilizator user, Utilizator otherUser, Stage senderStage) {
        this.service = service;
        this.service.addMessageObserver(this);
        this.user = user;
        this.otherUser = otherUser;
        this.senderStage = senderStage;
        this.initmodel();
    }

    @Override
    public void update(MessageChangeEvent messageChangeEvent) {
        this.initmodel();
    }

    private void showReplySender(Utilizator sender, Utilizator receiver, Message message){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/replyView.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Stage senderStage = new Stage();
            senderStage.setTitle("Send Reply from " + sender.getFirstName() + " to " + receiver.getLastName());
            senderStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            senderStage.setScene(scene);

            ReplyController replyController = loader.getController();
            replyController.setService(service, senderStage, sender, message);

            senderStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleFirstReplyButton(ActionEvent actionEvent) {
        Message message = firstListView.getSelectionModel().getSelectedItem();
        if(message != null){
            showReplySender(this.user, this.otherUser, message);
        }
        else
            MessageAlert.showErrorMessage(null, "You have to select a message!");
    }

    public void handleSecondReplyButton(ActionEvent actionEvent) {
        Message message = secondListView.getSelectionModel().getSelectedItem();
        if(message != null){
            showReplySender(this.otherUser, this.user, message);
        }
        else
            MessageAlert.showErrorMessage(null, "You have to select a message!");
    }
}
