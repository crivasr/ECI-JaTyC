import java.net.*;
import java.io.*;
import jatyc.lib.Typestate;
import jatyc.lib.Nullable;

@Typestate("FileServer")
public class FileServer {
  private @Nullable Socket socket;
  protected @Nullable OutputStream out;
  protected @Nullable BufferedReader in;
  protected @Nullable String lastFilename;
  protected final int EOF = 0;

  public boolean start(Socket s) {
    try {
      socket = s;
      out = socket.getOutputStream();
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean hasRequest() throws Exception {
    String command = in.readLine();
    if (command != null && command.equals("REQUEST")) {
      String filename = in.readLine();
      if (filename != null) {
        lastFilename = filename;
        return true;
      }
    }
    return false;
  }

  public void handleRequest() throws Exception {
    try {
      FileReader f = new FileReader(lastFilename);
      
      int lastChar = f.read();
      while(lastChar != -1) {
        out.write(lastChar);
        lastChar = f.read();
      }
      
      f.close();
    } catch (FileNotFoundException e) {
      // File not found
    }
    
    out.write(EOF);
  }

  public void close() throws Exception {
    socket.close();
    out.close();
    in.close();
  }

  public static void main(String[] args) throws Exception {
    ServerSocket serverSocket = new ServerSocket(1234);
    while (true) {
      new FileServerThread(serverSocket.accept()).start();
    }
  }
}