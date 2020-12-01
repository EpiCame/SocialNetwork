
//import controller.UserController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import controller.UserController;
import socialnetwork.domain.*;
import socialnetwork.domain.validators.MessageValidator;
import socialnetwork.domain.validators.PrietenieValidator;
import socialnetwork.domain.validators.RequestValidator;
import socialnetwork.domain.validators.UtilizatorValidator;
import socialnetwork.repository.file.*;
import socialnetwork.service.*;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        String fileName = "data/users.csv";
        String fileNameReq = "data/requests.csv";
        String fileNameMsg = "data/messages.csv";
        String fileNameFriend = "data/friends.csv";
        AbstractFileRepository<Long, Utilizator> userFileRepository = new UtilizatorFile(fileName
                , new UtilizatorValidator());
        UtilizatorService userService = new UtilizatorService(userFileRepository);
        AbstractFileRepository<Tuple<Long,Long>, Prietenie> friendFileRepository = new PrietenieFile(fileNameFriend, new PrietenieValidator());
        PrietenieService friendService = new PrietenieService(friendFileRepository);
        AbstractFileRepository<Long, Message> msgRepo = new MessageFile(fileNameMsg, new MessageValidator());
        MessageService messageService = new MessageService(msgRepo);
        AbstractFileRepository<Long, FriendRequest> requestRepository = new FriendReqFile(fileNameReq, new RequestValidator());
        FriendReqService reqService = new FriendReqService(requestRepository);
        MasterService service = new MasterService(userService, friendService, messageService, reqService);
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/userView.fxml"));
        AnchorPane root=loader.load();
        UserController ctrl=loader.getController();
        ctrl.setService(service);

        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.setTitle("Social Network");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}