package net.twasi.core.plugin.api.customcommands;

import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.User;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.models.Message.TwasiCommand;
import net.twasi.core.plugin.api.customcommands.cooldown.CooldownEntity;
import net.twasi.core.plugin.api.customcommands.cooldown.CooldownRepository;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.core.services.providers.config.ConfigService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static net.twasi.core.database.models.permissions.PermissionGroups.BROADCASTER;

public abstract class TwasiCustomCommand {

    private final CooldownRepository cooldownRepo = ServiceRegistry.get(DataService.class).get(CooldownRepository.class);

    protected boolean execute(TwasiCustomCommandEvent event) {
        process(event);
        return false;
    }

    protected void process(TwasiCustomCommandEvent event){
        TwasiLogger.log.warn("Command " + getCommandName() + " has no handler.");
    }

    public final void processInternal(TwasiCommand command, ClassLoader loader) {
        User user = command.getTwasiInterface().getStreamer().getUser();
        TwitchAccount sender = command.getSender();

        // PERMISSION CHECK
        if (requirePermissionKey() != null) { // If permission key is required
            if (!user.hasPermission(sender, requirePermissionKey())) // And sender doesn't have that permission key
                return; // Skip command execution
        }

        // COOLDOWN CHECK
        if (sender.getGroups().contains(BROADCASTER) || user.hasPermission(sender, "twasi.skipcooldown")) // If user has permission to skip cooldown
            execute(new TwasiCustomCommandEvent(command, loader)); // Execute the command
        else { // If user cannot skip cooldown
            CooldownEntity cd = cooldownRepo.getCooldown(user, sender, getCommandName()); // Get current or new Cooldown Entity
            if (cd.hasCooldown()) return; // If there is a cooldown skip command execution
            if (execute(new TwasiCustomCommandEvent(command, loader))) { // If command was executed successfully
                cooldownRepo.commit(cd.setCooldown(getCooldown())); // Reset the cooldown and commit
            }
        }
    }

    public abstract String getCommandName();

    public final String getFormattedCommandName() {
        return ServiceRegistry.get(ConfigService.class).getCatalog().bot.prefix + getCommandName();
    }

    public abstract boolean allowsTimer();

    public abstract boolean allowsListing();

    public List<String> getAliases() {
        return new ArrayList<>();
    }

    public final List<String> getCommandNames() {
        List<String> list = new ArrayList<>(getAliases());
        list.add(0, getCommandName());
        return list;
    }

    public String requirePermissionKey() {
        return null;
    }

    public Duration getCooldown() {
        return Duration.ofMinutes(1);
    }

}
