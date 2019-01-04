package net.twasi.core.events;

public abstract class TwasiEventHandler<T extends  TwasiEvent> {

    public abstract void on(T event);

}
