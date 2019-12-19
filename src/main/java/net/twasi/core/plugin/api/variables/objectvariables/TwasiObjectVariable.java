package net.twasi.core.plugin.api.variables.objectvariables;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.models.Message.TwasiMessage;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.variables.TwasiVariableBase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class TwasiObjectVariable<T> extends TwasiVariableBase {

    public TwasiObjectVariable(TwasiUserPlugin owner) {
        super(owner);
    }

    @Override
    public final String process(String name, TwasiInterface inf, String[] params, TwasiMessage message) {
        String path;
        if (params.length == 0) {
            if (getClass().isAnnotationPresent(Properties.class)) {
                Properties properties = getClass().getAnnotation(Properties.class);
                path = properties.key();
            } else return "NO_FIELD_PRESENT";
        } else path = params[0];
        try {
            List<String> actualParams = new ArrayList<>(Arrays.asList(params).subList(1, params.length));
            return resolveFields(path, getObject(name, inf, actualParams, message));
        } catch (Exception e) {
            return "ERROR";
        } catch (ForbiddenException e) {
            return "FORBIDDEN";
        }
    }

    public abstract T getObject(String name, TwasiInterface inf, List<String> params, TwasiMessage message);

    private String resolveFields(String fields, T object) throws Exception, ForbiddenException {
        String[] splitted = fields.split(".");
        Object currentObject = object;

        for (String part : splitted) {
            currentObject = resolveField(part, currentObject);
        }

        return resolveObject(currentObject);
    }

    private Object resolveField(String field, Object object) throws Exception, ForbiddenException {
        boolean found = false;
        String exactName = null;

        // Try to resolve as field
        outerLoop:
        for (boolean insensitive : Arrays.asList(false, true)) // first case sensitive, then case insensitive
            for (Field f : object.getClass().getFields()) {
                // Get name
                String name = f.getName();

                // Test for match
                if (!((insensitive && name.equalsIgnoreCase(field)) || (!insensitive && name.equals(field))))
                    continue;

                // Check whether resolving this object is allowed
                if (!f.isAnnotationPresent(Resolvable.class))
                    throw new ForbiddenException();

                // Break loop to resolve
                found = true;
                exactName = f.getName();
                break outerLoop;
            }

        if (found)
            return object.getClass().getField(exactName).get(object);

        // Nothing found, now try to resolve as method/function
        outerLoop:
        for (boolean insensitive : Arrays.asList(false, true)) // first case sensitive, then case insensitive
            for (String prefix : Arrays.asList("", "get", "has", "is")) // First without prefix, then with common function Prefixes
                for (Method m : object.getClass().getMethods()) {
                    // Generate name
                    String name = m.getName();
                    name = prefix.equals("") ? name : prefix + name.toUpperCase().charAt(0) + name.substring(1);

                    // Test for match
                    if (!((insensitive && name.equalsIgnoreCase(field)) || (!insensitive && name.equals(field))))
                        continue;

                    // Check whether resolving this object is allowed
                    if (!m.isAnnotationPresent(Resolvable.class)) throw new ForbiddenException();

                    // Break loop to resolve
                    found = true;
                    exactName = m.getName();
                    break outerLoop;
                }

        if (found)
            return object.getClass().getMethod(exactName).invoke(object);

        // Nothing found so let's return null
        return null;
    }

    private String resolveObject(Object object) {
        if (object instanceof String) return (String) object;
        if (object instanceof Number) return String.valueOf(object);
        return object.toString();
        // TODO resolve more types
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Properties {
        String key() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.METHOD})
    public @interface Resolvable {
    }
}
