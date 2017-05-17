package wickedmonkstudio.tictactoe.Model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import wickedmonkstudio.tictactoe.Enum.PlayerSign;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * Created by Wojciech on 12.05.2017.
 */
public class Tile extends Rectangle implements Serializable {
    private BufferedImage image;
    private int tileId;
    private PlayerSign sign;

    public Tile(double width, double height, BufferedImage tileImage, int id){
        super(width, height);
        this.image =tileImage;
        this.tileId=id;
        this.sign=null;
    }

    public Tile(BufferedImage img, int id){
        this.image =img;
        this.tileId=id;
        this.sign=null;
    }

    public void mark(PlayerSign playerSign){
        switch (playerSign){
            case X: this.setFill(Color.BLACK);
            break;

            case O: this.setFill(Color.RED);
            break;
        }
        this.sign=playerSign;
    }

    public void unmark(){
        this.setSign(null);
        this.setFill(Color.WHITE);
    }

    public BufferedImage getTileImage() {
        return image;
    }

    public void setTileImage(BufferedImage partOfImage) {
        this.image = partOfImage;
    }

    public int getTileId() {
        return tileId;
    }

    public void setTileId(int tileId) {
        this.tileId = tileId;
    }

    public PlayerSign getSign() {
        return sign;
    }

    public void setSign(PlayerSign sign) {
        this.sign = sign;
    }

}

