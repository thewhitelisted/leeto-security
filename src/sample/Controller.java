package sample;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

/**
 *
 * @author clee2
 */
public class Controller implements Initializable {

    @FXML
    private ListView listView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listView.getItems().addAll("Golf Balls","Wedges","Irons","Tees","Driver","Putter");
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

}
