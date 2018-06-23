package org.zj.yourbatis.core;

import com.alibaba.druid.pool.DruidDataSource;
import org.zj.yourbatis.annotation.Autofired;
import org.zj.yourbatis.annotation.DataSource;
import org.zj.yourbatis.annotation.Mapper;
import org.zj.yourbatis.annotation.MapperScan;
import org.zj.yourbatis.dao.StudentMapper;

import java.awt.*;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;

/**
 * Created by ZhangJun on 2018/6/22.
 */
public class Handle {
    DruidDataSource druidDataSource=new DruidDataSource();
    List<String> packages=new ArrayList<>();
    List<Class<?>> classes=new ArrayList<>();
   public Map<String,Object> instanceMap=new HashMap<>();

    public void handle(){
        //先扫描配置类获得包名
        try {
            //获得包名并获得数据源
            init();
            System.out.println("初始化完毕");
            //扫描所有包，将类放到容器里
            getClasses();
            System.out.println("已经放到容器");
            //遍历容器里的class,对其进行实例化，放到容器里

            instance();



            //System.out.println("已经实例化");

            //对所有使用的地方进行注入
            doDI();
            //System.out.println("已经注入");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doDI() {
        //遍历所有并进行注入

        System.out.println ("       >"+classes.size()+"     共有这么多类");

        for(Class c:classes){
            //System.out.println("      " +c.getName()+"   处理");

           // System.out.println(c.getDeclaredFields().length+"      字段大小");




            for(Field field:c.getDeclaredFields()){

                System.out.println(field+"       到这里了 判断是否加了注解");

                System.out.println(field.isAnnotationPresent(Autofired.class)+"        是否加了注解");

                if(field.isAnnotationPresent(Autofired.class)){
/*

                    JdkMethodInvocation jdkMethodInvocation=new JdkMethodInvocation(field.getType(),druidDataSource);
                    Object o = Proxy.newProxyInstance(jdkMethodInvocation.getClass().getClassLoader(), new Class[]{field.getType()}, jdkMethodInvocation);
*/

                    System.out.println("代理了 使用jdk");
                    try {

//                        System.out.println(instanceMap.get(field.getType().getName())+"  isntancemap中的值");

                        System.out.println("            字段类名"+field.getType().getName());
                        for(String key:instanceMap.keySet()){
                            System.out.println("          >"+key);
                        }
                        field.setAccessible(true);

                       // System.out.println("<><><         "+instanceMap.get(field.getType().getName())+"        这是得到的对象");

                        System.out.println("         我在呢我给你了"+field.getType().getName());

                        System.out.println(c.getName());

                        field.set(instanceMap.get(c.getName()),instanceMap.get(field.getType().getName()));

                        System.out.println(instanceMap.get(c.getName())+":");

                        System.out.println("我这是住进去了吧");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    //对容器里的class进行初始化并放到容器里
    public void instance(){
        for(Class c:classes){

            if(!c.isInterface()){

                try {
                    Object o = c.newInstance();

                    System.out.println("<<<<<<<<<"+c.getName()+":"+o);

                    instanceMap.put(c.getName(),o);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                continue;

            }

            System.out.println("          ><><><>"+c.getName());

            //if(!c.isAnnotationPresent(Mapper.class)){continue;}
            System.out.println("           >我肯定走了啊         ");

            System.out.println("接口名          >>>>"+c.getName()+"        接口名");

            System.out.println(c.isInterface()+"  是不是接口");

            JdkMethodInvocation jdkMethodInvocation=new JdkMethodInvocation(c,druidDataSource);
            Object o = Proxy.newProxyInstance(jdkMethodInvocation.getClass().getClassLoader(), new Class[]{c}, jdkMethodInvocation);

            System.out.println("           >>>>>>>>"+c.getName());
            instanceMap.put(c.getName(),o);

            System.out.println(o==null+"      >初始化的key"+c.getName()+"     ");

            System.out.println(instanceMap.size()+"      大小");
        }
    }

    public void getClasses(){
        //遍历所有类
        for(String packageName:packages){
            //获得class
            System.out.println("          >"+packageName+"  包名");
             packageName= packageName.replace(".","/");
            File f=new File(getSrcPath()+"/"+packageName);
            inflateClazz(classes,f);
        }
    }

    /**
     * 将类通过反射得到引用，然后放到容器
     *
     * @param classes
     * @param file
     */
    private void inflateClazz(List<Class<?>> classes, File file) {
        System.out.println(file.getAbsolutePath() + "路径");

        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                inflateClazz(classes, f);
            }
        }


        if (file.isFile()) {

            String name = file.getAbsolutePath();
            name = name.replace("\\", "/");
            name = name.replace(getSrcPath(), "");
            System.out.println(getSrcPath() + "-    替换掉项目名后-" + name);
            name = name.replace("/", ".");
            name = name.substring(1);
            System.out.println(name + "     这是类名哦");
            try {
                Class clazz = Class.forName(name.substring(0, name.lastIndexOf(".java")));
                classes.add(clazz);
            } catch (ClassNotFoundException e) {
                return;
            }
        }
    }


    private void init () throws ClassNotFoundException {
        String path=getSrcPath();

        File file=new File(path);

        System.out.println(path+" 路径");

        for(File f:file.listFiles()){
            if(f.isFile()){
                File absoluteFile = f.getAbsoluteFile();
                String className=absoluteFile.getAbsolutePath().replace(getSrcPath(),"");
                className= className.replace("\\","/");
                className=className.replace(getSrcPath(),"");
                System.out.println(className+"   替换项目名后"+getSrcPath());

                className=className.replace("\\",".");
                System.out.println(className);

                Class<?> aClass = Class.forName(className.substring(1,className.lastIndexOf(".")));

                System.out.println(aClass.getName()+"  配置类名");

                if(aClass.isAnnotationPresent(org.zj.yourbatis.annotation.MapperScan.class)){
                    //把包名放进去
                    for(String s:aClass.getAnnotation(MapperScan.class).value()){
                        packages.add(s);
                    }

                }
                if(aClass.isAnnotationPresent(DataSource.class)){
                    DataSource dataSource=aClass.getAnnotation(DataSource.class);
                    setDataSource(dataSource,druidDataSource);
                }

            }
        }


    }

    private void setDataSource(DataSource dataSource, DruidDataSource druidDataSource) {
        String username=dataSource.username();
        String password=dataSource.password();
        String url=dataSource.url();
        String driver=dataSource.driver();
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setDriverClassName(driver);
    }


    /**
     * 获得源码的绝对路径
     *
     * @return
     */
    private String getSrcPath() {
        String path = null;
        try {
            path = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath();
           // System.out.println(path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

       // System.out.println(path);

        path = path.substring(1, path.indexOf("/target/classes")) + "/src/main/java";
        //System.out.println(path);
        return path;
    }
}
