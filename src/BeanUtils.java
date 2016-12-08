import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zabor on 23.11.2016.
 */
public class BeanUtils {
    /**
     * Scans object "from" for all getters. If object "to"
     * contains correspondent setter, it will invoke it
     * to set property value for "to" which equals to the property
     * of "from".
     * <p/>
     * The type in setter should be compatible to the value returned
     * by getter (if not, no invocation performed).
     * Compatible means that parameter type in setter should
     * be the same or be superclass of the return type of the getter.
     * <p/>
     * The method takes care only about public methods.
     *
     * @param to   Object which properties will be set.
     * @param from Object which properties will be used to get values.
     */
    public static void assign(Object to, Object from) throws InvocationTargetException, IllegalAccessException {
        Method[] fromMethods = from.getClass().getMethods();
        List<Method> fromGetters = new ArrayList<>();
        Method[] toMethods = to.getClass().getMethods();
        List<Method> toSetters = new ArrayList<>();

        for (Method toMethod : toMethods) {
            if (isSetter(toMethod)) {
                toSetters.add(toMethod);
            }
        }

        for (Method fromMethod : fromMethods) {
            if (isGetter(fromMethod)) {
                fromGetters.add(fromMethod);
            }
        }

        for (Method fromGetter : fromGetters) {
            for (Method toSetter : toSetters) {
                if (arePairMethods(fromGetter, toSetter)) {
                    Object valueToSet = fromGetter.invoke(from);
                    toSetter.invoke(to, valueToSet);
                }
            }
        }


    }

    private static boolean isGetter(Method m) {
        return (m.getName().matches("^(get).+$") && m.getParameterCount() == 0 && !m.getReturnType().equals(void.class));
    }

    private static boolean isSetter(Method m) {
        return m.getName().matches("^(set).+$");
    }

    private static boolean arePairMethods(Method getter, Method setter) {
        return namesArePaired(getter, setter) && typesArePaired(getter, setter);
    }

    private static boolean namesArePaired(Method getter, Method setter) {
        return getter.getName().substring(2).equals(setter.getName().substring(2));
    }

    private static boolean typesArePaired(Method getter, Method setter) {
        return setter.getParameterTypes()[0].isAssignableFrom(getter.getReturnType());
    }


}

