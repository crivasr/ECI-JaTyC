import java.net.*;
import java.io.*;
import jatyc.lib.Typestate;
import jatyc.lib.Nullable;

@Typestate("FileClient2")
public class FileClient2 extends FileClient {
    public String readLine() throws Exception {
        StringBuilder sb = new StringBuilder();

        while (lastByte != '\n' && lastByte != EOF) {
            sb.append((char) lastByte);
            lastByte = in.read();
        }

        if (lastByte == '\n') lastByte = in.read();

        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java FileClient2 <filename>");
            return;
        }
        
        String filename = args[0];
        if (filename == null) {
            System.out.println("Filename cannot be null!");
            return;
        }
        
        FileClient2 client = new FileClient2();
        if (client.start()) {
            System.out.println("File client started!");
            client.request(filename);

            while (!client.eof()) {
                String line = client.readLine();
                System.out.println(line);
            }

            client.close();
        } else {
            System.out.println("Could not start client!");
        }
    }
}
