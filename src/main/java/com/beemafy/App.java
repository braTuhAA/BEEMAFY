package com.beemafy;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class App extends Application {

    private static ObservableList<Claim> globalClaims = FXCollections.observableArrayList();
    private Stage primaryStage;

    @Override
    public void init() {
        // Sample data for testing
        globalClaims.add(new Claim("P101", "MRI Scan", 5000));
        globalClaims.add(new Claim("P102", "Dental Filling", 1200));
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        showLoginScreen();
    }

    // --- 1. MAIN LOGIN SCREEN (3 OPTIONS) ---
    private void showLoginScreen() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: #f0f4f7;");

        Label logo = new Label("Beemafy");
        logo.setFont(Font.font("System", FontWeight.BOLD, 40));
        logo.setTextFill(Color.web("#2A52BE"));

        Label subHeading = new Label("Select Your Portal");
        subHeading.setFont(Font.font("System", 16));

        Button patientBtn = createMenuButton("Patient Login", "#4A90E2");
        Button hospitalBtn = createMenuButton("Hospital Login", "#50C878");
        Button insuranceBtn = createMenuButton("Insurance Company Login", "#FF8C00");

        patientBtn.setOnAction(e -> showPatientLogin());
        hospitalBtn.setOnAction(e -> showHospitalDashboard());
        insuranceBtn.setOnAction(e -> showInsuranceDashboard());

        root.getChildren().addAll(logo, subHeading, patientBtn, hospitalBtn, insuranceBtn);
        primaryStage.setScene(new Scene(root, 600, 700));
        primaryStage.setTitle("Beemafy - Multi-Link Portal");
        primaryStage.show();
    }

    private Button createMenuButton(String text, String hexColor) {
        Button b = new Button(text);
        b.setMinWidth(250);
        b.setStyle("-fx-background-color: " + hexColor + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 15; -fx-background-radius: 10;");
        return b;
    }

    // --- 2. PATIENT LOGIN & DASHBOARD ---
    private void showPatientLogin() {
        TextInputDialog dialog = new TextInputDialog("P101");
        dialog.setTitle("Patient Login");
        dialog.setHeaderText("Enter Patient ID to view your claims");
        dialog.setContentText("Patient ID:");
        dialog.showAndWait().ifPresent(id -> showPatientDashboard(id));
    }

    private void showPatientDashboard(String patientID) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        
        Label title = new Label("My Claims History (Patient: " + patientID + ")");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));

        TableView<Claim> table = createBaseTable();
        // Filtering: Sirf wahi claims dikhao jo is Patient ID ke hain
        FilteredList<Claim> filteredData = new FilteredList<>(globalClaims, c -> c.getPatientName().equalsIgnoreCase(patientID));
        table.setItems(filteredData);

        Button back = new Button("Back to Main Menu");
        back.setOnAction(e -> showLoginScreen());

        root.getChildren().addAll(title, table, back);
        primaryStage.getScene().setRoot(root);
    }

    // --- 3. HOSPITAL DASHBOARD (Submit & Search) ---
    private void showHospitalDashboard() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label title = new Label("Hospital Portal - Claim Submission");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));

        TextField pIdField = new TextField(); pIdField.setPromptText("Patient ID");
        TextField treatField = new TextField(); treatField.setPromptText("Treatment Name");
        TextField amtField = new TextField(); amtField.setPromptText("Amount");

        Button submitBtn = new Button("Submit New Claim");
        submitBtn.setStyle("-fx-background-color: #50C878; -fx-text-fill: white;");
        submitBtn.setOnAction(e -> {
            try {
                globalClaims.add(new Claim(pIdField.getText(), treatField.getText(), Double.parseDouble(amtField.getText())));
                pIdField.clear(); treatField.clear(); amtField.clear();
            } catch (Exception ex) { System.out.println("Invalid Input"); }
        });

        Separator sep = new Separator();
        Label searchLabel = new Label("Search Patient Status:");
        TextField searchField = new TextField(); searchField.setPromptText("Enter Patient ID to Search");
        
        TableView<Claim> searchTable = createBaseTable();
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            searchTable.setItems(new FilteredList<>(globalClaims, c -> c.getPatientName().contains(newVal)));
        });

        Button back = new Button("Back");
        back.setOnAction(e -> showLoginScreen());

        root.getChildren().addAll(title, pIdField, treatField, amtField, submitBtn, sep, searchLabel, searchField, searchTable, back);
        primaryStage.getScene().setRoot(root);
    }

    // --- 4. INSURANCE COMPANY DASHBOARD (Approve/Reject) ---
    private void showInsuranceDashboard() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label title = new Label("Insurance Admin - Claim Processing");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));

        TableView<Claim> table = createBaseTable();
        table.setItems(globalClaims);

        HBox controls = new HBox(10);
        Button approveBtn = new Button("Approve");
        approveBtn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        Button rejectBtn = new Button("Reject");
        rejectBtn.setStyle("-fx-background-color: red; -fx-text-fill: white;");

        approveBtn.setOnAction(e -> {
            Claim selected = table.getSelectionModel().getSelectedItem();
            if(selected != null) selected.updateStatus(ClaimStatus.APPROVED);
            table.refresh();
        });

        rejectBtn.setOnAction(e -> {
            Claim selected = table.getSelectionModel().getSelectedItem();
            if(selected != null) selected.updateStatus(ClaimStatus.REJECTED);
            table.refresh();
        });

        controls.getChildren().addAll(approveBtn, rejectBtn);

        Button back = new Button("Back");
        back.setOnAction(e -> showLoginScreen());

        root.getChildren().addAll(title, new Label("Pending Claims:"), table, controls, back);
        primaryStage.getScene().setRoot(root);
    }

    // Helper method to keep table styling consistent
    private TableView<Claim> createBaseTable() {
        TableView<Claim> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Claim, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("claimId"));

        TableColumn<Claim, String> patientCol = new TableColumn<>("Patient ID");
        patientCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));

        TableColumn<Claim, String> treatCol = new TableColumn<>("Treatment");
        treatCol.setCellValueFactory(new PropertyValueFactory<>("treatmentName"));

        TableColumn<Claim, ClaimStatus> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        statusCol.setCellFactory(column -> new TableCell<Claim, ClaimStatus>() {
            @Override
            protected void updateItem(ClaimStatus item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null); setStyle("");
                } else {
                    setText(item.toString());
                    if (item == ClaimStatus.APPROVED) setTextFill(Color.GREEN);
                    else if (item == ClaimStatus.REJECTED) setTextFill(Color.RED);
                    else setTextFill(Color.ORANGE);
                    setFont(Font.font("System", FontWeight.BOLD, 12));
                }
            }
        });

        table.getColumns().addAll(idCol, patientCol, treatCol, statusCol);
        return table;
    }

    public static void main(String[] args) {
        launch(args);
    }
}