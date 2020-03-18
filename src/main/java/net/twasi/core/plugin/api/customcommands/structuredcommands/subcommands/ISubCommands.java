package net.twasi.core.plugin.api.customcommands.structuredcommands.subcommands;

import net.twasi.core.plugin.api.TwasiUserPlugin;

import java.util.List;

public interface ISubCommands {

    TwasiUserPlugin getProvidingUserPlugin();

    SubCommandCollection getSubCommands();

    String getCommandName();

    List<String> getParentCommandNames();

    default String getSyntaxKey() {
        return String.join(".", getParentCommandNames()) + "." + getCommandName() + ".syntax";
    }

    default String requirePermissionKey() {
        return null;
    }

}
