package wickedmonkstudio.tictactoe.Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfejsy do ustawiania callbacków na serwerze (metod z klienta uruchamianych przez serwer)
 */
public interface ClientCallback extends Remote {
    void setClientCallbackListener(ClientCallbackListener clientCallbackListener) throws RemoteException;
    ClientCallbackListener getClientCallbackListener() throws RemoteException;
}
