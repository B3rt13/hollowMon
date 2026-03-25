package HolloMon_Features;

import HolloMon_Log.HolloLog;

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
    private int last_price;

    public HolloCard(int id, String name, CardRank rank, int last_price) {
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

    public int GetLastPrice() {
        return last_price;
    }

    @Override
    public String toString() {
        Hollolog.Console(
            HolloLog.Level.INFO,
            "--------------------------------------------------------------"
        );
        Hollolog.Console(
            HolloLog.Level.INFO,
            "| Card Rank: ",
            GetRank(),
            "       |     Card Name: ",
            GetID(),
            "|"
        );
        Hollolog.Console(
            HolloLog.Level.INFO,
            "------------------------------------------------------------"
        );
        Hollolog.Console(
            HolloLog.Level.INFO,
            "| -> ID:  ",
            GetID(),
            "                                  |"
        );
        Hollolog.Console(
            HolloLog.Level.INFO,
            "| -> Last Price:  ",
            GetRank(),
            "                            |"
        );
        Hollolog.Console(
            HolloLog.Level.INFO,
            "--------------------------------------------------------------"
        );
    }
}
