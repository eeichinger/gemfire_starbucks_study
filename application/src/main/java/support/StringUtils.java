package support;

import java.util.Collection;

/**
 * @author: Erich Eichinger
 * @date: 22/01/12
 */
public class StringUtils {
    
    public static String join(Collection coll, Object sep) {
        if (coll == null || coll.size() == 0) return "";
        boolean first = true;
        StringBuffer sb = new StringBuffer();
        for(Object el:coll) {
            if (first) {
                first = false;
                sb.append(el);
            } else {
                sb.append(el).append(sep);
            }
        }
        return sb.toString();
    }
}
