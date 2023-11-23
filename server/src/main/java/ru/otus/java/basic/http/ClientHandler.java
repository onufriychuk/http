package ru.otus.java.basic.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ClientHandler implements Runnable{
    private static final Logger logger = LogManager.getLogger(ClientHandler.class.getName());
    private Socket socket;
    private Server server;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            Request request = new Request(socket.getInputStream());
            logger.info("Получен запрос");
            request.show();
            boolean executed = false;
            for (Map.Entry<String, MyWebApplication> e : server.getRouter().entrySet()) {
                if (request.getUri().startsWith(e.getKey())) {
                    e.getValue().execute(request, socket.getOutputStream());
                    executed = true;
                    break;
                }
            }
            if (!executed) {
                socket.getOutputStream().write(("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<html><body><h1>Unknown application</h1></body></html>").getBytes(StandardCharsets.UTF_8));
            }
            socket.close();
        } catch (IOException e) {
            logger.error("Ошибка при работе с клиентом " + e);
            throw new RuntimeException(e);
        }
    }
}
