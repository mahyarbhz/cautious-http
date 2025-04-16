import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Response {
    public static class Builder {
        private int statusCode = 200;
        private final Map<String, String> headers = new HashMap<>();
        private String body = "";
        private final OutputStream outputStream;

        public Builder(OutputStream stream) {
            this.outputStream = stream;
            // Set default headers
            headers.put("Content-Type", "text/plain");
        }

        public Builder status(int code) {
            statusCode = code;
            return this;
        }

        public Builder header(String name, String value) {
            headers.put(name, value);
            return this;
        }

        public Builder body(String body) {
            this.body = body;

            if (!headers.containsKey("Content-Length")) {
                headers.put("Content-Length", String.valueOf(body.length()));
            }
            return this;
        }

        public void send() throws IOException {
            String statusLine = "HTTP/1.1 " + statusCode + " " + getStatusText(statusCode);

            StringBuilder headerSection = new StringBuilder();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                headerSection.append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue())
                        .append("\r\n");
            }

            String fullResponse = statusLine + "\r\n" +
                    headerSection.toString() + "\r\n" +
                    body;

            outputStream.write(fullResponse.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }

        private String getStatusText(int statusCode) {
            return switch (statusCode) {
                case 200 -> "OK";
                case 404 -> "Not Found";
                default -> "Unknown Status";
            };
        }
    }
}