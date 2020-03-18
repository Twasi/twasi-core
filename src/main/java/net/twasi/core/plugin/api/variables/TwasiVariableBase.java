package net.twasi.core.plugin.api.variables;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.models.Message.TwasiMessage;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.TwasiVariableInterface;

import java.util.ArrayList;
import java.util.List;

public class TwasiVariableBase implements TwasiVariableInterface {
    private TwasiUserPlugin owner;

    public TwasiVariableBase(TwasiUserPlugin owner) {
        this.owner = owner;
    }

    public List<String> getNames() {
        return new ArrayList<>();
    }

    @Override
    public String process(String name, TwasiInterface inf, String[] params, TwasiMessage message) {
        TwasiLogger.log.warn("Plugin " + owner.getCorePlugin().getName() + " has registered for variable '" + name + "' but doesn't process it :c");
        return null;
    }
}
