package wickedmonkstudio.tictactoe.View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import wickedmonkstudio.tictactoe.JoinDialog;
import wickedmonkstudio.tictactoe.Main;

public class JoinDialogController {
    private Main main =null;
    private JoinDialog joinDialog = null;

    @FXML
    private Button joinButton;

    @FXML
    private TextField ipAdresTextField;
    @FXML
    private TextField portTextField;


    @FXML
    public void initialize(){
        joinButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                main.setAddressIP(ipAdresTextField.getText());
                main.setPort(portTextField.getText());
                joinDialog.close();
            }
        });

        ipAdresTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            main.setAddressIP(newValue);
        });

        portTextField.textProperty().addListener((observable, oldValue, newValue) ->
        main.setPort(newValue));
    }


    public void setJoinDialog(JoinDialog joinDialog) {
        this.joinDialog = joinDialog;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
