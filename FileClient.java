import java.net.*;
import java.io.*;
import jatyc.lib.Typestate;
import jatyc.lib.Nullable;

@Typestate("FileClient")
public class FileClient {
  private @Nullable Socket socket;
  protected @Nullable OutputStream out;
  protected @Nullable BufferedReader in;
  protected @Nullable int lastByte;
  protected final int EOF = 0;

  public boolean start() {
    try {
      socket = new Socket("localhost", 1234);
      out = socket.getOutputStream();
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public void request(String filename) throws Exception {
    out.write("REQUEST\n".getBytes());
    out.write((filename + "\n").getBytes());
    lastByte = in.read();
  }

  public boolean eof() {
    return lastByte == EOF;
  }

  public int read() throws Exception {
    int tmp = lastByte;
    lastByte = in.read();
    return tmp;
  }

  public void close() throws Exception {
    out.write("CLOSE\n".getBytes());
    socket.close();
    out.close();
    in.close();
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.out.println("Usage: java FileClient <filename>");
      return;
    }
    
    String filename = args[0];
    if (filename == null) {
      System.out.println("Filename cannot be null!");
      return;
    }

    FileClient client = new FileClient();
    if (client.start()) {
      System.out.println("File client started!");
      client.request(filename);

      while (!client.eof()) {
        System.out.print((char) client.read());
      }

      client.close();
    } else {
      System.out.println("Could not start client!");
    }
  }
}
