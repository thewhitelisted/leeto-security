package sample;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 * @author clee2
 */
public class Controller implements Initializable {

    @FXML
    private ListView<Password> listView;
    @FXML
    public Label dateLabel;
    @FXML
    public Label keyLabel;
    @FXML
    public Label passwordLabel;
    @FXML
    public Label organizationLabel;

    @FXML
    TextField OrganizationField;
    @FXML
    TextField OptionalKeyField;
    @FXML
    Button CreatePasswordButton;

    @FXML
    TextField EditOrganizationField;
    @FXML
    TextField EditKeyField;
    @FXML
    TextField EditPasswordField;

    @FXML
    Button PasswordCheckButton = new Button();
    @FXML
    Label ratingLabel = new Label();
    @FXML
    Label ratingTitle = new Label();
    @FXML
    Text recommendedList;
    @FXML
    PasswordField PasswordCheckField;

    @FXML
    TextField searchField;

    Password currentPassword;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //Update password list and add the password info with fx:id labels
            //listView.getItems().addAll(createPasswordList());
            ObservableList<Password> passwords = createPasswordList();
            listView.getItems().addAll(passwords);

            //Display only the name of the Password on the ListView:
            listView.setCellFactory(new Callback<ListView<Password>, ListCell<Password>>() {
                @Override
                public ListCell<Password> call(ListView<Password> listView) {
                    return new ListCell<Password>() {
                        @Override
                        protected void updateItem(Password item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setText(null);
                            } else {
                                setText(item.getName());
                            }
                        }
                    };
                }
            });

            //Code to basically change the view according to the password that is selected:
            listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Password>() {
                @Override
                public void changed(ObservableValue<? extends Password> observableValue, Password password, Password t1) {

                    currentPassword = listView.getSelectionModel().getSelectedItem();
                    dateLabel.setText("Date Created: " + currentPassword.getTimeCreated());
                    keyLabel.setText("Key: " + currentPassword.getKey());
                    organizationLabel.setText("Organization: "+currentPassword.getName());
                    try {
                        passwordLabel.setText("Password: " + Algorithms.hashString(currentPassword.getKey()));
                    } catch (NoSuchAlgorithmException | NullPointerException e) {
                        throw new RuntimeException(e);
                    }

                }

            });

            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                List<Password> filteredPasswords = passwords.stream()
                        .filter(password -> password.getName().contains(newValue))
                        .collect(Collectors.toList());
                listView.setItems(FXCollections.observableArrayList(filteredPasswords));
            });



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    @FXML
    public ObservableList<Password> createPasswordList() throws IOException {
        FileReader fileReader = new FileReader("passwordlist.txt");
        BufferedReader reader = new BufferedReader(fileReader);
        ObservableList<Password> passwords = FXCollections.observableArrayList();

        String line;
        Password data;
        while ((line = reader.readLine()) != null) {
            data = FileIO.importData(line);
            passwords.add(data);
        }
        return passwords;
    }

    //Functionality Buttons:
    @FXML
    public void CreatePasswordButtonClick(ActionEvent actionEvent) {
        //On button click create some sample password with Password Creation Algorithm:
        //String generatedPassword = Algorithms.createPassword(6);
        try {
            if (OrganizationField.getText().isEmpty()) {
                OrganizationField.setPromptText("Organization is Required");
                return;
            }

            //Once the required organization is filled out allow the user to optionally input a key:
            //If empty, return true and generate a random 4-letter key, if not empty, return false and simply use the inputted text
            String key = OptionalKeyField.getText().isEmpty() ? Algorithms.createPassword(4) : OptionalKeyField.getText();
            Password newPassword = new Password(OrganizationField.getText(), key, Algorithms.getDate_Time());

            FileIO.exportData(newPassword);
            ObservableList<Password> passwords = createPasswordList();
            //Stream all the passwords; use map to transform each Password object into its name, collect them into the list with Collectors.toList()
            List<String> PasswordNames = passwords.stream()
                    .map(Password::getName)
                    .collect(Collectors.toList());
            if (FileIO.exists(newPassword, PasswordNames)) {
                CreatePasswordButton.setText("Password Already Exists");
                //Might want to create PauseTransition class
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(e -> CreatePasswordButton.setText("Create Secure Password"));
                pause.play();
            }
            listView.getItems().clear();
            listView.getItems().addAll(passwords);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void PasswordCheckButton(ActionEvent actionEvent) {
        String password = PasswordCheckField.getText();
        ratingLabel.setText(Algorithms.ratePassword(password));

        List<String> recommendations = Recommendations.ratingv2(password);

        if (!recommendations.isEmpty()) {
            ratingTitle.setText("Here are some recommendations on how you can improve your password using Leeto Security's Password Checking Algorithms: ");
        } else {
            ratingTitle.setText("After using Leeto Security's Password Checking Algorithms, we have no recommendations to make, meaning that your password is truly strong!");
        }

        StringBuilder sb = new StringBuilder();
        for (String item : recommendations) {
            sb.append("- ").append(item).append("\n");
        }
        recommendedList.setText(sb.toString());
    }

    @FXML
    public void ChangePasswordButtonClick(ActionEvent actionEvent) {
        try {
            // Ensure that the list view has focus
            listView.requestFocus();
            //Get the current password name from the listView, then retrieve the existing data from FileIO to update
            Password currentPassword = listView.getSelectionModel().getSelectedItem();
            if (currentPassword == null) {
                return;
            }
            //Need to have something here to update organization as well, will do later.
            String changedKey = EditKeyField.getText(); //get updated key value from the user
            String changedOrganization = EditOrganizationField.getText();

            //3 cases to check if the inputs are empty or not
            if (!changedKey.isEmpty()) {
                currentPassword.setKey(changedKey);
            }
            if (!changedOrganization.isEmpty()) {
                currentPassword.setName(changedOrganization);
            }
            if (changedKey.isEmpty() && changedOrganization.isEmpty()) {
                return;
            }

            Password updatePassword = new Password(currentPassword.getName(), currentPassword.getKey(), currentPassword.getTimeCreated());

            //Get index of current password and use ArrayList .set() method to update the password if the user changed anything
            //Update password and export the updated password to the text file/create a new dat file
            ObservableList<Password> passwords = listView.getItems();
            int index = passwords.indexOf(currentPassword);
            passwords.set(index, updatePassword);
            listView.getItems().set(index, updatePassword);

            //Clear the text file so that we don't keep appending the same data on top of the text file
            PrintWriter pw = new PrintWriter("passwordlist.txt");
            pw.print("");
            pw.close();
            for (int i = 0; i < passwords.size(); i++) {
                Password getPassword = passwords.get(i);
                FileIO.exportData(getPassword);
            }
            listView.setItems(createPasswordList());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
