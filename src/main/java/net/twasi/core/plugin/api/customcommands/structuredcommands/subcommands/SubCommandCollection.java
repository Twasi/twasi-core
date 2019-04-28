package net.twasi.core.plugin.api.customcommands.structuredcommands.subcommands;

import java.util.ArrayList;
import java.util.Arrays;

public class SubCommandCollection extends ArrayList<Class<? extends TwasiSubCommand>> {

    private SubCommandCollection() {
        super();
    }

    @SafeVarargs
    private SubCommandCollection(Class<? extends TwasiSubCommand>... classes) {
        super();
        addAll(Arrays.asList(classes));
    }

    @SafeVarargs
    public static SubCommandCollection OFCLASSES(Class<? extends TwasiSubCommand>... subCommands) {
        return new SubCommandCollection(subCommands);
    }

    public static SubCommandCollection EMPTY() {
        return new SubCommandCollection();
    }
}
