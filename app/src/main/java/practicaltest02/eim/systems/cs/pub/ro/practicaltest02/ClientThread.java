package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by student on 24.05.2018.
 */

public class ClientThread extends Thread {

    private String address;
    private int port;
    private String query;
    private TextView resultTextView;

    private Socket socket;

    public ClientThread(String address, int port, String query, TextView resultTextView) {
        this.address = address;
        this.port = port;
        this.query = query;
        this.resultTextView = resultTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            printWriter.println(query);
            printWriter.flush();
            String result;
            //String prevResults = "";
            while ((result = bufferedReader.readLine()) != null) {
                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
                final String displayedResults = result;
                //prevResults = prevResults + ", " + result;
                //Log.e(Constants.TAG, prevResults);
                resultTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        resultTextView.setText(displayedResults);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
