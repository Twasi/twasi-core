package net.twasi.core.messages.variables;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.models.Message.TwasiMessage;
import net.twasi.core.plugin.TwasiDependency;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.PluginManagerService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class VariablePreprocessor {

    private static String charsRegex = "[a-zA-Z0-9]";
    private static String indicatorRegex = "\\$";
    private static char indicator = '$';

    public static String process(TwasiInterface inf, String text, TwasiMessage message) {
        AtomicReference<String> parsed = new AtomicReference<>(text);
        List<ParsedVariable> variables = parseVars(text);
        variables.forEach(var -> parsed.set(parsed.get().replace(var.raw, var.resolve(inf, message))));
        return parsed.get();
    }

    private static List<ParsedVariable> parseVars(String text) {
        List<ParsedVariable> variables = new ArrayList<>();
        while (text.matches(".*" + indicatorRegex + charsRegex + "+.*")) {
            int start = -1, end = -1;
            StringBuilder varName = new StringBuilder();
            StringBuilder varArgs = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c == indicator && String.valueOf(text.charAt(i + 1)).matches(charsRegex)) {
                    start = i;
                    continue;
                }
                if (!String.valueOf(c).matches(charsRegex)) {
                    end = i;
                    if (c == '(') {
                        int depth = 0;
                        for (int j = i + 1; j < text.length(); j++) {
                            if (text.charAt(j) == '(') {
                                depth++;
                            }
                            if (text.charAt(j) == ')') {
                                if (depth == 0) {
                                    end = j;
                                    break;
                                }
                                depth--;
                                varArgs.append(text.charAt(j));
                            } else {
                                varArgs.append(text.charAt(j));
                            }
                        }
                    }
                    if (end == i) {
                        varArgs.setLength(0);
                    }
                    break;
                } else if (start != -1) {
                    end = i;
                    varName.append(c);
                }
            }
            if (start == -1 || end == -1) break;
            end += 1; // Chars of strings start at 1 not at 0
            variables.add(new ParsedVariable(text.substring(start, end), varName.toString(), varArgs.toString()));
            text = text.substring(0,start) + text.substring(end);
        }
        TwasiLogger.log.debug("Number of parsed variables: " + variables.size());
        return variables;
    }

    private static class ParsedVariable {
        private String raw;
        private String name;
        private String args;

        public ParsedVariable(String raw, String name, String args) {
            this.raw = raw;
            this.name = name;
            this.args = args;
        }

        public String resolve(TwasiInterface twasiInterface, TwasiMessage message) {
            List<String> args = new ArrayList<>();
            if (!this.args.equals("")) {
                int depth = 0;
                int start = 0;
                for (int i = 0; i < this.args.length(); i++) {
                    char current = this.args.charAt(i);
                    if (current == '(') depth++;
                    if (current == ')' && depth > 0) depth--;
                    if (current == ',' && depth == 0) {
                        args.add(this.args.substring(start, i));
                        start = i+1;
                    }
                }
                args.add(this.args.substring(start)); // Add last arg
            }
            for(int i=0; i<3;i++) {
                List<String> resolvedArgs = new ArrayList<>();
                args.forEach(arg -> {
                    AtomicReference<String> parsed = new AtomicReference<>(arg);
                    List<ParsedVariable> variables = parseVars(arg);
                    variables.forEach(var -> {
                        parsed.set(parsed.get().replace(var.raw, var.resolve(twasiInterface, message)));
                        resolvedArgs.add(parsed.get());
                    });
                });
                args = resolvedArgs;
            }
            return resolveVar(this.name, args.toArray(new String[0]), raw, twasiInterface, message);
        }
    }

    private static String resolveVar(String name, String[] args, String raw, TwasiInterface twasiInterface, TwasiMessage message) {
        TwasiDependency dependency;
        TwasiUserPlugin plugin;

        try {
            plugin = twasiInterface.getPlugins().stream().filter(pl ->
                    pl.getVariables().stream().anyMatch(var ->
                            var.getNames().stream().anyMatch(name::equalsIgnoreCase))).findAny().orElse(null);

            if (plugin != null) {
                return plugin.getVariables().stream().filter(var -> var.getNames().stream().anyMatch(name::equalsIgnoreCase)).findAny().get().process(name, twasiInterface, args, message);
            }

            PluginManagerService pm = ServiceRegistry.get(PluginManagerService.class);
            dependency = pm.getDependencies().stream().filter(dep ->
                    dep.getVariables().stream().anyMatch(var ->
                            var.getNames().stream().anyMatch(name::equalsIgnoreCase))).findAny().orElse(null);

            if (dependency != null) {
                return dependency.getVariables().stream().filter(var -> var.getNames().stream().anyMatch(name::equalsIgnoreCase)).findAny().get().process(name, twasiInterface, args, message);
            }
        } catch (Exception ignored) {
        }

        return raw;
    }
}
