package com.example.oopcontactmanagementfxml;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private TableView<Person> PersonTableView;

    @FXML
    private TableColumn<Person, String> addressCol;

    @FXML
    private TextField addressText;

    @FXML
    private TableColumn<Person, String> contactCol;

    @FXML
    private TextField contactText;

    @FXML
    private TableColumn<Person, String> emailCol;

    @FXML
    private TextField emailText;

    @FXML
    private TableColumn<Person, String> nameCol;

    Person editPerson;

    @FXML
    private TextField nameText;
    private ObservableList<Person> initialData() {
        return FXCollections.observableArrayList();
    }
    @FXML
    void handleSaveAction(ActionEvent event) {
        Person newPerson = new Person(nameText.getText(), addressText.getText(), contactText.getText(), emailText.getText());
        try {
            Statement statement = DBconnection.getStatement();
            String insertQuery = "INSERT INTO person (name, address, contact, email) VALUES ('" + newPerson.getName()
                    + "', '" + newPerson.getAddress() + "', '" + newPerson.getContact() + "', '" + newPerson.getEmail() + "')";
            System.out.println(insertQuery);
            statement.execute(insertQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        PersonTableView.setItems(getPersonList());

        nameText.clear();
        addressText.clear();
        contactText.clear();
        emailText.clear();
    }
    private ObservableList<Person> getPersonList() {
        ObservableList<Person> personList = FXCollections.observableArrayList();
        try {
            Statement statement = DBconnection.getStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM person");
            while (resultSet.next()) {
                Person person = new Person(resultSet.getString("name"), resultSet.getString("address"),
                        resultSet.getString("contact"), resultSet.getString("email"));
                personList.add(person);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(editPerson != null) {

            personList.remove(editPerson);
            editPerson = null;
        }
        return personList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        PersonTableView.setItems(getPersonList());
    }
    public void hadleSelectPerson(MouseEvent mouseEvent) {
        editPerson = PersonTableView.getSelectionModel().getSelectedItem();

        nameText.setText(editPerson.getName());
        addressText.setText(editPerson.getAddress());
        contactText.setText(editPerson.getContact());
        emailText.setText(editPerson.getEmail());
    }
}
