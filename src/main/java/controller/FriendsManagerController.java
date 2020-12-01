package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.MasterService;
import utils.events.FriendshipChangeEvent;
import utils.observer.Observer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FriendsManagerController implements Observer<FriendshipChangeEvent> {
    private ObservableList<Prietenie> modelFriends = FXCollections.observableArrayList();
    private MasterService service;
    private Stage friendStage;
    private Utilizator user;

    @FXML
    TableView<Prietenie> tableViewFriends;
    @FXML
    TableColumn<Prietenie, String> tableColumnFriends;
    @FXML
    TableColumn<Prietenie, String> tableColumnDate;
    @FXML
    Button deleteFriendButton;
    @FXML
    Button sendRequestButton;

    private void initModel(){
        modelFriends.setAll(this.service.getFriendshipsOfUser(user));
    }
    @FXML
    public void initialize(){
        tableColumnFriends.setCellValueFactory(new PropertyValueFactory<Prietenie,String>("id"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<Prietenie,String>("date"));
        tableViewFriends.setItems(modelFriends);
    }

    public void setService(MasterService service, Stage friendsStage, Utilizator user) {
        this.service = service;
        this.service.addFriendObserver(this);
        this.friendStage = friendsStage;
        this.user = user;
        initModel();
    }

    @Override
    public void update(FriendshipChangeEvent friendshipChangeEvent) {
        initModel();
    }

    public void handleDeleteFriendButton(ActionEvent actionEvent) {
        Prietenie friendship = tableViewFriends.getSelectionModel().getSelectedItem();
        if(friendship!=null){
            List<String> args = new ArrayList<>();
            args.add(friendship.getId().getLeft().toString());
            args.add(friendship.getId().getRight().toString());
            Prietenie deletedFriendship = this.service.deleteFriendship(args);
            if(deletedFriendship==null)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Delete friendship","The friendship was deleted!");
            else
                MessageAlert.showErrorMessage(null, "The friendship was not deleted!");
        }
        else
            MessageAlert.showErrorMessage(null, "You have to select a friendship!");
    }
    private void showRequestSender(Utilizator user){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/senderView.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Stage senderStage = new Stage();
            senderStage.setTitle("Send requests from " + user.getFirstName());
            senderStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            senderStage.setScene(scene);

            SenderController senderController = loader.getController();
            senderController.setService(service, senderStage, user);

            senderStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleSendRequestButton(ActionEvent actionEvent) {
        showRequestSender(this.user);
    }
}
