package net.twasi.core.plugin.api;

import net.twasi.core.plugin.api.variables.TwasiVariableBase;

@Deprecated
public class TwasiVariable extends TwasiVariableBase {
    public TwasiVariable(TwasiUserPlugin owner) {
        super(owner);
    }

    // This class is deprecated but necessary whilst other plugins still depend on it
    // TODO update plugins and switch TwasiVariable class to net.twasi.core.plugin.api.variables.TwasiVariableBase
}
