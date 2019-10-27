package net.twasi.core.database.models;

import net.twasi.core.database.repositories.UserRepository;
import net.twasi.core.graphql.model.customthemes.CustomThemeDTO;
import net.twasi.core.services.providers.DataService;
import org.bson.types.ObjectId;

import java.util.Calendar;
import java.util.Date;

public class CustomTheme extends BaseEntity {

    private ObjectId creator;

    private String name;
    private Date created = Calendar.getInstance().getTime();
    private boolean approved = false;
    private CustomThemeDTO properties;

    public CustomTheme(ObjectId creator, String name, CustomThemeDTO properties) {
        this.creator = creator;
        this.name = name;
        this.properties = properties;
    }

    public CustomTheme() {
    }

    public ObjectId getCreatorId() {
        return creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public CustomThemeDTO getProperties() {
        return properties;
    }

    public void setProperties(CustomThemeDTO properties) {
        this.properties = properties;
    }

    public User getCreator() {
        return DataService.get().get(UserRepository.class).getById(creator);
    }
}
