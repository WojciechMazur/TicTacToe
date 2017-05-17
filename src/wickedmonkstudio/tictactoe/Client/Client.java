package wickedmonkstudio.tictactoe.Client;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import wickedmonkstudio.tictactoe.Enum.GameState;
import wickedmonkstudio.tictactoe.Enum.PlayerSign;
import wickedmonkstudio.tictactoe.Enum.UserConnectionType;
import wickedmonkstudio.tictactoe.Model.Tile;
import wickedmonkstudio.tictactoe.Server.ServerInterface;
import wickedmonkstudio.tictactoe.TicTacToeInstance;

import java.net.MalformedURLException;
import java.rmi.*;

/**
 * Created by Wojciech on 12.05.2017.
 */
public class Client extends TicTacToeInstance implements ClientCallbackListener {

    private String serverAdresIp;
    private String serverPort;
    private Remote remoteService =null;


    public Client(String ip, String port) throws RemoteException {
        super(UserConnectionType.CLIENT);
        this.serverAdresIp=ip;
        this.serverPort=port;

        try {
            initClient();
        } catch (RemoteException | MalformedURLException | NotBoundException e) {
            e.printStackTrace();
        }
        System.out.println("Client started");
    }

    private void initClient() throws RemoteException, MalformedURLException, NotBoundException {
        remoteService  = Naming.lookup("rmi://"+serverAdresIp+":"+serverPort+"/TicTacToe");
        ClientCallback clientCallback = (ClientCallback)remoteService;
        clientCallback.setClientCallbackListener(this);
    }

    public void sendToServer(String text) throws RemoteException {
        try{
            ServerInterface server = (ServerInterface)remoteService;
            server.sendText(text);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public void markTileOnServer(Tile tile) throws RemoteException {
            try{
                ServerInterface server = (ServerInterface)remoteService;
                server.markTile(tile);
            }catch (RemoteException e){
                e.printStackTrace();
            }
    }

    public void sendStatusToServer(GameState state) {
        try {
            ServerInterface server = (ServerInterface) remoteService;
            server.sendStatus(state);
        } catch (ConnectException e) {
            System.err.println("Server is unreachable");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void resetTilesOnServer(){
        try {
            ServerInterface server = (ServerInterface)remoteService;
            server.resetTiles();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendToClient(String text) throws RemoteException {
        System.out.println("Message from server:: " + text);
    }

    @Override
    public void markTileOnClient(Tile tile) throws RemoteException {
        getGameboardController().getTilesList().get(tile.getTileId()).mark(PlayerSign.X);
        getGameboardController().setYourTurn(true);
    }

    @Override
    public void sendStatusToClient(GameState state) throws RemoteException {
        Platform.runLater(() -> {
            switch (state) {
                case CONNECTED:
                    getGameboardController().getStatusLabel().setText("Connected! Click on tile to make move");
                    break;

                case YOUR_MOVE:
                    getGameboardController().getStatusLabel().setText("Your move!");
                    break;

                case ENEMY_MOVE:
                    getGameboardController().getStatusLabel().setText("Enemys move!");
                    break;

                case CLIENT_WON:
                    getGameboardController().getStatusLabel().setText("You won!");
                    getGameboardController().setYourTurn(false);
                    break;

                case SERVER_WON:
                    getGameboardController().getStatusLabel().setText("You lose!");
                    getGameboardController().setYourTurn(false);
                    break;

                case DRAW:
                    getGameboardController().getStatusLabel().setText("Draw, you lucky bastard...");
                    getGameboardController().setYourTurn(false);
                    break;

                case IN_PROGRESS:
                    getGameboardController().getStatusLabel().setText("Game in progess...");
                    break;

                case DISCONNECTED:
                    getGameboardController().getStatusLabel().setText("Disconnected from server");
                    break;
            }
        });
    }

    @Override
    public void resetTilesOnClient() throws RemoteException {
        Platform.runLater(()->{
            for(Tile tile : getGameboardController().getTilesList()){
                tile.unmark();
            }
            getGameboardController().setYourTurn(true);
        });
    }
}
