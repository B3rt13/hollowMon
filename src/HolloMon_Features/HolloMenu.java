import HolloMon_Network.*;
import HolloMon_Log.*;

public class HolloMenu  {

    private HollowClient m_Instance;


    HollowMenu()
    {
        this->m_Instance = HollowClient.Instance();
        HolloLog.Console(Level.INFO, "[HolloMon] -> ======================");
        HolloLog.Console(Level.INFO, "[HolloMon] -> | Cards: ");
        HolloLog.Console(Level.INFO, "[HolloMon] -> | ", m_Instance.Receive());
        HolloLog.Console(Level.INFO, "[HolloMon] -> ======================");
    }




}
