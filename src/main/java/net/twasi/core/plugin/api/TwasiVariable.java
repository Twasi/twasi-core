package net.twasi.core.plugin.api;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.logger.TwasiLogger;

import java.util.ArrayList;
import java.util.List;

public class TwasiVariable implements TwasiVariableInterface {
    private TwasiUserPlugin owner;

    public TwasiVariable(TwasiUserPlugin owner) {
        this.owner = owner;
    }

    public List<String> getNames() {
        return new ArrayList<>();
    }

    @Override
    public String process(String name, TwasiInterface inf, String[] params) {
        TwasiLogger.log.warn("Plugin " + owner.getCorePlugin().getName() + " has registered for variable '" + name + "' but doens't process it :c");
        return null;
    }
}
