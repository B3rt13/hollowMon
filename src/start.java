import java.io.*;
import java.net.*;
import java.util.Scanner;

class hollowClient {

  private Socket hollowMon_;
  private boolean isConnected_;
  private BufferedReader hollowIn_;
  private PrintWriter hollowOut_;

  private static hollowClient s_hollowMon = new hollowClient();

  private hollowClient() {
    this.hollowMon_ = CreateSocket();
    this.isConnected_ = CheckSocket();
    this.hollowIn_ = HollowResponse();
    this.hollowOut_ = HollowSend();
    Log.Console(Log.Level.INFO, "hollowMon Setup Complete! \n ------------");
  }

  public static hollowClient hollowInstance() {
    return s_hollowMon;
  }

  public void Send(String message) {
    hollowOut_.println(message);
  }

  public void Receive() {
    try {
      Log.Console("| [hollowMon] ->", hollowIn_.readLine());
    } catch (IOException e) {
      Log.Console(Log.Level.CRITICAL, "IOException Attempting To Read Response.");
    }
  }

  private BufferedReader HollowResponse() {
    BufferedReader hollowReader;

    try {
      Log.Console(Log.Level.INFO, "Creating HollowMon Input Gateway...");
      hollowReader = new BufferedReader(new InputStreamReader(hollowMon_.getInputStream()));
    } catch (IOException e) {
      Log.Console(Log.Level.CRITICAL, "Unexpected Error Creating Input Gateway.");
      return null;
    }
    ;

    return hollowReader;
  }

  private PrintWriter HollowSend() {
    PrintWriter hollowSender;

    try {
      Log.Console(Log.Level.INFO, "Creating HollowMon Output Gateway...");
      hollowSender = new PrintWriter(hollowMon_.getOutputStream(), true);
    } catch (IOException e) {
      Log.Console(Log.Level.CRITICAL, "Unexpected Error Creating Output Gateway");
      return null;
    }

    return hollowSender;
  }

  public Socket GetSocket() {
    return hollowMon_;
  }

  private Socket CreateSocket() {

    Log.Console("--- Attempting Connection to HollowMon Server ---");
    Socket tmpConnection;

    try {
      tmpConnection = new Socket("netsrv.cim.rhul.ac.uk", 1812);
      Log.Console(Log.Level.INFO, "Connection Established.");
    } catch (IOException e) {
      Log.Console(Log.Level.CRITICAL, "Unable to establish connection. Closing Socket. \n");
      return null;
    }

    return tmpConnection;
  }

  private boolean CheckSocket() {
    return hollowMon_.isClosed() ? false : true;
  }


  public static void main(String[] args) {
          hollowSetup setup = new hollowSetup();
      }

};





class hollowSetup {

  private hollowClient hollowMon;
  private String username;
  private String password;
  private Scanner read = new Scanner(System.in);

  hollowSetup() {
    this.hollowMon = hollowClient.hollowInstance();
    this.username = GetUsername();
    this.password = GetPassword();
    Login();
  }

  private String GetUsername() {
    Log.Console("| [hollowMon] -> Enter Your Username");
    return read.nextLine();
  }

  private String GetPassword() {
    Log.Console("| [hollowMon] -> Enter Your Password");
    return read.nextLine();
  }

  private void Login() {
    hollowMon.Send(username);
    hollowMon.Send(password);
    hollowMon.Receive();
  }
}

}
