package ro.damaris.biblioteca.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ro.damaris.biblioteca.repository.UserRepository;

/**
 *ecran pentru creare cont (cititor sau bibliotecar)
 */
public class RegisterView extends VBox {

    private final UserRepository userRepo = new UserRepository();

    /**
     *construieste ecranul de inregistrare
     */
    public RegisterView(Stage stage) {
        setAlignment(Pos.CENTER);
        setSpacing(14);
        setPadding(new Insets(30));
        setStyle("-fx-background-color: #f7f7f7;");

        Label title = new Label("Creeaza cont");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: 900; -fx-text-fill: #111111;");

        VBox card = new VBox(10);
        card.setPadding(new Insets(18));
        card.setMaxWidth(520);
        card.setStyle("""
                -fx-background-color: #ffffff;
                -fx-background-radius: 18;
                -fx-border-radius: 18;
                -fx-border-color: rgba(0,0,0,0.08);
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 22, 0.25, 0, 10);
                """);

        TextField tfNume = new TextField();
        tfNume.setPromptText("nume");
        styleInput(tfNume);

        TextField tfEmail = new TextField();
        tfEmail.setPromptText("email");
        styleInput(tfEmail);

        PasswordField pfPass = new PasswordField();
        pfPass.setPromptText("parola");
        styleInput(pfPass);

        PasswordField pfPass2 = new PasswordField();
        pfPass2.setPromptText("confirma parola");
        styleInput(pfPass2);

        ComboBox<String> cbTip = new ComboBox<>();
        cbTip.getItems().addAll("CITITOR", "BIBLIOTECAR");
        cbTip.setValue("CITITOR");
        cbTip.setStyle("""
                -fx-background-color: #ffffff;
                -fx-background-radius: 12;
                -fx-border-radius: 12;
                -fx-border-color: rgba(0,0,0,0.15);
                -fx-padding: 6 8;
                -fx-font-size: 13px;
                """);

        TextField tfNrLeg = new TextField();
        tfNrLeg.setPromptText("nr legitimatie (doar pt cititor)");
        styleInput(tfNrLeg);

        //aratam/ascundem nr legitimatie in functie de tip
        cbTip.setOnAction(e -> tfNrLeg.setDisable(!"CITITOR".equals(cbTip.getValue())));
        tfNrLeg.setDisable(false);

        Label msg = new Label();
        msg.setVisible(false);

        Button btnCreate = primaryButton("Creeaza cont");
        Button btnBack = secondaryButton("Inapoi");

        btnBack.setOnAction(e -> stage.getScene().setRoot(new LoginView(stage)));

        btnCreate.setOnAction(e -> {
            try {
                if (!pfPass.getText().equals(pfPass2.getText())) {
                    showMsg(msg, "parolele nu coincid", true);
                    return;
                }

                String tip = cbTip.getValue();
                Integer nrLeg = null;

                if ("CITITOR".equals(tip)) {
                    String s = tfNrLeg.getText().trim();
                    if (s.isEmpty()) {
                        showMsg(msg, "completeaza nr legitimatie", true);
                        return;
                    }
                    nrLeg = Integer.parseInt(s);
                }

                userRepo.register(tfNume.getText(), tfEmail.getText(), pfPass.getText(), tip, nrLeg);
                showMsg(msg, "cont creat. te poti loga.", false);

            } catch (NumberFormatException nfe) {
                showMsg(msg, "nr legitimatie trebuie sa fie numar", true);
            } catch (Exception ex) {
                showMsg(msg, ex.getMessage(), true);
            }
        });

        HBox buttons = new HBox(10, btnCreate, btnBack);
        buttons.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().addAll(label("Nume"), tfNume, label("Email"), tfEmail, label("Parola"), pfPass, label("Confirma parola"), pfPass2, label("Tip cont"), cbTip, tfNrLeg, buttons, msg
        );

        getChildren().addAll(title, card);
    }

    /**
     *label simplu pentru formular
     */
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
     *afiseaza mesaj pe ecran
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
