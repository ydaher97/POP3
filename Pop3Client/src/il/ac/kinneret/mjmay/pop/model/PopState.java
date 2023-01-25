package il.ac.kinneret.mjmay.pop.model;

import com.sun.webkit.SimpleSharedBufferInputStream;

import java.io.*;
import java.net.Socket;

public class PopState {

    private Socket clientSocket;
    PrintWriter pwOut;
    BufferedReader brIn;

    public PopState (String serverName, int port)
    {
        try {
            clientSocket = new Socket(serverName, port);
            pwOut = new PrintWriter(clientSocket.getOutputStream());
            brIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            clientSocket = null;
            pwOut = null;
            brIn = null;
            return;
        }
    }

    /**
     * Returns the input stream associated with the connection to enable the input to be printed to the log.  NULL if the connection is not available.
     * @return The input stream associated with the connection to the server
     */
    public InputStream getConnectionInputStream() {
        if (isConnected())
        {
            try {
                return clientSocket.getInputStream();
            } catch (IOException e) {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
    public boolean isConnected()
    {
        return (clientSocket != null && !clientSocket.isClosed());
    }

    public boolean close()
    {
        try {
            pwOut.println("QUIT");
            pwOut.flush();
            clientSocket.close();
            pwOut = null;
            brIn = null;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Do the USER command
     * @param username The user name to send
     * @return The command sent and the result from the server
     */
    public String doUser(String username) {
        if (isConnected()) {
            pwOut.println("USER " + username);
            pwOut.flush();
            String response;
            synchronized (SharedData.getSB())
            {
                response=SharedData.getSB().toString();
            }
            return response;
        }
        return "NO";
    }


    /**
     * Do the PASS command
     * @param password The user password to send
     * @return The command sent and the result from the server
     */
    public String doPass(String password) {
        if (isConnected()) {
            pwOut.println("PASS " + password);
            pwOut.flush();
            String response;
            synchronized (SharedData.getSB())
            {
                response=SharedData.getSB().toString();
            }
            return response;
        }
       return "NO";
    }

    public String doStat() {
        if (isConnected()) {
            pwOut.println("STAT");
            pwOut.flush();
            String response;
            synchronized (SharedData.getSB())
            {
                response=SharedData.getSB().toString();
            }
            return response;
        }
        return "NO";
    }


    public String doList(String listNumber) {
        if (isConnected()) {
            if (listNumber == null || listNumber.isEmpty()) {
                pwOut.println("LIST");
            } else {
                pwOut.println("LIST " + listNumber);
            }
            pwOut.flush();
            String response;
            synchronized (SharedData.getSB())
            {
                response=SharedData.getSB().toString();
            }
            return response;
        }
        return "NO";
    }




    public String doRetr(String retrNumber)
    {
        if (isConnected()) {
            pwOut.println("RETR " + retrNumber);
            pwOut.flush();
            String response;
            synchronized (SharedData.getSB())
            {
                response=SharedData.getSB().toString();
            }
            return response;
        }
        return "NO";
    }


    public String doDele(String deleNumber) {
        if (isConnected()) {
            pwOut.println("DELE " + deleNumber);
            pwOut.flush();
            String response;
            synchronized (SharedData.getSB())
            {
                response=SharedData.getSB().toString();
            }
            return response;
        }
        return "NO";
    }


    public String doUidl(String uidlNumber) {
        if (isConnected()) {
            pwOut.println("uidl " + uidlNumber);
            pwOut.flush();
            String response;
            synchronized (SharedData.getSB())
            {
                response=SharedData.getSB().toString();
            }
            return response;
        }
        return "NO";
    }

    public String doRset() {
        if(isConnected()) {
            pwOut.println("RSET");
            pwOut.flush();
            String response;
            synchronized (SharedData.getSB())
            {
                response=SharedData.getSB().toString();
            }
            return response;
        }
        return "NO";
        }


    public String doQuit() {
        if (isConnected())
            try {
                pwOut.println("QUIT");
                pwOut.flush();
                String response;
                synchronized (SharedData.getSB())
                {
                    response=SharedData.getSB().toString();
                }
                clientSocket.close();
                return response;
            } catch (IOException e) {
                e.printStackTrace();
                return "Error sending QUIT command";
            }
    return "NO";
    }


    public String doRaw(String rawCommand) {
        if (isConnected()) {
            pwOut.println(rawCommand);
            pwOut.flush();
            String response;
            synchronized (SharedData.getSB())
            {
                response=SharedData.getSB().toString();
            }
            return response;
        }
    return "NO";
    }

}
