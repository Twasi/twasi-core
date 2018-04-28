package net.twasi.core.graphql.model;

public class AppInfoDTO {
    public String getVersion() {
        String version = getClass().getPackage().getImplementationVersion();

        if (version == null) {
            version = "LIVE";
        }

        return version;
    }
}
