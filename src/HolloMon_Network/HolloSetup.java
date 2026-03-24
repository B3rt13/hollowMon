package HolloMon_Network;

import HolloMon_Features.*;
import HolloMon_Log.*;
import java.util.Scanner;

public class HolloSetup {

    private HolloClient m_client;
    private HolloMenu m_menu;
    private String username;
    private String password;
    private Scanner read = new Scanner(System.in);

    public HolloSetup() {
        this.m_client = HolloClient.HolloInstance();
        this.username = SetUsername();
        this.password = SetPassword();
        Login();
        this.m_menu = new HolloMenu(this);
    }

    private String SetUsername() {
        HolloLog.Console("| [Hollomon] -> Enter Your Username");
        return read.nextLine();
    }

    private String SetPassword() {
        HolloLog.Console("| [Hollomon] -> Enter Your Password");
        return read.nextLine();
    }

    public String GetUsername() {
        return username;
    }

    private void Login() {
        HolloClient.Send(username);
        HolloClient.Send(password);
        String resp;

        try{
            resp = HolloClient.holloIn.readLine();
        }
        catch(Exception e)
        {
            HolloLog.Console(HolloLog.Level.CRITICAL, "[Hollomon] -> Unable To authenticate. Closing Socket.");
            m_client.CloseSocket();
        }
    }
}
