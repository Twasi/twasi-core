package net.twasi.core.plugin.api;

import net.twasi.core.interfaces.api.TwasiInterface;

import java.util.List;

public interface TwasiVariableInterface {
    List<String> getNames();

    String process(String text, TwasiInterface inf, String[] params);
}
