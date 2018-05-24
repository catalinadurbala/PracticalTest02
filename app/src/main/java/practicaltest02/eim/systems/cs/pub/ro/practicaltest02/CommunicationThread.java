package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by student on 24.05.2018.
 */

public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type!");
            String query = bufferedReader.readLine();
            if (query == null || query.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type!");
                return;
            }
            //parse query
            String params[] = query.split(",");
            if (params.length != 3) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Insufficient parameters!");
                return;
            }
            String result = null;
            if (params[0].equals("add")) {
                result = Integer.toString(Integer.parseInt(params[1]) + Integer.parseInt(params[2]));
            } else if (params[0].equals("mul")) {
                sleep(4000);
                result = Integer.toString(Integer.parseInt(params[1]) * Integer.parseInt(params[2]));
            }
            // get result
            printWriter.println(result);
            printWriter.flush();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }  catch (InterruptedException interruptedException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + interruptedException.getMessage());
            if (Constants.DEBUG) {
                interruptedException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
