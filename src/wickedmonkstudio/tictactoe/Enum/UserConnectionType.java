package wickedmonkstudio.tictactoe.Enum;

/**
 * Created by Wojciech on 12.05.2017.
 */
public enum UserConnectionType {
    SERVER("server"),
    CLIENT("client");

    private String type;

    UserConnectionType(String type){this.type=type;}

    public String getType() {
        return type;
    }
}
