package wickedmonkstudio.tictactoe.Enum;

import java.io.Serializable;

/**
 * Created by Wojciech on 17.05.2017.
 */
public enum GameState implements Serializable {
    DRAW("draw"),
    SERVER_WON("server won"),
    CLIENT_WON("client won"),
    IN_PROGRESS("in progress"),
    CONNECTED("connected"),
    ENEMY_MOVE("enemy move"),
    YOUR_MOVE("your move"),
    DISCONNECTED("disconnected");
    private String gameState;

    GameState(String gamestate){this.gameState=gamestate;}

    public String getGameState() {
        return gameState;
    }
}
