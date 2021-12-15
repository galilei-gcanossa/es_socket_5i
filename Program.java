import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.HashMap;

public class Program 
{
  public static void main(String[] args)
  {
    int portNumber = ParseArgs(args);
    
    try
    {
      ServerSocket server = new ServerSocket(portNumber);
  
      while(true)
      {
        Socket endpoint = server.accept();
  
        BufferedReader reader = new BufferedReader(
          new InputStreamReader(endpoint.getInputStream()));
  
        String tmp;
        String request = "";
        while((tmp = reader.readLine()).length() > 0)
        {
          request+=tmp+"\n";
        }

        String firstLine = request.split("\n")[0];
        System.out.println(firstLine);
        
        //modifica per domanda Ganzarolli
        HashMap<String,String> query = ParseQuery(request);
  
        PrintWriter writer = new PrintWriter(endpoint.getOutputStream());
  
        String response = "Ciao {nome}";

        response = response
          .replace("{nome}", query.get("nome") == null ? "mondo" : query.get("nome"));
  
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: text/html");
        writer.println("Content-Length: "+response.length());
        writer.println();
        writer.println(response);
  
        writer.flush();
  
        endpoint.close();
      }
    }
    catch(IOException e)
    {
      System.err.println(e.getMessage());
    }
  }

  public static HashMap<String, String> ParseQuery(String request)
  {
    String firstLine = request.split("\n")[0];
    
    HashMap<String, String> result = new HashMap<String, String>();
    if(firstLine.contains("?"))
    {
      for (String keyValue : firstLine.split(" ")[1].split("\\?")[1].split("&"))
      {
        String[] kv = keyValue.split("=");
        result.put(kv[0], kv[1]);
      }
    }
    
    return result;
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