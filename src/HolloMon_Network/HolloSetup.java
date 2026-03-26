package HolloMon_Network;

import HolloMon_Features.*;
import HolloMon_Log.*;

import java.util.Scanner;
import java.util.InputMismatchException;

public class HolloSetup {

    private HolloClient m_client;
    private String username;
    private String password;

    private static Scanner read = new Scanner(System.in);

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


    private static Scanner GetScanner()
    {
        return read;
    }

    public static void CloseScanner()
    {
        if(GetScanner() != null) GetScanner().close();
        HolloLog.Console(HolloLog.Level.INFO, "Scanner Was Successfully Closed!");
    }

    public static int ReadInt(String message)
    {
        System.out.print("| [Hollomon] -> " + message);

        try{
            return Integer.parseInt(GetScanner().nextLine());
        }
        catch(NumberFormatException e)
        {
            return -1;
        }
    }

    public static Long ReadLong(String message)
    {
        System.out.print("| [Hollomon] -> " + message);

        try{
            return Long.parseLong(GetScanner().nextLine());
        }
        catch(NumberFormatException e)
        {
            return -1L;
        }
    }



}
