package com.empresa.hito2_3t_programacion_lucia_sanchez_bonilla;

import com.example.mongodb.MongoDBConnection;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.*;

public class HelloController {

    @FXML
    private Label welcomeText;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField filterUsernameField;

    @FXML
    private TextField filterEmailField;

    @FXML
    private TableView<Document> tableView;

    @FXML
    private TableColumn<Document, String> usernameColumn;

    @FXML
    private TableColumn<Document, String> emailColumn;

    private MongoCollection<Document> collection;
    private ObservableList<Document> userList;

    public HelloController() {
        // Conectar a MongoDB usando MongoDBConnection
        MongoDBConnection mongoDBConnection = new MongoDBConnection();
        collection = mongoDBConnection.getCollection();
    }

    @FXML
    protected void initialize() {
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getString("username")));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getString("email")));

        tableView.setRowFactory(tv -> {
            TableRow<Document> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem editItem = new MenuItem("Modificar");
            editItem.setOnAction(event -> editUser(row.getItem()));

            MenuItem deleteItem = new MenuItem("Eliminar");
            deleteItem.setOnAction(event -> deleteUser(row.getItem()));

            contextMenu.getItems().addAll(editItem, deleteItem);

            row.contextMenuProperty().bind(
                    javafx.beans.binding.Bindings.when(javafx.beans.binding.Bindings.isNotNull(row.itemProperty()))
                            .then(contextMenu)
                            .otherwise((ContextMenu) null));

            return row;
        });
    }

    @FXML
    protected void onHelloButtonClick() {
        List<Document> users = collection.find().into(new ArrayList<>());
        userList = FXCollections.observableArrayList(users);
        tableView.setItems(userList);
    }

    @FXML
    protected void onAddButtonClick() {
        String username = usernameField.getText();
        String email = emailField.getText();

        if (!username.isEmpty() && !email.isEmpty()) {
            try {
                Document user = new Document("username", username)
                        .append("email", email);
                collection.insertOne(user);
                welcomeText.setText("Usuario añadido: " + username);
                usernameField.clear();
                emailField.clear();
                System.out.println("Usuario añadido a MongoDB: " + user.toJson());
                onHelloButtonClick(); // Refrescar la lista de usuarios
            } catch (MongoException e) {
                showErrorDialog("Error al agregar usuario", "Ha ocurrido un error al agregar el usuario a la base de datos MongoDB.");
                e.printStackTrace(); // Opcional: Imprimir la traza de la excepción para depuración
            }
        } else {
            welcomeText.setText("Por favor, ingresa todos los campos.");
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    protected void onFilterButtonClick() {
        try {
            String filterUsername = filterUsernameField.getText();
            String filterEmail = filterEmailField.getText();

            List<Document> filteredUsers;
            if (!filterUsername.isEmpty() && !filterEmail.isEmpty()) {
                filteredUsers = collection.find(and(eq("username", filterUsername), eq("email", filterEmail))).into(new ArrayList<>());
            } else if (!filterUsername.isEmpty()) {
                filteredUsers = collection.find(eq("username", filterUsername)).into(new ArrayList<>());
            } else if (!filterEmail.isEmpty()) {
                filteredUsers = collection.find(eq("email", filterEmail)).into(new ArrayList<>());
            } else {
                filteredUsers = collection.find().into(new ArrayList<>());
            }

            userList = FXCollections.observableArrayList(filteredUsers);
            tableView.setItems(userList);
        } catch (MongoException e) {
            showErrorDialog("Error al filtrar usuarios", "Ha ocurrido un error al filtrar usuarios en la base de datos MongoDB.");
            e.printStackTrace(); // Opcional: Imprimir la traza de la excepción para depuración
        }
    }

    private void editUser(Document user) {
        TextInputDialog dialog = new TextInputDialog(user.getString("email"));
        dialog.setTitle("Modificar Usuario");
        dialog.setHeaderText("Modifica el correo electrónico de " + user.getString("username"));
        dialog.setContentText("Nuevo correo electrónico:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newEmail -> {
            if (!newEmail.isEmpty()) {
                Document update = new Document("$set", new Document("email", newEmail));
                collection.updateOne(new Document("username", user.getString("username")), update);
                welcomeText.setText("Correo actualizado para " + user.getString("username"));
                System.out.println("Correo actualizado en MongoDB: " + user.toJson());
                onHelloButtonClick(); // Refrescar la lista de usuarios
            }
        });
    }

    private void deleteUser(Document user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Usuario");
        alert.setHeaderText("Eliminar " + user.getString("username"));
        alert.setContentText("¿Estás seguro de que quieres eliminar este usuario?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            collection.deleteOne(new Document("username", user.getString("username")));
            welcomeText.setText("Usuario eliminado: " + user.getString("username"));
            System.out.println("Usuario eliminado de MongoDB: " + user.toJson());
            onHelloButtonClick(); // Refrescar la lista de usuarios
        }
    }
}
