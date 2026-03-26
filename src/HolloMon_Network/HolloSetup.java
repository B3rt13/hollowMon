package HolloMon_Network;

import HolloMon_Features.*;
import HolloMon_Log.*;
import java.util.Scanner;

public class HolloSetup {

    private HolloClient m_client;
    private String username;
    private String password;

    public Scanner read = new Scanner(System.in);

    public HolloSetup() {

        this.m_client = HolloClient.HolloInstance();
        this.username = SetUsername();
        this.password = SetPassword();

        System.out.println("| \n===================================================");

        if(!Login())
        {
            HolloLog.Console("[HolloMon] -> Unable To Authenticate User: [", GetUsername(), "] Please ensure you check your details.");
            m_client.CloseSocket();
            return;
        }

        HolloMenu Create = new HolloMenu(this);
    }

    private String SetUsername() {

        System.out.println("|");
        System.out.print("| [Hollomon] -> Enter Your Username: ");

        return read.nextLine();
    }

    private String SetPassword() {

        System.out.println("|");

        System.out.print("| [Hollomon] -> Enter Your Password: ");

        return read.nextLine();
    }

    public String GetUsername() {
        return username;
    }

    private boolean Login() {

        HolloClient.Send(username);
        HolloClient.Send(password);

        try{

            String resp = m_client.holloIn.readLine();

            return resp.contains("No such user") ? false : true;
        }
        catch(Exception e)
        {
            HolloLog.Console(HolloLog.Level.CRITICAL, "[Hollomon] -> Error Occured. Closing Socket.");

            return false;
        }
    }
}
