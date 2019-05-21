package net.twasi.core.messages.variables;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.models.Message.TwasiMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VariablePreprocessor {

    private static String charsRegex = "[a-zA-Z0-9]";
    private static String indicatorRegex = "\\$";
    private static char indicator = '$';

    public static String process(TwasiInterface inf, String text, TwasiMessage message) {

        return text;
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
                            if(text.charAt(j) == '(') {
                                depth ++;
                            }
                            if (text.charAt(j) == ')') {
                                if(depth == 0) {
                                    end = j;
                                    break;
                                }
                                depth --;
                            } else {
                                varArgs.append(text.charAt(j));
                            }
                        }
                    }
                    if(end == i) {
                        varArgs.setLength(0);
                    }
                    break;
                } else if (start != -1) {
                    varName.append(c);
                }
            }
            variables.add(new ParsedVariable(text.substring(start, end), varName.toString(), varArgs.toString()));
        }
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

        public String resolve(){
            List<String> args = new ArrayList<>();
            if(!this.args.equals("")) {
                int depth = 0;
                int start = 0;
                for(int i=0;i<this.args.length(); i++){
                    char current = this.args.charAt(i);
                    if(current == '(') depth++;
                    if(current == ')' && depth > 0) depth--;
                    if(current == ',' && depth == 0) {

                    }
                }
            }
        }
    }

    private static String resolveVar(String name, String[] args, String raw, TwasiInterface twasiInterface) {
        return raw;
    }
}
