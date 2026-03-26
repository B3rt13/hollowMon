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
        HolloClient.Send("BUY " + id);
        return HolloClient.Receive().contains("OK");
    }

    public boolean Sell(int id, long price) {
        HolloClient.Send("SELL " + id + " " + price);
        return HolloClient.Receive().contains("OK");
    }


    public void AutoBuy(List<HolloCard> cards) {

        CardRank targetRank = GetRarity();
        long targetBudget = GetBudget();
        long price = GetPrice();

        int check_budget = 0;

        List<HolloCard> filter = HolloCard.GetSetByRank(cards, targetRank);

        if(cards.isEmpty()) return;

        for(HolloCard card : filter) HolloCard.ShowCard(card);

        for (HolloCard card : filter) {
            int id = card.GetID();
            long c_price = card.GetLastPrice();

            if (!this.Buy(id)) {
                HolloLog.Console(HolloLog.Level.HOLLOMON, "Unable To AutoBuy Card with ID: ", id);
                break;
            }

            if (check_budget + c_price > targetBudget) break;
            check_budget += c_price;

            HolloLog.Console(HolloLog.Level.HOLLOMON, "Item Bought! ID: [", id, "], Price: [", c_price, "]");
        }
        HolloLog.Console(HolloLog.Level.HOLLOMON, "Auto Buyer Finished!");
    }

    public void AutoSell(List<HolloCard> cards) {

        CardRank targetRank = GetRarity();
        long price = GetPrice();

        List<HolloCard> filter = HolloCard.GetSetByRank(cards, targetRank);


        if(filter.isEmpty()) return;

        for (HolloCard card : filter){

            int id = card.GetID();

            if(!Sell(id, price)){
                HolloLog.Console(HolloLog.Level.HOLLOMON, "Unable To AutoBuy Card with ID: ", id);
                break;
            }

            HolloLog.Console(HolloLog.Level.HOLLOMON, "Auto Seller Finished!");
        }
    }

    public CardRank GetRarity()
    {
        CardRank cardRank;

        HolloLog.Console(HolloLog.Level.HOLLOMON, "---------------------------------- Auto Trade Setup ----------------------------------------");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[1]: UNIQUE");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[2]: RARE");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[3]: UNCOMMON");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[4]: COMMON\n\n");
        System.out.print(HolloLog.Level.HOLLOMON + "[HolloTrader] -> Enter Rarity [1-4]: ");

        int opt = HolloSetup.read.nextInt();

        switch (opt) {
            case 1: {
                cardRank = CardRank.UNIQUE;
                HolloLog.Console(HolloLog.Level.HOLLOMON, "[HolloTrader] -> You Have Selected: ", CardRank.UNIQUE);
                break;
            }
            case 2: {
                cardRank = CardRank.RARE;
                HolloLog.Console(HolloLog.Level.HOLLOMON, "[HolloTrader] -> You Have Selected: ", CardRank.RARE);
                break;
            }
            case 3: {
                cardRank = CardRank.UNCOMMON;
                HolloLog.Console(HolloLog.Level.HOLLOMON, "[HolloTrader] -> You Have Selected: ", CardRank.UNCOMMON);
                break;
            }
            case 4: {
                cardRank = CardRank.COMMON;
                HolloLog.Console(HolloLog.Level.HOLLOMON, "[HolloTrader] -> You Have Selected: ", CardRank.COMMON);
                break;
            }
            default: {
                System.out.print(HolloLog.Level.HOLLOMON + "[HolloTrader] -> Please Enter A Valid Rarity... ");
                return null;
            }

            return cardRank;

        }
    }

    public long GetBudget()
    {
        HolloLog.Console(HolloLog.Level.HOLLOMON, "--------------------------------------- Budget [Current Credits: ", HolloMenu.GetCredits(), "] ----------------------------------------------");
        System.out.print(HolloLog.Level.HOLLOMON + "[HolloTrader] -> Enter Amount You Wish To Spend: ");

        long budget =  HolloSetup.read.nextLong();

        HolloLog.Console(HolloLog.Level.HOLLOMON + "[HolloTrader] -> You've Selected: ", budget);


        return budget >= HolloMenu.GetCredits() ? budget : 0;
    }

    public long GetPrice()
    {
        HolloLog.Console(HolloLog.Level.HOLLOMON, "--------------------------------------- Sniper [Current Credits: ", HolloMenu.GetCredits(), "] ----------------------------------------------");
        System.out.print(HolloLog.Level.HOLLOMON + "[HolloTrader] -> Enter The Price You Wish To Snipe For: ");

        long price =  HolloSetup.read.nextLong();

        return price > 0 ? price : 0;

    }


}
