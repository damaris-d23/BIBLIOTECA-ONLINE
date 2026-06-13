package ro.damaris.biblioteca.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ro.damaris.biblioteca.model.Utilizator;
import ro.damaris.biblioteca.repository.UserRepository;

/**
 *ecranul de autentificare al aplicatiei
 */
public class LoginView extends VBox {

    private final UserRepository userRepo = new UserRepository();

    /**
     *construieste ecranul de login
     *
     */
    public LoginView(Stage stage) {
        setAlignment(Pos.CENTER);
        setSpacing(14);
        setPadding(new Insets(30));
        setStyle("-fx-background-color: #f7f7f7;");

        Label title = new Label("Biblioteca Online");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: 800; -fx-text-fill: #111111;");

        Label subtitle = new Label("Autentificare");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #444444;");

        VBox card = new VBox(10);
        card.setPadding(new Insets(18));
        card.setMaxWidth(420);
        card.setStyle("""
                -fx-background-color: #ffffff;
                -fx-background-radius: 18;
                -fx-border-radius: 18;
                -fx-border-color: rgba(0,0,0,0.08);
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 22, 0.25, 0, 10);
                """);

        Label l1 = label("Email");
        TextField tfEmail = new TextField();
        tfEmail.setPromptText("email");
        styleInput(tfEmail);

        Label l2 = label("Parola");
        PasswordField pfPass = new PasswordField();
        pfPass.setPromptText("parola");
        styleInput(pfPass);

        Label msg = new Label();
        msg.setVisible(false);

        Button btnLogin = primaryButton("Intra");
        btnLogin.setDefaultButton(true);

        Button btnRegister = secondaryButton("Creeaza cont");

        btnLogin.setOnAction(e -> {
            Utilizator u = userRepo.login(tfEmail.getText(), pfPass.getText());
            if (u == null) {
                showMsg(msg, "Date incorecte.", true);
                return;
            }
            stage.getScene().setRoot(new LibraryView(stage, u));
        });

        btnRegister.setOnAction(e -> stage.getScene().setRoot(new RegisterView(stage)));

        card.getChildren().addAll(l1, tfEmail, l2, pfPass, btnLogin, btnRegister, msg);
        getChildren().addAll(title, subtitle, card);
    }

    private Label label(String t) {
        Label l = new Label(t);
        l.setStyle("-fx-text-fill: #111111; -fx-font-weight: 800;");
        return l;
    }

    private void styleInput(TextField tf) {
        tf.setStyle("""
                -fx-background-color: #ffffff;
                -fx-background-radius: 12;
                -fx-border-radius: 12;
                -fx-border-color: rgba(0,0,0,0.15);
                -fx-padding: 10 12;
                -fx-font-size: 13px;
                """);
    }

    /**
     *buton principal
     */
    private Button primaryButton(String text) {
        Button b = new Button(text);
        b.setStyle("""
                -fx-background-color: #1f5f3b;
                -fx-text-fill: white;
                -fx-font-weight: 800;
                -fx-background-radius: 12;
                -fx-padding: 10 14;
                -fx-cursor: hand;
                """);
        return b;
    }

    /**
     *buton secundar
     */
    private Button secondaryButton(String text) {
        Button b = new Button(text);
        b.setStyle("""
                -fx-background-color: rgba(0,0,0,0.06);
                -fx-text-fill: #111111;
                -fx-font-weight: 800;
                -fx-background-radius: 12;
                -fx-padding: 10 14;
                -fx-cursor: hand;
                """);
        return b;
    }

    /**
     *afiseaza mesaj de eroare sau succes
     */
    private void showMsg(Label msg, String text, boolean error) {
        msg.setVisible(true);
        msg.setText(text);
        msg.setPadding(new Insets(10));
        msg.setStyle(error ? """
                -fx-background-color: rgba(220, 60, 60, 0.10);
                -fx-text-fill: #8b1a1a;
                -fx-background-radius: 12;
                -fx-font-weight: 700;
                """ : """
                -fx-background-color: rgba(31,95,59,0.12);
                -fx-text-fill: #1f5f3b;
                -fx-background-radius: 12;
                -fx-font-weight: 700;
                """);
    }
}
