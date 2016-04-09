package roff.androidhotfix.hotfix.util;

import java.lang.reflect.Array;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by wuyongbo on 16-4-9.
 */
public class DexUtils {

    /**
     * 将patch的dex注入到host dex中，从而实现方法的替换
     *
     * @param dexPath
     * @param defaultDexOptPath
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public static void injectWithHostDex(String dexPath, String defaultDexOptPath)
            throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException
    {
        DexClassLoader dexClassLoader = getDexClassLoader(dexPath, defaultDexOptPath, dexPath, getPathClassLoader());
        //获取应用默认的classloader的elements
        Object baseDexElements = getDexElements(getPathList(getPathClassLoader()));
        //获取应用默认的dex classloader的elements
        Object newDexElements = getDexElements(getPathList(dexClassLoader));
        //合并以上两部分elements　保证patch dex的elements在前
        Object allDexElements = combineArray(newDexElements, baseDexElements);

        Object pathList = getPathList(getPathClassLoader());

        //给PathClassLoader设置新的elements
        ReflectionUtils.setField(pathList, pathList.getClass(), "dexElements", allDexElements);
    }

    /**
     * DexClassLoader可以从包含classes.dex的apk或jar文件中加载class文件；因此可以用作加载未安装的apk．
     * 该loader需要有写权限的目录去缓存优化过的class文件，建议使用Context.getCodeCacheDir()；
     * 处于权限控制的原因，不建议使用外存．
     *
     * @param dexPath   要加载的包含dex文件的apk或jar文件路径
     * @param optimizedDirectory    目标dex文件的路径
     * @param libraryPath   需要的library文件路径　可以是null
     * @param parent    父class loader是一个PathClassLoader
     */
    private static DexClassLoader getDexClassLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent)
    {
        DexClassLoader dexClassLoader = new DexClassLoader(dexPath, optimizedDirectory, libraryPath, parent);
        return dexClassLoader;
    }

    /**
     * PatchClassLoader只能加载本应用目录下的文件，也就是说只能加载已经安装的apk里的dex文件；一般用作系统和应用的类加载器．
     * 为什么能从.class文件中强转？？？
     * @return
     */
    private static PathClassLoader getPathClassLoader()
    {
        PathClassLoader pathClassLoader = (PathClassLoader) DexUtils.class.getClassLoader();
        return pathClassLoader;
    }

    /**
     * 获取classloader的所有{@code Element} dexElements  {@code Field}
     *
     *
     * @param paramObject
     * @return
     * @throws IllegalArgumentException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Object getDexElements(Object paramObject)
            throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException
    {
        return ReflectionUtils.getField(paramObject, paramObject.getClass(), "dexElements");
    }

    /**
     * 获取class loader的pathList {@code Field}
     *
     * @param baseDexClassLoader
     * @return
     * @throws IllegalArgumentException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    private static Object getPathList(Object baseDexClassLoader)
            throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException
    {
        return ReflectionUtils.getField(baseDexClassLoader, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }

    //合并elements数组
    private static Object combineArray(Object firstArray, Object secondArray)
    {
        Class<?> localClass = firstArray.getClass().getComponentType();
        int firstArrayLength = Array.getLength(firstArray);
        int allLength = firstArrayLength + Array.getLength(secondArray);
        Object result = Array.newInstance(localClass, allLength);
        for (int k = 0; k < allLength; ++k) {
            if (k < firstArrayLength) {
                Array.set(result, k, Array.get(firstArray, k));
            } else {
                Array.set(result, k, Array.get(secondArray, k - firstArrayLength));
            }
        }
        return result;
    }
}
