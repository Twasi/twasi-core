package net.twasi.core.messages.variables;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.models.Message.TwasiMessage;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.TwasiVariable;

import java.util.Arrays;
import java.util.stream.Collectors;

public class VariablePreprocessor {
    public static String process(TwasiInterface inf, String text, TwasiMessage message) {
        // Check if at least 1 $ is contained
        if (!text.contains("$")) {
            // There could be no variables in this text
            return text;
        }

        // Split text to words
        String[] words = text.split(" ");

        for (int i = 0; i < words.length; i++) {
            if (words[i].startsWith("$")) {
                String variable = words[i].substring(1);
                String[] parameters = new String[0];

                if (variable.contains("(")) {
                    // Special case: $variable()
                    if (variable.endsWith("()")) {
                        variable = variable.substring(0, variable.length() - 2);
                    } else {
                        // There are parameters available
                        variable = variable.substring(0, variable.length() - 1);
                        String parameterString = variable.split("\\(", 2)[1];
                        variable = variable.split("\\(", 2)[0];
                        parameters = parameterString.split(",");
                    }
                }
                String finalName = variable;
                TwasiUserPlugin handlingPlugin = inf
                        .getPlugins()
                        .stream()
                        .filter(plugin -> plugin
                                .getVariables()
                                .stream()
                                .anyMatch(var -> var
                                        .getNames()
                                        .stream()
                                        .anyMatch(name -> name.equalsIgnoreCase(finalName))
                                )
                        ).findFirst().orElse(null);
                if (handlingPlugin == null) {
                    words[i] = "ERROR_NOT_FOUND";
                } else {
                    TwasiVariable handlingVariable = handlingPlugin
                            .getVariables()
                            .stream()
                            .filter(var -> var
                                    .getNames()
                                    .stream()
                                    .anyMatch(name -> name.equalsIgnoreCase(finalName))
                            ).findFirst().orElse(null);

                    if (handlingVariable == null) {
                        throw new RuntimeException("Error while trying to process variable " + variable + " for user plugin " + handlingPlugin.getCorePlugin().getName());
                    }

                    words[i] = handlingVariable.process(variable, inf, parameters, message);
                }
            }
        }

        return Arrays.stream(words).collect(Collectors.joining(" "));
    }
}
