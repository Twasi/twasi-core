package net.twasi.core.webinterface.dto.error;

import net.twasi.core.webinterface.dto.ApiDTO;

public class ErrorDTO extends ApiDTO {
    public String message;

    public ErrorDTO(boolean status, String message) {
        super(status);
        this.message = message;
    }
}
