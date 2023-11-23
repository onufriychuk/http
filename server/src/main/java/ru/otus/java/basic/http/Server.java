package ru.otus.java.basic.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class.getName());
    private final int port;
    private final Map<String, MyWebApplication> router = new HashMap<>();

    public Map<String, MyWebApplication> getRouter() {
        return router;
    }

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        router.put("/calculator", new CalculatorWebApplication());
        router.put("/greetings", new GreetingsWebApplication());
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Сервер запущен, порт: " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                logger.info("Клиент подключился");
                executorService.execute(new ClientHandler(socket, this));
            }
        } catch (IOException e) {
            logger.error("Ошибка при запуске сервера " + e);

        } finally {
            executorService.shutdown();
        }
    }
}
