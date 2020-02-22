package ru.alfomine.serverapi.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.*;
import ru.alfomine.serverapi.api.IServer;
import ru.alfomine.serverapi.server.methods.Method;
import ru.alfomine.serverapi.server.methods.MethodCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class HTTPServer implements Runnable {
    private int port;
    private IServer serverImpl;
    private List<Method> methods = new ArrayList<>();

    public HTTPServer(int port, IServer serverImpl) {
        this.port = port;
        this.serverImpl = serverImpl;

        methods.add(new MethodCommand());
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

            if (!jsonCommand.has("method") || !jsonCommand.has("args")) {
                responseString(exchange, 400, "Required JSON fields not found");
                return;
            }

            String methodName = jsonCommand.get("method").getAsString();
            List<String> methodArgs = new Gson().fromJson(jsonCommand.get("args"), new TypeToken<List<String>>() {
            }.getType());

            CommandResult result = null;

            for (Method command : methods) {
                if (command.getName().equalsIgnoreCase(methodName)) {
                    try {
                        result = command.run(methodArgs, serverImpl);
                    } catch (Exception e) {
                        result = new CommandResult().error("Failed to execute command: " + e.getMessage(), 500);
                    }
                    break;
                }
            }

            if (result == null) {
                result = new CommandResult().error("Unknown method: " + methodName, 400);
            }

            responseString(exchange, result.code, new Gson().toJson(result));
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