package HolloMon_Network;

import HolloMon_Log.*;
import java.util.Scanner;

public class HolloSetup {

    private HolloClient holloMon;
    private String username;
    private String password;
    private Scanner read = new Scanner(System.in);

    public HolloSetup() {
        this.holloMon = HolloClient.holloInstance();
        this.username = GetUsername();
        this.password = GetPassword();
        Login();
    }

    private String GetUsername() {
        HolloLog.Console("| [Hollomon] -> Enter Your Username");
        return read.nextLine();
    }

    private String GetPassword() {
        HolloLog.Console("| [Hollomon] -> Enter Your Password");
        return read.nextLine();
    }

    private void Login() {
        holloMon.Send(username);
        holloMon.Send(password);
        holloMon.Receive();
    }
}
