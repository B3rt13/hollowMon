package HolloMon_Network;
import HolloMon_Log.HolloLog;

import java.io.*;
import java.net.*;

import java.util.ArrayList;
import java.util.List;



public class HolloClient {

    private Socket hollomon;
    public static BufferedReader hollomonIn;
    public static PrintWriter hollomonOut;

    private static HolloClient s_hollomon = new HolloClient();

    private HolloClient() {

        this.hollomon = CreateSocket();
        this.hollomonIn = HolloResponse();
        this.hollomonOut = HolloSend();

        HolloLog.Console(HolloLog.Level.INFO, "Hollomon Client Complete!");
        HolloLog.Console(HolloLog.Level.INFO);

        System.out.println("====================================================");
    }

    // ==== Public Methods ===

    public static HolloClient HolloInstance() {
        return s_hollomon;
    }

    public static void Send(String message) {
        hollomonOut.println(message);
    }

    public static List<String> Receive() {
        try {
            List<String> resp = new ArrayList<>();
            String currentResp;

            while (true) {
                currentResp = hollomonIn.readLine();

                if (currentResp == null) break;

                if (currentResp.equals("OK")) break;

                resp.add(currentResp);
            }

            return resp;
        } catch (IOException e) {
            HolloLog.Console(HolloLog.Level.CRITICAL, "IOException Attempting To Read Response.");
            return null;
        }
    }

    public void CloseSocket() {
        try {
            this.hollomon.close();
            HolloLog.Console(HolloLog.Level.INFO, "Socket Was Succesfully Closed!");

        } catch (IOException e) {
            HolloLog.Console(HolloLog.Level.CRITICAL, "Critical Error Occurred, aborting process. \n");
            System.exit(0);
        }
    }

    public void CloseBuffers()
    {
        try
        {
            hollomonIn.close();
            hollomonOut.close();
            HolloLog.Console(HolloLog.Level.INFO, "Buffers Were Succesfully Closed!");
        }
        catch (IOException e)
        {
            HolloLog.Console(HolloLog.Level.CRITICAL, "Unable To Close Buffers.");
            System.exit(0);
        }
    }



    // ==== Private Methods ===


    private BufferedReader HolloResponse() {
        BufferedReader holloReader;
        try {
            HolloLog.Console(HolloLog.Level.INFO, "Creating Hollomon Input Gateway...");
            holloReader = new BufferedReader(
                new InputStreamReader(hollomon.getInputStream())
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
            holloSender = new PrintWriter(hollomon.getOutputStream(), true);
        } catch (IOException e) {
            HolloLog.Console(HolloLog.Level.CRITICAL, "Unexpected Error Creating Output Gateway");
            return null;
        }

        return holloSender;
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

}
