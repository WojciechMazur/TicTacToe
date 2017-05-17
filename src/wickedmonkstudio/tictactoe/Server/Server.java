package wickedmonkstudio.tictactoe.Server;

import javafx.application.Platform;
import wickedmonkstudio.tictactoe.Client.ClientCallback;
import wickedmonkstudio.tictactoe.Client.ClientCallbackListener;
import wickedmonkstudio.tictactoe.Enum.GameState;
import wickedmonkstudio.tictactoe.Enum.PlayerSign;
import wickedmonkstudio.tictactoe.TicTacToeInstance;
import wickedmonkstudio.tictactoe.Enum.UserConnectionType;
import wickedmonkstudio.tictactoe.Model.Tile;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Wojciech on 12.05.2017.
 */
public class Server extends TicTacToeInstance implements ClientCallback, ServerInterface{
    private String serverIPAdres;
    private String serverPort;

    private ClientCallbackListener clientCallbackListener;

    public Server(String ip, String port) throws RemoteException {
        super(UserConnectionType.SERVER);
        Registry registry = LocateRegistry.createRegistry(Integer.parseInt(port));
        registry.rebind("TicTacToe", this);

        System.out.println("Server started.");
    }


    @Override
    public void sendText(String text) throws RemoteException {
        System.out.println("Message from client:: "+text);
    }

    public void sendToClient(String text){
        if(clientCallbackListener!=null){
            try {
                clientCallbackListener.sendToClient(text);
            }catch (RemoteException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendStatus(GameState status) throws RemoteException {
        Platform.runLater(()-> {
            switch (status) {
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
                    getGameboardController().getStatusLabel().setText("You lost!");
                    getGameboardController().setYourTurn(false);
                    break;

                case SERVER_WON:
                    getGameboardController().getStatusLabel().setText("You won!");
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
                    getGameboardController().getStatusLabel().setText("Disconnected");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getGameboardController().getStatusLabel().setText("Waiting for new connection.");
                    break;
            }
        });
    }

    @Override
    public void resetTiles() throws RemoteException {
        Platform.runLater(()->{
            for(Tile tile : getGameboardController().getTilesList())
                tile.unmark();
            getGameboardController().setYourTurn(true);
        });
    }


    public void sendStatusToClient(GameState gameState){
        if(clientCallbackListener!=null) {
            try {
                clientCallbackListener.sendStatusToClient(gameState);
            }catch (RemoteException e){
                e.printStackTrace();
            }
        }
    }

    public void markTileOnClient(Tile tile){
        if(clientCallbackListener!=null)try{
            clientCallbackListener.markTileOnClient(tile);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public void resetTilesOnClient(){
        if(clientCallbackListener!=null)try{
            clientCallbackListener.resetTilesOnClient();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void markTile(Tile tile) throws RemoteException {
        getGameboardController().getTilesList().get(tile.getTileId()).mark(PlayerSign.O);
        getGameboardController().setYourTurn(true);
    }

    @Override
    public void setClientCallbackListener(ClientCallbackListener clientCallbackListener) throws RemoteException {
       System.out.println("Adding listener: "+clientCallbackListener);
       this.clientCallbackListener = clientCallbackListener;
       sendStatus(GameState.CONNECTED);
       resetTiles();
    }

    @Override
    public ClientCallbackListener getClientCallbackListener() throws RemoteException {
        return clientCallbackListener;
    }


}
