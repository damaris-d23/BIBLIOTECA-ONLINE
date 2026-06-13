package ro.damaris.biblioteca.ui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import ro.damaris.biblioteca.model.Carte;
import ro.damaris.biblioteca.model.Cititor;
import ro.damaris.biblioteca.model.Utilizator;
import ro.damaris.biblioteca.service.BibliotecaService;

import java.util.List;

/**
 *ecranul principal al aplicatiei.
 *afiseaza cartile si permite cautare, imprumut si returnare
 *
 */
public class LibraryView extends BorderPane {

    private final BibliotecaService service = new BibliotecaService();
    private final TableView<Carte> table = new TableView<>();
    private final Label msg = new Label();

    private final Label badgeDisponibile = new Label();
    private final Label badgeImprumutate = new Label();

    /**
     *construieste interfata principala
     */
    public LibraryView(Stage stage, Utilizator user) {
        setPadding(new Insets(16));
        setStyle("-fx-background-color: #f7f7f7;");

        //header
        Label title = new Label("Biblioteca Online");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: 900; -fx-text-fill: #111111;");

        Label who = new Label("Conectat ca: " + user.getNume() + " (" + user.getTipUtilizator().toLowerCase() + ")");
        who.setStyle("-fx-text-fill: #444444; -fx-font-weight: 700;");

        styleBadge(badgeDisponibile, "Disponibile: 0", false);
        styleBadge(badgeImprumutate, "Imprumutate: 0", true);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnLogout = secondaryButton("Logout");
        btnLogout.setOnAction(e -> stage.getScene().setRoot(new LoginView(stage)));

        HBox header = new HBox(12, title, who, badgeDisponibile, badgeImprumutate, spacer, btnLogout);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(2, 2, 12, 2));

        //toolbar
        TextField tfSearch = new TextField();
        tfSearch.setPromptText("Cauta dupa titlu sau autor...");
        tfSearch.setStyle("""
                -fx-background-color: #ffffff;
                -fx-background-radius: 12;
                -fx-border-radius: 12;
                -fx-border-color: rgba(0,0,0,0.15);
                -fx-padding: 10 12;
                """);

        Button btnSearch = primaryButton("Cauta");
        Button btnReset = secondaryButton("Toate");

        Button btnBorrow = primaryButton("Imprumuta");
        Button btnReturn = secondaryButton("Returneaza");

        Button btnAdmin = secondaryButton("Admin");
        btnAdmin.setDisable(!"BIBLIOTECAR".equals(user.getTipUtilizator()));

        HBox toolbar = new HBox(10, tfSearch, btnSearch, btnReset, new Separator(), btnBorrow, btnReturn, btnAdmin);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(0, 2, 12, 2));

        setTop(new VBox(header, toolbar));

        //tabel
        VBox card = new VBox(10);
        card.setPadding(new Insets(14));
        card.setStyle("""
                -fx-background-color: #ffffff;
                -fx-background-radius: 18;
                -fx-border-radius: 18;
                -fx-border-color: rgba(0,0,0,0.08);
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 22, 0.25, 0, 10);
                """);

        setupTable();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setFixedCellSize(34);

        msg.setVisible(false);

        card.getChildren().addAll(table, msg);
        setCenter(card);

        //load initial
        Runnable refreshAll = () -> {
            List<Carte> all = service.toateCartile();
            table.setItems(FXCollections.observableArrayList(all));
            updateBadges(all);
            hideMsg();
        };
        refreshAll.run();

        //actiuni
        btnSearch.setOnAction(e -> {
            List<Carte> rez = service.cauta(tfSearch.getText());
            table.setItems(FXCollections.observableArrayList(rez));
            updateBadges(rez);
            hideMsg();
        });

        btnReset.setOnAction(e -> {
            tfSearch.clear();
            refreshAll.run();
        });

        btnBorrow.setOnAction(e -> {
            Carte c = table.getSelectionModel().getSelectedItem();
            if (c == null) { showMsg("Selecteaza o carte din tabel.", true); return; }
            if (!"CITITOR".equals(user.getTipUtilizator())) { showMsg("Doar cititorul poate imprumuta.", true); return; }

            try {
                service.imprumutaCarte(c.getIdCarte(), (Cititor) user);
                showMsg("Imprumut realizat.", false);
                refreshAll.run();
            } catch (Exception ex) {
                showMsg(ex.getMessage(), true);
            }
        });

        btnReturn.setOnAction(e -> {
            Carte c = table.getSelectionModel().getSelectedItem();
            if (c == null) { showMsg("Selecteaza o carte din tabel.", true); return; }

            try {
                service.returneazaCarte(c.getIdCarte());
                showMsg("Retur realizat.", false);
                refreshAll.run();
            } catch (Exception ex) {
                showMsg(ex.getMessage(), true);
            }
        });

        btnAdmin.setOnAction(e -> stage.getScene().setRoot(new AdminView(stage, user)));
    }

    /**
     *configureaza coloanele tabelului
     */
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

        TableColumn<Carte, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().isDisponibila() ? "disponibila" : "imprumutata"
        ));
        colStatus.setMaxWidth(140);

        table.getColumns().setAll(colId, colTitlu, colAutor, colAn, colStatus);

        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Carte item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setStyle("");
                else if (!item.isDisponibila()) setStyle("-fx-background-color: rgba(31,95,59,0.08);");
                else setStyle("");
            }
        });
    }

    /**
     *stil pentru buton principal
     */
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

    /**
     *stil pentru buton secundar
     */
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

    private void styleBadge(Label l, String text, boolean dark) {
        l.setText(text);
        l.setStyle(dark ? """
                -fx-background-color: rgba(0,0,0,0.06);
                -fx-text-fill: #111111;
                -fx-padding: 6 10;
                -fx-background-radius: 999;
                -fx-font-weight: 800;
                """ : """
                -fx-background-color: rgba(31,95,59,0.12);
                -fx-text-fill: #1f5f3b;
                -fx-padding: 6 10;
                -fx-background-radius: 999;
                -fx-font-weight: 800;
                """);
    }

    /**
     *actualizeaza badge-urile de status
     */
    private void updateBadges(List<Carte> list) {
        long disp = list.stream().filter(Carte::isDisponibila).count();
        long imp = list.size() - disp;
        styleBadge(badgeDisponibile, "Disponibile: " + disp, false);
        styleBadge(badgeImprumutate, "Imprumutate: " + imp, true);
    }

    /**
     *afiseaza mesaj de status
     */
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

    /**
     *ascunde mesajul
     */
    private void hideMsg() {
        msg.setVisible(false);
        msg.setText("");
    }
}
