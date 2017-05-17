package wickedmonkstudio.tictactoe.Enum;

import java.io.Serializable;

/**
 * Created by Wojciech on 12.05.2017.
 */
public enum PlayerSign implements Serializable {
    X("X"),
    O("O");

    private String sign;

    PlayerSign(String sign){this.sign=sign;}

    public String getSign() {
        return sign;
    }
}
