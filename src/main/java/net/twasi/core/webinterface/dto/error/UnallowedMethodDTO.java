package net.twasi.core.webinterface.dto.error;

public class UnallowedMethodDTO extends ErrorDTO {
    public UnallowedMethodDTO(boolean status) {
        super(status, "Unallowed or unhandled Method called.");
    }
}
