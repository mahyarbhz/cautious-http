import java.io.*;
import java.net.*;

public class CautiousHTTP {
    public static void main(String[] args) {
        final int PORT = 8080;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.printf("Listening on port %d\n", PORT);

            while (true) {
                Socket clienSocket = serverSocket.accept();
//                System.out.println(clienSocket.getRemoteSocketAddress());
//                clienSocket.close();
                System.out.printf("Client connected: %s\n", clienSocket.getInetAddress());
                handleRequest(clienSocket);
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static void handleRequest(Socket clientSocket) {
//        try (InputStream in = clientSocket.getInputStream()) {
//            System.out.println("Raw data (bytes):");
//            byte[] buffer = new byte[128];
//            int bytesRead;
//            while ((bytesRead = in.read(buffer)) != -1) {
//                // Print bytes in HEX format
//                for (int i = 0; i < bytesRead; i++) {
//                    System.out.printf("%02X ", buffer[i]); // e.g., "48 65 6C 6C 6F" = "Hello"
//                }
//                System.out.println();
//            }
//        } catch (IOException e) {
//            System.err.println("Server error: " + e.getMessage());
//        }
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {

            // Read request
            String requestLine = in.readLine();
            System.out.println("Received request: " + requestLine);
            String nextLine = in.readLine();
            System.out.println("Next line: " + nextLine);

            // Prepare response,
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
