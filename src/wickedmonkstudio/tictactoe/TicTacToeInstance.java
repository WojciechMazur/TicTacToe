package wickedmonkstudio.tictactoe;

import wickedmonkstudio.tictactoe.Enum.PlayerSign;
import wickedmonkstudio.tictactoe.Enum.UserConnectionType;
import wickedmonkstudio.tictactoe.View.GameboardController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Wojciech on 12.05.2017.
 */
public class TicTacToeInstance extends UnicastRemoteObject {
   protected GameboardController gameboardController;
    protected UserConnectionType userConnectionType;
    protected PlayerSign sign;

    protected TicTacToeInstance(UserConnectionType type) throws RemoteException {
    super();
    this.userConnectionType=type;
    this.sign=(userConnectionType.equals(UserConnectionType.SERVER)? PlayerSign.X :PlayerSign.O);
    }

    public void setGameboardController(GameboardController controller){
       this.gameboardController=controller;
   }

    public GameboardController getGameboardController() {
        return gameboardController;
    }

    public UserConnectionType getUserConnectionType() {
        return userConnectionType;
    }

    public PlayerSign getSign() {
        return sign;
    }

}
