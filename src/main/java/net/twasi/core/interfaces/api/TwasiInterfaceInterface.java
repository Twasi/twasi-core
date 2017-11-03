package net.twasi.core.interfaces.api;

import net.twasi.core.models.Streamer;

public interface TwasiInterfaceInterface {

    /**
     * Called when the plugin gets enabled
     */
    void onEnable();

    /**
     * Called when the plugin gets disabled
     */
    void onDisable();

    /**
     * Called if the bot tries to connect to the servers
     * @return if the connection was established successfully
     */
    boolean connect();

    /**
     * Called if the bot tries to disconnect from the servers
     * @return if the connection was closed successfully
     */
    boolean disconnect();

    /**
     * Called to join a certain channel of the streamer
     * @param streamer Join this streamers channel
     * @return if the channel was joined successfully
     */
    boolean join(Streamer streamer);

    /**
     * Get the communication handler to read and send messages
     * @return the CommunicationHandler
     */
    CommunicationHandlerInterface getCommunicationHandler();

}