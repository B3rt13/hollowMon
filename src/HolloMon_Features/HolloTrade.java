package HolloMon_Features;

import HolloMon_Log.*;
import HolloMon_Network.*;

import HolloMon_Features.HolloCard;
import HolloMon_Features.HolloMenu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class HolloTrade
{

    public boolean Buy(int id) {

        HolloClient.Send("BUY" + id);

        boolean check = HolloClient.Receive().contains("ERROR");

        if(check) {
            HolloLog.Console(HolloLog.Level.HOLLOMON, "Card: ID: [", id, "] Was Not Bought!");
            return check;
        }

        return check;

    }

    public boolean Sell(int id, long price) {

        HolloClient.Send("SELL " + id + " " + price);
        boolean check = HolloClient.Receive().contains("ERROR");

        if(check) {
            HolloLog.Console(HolloLog.Level.HOLLOMON, "Card: ID: [", id, "] Was Not Listed!");
            return check;
        }

        HolloLog.Console(HolloLog.Level.HOLLOMON,"Card: ID: [", id, "] Was Listed! | Asking Price: ", price);
        return check;
    }


    public void AutoBuy(List<HolloCard> cards) {

        CardRank targetRank = GetRarity();
        long price = GetPrice();
        int bought = 0;

        List<HolloCard> filter = HolloCard.GetSetByRank(cards, targetRank);
        filter.removeIf(card -> card.GetLastPrice() > price);

        if(filter.isEmpty())
        {
            HolloLog.Console(HolloLog.Level.HOLLOMON, "No Cards Match These Arguments!");
        }

        for (HolloCard card : filter){

            int id = card.GetID();
            Buy(id);
            bought++;
        }

        HolloLog.Console(HolloLog.Level.HOLLOMON, "AutoBuyer Finished! Items Bought: [", bought, "]");
    }

    public void AutoSell(List<HolloCard> cards) {

        CardRank targetRank = GetRarity();
        long price = GetPrice();
        int listed = 0;

        List<HolloCard> filter = HolloCard.GetSetByRank(cards, targetRank);


        if(filter.isEmpty()) return;

        for (HolloCard card : filter){

            int id = card.GetID();
            Sell(id, price);
            listed++;
        }

        HolloLog.Console(HolloLog.Level.HOLLOMON, "AutoSeller Finished! Items Listed: [",listed,"]");

    }

    public CardRank GetRarity()
    {
        CardRank cardRank;


        HolloLog.Console(HolloLog.Level.HOLLOMON, "---------------------------------- [Auto Trade Setup] ----------------------------------------");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[1]: UNIQUE");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[2]: RARE");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[3]: UNCOMMON");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[4]: COMMON");

        int opt = HolloSetup.ReadInt("Enter Rarity [1-4]: ");

        switch (opt) {
            case 1: {
                cardRank = CardRank.UNIQUE;
                HolloLog.Console(HolloLog.Level.HOLLOMON, "You Have Selected: ", CardRank.UNIQUE);
                return cardRank;
            }
            case 2: {
                cardRank = CardRank.RARE;
                HolloLog.Console(HolloLog.Level.HOLLOMON, "You Have Selected: ", CardRank.RARE);
                return cardRank;
            }
            case 3: {
                cardRank = CardRank.UNCOMMON;
                HolloLog.Console(HolloLog.Level.HOLLOMON, "You Have Selected: ", CardRank.UNCOMMON);
                return cardRank;
            }
            case 4: {
                cardRank = CardRank.COMMON;
                HolloLog.Console(HolloLog.Level.HOLLOMON, "You Have Selected: ", CardRank.COMMON);
                return cardRank;
            }
            default: {
                System.out.print(HolloLog.Level.HOLLOMON + "Please Enter A Valid Rarity... ");
                return null;
            }
        }
    }

    public long GetBudget()
    {

        HolloLog.Console(HolloLog.Level.HOLLOMON, "--------------------------------------- Budget [Current Credits: ", HolloMenu.GetCredits(), "] ----------------------------------------------");

        long budget = HolloSetup.ReadLong("Enter Amount You Wish To Spend: ");

        HolloLog.Console(HolloLog.Level.HOLLOMON, "You've Selected: ", budget);


        return budget > HolloMenu.GetCredits() ? budget : 0;
    }

    public long GetPrice()
    {

        HolloLog.Console(HolloLog.Level.HOLLOMON, "--------------------------------------- Price [Current Credits: ", HolloMenu.GetCredits(), "] ----------------------------------------------");
        long price =  HolloSetup.ReadLong("Enter The Price You Wish To [BUY/SELL] at: ");


        return price > 0 ? price : 0;

    }


}
