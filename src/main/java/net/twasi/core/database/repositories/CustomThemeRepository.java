package net.twasi.core.database.repositories;

import net.twasi.core.database.lib.Repository;
import net.twasi.core.database.models.CustomTheme;
import net.twasi.core.database.models.User;
import net.twasi.core.graphql.model.customthemes.CustomThemeDTO;
import net.twasi.core.graphql.model.customthemes.StoreCustomThemeDTO;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

public class CustomThemeRepository extends Repository<CustomTheme> {

    private List<StoreCustomThemeDTO> map(List<CustomTheme> list, User queryingUser) {
        return list.stream().map(t -> new StoreCustomThemeDTO(t, queryingUser)).collect(Collectors.toList());
    }

    public List<StoreCustomThemeDTO> getThemesByUser(User user, int page) {
        return map(query().field("creator").equal(user.getId()).asList(paginated(page)), user);
    }

    public long countThemesByUser(User user) {
        return query().field("creator").equal(user.getId()).count();
    }

    public long countThemesByUser(User user, boolean approved) {
        return query().field("creator").equal(user.getId()).field("approved").equal(approved).count();
    }

    public List<StoreCustomThemeDTO> getAvailableThemes(int page, User user, boolean approvedOnly) {
        if (approvedOnly)
            return map(query().field("approved").equal(true).asList(paginated(page)), user);
        else
            return map(query().asList(paginated(page)), user);
    }

    public long countAvailableThemes(boolean approvedOnly) {
        if (approvedOnly)
            return query().field("approved").equal(true).count();
        else
            return count();
    }

    public List<StoreCustomThemeDTO> getInstalledThemesByUser(User user) {
        return map(query().field("_id").in(user.getInstalledThemes().stream().map(ObjectId::new).collect(Collectors.toList())).asList(), user);
    }

    public StoreCustomThemeDTO create(CustomThemeDTO properties, User user, String name) {
        CustomTheme theme = new CustomTheme(user.getId(), name, properties);
        add(theme);
        return new StoreCustomThemeDTO(theme, user);
    }

    public int delete(String id, User user) {
        return store.delete(query().field("creator").equal(user).field("_id").equal(new ObjectId(id))).getN();
    }

    public boolean themeExists(String themeId) {
        return query().field("_id").equal(new ObjectId(themeId)).count() > 0;
    }

    public boolean approveTheme(String themeId) {
        CustomTheme byId = query().field("_id").equal(new ObjectId(themeId)).get();
        if (byId == null) {
            return false;
        }
        byId.setApproved(true);
        commit(byId);
        return true;
    }
}
