package HolloMon_Network;

import HolloMon_Log.HolloLog;
import java.io.*;
import java.net.*;

public class HolloClient {

    private Socket holloMon_;
    private boolean isConnected_;
    public static BufferedReader holloIn_;
    public static PrintWriter holloOut_;

    private static HolloClient s_holloMon = new HolloClient();

    private HolloClient() {
        this.holloMon_ = CreateSocket();
        this.isConnected_ = CheckSocket();
        this.holloIn_ = holloResponse();
        this.holloOut_ = holloSend();
        HolloLog.Console(
            HolloLog.Level.INFO,
            "HollomonHollomon Setup Complete! \n ------------"
        );
    }

    public static HolloClient HolloInstance() {
        return s_holloMon;
    }

    public void Send(String message) {
        holloOut_.println(message);
    }

    public void Receive() {
        try {
            HolloLog.Console("| [Hollomon] ->", holloIn_.readLine());
        } catch (IOException e) {
            HolloLog.Console(
                HolloLog.Level.CRITICAL,
                "IOException Attempting To Read Response."
            );
        }
    }

    private BufferedReader holloResponse() {
        BufferedReader holloReader;

        try {
            HolloLog.Console(
                HolloLog.Level.INFO,
                "Creating Hollomon Input Gateway..."
            );
            holloReader = new BufferedReader(
                new InputStreamReader(holloMon_.getInputStream())
            );
        } catch (IOException e) {
            HolloLog.Console(
                HolloLog.Level.CRITICAL,
                "Unexpected Error Creating Input Gateway."
            );
            return null;
        }

        return holloReader;
    }

    private PrintWriter holloSend() {
        PrintWriter holloSender;

        try {
            HolloLog.Console(
                HolloLog.Level.INFO,
                "Creating Hollomon Output Gateway..."
            );
            holloSender = new PrintWriter(holloMon_.getOutputStream(), true);
        } catch (IOException e) {
            HolloLog.Console(
                HolloLog.Level.CRITICAL,
                "Unexpected Error Creating Output Gateway"
            );
            return null;
        }

        return holloSender;
    }

    public Socket GetSocket() {
        return holloMon_;
    }

    private Socket CreateSocket() {
        HolloLog.Console("--- Attempting Connection to Hollomon Server ---");
        Socket tmpConnection;

        try {
            tmpConnection = new Socket("netsrv.cim.rhul.ac.uk", 1812);
            HolloLog.Console(HolloLog.Level.INFO, "Connection Established.");
        } catch (IOException e) {
            HolloLog.Console(
                HolloLog.Level.CRITICAL,
                "Unable to establish connection. Closing Socket. \n"
            );
            return null;
        }

        return tmpConnection;
    }

    private boolean CheckSocket() {
        return holloMon_.isClosed() ? false : true;
    }
}
