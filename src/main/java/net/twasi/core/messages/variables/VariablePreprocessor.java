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
    private static int resolveMaxDepth = 3;

    public static String process(TwasiInterface inf, String text, TwasiMessage message) {

        // Save text to atomic reference
        AtomicReference<String> parsed = new AtomicReference<>(text);

        // Check text for variables and parse them (and also reparse if they return other variables)
        int i = resolveMaxDepth;
        while (parsed.get().contains(String.valueOf(indicator)) && i-- > 0) {
            List<ParsedVariable> variables = parseVars(parsed.get());
            variables.forEach(var -> parsed.set(parsed.get().replace(var.raw, var.resolve(inf, message))));
        }
        return parsed.get();
    }

    private static List<ParsedVariable> parseVars(String text) {

        // List to save found variables in (unresolved)
        List<ParsedVariable> variables = new ArrayList<>();

        // Regex to quickly find out if variables are contained
        while (text.matches(".*" + indicatorRegex + charsRegex + "+.*")) {

            // Variables to temporarily save beginning/ending of the current variable
            int start = -1, end = -1;

            // Variables to save the current variable's name and args in
            StringBuilder varName = new StringBuilder();
            StringBuilder varArgs = new StringBuilder();

            // Loop through every char of the text
            for (int i = 0; i < text.length(); i++) {

                // & save current char
                char c = text.charAt(i);

                // If char is variable indicator
                if (c == indicator && String.valueOf(text.charAt(i + 1)).matches(charsRegex)) {

                    // Then save the current position as start and go to next char
                    start = i;
                    continue;
                }

                // If char is alphanumeric and we currently parse a variable
                if (!String.valueOf(c).matches(charsRegex) && start != -1) {

                    // Save the current position as end
                    end = i - 1;

                    // Check for variable arguments
                    if (c == '(') {

                        // Variable to save depth in (to not end argument parsing when brackets are unclosed)
                        int depth = 0;

                        // Loop through every char after the already parsed variable name
                        for (int j = i + 1; j < text.length(); j++) {

                            // If current char is an opening bracket
                            if (text.charAt(j) == '(') {

                                // Increment depth to prevent too early end of parsing
                                depth++;
                            }

                            // If current char is a closing bracket
                            if (text.charAt(j) == ')') {

                                // Check whether the depth is 0 or not
                                if (depth == 0) {

                                    // If so we can stop parsing
                                    end = j;
                                    break;
                                }

                                // Else we decrement the depth and save the current char
                                depth--;
                                varArgs.append(text.charAt(j));
                            } else {

                                // If the current char is no closing bracket just save it
                                varArgs.append(text.charAt(j));
                            }
                        }
                    }

                    // If there was no closing bracket found
                    if (end == i) {

                        // There can be no valid args so just reset them
                        varArgs.setLength(0);
                    }

                    // And stop parsing
                    break;

                    // If variable name is unfinished
                } else if (start != -1) {

                    // Set the end position to the position of the current char and save the char
                    end = i;
                    varName.append(c);
                }
            }

            // If there was no variable at all just break the loop
            if (start == -1 || end == -1) break;

            // Increment the end position by one
            end += 1; // Chars of strings start at 1 not at 0

            // Save the last parsed variable
            variables.add(new ParsedVariable(text.substring(start, end), varName.toString(), varArgs.toString()));

            // And prevent reparsing it
            text = text.substring(0, start) + text.substring(end);
        }

        TwasiLogger.log.debug("Number of parsed variables: " + variables.size());
        return variables;
    }

    private static class ParsedVariable {

        // The raw variable string (containing indicator, name and optionally args) -> To replace in original text
        private String raw;

        // The variable name
        private String name;

        // The raw argument string without brackets
        private String args;

        public ParsedVariable(String raw, String name, String args) {
            this.raw = raw;
            this.name = name;
            this.args = args;
        }

        // Method to resolve variable arguments and variables (by dependencies and installed commands)
        public String resolve(TwasiInterface twasiInterface, TwasiMessage message) {

            // List to save found args in
            List<String> args = new ArrayList<>();

            // Check whether there are any args or not
            if (!this.args.equals("")) {

                // Init depth and start
                int depth = 0;
                int start = 0;

                // Loop through every char
                for (int i = 0; i < this.args.length(); i++) {

                    // And save the current char
                    char current = this.args.charAt(i);

                    // If that char is an opening bracket then increment the depth to prevent too early parsing end
                    if (current == '(') depth++;

                    // If char is a closing bracket and the depth is gt 0 then decrement the depth
                    if (current == ')' && depth > 0) depth--;

                    // If current char is a comma then save the parsed arg
                    if (current == ',' && depth == 0) {
                        args.add(this.args.substring(start, i));

                        // And also set the start to one char beyond the comma to parse the next arg
                        start = i + 1;
                    }
                }

                // Add the last arg (won't be added by comma)
                args.add(this.args.substring(start));
            }

            // Resolve vars in args and re-resolve them if they return any other vars
            List<String> resolvedArgs = new ArrayList<>();
            for (String arg : args) {
                AtomicReference<String> parsed = new AtomicReference<>(arg);
                int i = resolveMaxDepth;
                while (parsed.get().contains(String.valueOf(indicator)) && i-- > 0) {
                    List<ParsedVariable> variables = parseVars(arg);
                    variables.forEach(var -> parsed.set(parsed.get().replace(var.raw, var.resolve(twasiInterface, message))));
                }
                resolvedArgs.add(parsed.get());
            }

            // No resolve and return
            return resolveVar(this.name, resolvedArgs.toArray(new String[0]), raw, twasiInterface, message);
        }
    }

    private static String resolveVar(String name, String[] args, String raw, TwasiInterface twasiInterface, TwasiMessage message) {
        TwasiDependency<?> dependency;
        TwasiUserPlugin plugin;

        try {
            // Find handling plugin
            plugin = twasiInterface.getPlugins().stream().filter(pl ->
                    pl.getVariables().stream().anyMatch(var ->
                            var.getNames().stream().anyMatch(name::equalsIgnoreCase))).findAny().orElse(null);

            // If there is a handling plugin available then handle the variable
            if (plugin != null) {
                return reformat(plugin.getVariables().stream().filter(var -> var.getNames().stream().anyMatch(name::equalsIgnoreCase)).findAny().get().process(name, twasiInterface, args, message));
            }

            // Else find handling dependency
            PluginManagerService pm = ServiceRegistry.get(PluginManagerService.class);
            dependency = pm.getDependencies().stream().filter(dep ->
                    dep.getVariables().stream().anyMatch(var -> var.getNames().stream().anyMatch(name::equalsIgnoreCase)))
                    .findAny().orElse(null);

            // If there is a handling dependency then handle the variable
            if (dependency != null) {
                return reformat(dependency.getVariables().stream().filter(var ->
                        var.getNames().stream().anyMatch(name::equalsIgnoreCase)).findAny().get()
                        .process(name, twasiInterface, args, message));
            }
        } catch(ArrayIndexOutOfBoundsException ignored) {
            return "INSUFFICIENT_PARAMETERS";
        } catch (Exception ignored) {
            return "ERROR";
        }

        // If there is no handling plugin or dependency return UNRESOLVED
        return "UNRESOLVED";
    }

    private static String reformat(String resolved) {
        return resolved
                .replaceAll("\n", " ");
    }
}
