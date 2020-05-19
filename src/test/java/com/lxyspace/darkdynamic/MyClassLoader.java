package com.lxyspace.darkdynamic;

/**
 * @author luxinyu
 * @date 2020/5/19 16:16
 * @description new Class
 */
public class MyClassLoader extends ClassLoader {

    public Class<?> defineClassPublic(String name, byte[] b, int off, int len) throws ClassFormatError {
        Class<?> clazz = defineClass(name, b, off, len);
        return clazz;
    }

}
