package net.twasi.core.graphql.model.customthemes;

public class CustomThemeDTO {

    private String backgroundColor;
    private int buttonRadius;
    private int panelRadius;
    private int specialContentRadius;
    private String panelBackgroundColor;
    private String fontColor;
    private String buttonFontColor;
    private String primaryColor;
    private String secondaryColor;
    private String specialContentColor;

    private String outlineTextLogo;
    private String shadowPrimaryTextLogo;
    private String shadowSecondaryTextLogo;
    private String mainTextLogo;
    private Boolean darkMode;
    private CustomThemeSpecialProperties specialProperties = new CustomThemeSpecialProperties();

    public CustomThemeDTO() {
    }

    public CustomThemeDTO(String backgroundColor, int buttonRadius, int panelRadius, int specialContentRadius, String panelBackgroundColor, String fontColor, String buttonFontColor, String primaryColor, String secondaryColor, String specialContentColor, String outlineTextLogo, String shadowPrimaryTextLogo, String shadowSecondaryTextLogo, String mainTextLogo, Boolean darkMode, CustomThemeSpecialProperties specialProperties) {
        this.backgroundColor = backgroundColor;
        this.buttonRadius = buttonRadius;
        this.panelRadius = panelRadius;
        this.specialContentRadius = specialContentRadius;
        this.panelBackgroundColor = panelBackgroundColor;
        this.fontColor = fontColor;
        this.buttonFontColor = buttonFontColor;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.specialContentColor = specialContentColor;
        this.outlineTextLogo = outlineTextLogo;
        this.shadowPrimaryTextLogo = shadowPrimaryTextLogo;
        this.shadowSecondaryTextLogo = shadowSecondaryTextLogo;
        this.mainTextLogo = mainTextLogo;
        this.darkMode = darkMode;
        this.specialProperties = specialProperties;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public int getButtonRadius() {
        return buttonRadius;
    }

    public int getPanelRadius() {
        return panelRadius;
    }

    public int getSpecialContentRadius() {
        return specialContentRadius;
    }

    public String getPanelBackgroundColor() {
        return panelBackgroundColor;
    }

    public String getFontColor() {
        return fontColor;
    }

    public String getButtonFontColor() {
        return buttonFontColor;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public String getSpecialContentColor() {
        return specialContentColor;
    }

    public String getOutlineTextLogo() {
        return outlineTextLogo;
    }

    public String getShadowPrimaryTextLogo() {
        return shadowPrimaryTextLogo;
    }

    public String getShadowSecondaryTextLogo() {
        return shadowSecondaryTextLogo;
    }

    public String getMainTextLogo() {
        return mainTextLogo;
    }

    public Boolean getDarkMode() {
        return darkMode;
    }

    public CustomThemeSpecialProperties getSpecialProperties() {
        return specialProperties;
    }

    public static class CustomThemeSpecialProperties {
        private boolean snow = false;

        public CustomThemeSpecialProperties() {
        }

        public boolean isSnow() {
            return snow;
        }
    }
}
