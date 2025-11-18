import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SimpleWebServer {

    public static void main(String[] args) {
        int port = 8080;
        System.out.println("Starter webserver på port " + port + "...");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Ny forbindelse: " + client.getInetAddress());
                handleClient(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket client) {
        try (
            BufferedReader in = new BufferedReader(
                new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8)
            );
            OutputStream out = client.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true, StandardCharsets.UTF_8)
        ) {

            // Læs request line
            String requestLine = in.readLine();
            System.out.println("Request: " + requestLine);

            // Læs resten af headerne og smid væk
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                // Ingenting – vi læser dem bare
            }

            // HTML filen der skal sendes
            File file = new File("index.html");

            if (!file.exists()) {
                send404(writer);
            } else {
                sendFileResponse(file, out, writer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { client.close(); } catch (IOException ignore) {}
        }
    }

    private static void send404(PrintWriter writer) {
        String body = "<h1>404 - File not found</h1>";
        writer.println("HTTP/1.1 404 Not Found");
        writer.println("Content-Type: text/html; charset=utf-8");
        writer.println("Content-Length: " + body.getBytes(StandardCharsets.UTF_8).length);
        writer.println();
        writer.print(body);
        writer.flush();
    }

    private static void sendFileResponse(File file, OutputStream out, PrintWriter writer) throws IOException {
        // Læs hele filen i RAM (simpelt men fint til demo)
        byte[] fileBytes = readFileBytes(file);

        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: text/html; charset=utf-8");
        writer.println("Content-Length: " + fileBytes.length);
        writer.println();
        writer.flush();

        // Selve HTML-indholdet sendes som bytes
        out.write(fileBytes);
        out.flush();
    }

    private static byte[] readFileBytes(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return fis.readAllBytes();
        }
    }
}
