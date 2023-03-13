package remote;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        boolean run = true;

        ServerSocket serverSocket = new ServerSocket(8000);
        System.out.println("Server started");
        List<Thread> threads = new LinkedList<>();
        while (run) {
            Thread e = new Thread(new ClientHandler(serverSocket.accept(), "Thread" + threads.size()));
            System.out.println("Client connected!");
            threads.add(e);
            e.start();
        }

        threads.forEach(Thread::interrupt);
        serverSocket.close();
    }
}
