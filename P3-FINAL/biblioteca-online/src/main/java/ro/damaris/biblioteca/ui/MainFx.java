package ro.damaris.biblioteca.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ro.damaris.biblioteca.db.DbManager;

/**
 *punctul de start al aplicatiei
 */
public class MainFx extends Application {

    /**
     *initializeaza baza de date si porneste interfata
     */
    @Override
    public void start(Stage stage) {
        DbManager.initDb();

        LoginView root = new LoginView(stage);

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Biblioteca Online");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *metoda main
     */
    public static void main(String[] args) {
        launch(args);
    }
}
