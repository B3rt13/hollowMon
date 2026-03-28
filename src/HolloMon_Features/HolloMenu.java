package HolloMon_Features;

import HolloMon_Log.*;
import HolloMon_Network.*;
import HolloMon_Features.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



public class HolloMenu extends HolloTrade {

    private HolloSetup m_connection;
    private List<HolloCard> m_cards;
    private List<HolloCard> av_cards;
    private int m_credits;

    public HolloMenu(HolloSetup holloSetup) {
        this.m_connection = holloSetup;
        this.m_cards = GetCards();
        System.out.println("\n\n");
        HolloLog.Console("| [HolloMon] -> ============== Welcome To HolloMon, you're signed in as [", this.m_connection.GetUsername(), "] ==============");
        this.DisplayMenu();
        HolloLog.Console("| [HolloMon] -> ======================================================================================");
    }

    // === Public Methods ===

    public void DisplayMenu() {
        boolean menu = true;
        while (menu) {

            this.ShowArt();
            this.ShowOptions();

            int option = HolloSetup.ReadInt("\n| [Hollomon] ->" + " [!] Enter Your Choice [1-7]: ");

            switch (option) {
                case 1: {
                    m_cards = GetMyCards();
                    ShowCards();
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
                case 4: {
                    BuyChoice();
                    break;
                }

                case 5: {
                    SellChoice();
                    break;
                }

                case 6: {
                    StartAutoTrade();
                    break;
                }
                case 7: {
                    HolloLog.Console("| [HolloMon] -> =============================== I Hope You Enjoyed My Variation Of Hollomon! ======================================");
                    menu = false;
                    HolloClient.HolloInstance().CloseSocket();
                    HolloClient.HolloInstance().CloseBuffers();
                    HolloSetup.CloseScanner();

                    break;
                }

                default:
                    HolloLog.Console(HolloLog.Level.HOLLOMON, "---------------------------------------------------------------------------------");
                    HolloLog.Console(HolloLog.Level.HOLLOMON, "Please Enter A Valid Choice From [1-7]");
                    HolloLog.Console(HolloLog.Level.HOLLOMON, "---------------------------------------------------------------------------------");

            }
        }
    }

    public void ShowOptions()
    {
        HolloLog.Console(HolloLog.Level.HOLLOMON, "--- Hollomon Options ---");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[1]: Show Cards");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[2]: Show Credits");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[3]: List Available");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[4]: Buy Card");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[5]: Sell Card");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[6]: Auto-Trade");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[7]: Exit");
    }


    public List<HolloCard> GetMyCards() {
        HolloClient.Send("CARDS");
        return GetCards();
    }

    public int GetCredits() {
        HolloClient.Send("CREDITS");
        return Integer.parseInt(HolloClient.Receive().getFirst());
    }

    public List<HolloCard> GetAvailable() {
        HolloClient.Send("OFFERS");
        return GetCards();
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

    public void BuyChoice()
    {
        HolloLog.Console(HolloLog.Level.HOLLOMON, "------------------------- Buy Card ---------------------------");

        int id = HolloSetup.ReadInt("| [Hollomon] ->" + "  Enter ID: ");

        boolean check = false;

        List<HolloCard> available = GetAvailable();

        for(HolloCard card : available)
        {
            if(card.GetID() == id) check = true;
        }

        if(!check){
            HolloLog.Console(HolloLog.Level.HOLLOMON, "Card: ID:[", id, "] Was Not Found!");
            return;
        }

        Buy(id);

    }


    public void SellChoice()
    {

        HolloLog.Console(HolloLog.Level.HOLLOMON, "------------------------- Sell Card ---------------------------");

        int id = HolloSetup.ReadInt("Enter ID: ");

        long price = HolloSetup.ReadLong("Enter Price To Sell At: ");

        boolean check = false;

        List<HolloCard> mycards = GetMyCards();

        for(HolloCard card : mycards)
        {
            if(card.GetID() == id) check = true;
        }
        if(!check){
            HolloLog.Console(HolloLog.Level.HOLLOMON, "Card: ID:[", id, "] Was Not Found!");
            return;
        }

        Sell(id, price);
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


    public void StartAutoTrade()
    {
        HolloLog.Console("\n\n");

        HolloLog.Console(HolloLog.Level.HOLLOMON, "--------------------------------------- [BUY / SELL] ----------------------------------------------");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[1]: BUY");
        HolloLog.Console(HolloLog.Level.HOLLOMON, "[2]: SELL");
        int opt = HolloSetup.ReadInt("Enter Option For AutoTrader [1-2]: ");

        HolloLog.Console("\n\n");

        List<HolloCard> chosenCards;

        switch(opt)
        {
            case 1:
                chosenCards = GetAvailable();
                AutoBuy(chosenCards, GetCredits());
                break;

            case 2:
                chosenCards = GetMyCards();
                AutoSell(chosenCards, GetCredits());
                break;

            default:
                System.out.print(HolloLog.Level.HOLLOMON + "[HolloTrader] -> Invalid Option Chosen. ");
                return;
        }



    }

    // === Private Methods + Ugly Show Art Method... ===

    private List<HolloCard> SortCards(List<HolloCard> list) {
        list.sort(Comparator.comparingInt(holloCard -> holloCard.GetRank().ordinal()));
        return list.reversed();
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
