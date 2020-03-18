package net.twasi.core.cli.commands;

import net.twasi.core.cli.TwasiCLICommand;

import java.util.List;

public class HelpCommand extends TwasiCLICommand {

    private List<TwasiCLICommand> cmds;

    public HelpCommand(List<TwasiCLICommand> cmds) {
        this.cmds = cmds;
    }

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public String getCommandHelptext() {
        return "Bruh, using the help command isn't that difficult...";
    }

    @Override
    public String getCommandDescription() {
        return "Shows a list of all commands. Use 'help [command]' to get help for a specific command.";
    }

    @Override
    public String execute(List<String> args) {
        if (args.size() > 0) {
            TwasiCLICommand cmd = cmds.stream().filter(c -> c.getCommandName().equalsIgnoreCase(args.get(0))).findFirst().orElse(null);
            if (cmd == null) return "This command could not be found.";
            return cmd.getCommandHelptext();
        }
        StringBuilder cmds = new StringBuilder();
        for (TwasiCLICommand cmd : this.cmds) {
            cmds.append(cmd.getCommandName()).append(": ").append(cmd.getCommandDescription()).append("\n");
        }
        return "\nThe following commands are available:\n" + cmds;
    }
}
