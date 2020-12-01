package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.Message;
import socialnetwork.domain.Status;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.MasterService;
import utils.events.RequestChangeEvent;
import utils.observer.Observer;

public class RequestManagerController implements Observer<RequestChangeEvent> {
    private ObservableList<FriendRequest> modelRequestSent = FXCollections.observableArrayList();
    private ObservableList<FriendRequest> modelRequestReceived = FXCollections.observableArrayList();
    private ObservableList<String> statusModel = FXCollections.observableArrayList();
    private MasterService service;
    private Stage requestsStage;
    private Utilizator user;

    @FXML
    TableView<FriendRequest> tableViewSentRequests;
    @FXML
    TableView<FriendRequest> tableViewReceivedRequests;
    @FXML
    TableColumn<FriendRequest, String> tableColumnTo;
    @FXML
    TableColumn<FriendRequest, String> tableColumnToStatus;
    @FXML
    TableColumn<FriendRequest, String> tableColumnToDate;
    @FXML
    TableColumn<FriendRequest, String> tableColumnFrom;
    @FXML
    TableColumn<FriendRequest, String> tableColumnFromStatus;
    @FXML
    TableColumn<FriendRequest, String> tableColumnFromDate;
    @FXML
    ChoiceBox<String> statusChoiceBox;
    @FXML
    Button answerButton;
    @FXML
    Button withdrawButton;

    @FXML
    public void initialize(){
        statusModel.setAll("approved", "rejected");
        statusChoiceBox.setItems(statusModel);
        tableColumnTo.setCellValueFactory(new PropertyValueFactory<FriendRequest, String>("secondUserFirstName"));
        tableColumnToStatus.setCellValueFactory(new PropertyValueFactory<FriendRequest, String>("status"));
        tableColumnToDate.setCellValueFactory(new PropertyValueFactory<FriendRequest, String>("date"));
        tableViewSentRequests.setItems(modelRequestSent);
        tableColumnFrom.setCellValueFactory(new PropertyValueFactory<FriendRequest, String>("firstUserFirstName"));
        tableColumnFromStatus.setCellValueFactory(new PropertyValueFactory<FriendRequest, String>("status"));
        tableColumnFromDate.setCellValueFactory(new PropertyValueFactory<FriendRequest, String>("date"));
        tableViewReceivedRequests.setItems(modelRequestReceived);
    }
    public void initModel(){
        modelRequestSent.setAll(this.service.getRequestsBySender(user));
        modelRequestReceived.setAll(this.service.getRequestsByReceiver(user));
    }

    public void setService(MasterService service, Stage requestsStage, Utilizator user) {
        this.service = service;
        this.service.addRequestObserver(this);
        this.requestsStage = requestsStage;
        this.user = user;
        initModel();
    }

    @Override
    public void update(RequestChangeEvent requestChangeEvent) {
        initModel();
    }

    public void handleAnswerButton(ActionEvent actionEvent) {
        FriendRequest request = tableViewReceivedRequests.getSelectionModel().getSelectedItem();
        String choice = statusChoiceBox.getValue();
        if(request==null)
            MessageAlert.showErrorMessage(null, "You have to select a request!");
        else if(choice==null)
            MessageAlert.showErrorMessage(null, "You have to select a status!");
        else {
            Status status;
            if(choice.equals("approved"))
                status = Status.approved;
            else
                status = Status.rejected;
            FriendRequest reply = this.service.replyToARequest(request.getId(),status);
            if(reply == null)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Reply", "Reply made!");
            else
                MessageAlert.showErrorMessage(null, "Reply not made!");
        }
    }

    public void handleWithdrawButton(ActionEvent actionEvent) {
        FriendRequest request = tableViewSentRequests.getSelectionModel().getSelectedItem();
        if(request==null || request.getStatus()!=Status.pending)
            MessageAlert.showErrorMessage(null, "You have to select a pending request!");
        else{
            FriendRequest req = this.service.deleteRequest(request);
            if(req == null)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Withdraw", "Reply withdrew!");
            else
                MessageAlert.showErrorMessage(null, "Withdraw not made!");
        }
    }
}
