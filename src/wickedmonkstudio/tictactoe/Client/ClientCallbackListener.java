package wickedmonkstudio.tictactoe.Client;

import wickedmonkstudio.tictactoe.Enum.GameState;
import wickedmonkstudio.tictactoe.Model.Tile;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Metoda implementowana w kliencie ale uruchamiana na serwerze
 */
public interface ClientCallbackListener extends Remote {
    void sendToClient(String text) throws RemoteException;
    void markTileOnClient(Tile tile )throws RemoteException;
    void sendStatusToClient(GameState state) throws RemoteException;
    void resetTilesOnClient() throws RemoteException;
}
