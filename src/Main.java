import java.lang.reflect.InvocationTargetException;

/**
 * Created by zabor on 23.11.2016.
 */
public class Main {
    public void setUp(Number i) {
    }

    public Integer getUp() {
        return 1;
    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BeanUtils.assign(new Main(), new Main());
    }
}
