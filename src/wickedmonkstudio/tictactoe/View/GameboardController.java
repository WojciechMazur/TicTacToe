package wickedmonkstudio.tictactoe.View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import wickedmonkstudio.tictactoe.Client.Client;
import wickedmonkstudio.tictactoe.Enum.GameState;
import wickedmonkstudio.tictactoe.Enum.PlayerSign;
import wickedmonkstudio.tictactoe.TicTacToeInstance;
import wickedmonkstudio.tictactoe.Enum.UserConnectionType;
import wickedmonkstudio.tictactoe.Main;
import wickedmonkstudio.tictactoe.Model.Tile;
import wickedmonkstudio.tictactoe.Server.Server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Wojciech on 12.05.2017.
 */
public class GameboardController {

    private Main main =null;
    @FXML private AnchorPane tilesPane = new AnchorPane();
    @FXML private AnchorPane rootPane = new AnchorPane();
    private ArrayList<Tile> tilesList = new ArrayList<>();
    @FXML private Label statusLabel = new Label();
    @FXML private Button resetButton = new Button();

    private PlayerSign playerSign;

    private int tileWidth =100;
    private int tileHeight = 100;

    private int numberOfColumns =4;
    private int numberOfRows = 3;
    private int streakToWin =3;

    private boolean yourTurn =true;

    private double spacingBetweenTiles = 5;

    @FXML
    public void initialize(){
    initTiles();
    for(Tile tile : tilesList) {
        tile.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (yourTurn && tile.getSign() == null) {
                    if (getInstance().getUserConnectionType().equals(UserConnectionType.SERVER)) {
                        try {
                            if (((Server) getInstance()).getClientCallbackListener() != null) {
                                tile.mark(getInstance().getSign());
                                ((Server) getInstance()).markTileOnClient(tile);
                                GameState gameState = checkGameProgress();
                                ((Server)getInstance()).sendStatusToClient(gameState.equals(GameState.IN_PROGRESS) ? GameState.YOUR_MOVE : gameState);
                                ((Server) getInstance()).sendStatus(gameState.equals(GameState.IN_PROGRESS) ? GameState.ENEMY_MOVE : gameState);
                                yourTurn = false;
                            }
                        } catch (RemoteException e) {
                            System.err.println("Exception while handling message to from server");
                        }
                    }

                    if (getInstance().getUserConnectionType().equals(UserConnectionType.CLIENT)) {
                        try {
                            tile.mark(getInstance().getSign());
                            ((Client) getInstance()).markTileOnServer(tile);
                            GameState gameState = checkGameProgress();
                            ((Client)getInstance()).sendStatusToServer(gameState.equals(GameState.IN_PROGRESS) ? GameState.YOUR_MOVE : gameState);
                            ((Client) getInstance()).sendStatusToClient(gameState.equals(GameState.IN_PROGRESS) ? GameState.ENEMY_MOVE  : gameState);
                            yourTurn = false;
                        } catch (RemoteException e) {
                            System.err.println("Exception while handling message to from client");
                        } catch (ClassCastException e) {
                            System.err.println("Exception, client is not connected.");
                        }
                    }
                }
            }
            });
        }
        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(getInstance().getUserConnectionType().equals(UserConnectionType.SERVER)){
                    try {
                        ((Server)getInstance()).resetTilesOnClient();
                        ((Server)getInstance()).resetTiles();
                        ((Server)getInstance()).sendStatusToClient(GameState.CONNECTED);
                        ((Server)getInstance()).sendStatus(GameState.CONNECTED);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                if(getInstance().getUserConnectionType().equals(UserConnectionType.CLIENT)){
                    try {
                        ((Client)getInstance()).resetTilesOnServer();
                        ((Client)getInstance()).resetTilesOnClient();
                        ((Client)getInstance()).sendStatusToServer(GameState.CONNECTED);
                        ((Client)getInstance()).sendStatusToClient(GameState.CONNECTED);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private GameState checkGameProgress(){
        GameState status;
      //  printBoard();
        if((status=checkRows())==null)
            if((status=checkColumns())==null)
                if((status=checkDiagonal())==null)
                    if((status=checkReverseDiagonal())==null)
                        if((status=checkDraw())==null)
                 return GameState.IN_PROGRESS;

        return status;
    }

    private GameState checkDraw() {
        for(Tile tile :getTilesList()){
            if(tile.getSign()==null)
                return null;
        }
        return GameState.DRAW;
    }

    private GameState checkDiagonal() {

        int inRow=0;
        PlayerSign currentSign = null;
        for(int i=0; i<numberOfColumns;i++){
            Tile current =getTilesList().get(i*numberOfRows+i);
            if (current.getSign() == null) {
                inRow = 0;
                currentSign = null;
                continue;
            }
            if (current.getSign().equals(currentSign)) {
                if (++inRow == streakToWin)
                    return currentSign == PlayerSign.X ? GameState.SERVER_WON : GameState.CLIENT_WON;
            }else {
                    inRow = 1;
                    currentSign = current.getSign();
                }
        }
        return null;
    }

    private GameState checkReverseDiagonal(){

        int inRow=0;
        PlayerSign currentSign = null;
        for(int i=1; i<=numberOfColumns;i++){
            Tile current =getTilesList().get(i*(numberOfRows-1));
            if (current.getSign() == null) {
                inRow = 0;
                currentSign = null;
                continue;
            }
            if (current.getSign().equals(currentSign)) {
                if (++inRow == streakToWin)
                    return currentSign == PlayerSign.X ? GameState.SERVER_WON : GameState.CLIENT_WON;
            }else {
                inRow = 1;
                currentSign = current.getSign();
            }
        }
        return null;
    }

    private GameState checkRows() {
        int inRow=0;
        PlayerSign currentSign = null;
        for(int i=0; i<numberOfRows;i++) {
            inRow=0;
            for (int j = 0; j < numberOfColumns; j++) {
                Tile current = getTilesList().get(i*numberOfRows+j);

                if (current.getSign() == null) {
                    inRow = 0;
                    currentSign = null;
                    continue;
                }
                if (current.getSign().equals(currentSign)) {
                    if (++inRow == streakToWin)
                        return currentSign == PlayerSign.X ? GameState.SERVER_WON : GameState.CLIENT_WON;
                } else {
                    inRow = 1;
                    currentSign = current.getSign();
                }
            }
        }
        return null;
    }

    private GameState checkColumns(){
        int inRow=0;
     PlayerSign currentSign =null;
     for(int i=0;i<numberOfColumns;i++) {
         inRow = 0;
         for (int j = 0; j < numberOfRows; j++) {
             Tile current = getTilesList().get(j * numberOfRows + i);
             if (current.getSign()== null) {
                 inRow = 0;
                 currentSign = null;
                 continue;
             }
             if (current.getSign().equals(currentSign)) {
                 if (++inRow == streakToWin)
                     return currentSign == PlayerSign.X ? GameState.SERVER_WON : GameState.CLIENT_WON;
             }
              else {
                 inRow = 1;
                 currentSign = current.getSign();
             }
         }
     }
     return null;
    }

    private void printBoard(){
        for(int i=0;i<getTilesList().size();i++) {
            if (i % numberOfColumns == 0) System.out.println();
            System.out.print(getTilesList().get(i).getSign()!=null  ? getTilesList().get(i).getSign() + " " : "- ");
        }
    }

    private void initTiles() {
        int marginLeft=5;
        int idDistributor = 0;
        tileHeight=tileWidth=getTileSizeinPx();
        for(int row =0; row<numberOfRows;row++){
            for(int column =0; column<numberOfColumns;column++){
                Tile currentTile = new Tile(tileWidth, tileHeight, null, idDistributor++);
                currentTile.setLayoutX(column*(tileWidth+spacingBetweenTiles)+marginLeft);
                currentTile.setLayoutY(row*(tileHeight+spacingBetweenTiles));
                currentTile.setFill(Color.WHITE);
                currentTile.setStroke(Color.BLACK);
                tilesPane.getChildren().add(currentTile);
                tilesList.add(currentTile);
            }
        }
    }

    private int getTileSizeinPx(){
        if(tilesPane.getPrefWidth()!=0)
        return (int)(tilesPane.getPrefWidth()*0.85/numberOfColumns);
        else return 100;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setPlayerSign(PlayerSign playerSign) {
        this.playerSign = playerSign;
    }

    public ArrayList<Tile> getTilesList() {
        return tilesList;
    }

    public void setTilesList(ArrayList<Tile> tilesList) {
        this.tilesList = tilesList;
    }

    public PlayerSign getPlayerSign() {
        return playerSign;
    }

    public TicTacToeInstance getInstance() {
        return getMain().getTicTacToeInstance();
    }

    public void setYourTurn(boolean yourTurn) {
        this.yourTurn = yourTurn;
    }

    public boolean isYourTurn() {
        return yourTurn;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }
}
