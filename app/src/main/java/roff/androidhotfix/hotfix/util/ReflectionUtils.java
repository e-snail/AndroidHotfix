package roff.androidhotfix.hotfix.util;

import java.lang.reflect.Field;

/**
 * Created by wuyongbo on 16-4-9.
 */
public class ReflectionUtils {

    //获取classloader的Field
    public static Object getField(Object obj, Class<?> cl, String field)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
    {
        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);
        return localField.get(obj);
    }

    //设置classloader的Field
    public static void setField(Object obj, Class<?> cl, String field, Object value)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
    {
        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);
        localField.set(obj, value);
    }
}
