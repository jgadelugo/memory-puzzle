package MemoryPuzzle;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryPuzzleApp extends Application {

    private static final int NUM_OF_PAIRS = 8;
    private static final int NUM_PER_ROW = 4 ;

    //variable for tile selected
    private Tile selected = null;
    //limiting clicks
    private int clickCount = 2;

    public BufferedImage[] images() throws IOException {
        // Images are located in the geek folder
        // which is located in the images folder
        // which is located in the current directory.
        String prefix = "C:\\Users\\josep\\Desktop\\Java\\mp\\images\\";
        String[] ids = { "cat", "Dog", "eagle", "Elephant", "Fish", "Horse","Rabbit", "Tiger"};
        String ext = ".jpeg";
        BufferedImage[] images = new BufferedImage[ids.length];
        for(int i = 0; i < images.length; i++) {
            String path = prefix + ids[i] + ext;
            System.out.println(path);
            images[i] = ImageIO.read(new File(path));
        }
        return images;
    }
    private Parent createContent() throws IOException{
        Pane root = new Pane();
        root.setPrefSize(600,600);

        List<String> imageList = new ArrayList<String>();

        //Creates tiles for board using images
        BufferedImage[] c = images();

        List<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < NUM_OF_PAIRS; i++) {
            tiles.add(new Tile(String.valueOf(c[i])));
            tiles.add(new Tile(String.valueOf(c[i])));
            ;
        }
        // shuffles tiles so they are not in order
        Collections.shuffle(tiles);

        // displays tiles
    for (int i = 0; i< tiles.size(); i++){
        Tile tile = tiles.get(i);
        tile.setTranslateX(50*(i % NUM_PER_ROW));
        tile.setTranslateY(50*(i / NUM_PER_ROW));
        root.getChildren().add(tile);

    }

        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();

    }
    private class Tile extends StackPane {
        private Text text = new Text();


        public Tile(String value) {
            Rectangle border = new Rectangle(50,50);
            border.setFill(null);
            border.setStroke(Color.BLACK);

            text.setText(value);
            text.setFont(Font.font(30));

            setAlignment(Pos.CENTER);
            getChildren().addAll(border,text);

            //setting tiles to open on click if less than two tiles are open and tile is closed
            setOnMouseClicked(this::handleMouseClick);

            close();
        }

        public void handleMouseClick(MouseEvent event){
            if (isOpen() || clickCount==0)
                return;
            clickCount--;
            if (selected == null){
                selected = this;
                open(()->{});
            }
            else{
                open(()->{
                    if (!hasSameValue(selected)) {
                        selected.close();
                        this.close();
                    }
                    selected = null;
                    clickCount = 2;
                });
            }
        }
        //check to see if tile is open. If opacity is == to 1 its open
        public boolean isOpen(){
            return text.getOpacity() ==1;

        }


        //flip tile to show value
        public void open(Runnable action){
            FadeTransition ft = new FadeTransition(Duration.seconds(0.5), text);
            ft.setToValue(1);
            ft.setOnFinished(e -> action.run());
            ft.play();
        }
        //flip tile to hide value
        public void close(){
            FadeTransition ft = new FadeTransition(Duration.seconds(0.5), text);
            ft.setToValue(0);
            ft.play();
        }
        // return true if tiles have the same value
        public boolean hasSameValue(Tile other){
            return text.getText().equals(other.text.getText());
        }
    }
    public static void main(String[] args){
        launch(args);
    }

}
