package controller;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.MasterService;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserController{

    ObservableList<Utilizator> modelUser = FXCollections.observableArrayList();
    private MasterService service;


    @FXML
    TableColumn<Utilizator, String> tableColumnLastName;
    @FXML
    TableColumn<Utilizator, String> tableColumnFirstName;
    @FXML
    TableColumn<Utilizator, Integer> tableColumnID;
    @FXML
    TableView<Utilizator> tableViewUsers;
    @FXML
    Button friendsButton;


    private void initModel(){
        modelUser.setAll(getUserList());
    }
    @FXML
    public void initialize() {
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("lastName"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("firstName"));
        tableColumnID.setCellValueFactory(new PropertyValueFactory<Utilizator, Integer>("id"));
        tableViewUsers.setItems(modelUser);
    }

    private List<Utilizator> getUserList() {
        return StreamSupport.stream(this.service.getAllUsers().spliterator(), false)
                .collect(Collectors.toList());
    }


    public void setService(MasterService service) {
        this.service = service;
        this.initModel();
    }

    private void showFriendsWindow(Utilizator user){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/friendsView.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Stage friendsStage = new Stage();
            friendsStage.setTitle("Manage Friends of " + user.getFirstName());
            friendsStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            friendsStage.setScene(scene);

            FriendsManagerController friendsManagerController = loader.getController();
            friendsManagerController.setService(service, friendsStage, user);

            friendsStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showRequestsWindow(Utilizator user){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/requestsView.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Stage requestsStage = new Stage();
            requestsStage.setTitle("Manage Requests of " + user.getFirstName());
            requestsStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            requestsStage.setScene(scene);

            RequestManagerController requestManagerController = loader.getController();
            requestManagerController.setService(service, requestsStage, user);

            requestsStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleFriendsButton(ActionEvent actionEvent) {
        Utilizator user = tableViewUsers.getSelectionModel().getSelectedItem();
        if(user!=null){
            showFriendsWindow(user);
        }
        else
            MessageAlert.showErrorMessage(null, "You have to select an user!");
    }

    public void handleRequestButton(ActionEvent actionEvent) {
        Utilizator user = tableViewUsers.getSelectionModel().getSelectedItem();
        if(user!=null){
            showRequestsWindow(user);
        }
        else
            MessageAlert.showErrorMessage(null, "You have to select an user!");
    }
}

