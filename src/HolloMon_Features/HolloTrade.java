package HolloMon_Features;

import HolloMon_Log.*;
import HolloMon_Network.*;
import HolloMon_Features.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


class HolloTrade
{

    // No constructor needed, will extend

    // === Public Methods ===

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


    public void AutoBuy(List<HolloCard> cards, int credits) {

        int bought = 0;

        CardRank targetRank = GetRarity();
        if(targetRank == null) return;

        long price = GetPrice(credits);
        if(targetRank == null) return;


        List<HolloCard> filter = HolloCard.GetSetByRank(cards, targetRank);
        filter.removeIf(card -> card.GetLastPrice() > price);

        if(filter.isEmpty()) {
            HolloLog.Console("No Cards Match These Arguments!");
            return;
        }

        for (HolloCard card : filter){

            int id = card.GetID();
            Buy(id);
            bought++;
        }

        HolloLog.Console(HolloLog.Level.HOLLOMON, "AutoBuyer Finished! Items Bought: [", bought, "]");
    }

    public void AutoSell(List<HolloCard> cards, int credits) {

        int listed = 0;

        CardRank targetRank = GetRarity();
        if(targetRank == null) return;

        long price = GetPrice(credits);
        if(price == 0L) return;

        List<HolloCard> filter = HolloCard.GetSetByRank(cards, targetRank);

        if(filter.isEmpty()) {
            HolloLog.Console("No Cards Match These Arguments!");
            return;
        }

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
                HolloLog.Console(HolloLog.Level.HOLLOMON, "Please Select A Valid Rarity.");
                return null;
            }
        }
    }

    public long GetBudget(int credits)
    {

        HolloLog.Console(HolloLog.Level.HOLLOMON, "--------------------------------------- Budget [Current Credits: ", credits, "] ----------------------------------------------");

        long budget = HolloSetup.ReadLong("Enter Amount You Wish To Spend: ");

        if(budget != -1L) return budget > credits ? budget : 0;


        HolloLog.Console(HolloLog.Level.HOLLOMON, "Invalid Budget Chosen.");

        return 0;

    }

    public long GetPrice(int credits)
    {

        HolloLog.Console(HolloLog.Level.HOLLOMON, "--------------------------------------- Price [Current Credits: ", credits, "] ----------------------------------------------");
        long price =  HolloSetup.ReadLong("Enter The Price You Wish To [BUY/SELL] at: ");

        if(price != -1L) return price > 0L ? price : 0L;

        HolloLog.Console(HolloLog.Level.HOLLOMON, "Invalid Price Chosen.");

        return 0;

    }


}
