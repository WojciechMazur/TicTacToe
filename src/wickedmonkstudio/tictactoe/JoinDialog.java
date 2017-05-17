package wickedmonkstudio.tictactoe;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import wickedmonkstudio.tictactoe.View.JoinDialogController;

import java.io.IOException;

/**
 * Created by Wojciech on 12.05.2017.
 */
public class JoinDialog extends Stage {
    private Main mainApp=null;
    private JoinDialogController joinDialogController=null;

    public JoinDialog(Stage owner, Main mainApp) throws IOException {
        super();
        this.mainApp=mainApp;
        initOwner(owner);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("View/JoinDialog.fxml"));
        Parent root = loader.load();
        joinDialogController=loader.getController();
        joinDialogController.setMain(mainApp);
        joinDialogController.setJoinDialog(this);

        setTitle("Tic Tac Toe");
        setResizable(false);
        setScene(new Scene(root, 400 , 250));

    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}
