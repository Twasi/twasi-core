package net.twasi.core.plugin.api.customcommands.structuredcommands;

import net.twasi.core.plugin.api.customcommands.TwasiCustomCommandEvent;

import java.util.ArrayList;
import java.util.List;

public class TwasiStructuredCommandEvent extends TwasiCustomCommandEvent {

    private List<String> args;
    private String baseCommand = null;

    public TwasiStructuredCommandEvent(TwasiCustomCommandEvent event) {
        super(event.getCommand(), event.getLoader());
        args = event.getArgs();
        try {
            this.baseCommand = args.get(0).toLowerCase();
            args.remove(0);
        } catch (Throwable ignored) {
            // Subcommand end
        }
    }

    @Override
    public List<String> getArgs() {
        try {
            return new ArrayList<>(args);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public String getBaseCommand() {
        return baseCommand;
    }
}
