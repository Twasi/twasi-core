package net.twasi.core.webinterface.dto.error;

public class BadRequestDTO extends ErrorDTO {
    public BadRequestDTO() {
        super(false, "Bad request. Check submitted data.");
    }

    public BadRequestDTO(String msg) {
        super(false, msg);
    }
}