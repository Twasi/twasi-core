package net.twasi.core.plugin.api.customcommands.structuredcommands;

import net.twasi.core.plugin.api.customcommands.TwasiCustomCommandEvent;

import java.util.ArrayList;
import java.util.List;

public class TwasiStructuredCommandEvent extends TwasiCustomCommandEvent {

    private List<String> args;
    private String baseCommand;

    public TwasiStructuredCommandEvent(TwasiCustomCommandEvent event) {
        super(event.getCommand(), event.getLoader());
        this.args = event.getArgs();
        this.baseCommand = args.get(0).toLowerCase();
        args.remove(0);
    }

    @Override
    public List<String> getArgs() {
        try {
            List<String> a = new ArrayList<>(args);
            a.remove(0);
            return a;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public String getBaseCommand() {
        return baseCommand;
    }
}
