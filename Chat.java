import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.HashMap;

public class Chat 
{
  public static void main(String[] args)
  {
    int portNumber = ParseArgs(args);
    
    try
    {
      if(args.length==2 && args[1].equals("server"))
      {
        System.out.println("Sono il server");
        ServerSocket server = new ServerSocket(portNumber);
  
        while(true)
        {
          Socket endpoint = server.accept();
    
          DoChat(endpoint, true);
    
          endpoint.close();
        }
      }
      else
      {
        System.out.println("Sono il client");
        Socket endpoint = new Socket("localhost", portNumber);

        DoChat(endpoint, false);
      }
    }
    catch(IOException e)
    {
      System.err.println(e.getMessage());
    }
  }

  public static void DoChat(Socket endpoint, boolean server) throws IOException
  {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    BufferedReader reader = new BufferedReader(
      new InputStreamReader(endpoint.getInputStream()));

    PrintWriter writer = new PrintWriter(endpoint.getOutputStream());

    String message;
    if(server == false)
    {
      message = in.readLine();
      writer.println(message);
      writer.flush();
    }

    while((message = reader.readLine()).equals("quit")==false)
    {
      System.out.println("Remoto: "+message);
      System.out.print("Locale: ");
      message = in.readLine();
      writer.println(message);
      writer.flush();
    }
  }

  public static int ParseArgs(String[] args)
  {
    try{
      return Integer.parseInt(args[0]);  
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      System.err.println(e.getMessage());
      return 9000;
    }
    catch(NumberFormatException e)
    {
      System.err.println(e.getMessage());
      return 9000;
    }
  }
}