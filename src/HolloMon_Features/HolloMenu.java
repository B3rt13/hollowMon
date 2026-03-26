package HolloMon_Features;

import HolloMon_Log.*;
import HolloMon_Network.*;
import HolloMon_Features.HolloCard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HolloMenu {
    private HolloSetup m_connection;
    private List<HolloCard> m_cards;
    private List<HolloCard> av_cards;
    private int m_credits;

    public HolloMenu(HolloSetup holloSetup) {
        this.m_connection = holloSetup;
        this.m_cards = this.GetCards();
        System.out.println("\n\n");
        HolloLog.Console("| [HolloMon] -> ============== Welcome To HolloMon, you're signed in as [", this.m_connection.GetUsername(), "] ==============");
        this.DisplayMenu();
        HolloLog.Console("| [HolloMon] -> ======================================================================================");
    }

    public void DisplayMenu() {
        boolean menu = true;
        while (menu) {
            this.ShowArt();
            HolloLog.Console(HolloLog.Level.HOLLOMON, "--- Hollomon Options ---");
            HolloLog.Console(HolloLog.Level.HOLLOMON, "[1]: Show Cards");
            HolloLog.Console(HolloLog.Level.HOLLOMON, "[2]: Show Credits");
            HolloLog.Console(HolloLog.Level.HOLLOMON, "[3]: List Available");
            HolloLog.Console(HolloLog.Level.HOLLOMON, "[4]: Buy Card");
            HolloLog.Console(HolloLog.Level.HOLLOMON, "[5]: Sell Card");
            HolloLog.Console(HolloLog.Level.HOLLOMON, "[6]: Auto-Trade");
            HolloLog.Console(HolloLog.Level.HOLLOMON, "[7]: Exit\n");
            System.out.print(HolloLog.Level.HOLLOMON + "[!]: Enter Your Choice: ");
            int option = this.m_connection.read.nextInt();

            switch (option) {
                case 1: {
                    this.ShowCards();
                    break;
                }
                case 2: {
                    this.m_credits = GetCredits();
                    ShowCredits();
                    break;
                }
                case 3: {
                    this.av_cards = GetAvailable();
                    ShowAvailable();
                    break;
                }
                case 6: {
                    SetAutoTrade();
                    break;
                }
                case 7: {
                    menu = false;
                    break;
                }
            }
            HolloLog.Console(HolloLog.Level.HOLLOMON, "---------------------------------------------------------------------------------");
            HolloLog.Console(HolloLog.Level.HOLLOMON, "Please Enter A Valid Choice From [1-7]");
            HolloLog.Console(HolloLog.Level.HOLLOMON, "---------------------------------------------------------------------------------");
        }
    }

    public void ShowCards() {
        HolloLog.Console(HolloLog.Level.HOLLOMON, "------------------------- Your Cards [Rare -> Common] ---------------------------");
        for (HolloCard holloCard : this.m_cards) {
            HolloCard.ShowCard(holloCard);
        }
        HolloLog.Console(HolloLog.Level.HOLLOMON, "---------------------------------------------------------------------------------");
    }

    public void ShowCredits() {
        HolloLog.Console(HolloLog.Level.HOLLOMON, "--------------------------------- Your Credits ---------------------------------------");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "Your Current Credits -> ", GetCredits());
        HolloLog.Console(HolloLog.Level.HOLLOMON, "--------------------------------------------------------------------------------------");
    }

    public void ShowAvailable() {

        if (this.av_cards.isEmpty()) {
            return;
        }
        HolloLog.Console(HolloLog.Level.HOLLOMON, "------------------------- Available Cards [Rare -> Common] ---------------------------\n\n");
        HolloCard.ShowSetByRank(this.av_cards, CardRank.UNIQUE);
        HolloLog.Console(HolloLog.Level.HOLLOMON, "-------------------------------------\n\n");
        HolloCard.ShowSetByRank(this.av_cards, CardRank.RARE);
        HolloLog.Console(HolloLog.Level.HOLLOMON, "-------------------------------------\n\n");
        HolloCard.ShowSetByRank(this.av_cards, CardRank.UNCOMMON);
        HolloLog.Console(HolloLog.Level.HOLLOMON, "-------------------------------------\n\n");
        HolloCard.ShowSetByRank(this.av_cards, CardRank.COMMON);
        HolloLog.Console(HolloLog.Level.HOLLOMON, "-------------------------------------\n\n");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "--------------------------------------------------------------------------------------");
    }

    public List<HolloCard> GetCards() {

        List<String> resp = HolloClient.Receive();

        resp.removeIf(card -> card.equals("CARD"));

        List<HolloCard> cards = new ArrayList<HolloCard>();

        for (int i = 0; i < resp.size(); i += 4) {
            int id = Integer.parseInt(resp.get(i));
            String name = resp.get(i + 1);
            CardRank cardRank = CardRank.valueOf(resp.get(i + 2));
            long price = Long.parseLong(resp.get(i + 3));
            cards.add(new HolloCard(id, name, cardRank, price));
        }
        return SortCards(cards);
    }

    public int GetCredits() {
        HolloClient.Send("CREDITS");
        return Integer.parseInt(HolloClient.Receive().getFirst());
    }

    public List<HolloCard> GetAvailable() {
        HolloClient.Send("OFFERS");
        return this.GetCards();
    }

    private List<HolloCard> SortCards(List<HolloCard> list) {
        list.sort(Comparator.comparingInt(holloCard -> holloCard.GetRank().ordinal()));
        return list.reversed();
    }

    public void SetAutoTrade() {
        CardRank cardRank;
        int credits = this.GetCredits();


        HolloLog.Console(HolloLog.Level.HOLLOMON, "---------------------------------- Auto Trade Setup ----------------------------------------");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[1]: UNIQUE");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[2]: RARE");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[3]: UNCOMMON");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[4]: COMMON\n\n");
        System.out.print(String.valueOf((Object)HolloLog.Level.HOLLOMON) + "[HolloTrader] -> Enter Rarity [1-4]: ");

        int opt = this.m_connection.read.nextInt();

        switch (opt) {
            case 1: {
                cardRank = CardRank.UNIQUE;
                break;
            }
            case 2: {
                cardRank = CardRank.RARE;
                break;
            }
            case 3: {
                cardRank = CardRank.UNCOMMON;
                break;
            }
            case 4: {
                cardRank = CardRank.COMMON;
                break;
            }
            default: {
                System.out.print(HolloLog.Level.HOLLOMON + "[HolloTrader] -> Please Enter A Valid Rarity... ");
                return;
            }
        }
        HolloLog.Console(HolloLog.Level.HOLLOMON, "--------------------------------------- Budget [Current Credits: ", credits, "] ----------------------------------------------");
        System.out.print(HolloLog.Level.HOLLOMON + "[HolloTrader] -> Enter Amount You Wish To Spend: ");
        long budget = this.m_connection.read.nextInt();

        if (budget >= credits) {
            HolloLog.Console(HolloLog.Level.HOLLOMON, "Insufficient Funds.");
            return;
        }


        HolloLog.Console(HolloLog.Level.HOLLOMON, "--------------------------------------- Sniper [Current Credits: ", credits, "] ----------------------------------------------");
        System.out.print(HolloLog.Level.HOLLOMON + "[HolloTrader] -> Enter The Price You Wish To Snipe For: ");


        long price = this.m_connection.read.nextLong();

        this.AutoBuy(cardRank, price, budget);


        HolloLog.Console(HolloLog.Level.HOLLOMON, "--------------------------------------- [BUY / SELL] ----------------------------------------------");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[1]: BUY");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[2]: SELL\n\n");
    }

    public void AutoBuy(CardRank cardRank, long price, long budget) {

        int check_budget = 0;

        List<HolloCard> cards = HolloCard.GetSetByRank(GetAvailable(), cardRank);

        if(cards.isEmpty()) return;

        for(HolloCard card : cards) HolloCard.ShowCard(card);

        for (HolloCard card : cards) {
            int id = card.GetID();
            long c_price = card.GetLastPrice();

            if (!this.Buy(id)) {
                HolloLog.Console(HolloLog.Level.HOLLOMON, "Unable To AutoBuy Card with ID: ", id);
                break;
            }

            if (check_budget + c_price > budget) break;
            check_budget += c_price;

            HolloLog.Console(HolloLog.Level.HOLLOMON, "Item Bought! ID: [", id, "], Price: [", c_price, "]");
        }
        HolloLog.Console(HolloLog.Level.HOLLOMON, "Auto Buyer Finished!");
    }

    public boolean Buy(int id) {
        HolloClient.Send("BUY " + id);
        return HolloClient.Receive().contains("OK");
    }

    public void ShowArt()
       {
           HolloLog.Console("""
                   ,--,                                              ____
                 ,--.'|            ,--,    ,--,                    ,'  , `.
              ,--,  | :          ,--.'|  ,--.'|                 ,-+-,.' _ |
           ,---.'|  : '   ,---.  |  | :  |  | :     ,---.    ,-+-. ;   , ||   ,---.        ,---,
           |   | : _' |  '   ,'\\ :  : '  :  : '    '   ,'\\  ,--.'|'   |  ;|  '   ,'\\   ,-+-. /  |
           :   : |.'  | /   /   ||  ' |  |  ' |   /   /   ||   |  ,', |  ': /   /   | ,--.'|'   |
           |   ' '  ; :.   ; ,. :'  | |  '  | |  .   ; ,. :|   | /  | |  ||.   ; ,. :|   |  ,"' |
           '   |  .'. |'   | |: :|  | :  |  | :  '   | |: :'   | :  | :  |,'   | |: :|   | /  | |
           |   | :  | ''   | .; :'  : |__'  : |__'   | .; :;   . |  ; |--' '   | .; :|   | |  | |
           '   : |  : ;|   :    ||  | '.'|  | '.'|   :    ||   : |  | ,    |   :    ||   | |  |/
           |   | '  ,/  \\   \\  / ;  :    ;  :    ;\\   \\  / |   : '  |/      \\   \\  / |   | |--'
           ;   : ;--'    `----'  |  ,   /|  ,   /  `----'  ;   | |`-'        `----'  |   |/
           |   ,/                 ---`-'  ---`-'           |   ;/                    '---'
           '---'                                           '---'
           """); //https://patorjk.com/software/taag/#p=display&f=Graffiti&t=Type+Something+&x=none&v=4&h=4&w=80&we=false (had to replace weird characters throwing off print statement :( )
       }
}
