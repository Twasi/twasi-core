package net.twasi.core.database.models;

public enum AccountStatus {

    /**
     * Email confirmation is pending
     */
    EMAIL_CONFIRMATION,

    /**
     * The email is confirmed
     */
    OK,

    /**
     * The user is banned and should not be able to:
     * - Access the Panel
     * - Use the bot in the chat
     */
    BANNED

}
