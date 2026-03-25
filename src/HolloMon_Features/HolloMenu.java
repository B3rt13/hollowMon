package HolloMon_Features;

import java.util.ArrayList;
import HolloMon_Log.*;
import HolloMon_Network.*;

public class HolloMenu {

    private HolloSetup m_connection;

    public HolloMenu(HolloSetup connection) {

        m_connection = connection;

        HolloLog.Console(
            HolloLog.Level.INFO,
            "[HolloMon] -> ============== Welcome To HolloMon, you're signed in as [",m_connection.GetUsername(),"] =============="
        );
        DisplayCards();
        HolloLog.Console(
            HolloLog.Level.INFO,
            "[HolloMon] -> ==================================================="
        );
    }

    public void DisplayCards()
    {
        ArrayList<String> loginCards = HolloClient.Receive();
        for(String item : loginCards){

            switch(item)
            {


            }

        }
    }








}
