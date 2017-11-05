package net.twasi.core.webinterface.dto.error;

public class UnallowedMethodDTO extends ErrorDTO {
    public UnallowedMethodDTO() {
        super(false, "Unallowed or unhandled Method called.");
    }
}
