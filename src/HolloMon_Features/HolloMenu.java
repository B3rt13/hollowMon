package HolloMon_Features;

import HolloMon_Log.*;
import HolloMon_Network.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class HolloMenu {

    private HolloSetup m_connection;
    private List<HolloCard> m_cards;

    public HolloMenu(HolloSetup connection) {
        m_connection = connection;

        HolloLog.Console(
            HolloLog.Level.INFO,
            "[HolloMon] -> ============== Welcome To HolloMon, you're signed in as [",
            m_connection.GetUsername(),
            "] =============="
        );
        DisplayCards();
        HolloLog.Console(
            HolloLog.Level.INFO,
            "[HolloMon] -> ==================================================="
        );
    }

    public <T> void DisplayCards() {
        List<T> loginCards = HolloClient.Receive();
        loginCards
            .stream()
            .filter(item -> !item.equals("CARD"))
            .collect(Collectors.toList());

        for (int i = 0; i < loginCards.size(); i += 4) {
            int id = (int) loginCards.get(i);
            String name = (String) loginCards.get(i + 1);
            CardRank rank = (CardRank) loginCards.get(i + 2);
            int last_sale = (int) loginCards.get(i + 3);

            m_cards.add(new HolloCard(id, name, rank, last_sale));
        }

        for (HolloCard item : m_cards) item.toString();
    }
}
