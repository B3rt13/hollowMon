package HolloMon_Network;

import HolloMon_Log.HolloLog;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class HolloClient {

    private Socket holloMon_;
    public static BufferedReader holloIn;
    public static PrintWriter holloOut;

    private static HolloClient s_holloMon = new HolloClient();

    private HolloClient() {
        this.holloMon_ = CreateSocket();

        this.holloIn = HolloResponse();
        this.holloOut = HolloSend();

        HolloLog.Console(HolloLog.Level.INFO, "Hollomon Client Complete!");
        HolloLog.Console(HolloLog.Level.INFO);

        System.out.println("====================================================");
    }

    public static HolloClient HolloInstance() {
        return s_holloMon;
    }

    public static void Send(String message) {
        holloOut.println(message);
    }

    public static List<String> Receive() {
        try {
            List<String> resp = new ArrayList<>();
            String currentResp;

            while (true) {
                currentResp = holloIn.readLine();

                if (currentResp == null) {
                    HolloLog.Console(HolloLog.Level.WARNING, "Nothing Was Receieved From The Server");
                    break;
                }

                if (currentResp.equals("OK")) break;

                resp.add(currentResp);
            }

            return resp;
        } catch (IOException e) {
            HolloLog.Console(HolloLog.Level.CRITICAL, "IOException Attempting To Read Response.");
            return null;
        }
    }

    private BufferedReader HolloResponse() {
        BufferedReader holloReader;
        try {
            HolloLog.Console(HolloLog.Level.INFO, "Creating Hollomon Input Gateway...");
            holloReader = new BufferedReader(
                new InputStreamReader(holloMon_.getInputStream())
            );
        } catch (IOException e) {
            HolloLog.Console(HolloLog.Level.CRITICAL, "Unexpected Error Creating Input Gateway.");
            return null;
        }

        return holloReader;
    }

    private PrintWriter HolloSend() {
        PrintWriter holloSender;

        try {
            HolloLog.Console(HolloLog.Level.INFO, "Creating Hollomon Output Gateway...");
            HolloLog.Console(HolloLog.Level.INFO);
            holloSender = new PrintWriter(holloMon_.getOutputStream(), true);
        } catch (IOException e) {
            HolloLog.Console(HolloLog.Level.CRITICAL, "Unexpected Error Creating Output Gateway");
            return null;
        }

        return holloSender;
    }

    public Socket GetSocket() {
        return holloMon_;
    }

    private Socket CreateSocket() {
        HolloLog.Console("===== Attempting Connection to Hollomon Server =====");
        HolloLog.Console(HolloLog.Level.INFO);

        Socket tmpConnection;

        try {
            tmpConnection = new Socket("netsrv.cim.rhul.ac.uk", 1812);
            HolloLog.Console(HolloLog.Level.INFO, "Connection Established!");
            HolloLog.Console(HolloLog.Level.INFO);
        } catch (IOException e) {
            HolloLog.Console(HolloLog.Level.CRITICAL, "Unable to establish connection. Closing Socket.\n");
            tmpConnection = null;
            CloseSocket();
        }

        return tmpConnection;
    }


    public void CloseSocket() {
        try {
            this.holloMon_.close();
            HolloLog.Console(HolloLog.Level.INFO, "Socket Was Succesfully Closed!");

        } catch (IOException e) {
            HolloLog.Console(HolloLog.Level.CRITICAL, "Critical Error Occurred, aborting process. \n");
            System.exit(0);
        }
    }
}
