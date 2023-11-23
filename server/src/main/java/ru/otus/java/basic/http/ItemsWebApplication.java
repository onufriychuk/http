package ru.otus.java.basic.http;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemsWebApplication implements MyWebApplication {
    private String name;
    private List<Item> items;
    private static final String dbName = "jdbc:postgresql://localhost:5432/postgres";
    private static final String dbUser = "postgres";
    private static final String dbPassword = "MJxaN9bz";

    public ItemsWebApplication() {
        this.items = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(dbName, dbUser, dbPassword)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, title FROM public.items")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        long id = resultSet.getLong("id");
                        String title = resultSet.getString("title");
                        items.add(new Item(id, title));
                    }
                } catch (SQLException e) {
                }
            } catch (SQLException e) {
            }
        } catch (SQLException e) {
        }
    }

    @Override
    public void execute(Request request, OutputStream output) throws IOException {
        Gson gson = new Gson();
        String jsonItems = gson.toJson(items);



        output.write(("" +
                "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/json\r\n" +
                "Access-Control-Allow-Origin: *\r\n" +
                "\r\n" +
                jsonItems
        ).getBytes(StandardCharsets.UTF_8));
    }
}