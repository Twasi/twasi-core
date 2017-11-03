package net.twasi.core.instances;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.logger.TwasiLogger;

import java.util.ArrayList;
import java.util.List;

public class InstanceManager {

    public List<TwasiInterface> interfaces = new ArrayList<>();

    public void registerInterface(TwasiInterface inf) {
        if (interfaces.contains(inf)) {
            TwasiLogger.log.info("Tried to register Interface which is already registered: " + inf.toString());
            return;
        }

        interfaces.add(inf);
        TwasiLogger.log.debug("Registered interface: " + inf.toString());
    }

}
