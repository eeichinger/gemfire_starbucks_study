package support;

/**
 * @author: Erich Eichinger
 * @date: 20/01/12
 */
public class Assert {
    public static void notNull(Object arg, String argName) {
        if (arg == null) {
            throw new NullPointerException(String.format("Missing argument '%s'", argName));
        }
    }
}
