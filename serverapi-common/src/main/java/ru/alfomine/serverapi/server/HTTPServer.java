package ru.alfomine.serverapi.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.*;
import ru.alfomine.serverapi.CommandResult;
import ru.alfomine.serverapi.api.IServer;
import ru.alfomine.serverapi.server.commands.ServerCommand;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

public class HTTPServer implements Runnable {
    private int port;
    private IServer serverImpl;
    private List<ServerCommand> commands = new ArrayList<>();

    public HTTPServer(int port, IServer serverImpl) {
        this.port = port;
        this.serverImpl = serverImpl;


    }

    @Override
    public void run() {
        HttpServer server;

        serverImpl.serverLog("[SERVERAPI_SERVER] Starting HTTP server at port " + port);

        try {
            server = HttpServer.create();
            server.bind(new InetSocketAddress(port), 0);

            HttpContext context = server.createContext("/serverAPI", new EchoHandler());
            context.setAuthenticator(new Auth());

            server.setExecutor(null);
            server.start();

            serverImpl.serverLog("[AFMCP_APISERVER] HTTP server has been successfully started!");
        } catch (Exception e) {
            serverImpl.serverLog("[AFMCP_APISERVER] Error starting api server!");
            e.printStackTrace();
        }
    }

    private void responseString(HttpExchange exchange, int httpCode, String string) {
        byte[] bytes = string.getBytes();

        try {
            exchange.sendResponseHeaders(httpCode, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);

            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class EchoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody());
            BufferedReader br = new BufferedReader(isr);
            String input = br.readLine();

            JsonObject jsonCommand;

            try {
                jsonCommand = new Gson().fromJson(input, JsonObject.class);
            } catch (JsonParseException exception) {
                responseString(exchange, 400, "Failed to parse JSON");
                return;
            }

            if (!jsonCommand.has("cmd") || !jsonCommand.has("args")) {
                responseString(exchange, 400, "Required JSON fields not found");
                return;
            }

            String commandName = jsonCommand.get("name").getAsString();
            List<String> commandArgs = new Gson().fromJson(jsonCommand.get("args"), new TypeToken<List<String>>() {
            }.getType());

            for (ServerCommand command : commands) {
                if (command.getName().equalsIgnoreCase(commandName)) {
                    CommandResult result;

                    try {
                        result = command.run(commandArgs);
                    } catch (Exception e) {
                        responseString(exchange, 500, "Failed to execute command: " + e.getMessage());
                        return;
                    }

                    responseString(exchange, result.getCode(), result.getBody());

                    return;
                }
            }
        }
    }

    class Auth extends Authenticator {
        @Override
        public Result authenticate(HttpExchange httpExchange) {
            if ("/forbidden".equals(httpExchange.getRequestURI().toString()))
                return new Failure(403);
            else
                return new Success(new HttpPrincipal("c0nst", "realm"));
        }
    }
}