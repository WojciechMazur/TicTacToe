package wickedmonkstudio.tictactoe;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import wickedmonkstudio.tictactoe.Client.Client;
import wickedmonkstudio.tictactoe.Enum.GameState;
import wickedmonkstudio.tictactoe.Enum.UserConnectionType;
import wickedmonkstudio.tictactoe.Server.Server;
import wickedmonkstudio.tictactoe.View.GameboardController;
import wickedmonkstudio.tictactoe.View.JoinDialogController;

import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;

public class Main extends Application {
    private JoinDialogController joinDialogController =null;
    private GameboardController gameboardController =null;
    private TicTacToeInstance ticTacToeInstance = null;


    private String addressIP;
    private String port;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("View/Gameboard.fxml"));
        Parent root = loader.load();
        gameboardController = loader.getController();
        gameboardController.setMain(this);

        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.setScene(new Scene(root, 400 , 600));
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            if(getGameboardController().getInstance().getUserConnectionType().equals(UserConnectionType.SERVER))
                ((Server)ticTacToeInstance).sendStatusToClient(GameState.DISCONNECTED);
            else if(getGameboardController().getInstance().getUserConnectionType().equals(UserConnectionType.CLIENT))
                ((Client)ticTacToeInstance).sendStatusToServer(GameState.DISCONNECTED);
            Platform.exit();
            System.exit(1);
        });

        Stage joinDialog = new JoinDialog(primaryStage, this);
        joinDialog.showAndWait();

        System.setProperty("java.rmi.server.hostname", addressIP);
        UserConnectionType userConnectionType;
        try {
            userConnectionType =UserConnectionType.SERVER;
            ticTacToeInstance = new Server(addressIP, port);
            ticTacToeInstance.setGameboardController(loader.getController());
            ticTacToeInstance.getGameboardController().getStatusLabel().setText("Waiting for player to join");
            primaryStage.setTitle("Tic Tac Toe Server");
        }catch (ExportException e){
            LocateRegistry.getRegistry();
            userConnectionType = UserConnectionType.CLIENT;
            ticTacToeInstance = new Client(addressIP, port);
            ticTacToeInstance.setGameboardController(loader.getController());
            ticTacToeInstance.getGameboardController().getStatusLabel().setText("Connected! Click on tile to start a game");
            primaryStage.setTitle("Tic Tac Toe Client");

        }
        ticTacToeInstance.setGameboardController(gameboardController);

    }



    public static void main(String[] args) {
        launch(args);
    }

    public String getAddressIP() {
        return addressIP;
    }

    public void setAddressIP(String addressIP) {
        this.addressIP = addressIP;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public GameboardController getGameboardController() {
        return gameboardController;
    }

    public TicTacToeInstance getTicTacToeInstance() {
        return ticTacToeInstance;
    }
}
