package net.twasi.core.graphql.model.customthemes;

public class CustomThemeDTO {

    private String backgroundColor;
    private String contentColor;
    private String specialContentColor;
    private String primaryColor;
    private String secondaryColor;
    private String fontColor;
    private int borderRadius = 10;
    private String font = null;
    private boolean shadows = true;
    private boolean pillButtons = true;
    private String customHeader = null;

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getContentColor() {
        return contentColor;
    }

    public String getSpecialContentColor() {
        return specialContentColor;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public String getFontColor() {
        return fontColor;
    }

    public int getBorderRadius() {
        return borderRadius;
    }

    public String getFont() {
        return font;
    }

    public boolean isShadows() {
        return shadows;
    }

    public boolean isPillButtons() {
        return pillButtons;
    }

    public String getCustomHeader() {
        return customHeader;
    }
}
