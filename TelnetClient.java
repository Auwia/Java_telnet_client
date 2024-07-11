package com.example;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class TelnetClient {

    public static void main(String[] args) {
        TelnetClient telnet = new TelnetClient();
        try {
            // Connessione al server Telnet
            telnet.connect("localhost", 23);

            // Ottieni InputStream e OutputStream per comunicare con il server
            InputStream in = telnet.getInputStream();
            OutputStream out = telnet.getOutputStream();
            PrintStream printStream = new PrintStream(out);

            // Crea un thread per leggere la risposta del server
            Thread readerThread = new Thread(() -> {
                try {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        System.out.print(new String(buffer, 0, bytesRead));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            readerThread.start();

            // Scrivi comandi al server
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();
                printStream.println(command);
                printStream.flush();
            }

            // Chiudi la connessione
            telnet.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
