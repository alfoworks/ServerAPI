package ru.alfomine.serverapi.server.methods;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import ru.alfomine.serverapi.api.IServer;
import ru.alfomine.serverapi.api.exception.ServerAPIBaseException;
import ru.alfomine.serverapi.server.CommandResult;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class MethodEval extends Method {
    @Override
    public String getName() {
        return "eval";
    }

    @Override
    public CommandResult run(List<String> args, IServer server) throws ServerAPIBaseException {
        if (args.size() < 1) {
            return new CommandResult().error("Not enough arguments!", 400);
        }

        String code = args.get(0);

        String[] arguments = new String[]{"-strict", "--no-syntax-extensions", "-dump-on-error"};
        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        ScriptEngine engine = factory.getScriptEngine(arguments);
        StringWriter sw = new StringWriter();
        ScriptContext context = engine.getContext();

        context.setWriter(sw);
        context.setErrorWriter(sw);

        String output;

        try {
            engine.eval(
                    code);

            output = sw.toString();
        } catch (Exception e) {
            StringWriter exSw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));

            output = String.format("%s\n%s", sw, exSw);
        }

        return new CommandResult().ok(output);
    }
}
