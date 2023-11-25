package ru.otus.java.basic.http;

public class Main {
    public static final int PORT = 8189;
    public static void main(String[] args) {
        Server server = new Server(PORT);
        server.start();
    }
}
