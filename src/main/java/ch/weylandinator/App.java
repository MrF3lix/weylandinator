package ch.weylandinator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("view/Sample.fxml"));
        stage.setTitle("weylandinator");
        stage.setScene(new Scene(root, 1000, 800));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}