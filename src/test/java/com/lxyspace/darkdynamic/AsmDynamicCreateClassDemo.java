package com.lxyspace.darkdynamic;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author luxinyu
 * @date 2020/5/19 14:37
 * @description new Class
 */
public class AsmDynamicCreateClassDemo {

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {

        ClassWriter classWriter = new ClassWriter(0);
        // 通过visit 方法确定类的头部信息。
        classWriter.visit(Opcodes.V1_8,//java版本
                Opcodes.ACC_PUBLIC,//类修饰符
                "Programmer",//类的全限定名
                null,
                "java/lang/Object",
                null);
        //创建构造函数
        MethodVisitor mv = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>",
                "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.AALOAD,0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>","()V");
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
        //定义code 方法
        // 定义code方法
        MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "code", "()V",
                null, null);
        methodVisitor.visitCode();
        methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out",
                "Ljava/io/PrintStream;");
        methodVisitor.visitLdcInsn("I'm a Programmer,Just Coding.....");
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
                "(Ljava/lang/String;)V");
        methodVisitor.visitInsn(Opcodes.RETURN);
        methodVisitor.visitMaxs(2, 2);
        methodVisitor.visitEnd();
        classWriter.visitEnd();
        // 使classWriter类已经完成
        // 将classWriter转换成字节数组写到文件里面去
        byte[] data = classWriter.toByteArray();
        String path = "/Users/luxinyu/IdeaProjects/darkdynamic/target/classes/com/lxyspace/darkdynamic";
        File file = new File(path);
        if (!file.exists()){
            file.mkdirs();
        }
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file+"/Programmer.class");
            fout.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            fout.close();
        }
//        // 反射生成对象调用并调用方法。。
//        InputStream in = new BufferedInputStream(new FileInputStream(path));
//        Class<?> aClass = Class.forName("com.lxyspace.darkdynamic.asm.Programmer");
//        Constructor<?> declaredConstructor = aClass.getDeclaredConstructor();
//        Object object = declaredConstructor.newInstance();
//        Class<?> aClass = Class.forName("com.lxyspace.darkdynamic.Programmer.class");
//        Class.getClassLoader().getResourceAsStream(String path)
        ///Users/luxinyu/IdeaProjects/darkdynamic/target/classes/com/lxyspace/darkdynamic/Programmer.class
//        ClassLoader.getSystemResource("");

        //直接将二进制流加载到内存中
        MyClassLoader classLoader = new MyClassLoader();
        String fullNameType = "Programmer";
//        String fullNameType = "sdasd";
        Class<?> cls = classLoader.defineClassPublic(fullNameType, data, 0, data.length);
//        Object o = cls.newInstance();
        Constructor<?> declaredConstructor = cls.getDeclaredConstructor();
        Object o1 = declaredConstructor.newInstance();
        //调用setName赋值
        Method codeMd = cls.getMethod("code");
        Object invoke = codeMd.invoke(o1);
        System.out.println(invoke);

//        Method setNameMethod = cls.getMethod("setName", String.class);
//        setNameMethod.invoke(o, "k0bin");
//        //调用getName方法查看值
//        Method getNameMethod = cls.getMethod("getName");
//        Object name = getNameMethod.invoke(o);
//        System.out.println(name);

//        Constructor<?> declaredConstructor = aClass.getDeclaredConstructor();
//        Object o = declaredConstructor.newInstance();
//        Method[] methods = aClass.getMethods();
//        for (Method method : methods) {
//            System.out.println(method.getName());
//        }

    }
}
