package HolloMon_Features;

import HolloMon_Log.HolloLog;

import java.util.List;
import java.util.stream.Collectors;

enum CardRank {
    COMMON,
    UNCOMMON,
    RARE,
    UNIQUE,
}

class HolloCard {

    private int id;
    private String name;
    private CardRank rank;
    private long last_price;

    public HolloCard(int id, String name, CardRank rank, long last_price) {
        this.id = id;
        this.name = name;
        this.rank = rank;
        this.last_price = last_price;
    }

    public int GetID() {
        return this.id;
    }

    public String GetName() {
        return this.name;
    }

    public CardRank GetRank() {
        return this.rank;
    }

    public long GetLastPrice() {
        return last_price;
    }

    public static void ShowCard(HolloCard card) {

        System.out.println("\n\n-----------------------------------------------------------------");
        System.out.println("| Card Rank: " + card.GetRank() + "       |     Card Name: " + card.GetName());
        System.out.println("-----------------------------------------------------------------");
        System.out.println("| -> ID:  " + card.GetID());
        System.out.println("| -> Last Price:  " + card.GetLastPrice());
        System.out.println("-----------------------------------------------------------------\n\n");
    }

    public static void ShowSetByRank(List<HolloCard> cards, CardRank _filter)
    {
        List<HolloCard> filt = cards.stream()
        .filter(card -> card.GetRank().equals(_filter))
        .collect(Collectors.toList());

        int count = 0;



        System.out.println("Rank: [" + _filter + "]" + "    |   " + "Amount: [" + filt.size() + "]");
        System.out.println("-----------------------------------------------------------------------");
        if(filt.isEmpty()) {System.out.println("|"); return;};
        for(HolloCard card : filt)
        {
            System.out.println("| [" + count++ + "] ID: " + card.GetID() + "     |    " + "Last Sale Price: " + card.GetLastPrice());
        }

    }

    public static List<HolloCard> GetSetByRank(List<HolloCard> cards, CardRank _filter)
    {
        List<HolloCard> filt = cards.stream()
        .filter(card -> card.GetRank().equals(_filter))
        .collect(Collectors.toList());

        if(filt.isEmpty()) return null;

        return filt;
    }

};
