import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    Socket socket;

    BufferedReader br;
    PrintWriter out;

    public Client() {

        try{
            System.out.println("sending request to server");
        socket = new Socket("127.0.0.1",7777);
            System.out.println("Connection done");

            br= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startReading() {
        //thread- it will be reading data

        Runnable r1= ()->{

            System.out.println("reader started");

            try {
                while (true) {

                    String message = br.readLine();
                    if (message.equals("exit")) {
                        System.out.println("Sever terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Server: " + message);
                }
            }
            catch (Exception e) {
                System.out.println("connection closed");
            }
        };

        new Thread(r1).start();
    }

    public void startWriting() {
        //thread- It will take data form user and send it to the client
        Runnable r2 = ()->{

            System.out.println("writer started...");
            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("exit")) {
                        socket.close();
                        break;
                    }

                }
            }
            catch (Exception e) {

                System.out.println("Connection closed");
            }
        };

        new Thread(r2).start();

    }

    public static void main(String[] args) {
        System.out.println("This is client");
        new Client();
    }
}
