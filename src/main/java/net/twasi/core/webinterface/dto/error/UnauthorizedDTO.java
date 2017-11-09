package net.twasi.core.webinterface.dto.error;

public class UnauthorizedDTO extends ErrorDTO {
    public UnauthorizedDTO() {
        super(false, "401 - Unauthorized. Please sign in.");
    }
}
