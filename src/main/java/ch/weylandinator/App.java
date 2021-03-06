package ch.weylandinator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/stylesheets/DefaultStyles.css");
        
        stage.setTitle("Weylandinator");
        stage.setScene(scene);
        stage.getIcons().add(new Image("logo.png"));
        stage.setMinWidth(1020.0);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}