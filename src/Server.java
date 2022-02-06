
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
public class Server {


    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    //Constructor
    public Server() {
        try {
            server= new ServerSocket(7777);

            System.out.println("Server is ready to accept connection");
            System.out.println("waiting....");

            socket = server.accept();

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
                        System.out.println("Client terminated the chat");
                        socket.close();

                        break;
                    }
                    System.out.println("Client: " + message);

                }
            }
            catch (Exception e) {
//                e.printStackTrace();
                System.out.println("Connection closed");
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
//            e.printStackTrace();
            System.out.println("Connection closed");
        }
    };

    new Thread(r2).start();

    }



    public static void main(String[] args) {

        System.out.println("This is server");
        new Server();
    }
}
