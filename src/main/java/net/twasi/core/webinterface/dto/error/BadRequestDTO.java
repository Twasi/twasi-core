package net.twasi.core.webinterface.dto.error;

public class BadRequestDTO extends ErrorDTO {
    public BadRequestDTO(boolean status) {
        super(status, "Bad request. Check submitted data.");
    }
}