package ro.damaris.biblioteca.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import ro.damaris.biblioteca.model.Utilizator;
import ro.damaris.biblioteca.service.BibliotecaService;
import ro.damaris.biblioteca.model.Carte;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;


public class AdminView extends BorderPane {

    private final BibliotecaService service = new BibliotecaService();
    private final Label msg = new Label();
    private final TableView<Carte> table = new TableView<>();


    public AdminView(Stage stage, Utilizator user) {
        setPadding(new Insets(16));
        setStyle("-fx-background-color: #f7f7f7;");

        Label title = new Label("Admin - Gestionare carti");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: #111111;");

        Button btnBack = secondaryButton("Inapoi");
        btnBack.setOnAction(e -> stage.getScene().setRoot(new LibraryView(stage, user)));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(12, title, spacer, btnBack);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(2, 2, 12, 2));
        setTop(header);

        VBox card = new VBox(10);
        card.setPadding(new Insets(16));
        card.setMaxWidth(900);
        card.setStyle("""
                -fx-background-color: #ffffff;
                -fx-background-radius: 18;
                -fx-border-radius: 18;
                -fx-border-color: rgba(0,0,0,0.08);
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 22, 0.25, 0, 10);
                """);

        setupTable();
        table.setItems(FXCollections.observableArrayList(service.toateCartile()));


        TextField tfId = new TextField();
        tfId.setPromptText("id (doar pt update/delete)");
        styleInput(tfId);

        TextField tfTitlu = new TextField();
        tfTitlu.setPromptText("titlu");
        styleInput(tfTitlu);

        TextField tfAutor = new TextField();
        tfAutor.setPromptText("autor");
        styleInput(tfAutor);

        TextField tfAn = new TextField();
        tfAn.setPromptText("an aparitie (ex: 2001)");
        styleInput(tfAn);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, c) -> {
            if (c == null) return;
            tfTitlu.setText(c.getTitlu());
            tfAutor.setText(c.getAutor());
            tfAn.setText(String.valueOf(c.getAnAparitie()));
        });


        Button btnAdd = primaryButton("Add");
        Button btnUpdate = secondaryButton("Update");
        Button btnDelete = secondaryButton("Delete");

        HBox actions = new HBox(10, btnAdd, btnUpdate, btnDelete);
        actions.setAlignment(Pos.CENTER_LEFT);

        msg.setVisible(false);

        btnAdd.setOnAction(e -> {
            try {
                int an = Integer.parseInt(tfAn.getText().trim());
                service.adaugaCarte(tfTitlu.getText(), tfAutor.getText(), an);
                showMsg("Carte adaugata.", false);
                clear(tfId, tfTitlu, tfAutor, tfAn);
            } catch (Exception ex) {
                showMsg(ex.getMessage(), true);
            }
        });

        btnUpdate.setOnAction(e -> {
            try {
                Carte sel = table.getSelectionModel().getSelectedItem();
                if (sel == null) {
                    showMsg("selecteaza o carte din tabel.", true);
                    return;
                }

                int an = Integer.parseInt(tfAn.getText().trim());
                service.actualizeazaCarte(sel.getIdCarte(), tfTitlu.getText(), tfAutor.getText(), an);

                showMsg("Carte actualizata.", false);

                table.setItems(FXCollections.observableArrayList(service.toateCartile()));
                table.getSelectionModel().clearSelection();
                clear(tfTitlu, tfAutor, tfAn);

            } catch (Exception ex) {
                showMsg(ex.getMessage(), true);
            }
        });


        btnDelete.setOnAction(e -> {
            try {
                Carte sel = table.getSelectionModel().getSelectedItem();
                if (sel == null) {
                    showMsg("selecteaza o carte din tabel.", true);
                    return;
                }

                service.stergeCarte(sel.getIdCarte());
                showMsg("Carte arhivata.", false);

                table.setItems(FXCollections.observableArrayList(service.toateCartile()));
                table.getSelectionModel().clearSelection();
                clear(tfTitlu, tfAutor, tfAn);

            } catch (Exception ex) {
                showMsg(ex.getMessage(), true);
            }
        });


        card.getChildren().addAll(
                table,
                new Separator(),

                label("Titlu"), tfTitlu,
                label("Autor"), tfAutor,
                label("An aparitie"), tfAn,
                new Separator(),
                actions,
                msg
        );


        VBox centerWrap = new VBox(card);
        centerWrap.setAlignment(Pos.TOP_CENTER);
        centerWrap.setPadding(new Insets(8, 0, 0, 0));
        setCenter(centerWrap);



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

    private Button primaryButton(String text) {
        Button b = new Button(text);
        b.setStyle("""
                -fx-background-color: #1f5f3b;
                -fx-text-fill: white;
                -fx-font-weight: 800;
                -fx-background-radius: 12;
                -fx-padding: 9 12;
                -fx-cursor: hand;
                """);
        return b;
    }

    private Button secondaryButton(String text) {
        Button b = new Button(text);
        b.setStyle("""
                -fx-background-color: rgba(0,0,0,0.06);
                -fx-text-fill: #111111;
                -fx-font-weight: 800;
                -fx-background-radius: 12;
                -fx-padding: 9 12;
                -fx-cursor: hand;
                """);
        return b;
    }

    private void showMsg(String text, boolean error) {
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



    private void clear(TextField... fields) {
        for (TextField f : fields) f.clear();
    }

    private void setupTable() {
        TableColumn<Carte, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getIdCarte()));
        colId.setMaxWidth(80);

        TableColumn<Carte, String> colTitlu = new TableColumn<>("Titlu");
        colTitlu.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitlu()));

        TableColumn<Carte, String> colAutor = new TableColumn<>("Autor");
        colAutor.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAutor()));

        TableColumn<Carte, Number> colAn = new TableColumn<>("An");
        colAn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getAnAparitie()));
        colAn.setMaxWidth(120);

        table.getColumns().setAll(colId, colTitlu, colAutor, colAn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setFixedCellSize(34);
    }

}
