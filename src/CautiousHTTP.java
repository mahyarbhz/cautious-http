import java.io.*;
import java.net.*;

public class CautiousHTTP {
    public static void main(String[] args) {
        final int PORT = 8080;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.printf("Listening on port %d\n", PORT);

            while (true) {
                Socket clienSocket = serverSocket.accept();
                System.out.printf("Client connected: %s\n", clienSocket.getInetAddress());
                handleRequest(clienSocket);
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static void handleRequest(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {
            String requestLine = in.readLine();
            System.out.println("Received request: " + requestLine);
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

            // that \r\n\r defines that it is the end of the headers and
            // the body is being sent as the rest of the response.
            String response = "HTTP/1.1 200 OK\r\n\r\nHello World!";
            out.write(response.getBytes());
            out.flush();
        } catch (IOException e) {
            System.err.println("Request handling error: " + e.getMessage());
        }
    }
}
