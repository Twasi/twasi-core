package net.twasi.core.webinterface.dto.auth;

import net.twasi.core.webinterface.dto.ApiDTO;

public class CallbackResponseDTO extends ApiDTO {
    public String JWT;

    public CallbackResponseDTO(String JWT) {
        super(true);

        this.JWT = JWT;
    }
}
