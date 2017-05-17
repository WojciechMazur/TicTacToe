package wickedmonkstudio.tictactoe.Server;

import wickedmonkstudio.tictactoe.Enum.GameState;
import wickedmonkstudio.tictactoe.Model.Tile;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Wojciech on 17.05.2017.
 */
public interface ServerInterface extends Remote{
    void sendText(String text) throws RemoteException;
    void markTile(Tile tile)throws RemoteException;
    void sendStatus(GameState status) throws RemoteException;
    void resetTiles() throws RemoteException;
}
