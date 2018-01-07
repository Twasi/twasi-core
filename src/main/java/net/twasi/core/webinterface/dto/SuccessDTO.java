package net.twasi.core.webinterface.dto;

public class SuccessDTO extends ApiDTO {
    String message;

    public SuccessDTO(String message) {
        super(true);
        this.message = message;
    }
}
