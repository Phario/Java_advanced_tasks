package pl.pwr.ite.dynak.lib;

import org.graalvm.polyglot.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScriptService {
    private final Engine engine = Engine.create();
    private Map<String, Context> contextMap = new HashMap<>();

    public int[][] getPathFromScript(String scriptName, int[][] map, int startX, int startY, int endX, int endY) {
        Context context = contextMap.get(scriptName);

        if (context == null) {
            throw new RuntimeException("Script not found");
        }

        Value jsBindings = context.getBindings("js");
        Value generatePath = jsBindings.getMember("findPath");
        return generatePath.execute(map, startX, startY, endX, endY).as(int[][].class);
    }

    public int[][] getMapFromScript(String scriptName, int width, int height) {
        Context context = contextMap.get(scriptName);

        if (context == null) {
            throw new RuntimeException("Script not found");
        }

        Value jsBindings = context.getBindings("js");
        Value generateMap = jsBindings.getMember("generateMap");
        return generateMap.execute(width, height).as(int[][].class);
    }

    public void loadContexts(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory path: " + folder.getAbsolutePath());
        }

        File[] jsFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".js"));

        if (jsFiles == null || jsFiles.length == 0) {
            System.out.println("No JavaScript files found in: " + folder.getAbsolutePath());
            return;
        }

        for (File scriptFile : jsFiles) {
            String scriptId = scriptFile.getName().substring(0, scriptFile.getName().length() - 3);

            try {
                Source source = Source.newBuilder("js", scriptFile).build();

                Context context = Context.newBuilder("js")
                        .engine(engine)
                        .allowHostAccess(HostAccess.ALL)
                        .build();

                context.eval(source);

                contextMap.put(scriptId, context);

            } catch (IOException | PolyglotException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void unloadContexts() {
        for (Context context : contextMap.values()) {
            context.close();
        }
        contextMap.clear();
    }

    public java.util.Set<String> getLoadedScripts() {
        return contextMap.keySet();
    }
}
