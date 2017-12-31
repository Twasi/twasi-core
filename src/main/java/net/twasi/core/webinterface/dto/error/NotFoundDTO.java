package net.twasi.core.webinterface.dto.error;

public class NotFoundDTO extends ErrorDTO {
    public NotFoundDTO() {
        super(false, "404 - Not found.");
    }
}
