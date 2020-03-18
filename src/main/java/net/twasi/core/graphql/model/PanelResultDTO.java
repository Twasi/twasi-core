package net.twasi.core.graphql.model;

public class PanelResultDTO {

    private PanelResultType status;
    private String translationKey;

    public PanelResultDTO(PanelResultType status, String translationKey) {
        this.status = status;
        this.translationKey = translationKey;
    }

    public PanelResultDTO(PanelResultType status) {
        this(status, null);
    }

    public PanelResultType getStatus() {
        return status;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public enum PanelResultType {
        OK,
        WARNING,
        ERROR,
        UNPERMITTED;
    }
}

