package net.twasi.core.cli;

import java.util.List;

public abstract class TwasiCLICommand {

    public abstract String getCommandName();

    public abstract String getCommandHelptext();

    public abstract String getCommandDescription();

    public abstract String execute(List<String> args);

}
