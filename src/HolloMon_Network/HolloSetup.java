package HolloMon_Network;

import HolloMon_Log.*;
import HolloMon_Features.*;
import java.util.Scanner;

public class HolloSetup {

    private HolloClient m_holloMon;
    private HolloMenu m_holloMenu
    private String username;
    private String password;
    private Scanner read = new Scanner(System.in);

    public HolloSetup() {
        this.holloMon = HolloClient.HolloInstance();
        this.username = GetUsername();
        this.password = GetPassword();
        Login();
        this->m_holloMenu = new HollowMenu();
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
