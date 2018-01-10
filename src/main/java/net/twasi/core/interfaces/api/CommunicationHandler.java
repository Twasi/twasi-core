package net.twasi.core.interfaces.api;

public abstract class CommunicationHandler implements CommunicationHandlerInterface {

    private transient TwasiInterface twasiInterface;

    public CommunicationHandler(TwasiInterface inf) {
        twasiInterface = inf;
    }

    public TwasiInterface getInterface() {
        return twasiInterface;
    }

}
