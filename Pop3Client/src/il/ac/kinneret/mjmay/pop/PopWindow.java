package il.ac.kinneret.mjmay.pop;

import il.ac.kinneret.mjmay.pop.model.IncomingListener;
import il.ac.kinneret.mjmay.pop.model.PopState;
import il.ac.kinneret.mjmay.pop.model.SharedData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class PopWindow implements Initializable {
    public static final int POP_PORT = 110;
    public static final int IMAP_PORT = 143;
    public TextField tfUserName;
    public PasswordField tfPassword;
    public TextField tfServer;
    public TextField tfList;
    public TextField tfRetr;
    public TextField tfDele;
    public TextField tfUidl;
    public TextField tfRaw;
    public TextArea taLog;
    public Button bConnect;


    PopState popState;
    private Thread listeningThread;

    /**
     * Initializes the GUI.
     * @param url Ignored
     * @param resourceBundle Ingored
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfUserName.setText("kin101");
        tfPassword.setText("kin101");
        tfServer.setText("12cm.yaweli.com");
        popState = null;
    }

    /**
     * Runs when the connect button is pressed
     * @param actionEvent Ignored
     */
    public void connect(ActionEvent actionEvent) {
        // see if we should connect or disconnect
        if (bConnect.getText().equals("Connect")) {
            // only try if there is a server here
            if (tfServer.getText().length() > 0) {
                // try to connect to the server
                popState = new PopState(tfServer.getText(), POP_PORT);
                // if we succeeded
                if (popState.isConnected()) {
                    taLog.appendText("Connected to " + tfServer.getText() + " on port " + POP_PORT + ".\n");
                    bConnect.setText("Disconnect");

                    //listen for incoming messages
                    InputStream isr = popState.getConnectionInputStream();
                    if (isr != null)
                    {
                        IncomingListener isl = new IncomingListener(isr);
                        listeningThread = new Thread(isl);
                        listeningThread.start();
                        isl.messageProperty().addListener((observableValue, s, t1) -> {
                            synchronized (SharedData.getSB()) {
                                taLog.appendText(SharedData.getSB().toString());
                                SharedData.clearSB();
                            }
                        });
                    }
                } else {
                    taLog.appendText("Error connecting to " + tfServer.getText() + " on port " + POP_PORT + ".\n");
                    bConnect.setText("Connect");
                }
            }
        } else
        {
            // disconnect
            if (popState != null){
                popState.close();
                listeningThread.interrupt();
                taLog.appendText("Disconnected from the server.\n");
                bConnect.setText("Connect");
            }
        }
    }

    /**
     * Runs when the user button is pressed
     * @param actionEvent Ignored
     */
    public void user(ActionEvent actionEvent) {
        String username = tfUserName.getText();
        String response = popState.doUser(username);
        taLog.appendText("USER " + username + "\n" + response + "\n");
    }

    /**
     * Runs when the password button is pushed
     * @param actionEvent Ignored
     */
    public void pass(ActionEvent actionEvent) {
        String password = tfPassword.getText();
        String response = popState.doPass(password);
        taLog.appendText("PASS " + password + "\n" + response + "\n");
    }

    /**
     * Runs when the stat button is pushed
     * @param actionEvent Ignored
     */
    public void stat(ActionEvent actionEvent)
    {
        String response = popState.doStat();
        taLog.appendText("STAT" + "\n" + response + "\n");
    }

    /**
     * Runs when the list button is pushed
     * @param actionEvent Ignored
     */
    public void list(ActionEvent actionEvent) {
        String listNumber = tfList.getText();
        String response = popState.doList(listNumber);
        if(listNumber.isEmpty()){
            taLog.appendText("LIST" + "\n" + response + "\n");
        }
        else{
            taLog.appendText("LIST " + listNumber + "\n" + response + "\n");
        }

    }

    /**
     * Runs when the retrieve button is pressed
     * @param actionEvent Ignored
     */
    public void retr(ActionEvent actionEvent) {
        String retrNumber = tfRetr.getText();
        String response = popState.doRetr(retrNumber);
        taLog.appendText("RETR " + retrNumber + "\n" + response + "\n");
    }


    /**
     * Runs when the delete button is pressed
     * @param actionEvent Ignored
     */
    public void dele(ActionEvent actionEvent) {
        String Delete = tfDele.getText();
        String response = popState.doDele(Delete);
        taLog.appendText("DELE " + Delete + "\n" + response + "\n");
    }

    /**
     * Runs when the UIDL button is pressed
     * @param actionEvent Ignored
     */
    public void uidl(ActionEvent actionEvent) {
        String uidlNumber = tfUidl.getText();
        String response = popState.doUidl(uidlNumber);
        if(uidlNumber.isEmpty()){
            taLog.appendText("UIDL" + "\n" + response + "\n");
        }
        else{
            taLog.appendText("UIDL " + uidlNumber + "\n" + response + "\n");
        }

    }

    /**
     * Runs when the reset button is pressed
     * @param actionEvent Ignored
     */
    public void rset(ActionEvent actionEvent) {
        String response = popState.doRset();
        taLog.appendText("RSET" + "\n" + response + "\n");

    }

    /**
     * Runs when the reset button is pressed
     * @param actionEvent Ignored
     */
    public void quit(ActionEvent actionEvent) {
        String response = popState.doQuit();
        taLog.appendText("QUIT" + "\n" + response + "\n");
        bConnect.setText("Connect");
    }

    /**
     * Runs when the raw command button is pressed.
     * @param actionEvent Ignored
     */
    public void raw(ActionEvent actionEvent) {

        String raw = tfRaw.getText();
        String response = popState.doRaw(raw);
        if(raw.isEmpty()){
            taLog.appendText("RAW" + "\n" + response + "\n");
        }
        else{
            taLog.appendText("RAW " + raw + "\n" + response + "\n");
        }

    }
}
