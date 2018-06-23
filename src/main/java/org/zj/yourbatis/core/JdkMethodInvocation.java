package org.zj.yourbatis.core;

import org.zj.yourbatis.annotation.Select;

import javax.sql.DataSource;
import javax.swing.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhangJun on 2018/6/22.
 */
public class JdkMethodInvocation implements InvocationHandler {
    Object o;
    DataSource dataSource;

    public JdkMethodInvocation(Object o, DataSource dataSource){

        System.out.println("开始初始化");

        this.o=o;
        this.dataSource=dataSource;
;

        System.out.println("初始化完成");
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {



        System.out.println("我在 之前");

        if(method.isAnnotationPresent(Select.class)){

            Select annotation = method.getAnnotation(Select.class);
            String sql=annotation.value();
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            System.out.println("开始了");

            System.out.println(method.getReturnType());

            if(method.getReturnType().getName().contains("List")){

                Type genericReturnType = method.getGenericReturnType();

                String typeName=genericReturnType.getTypeName();

                System.out.println("  aa");

                typeName=typeName.substring(0,typeName.length()-1);
                typeName=typeName.substring(typeName.lastIndexOf("<")+1);

                Class c=Class.forName(typeName);


                //得到的数据
                List<Object> handle = new ListHandler<>().handle(c, resultSet);

                //还有怎么设置方法的返回值

                for(Object o:handle){
                    System.out.println(o);
                }
                return handle;
            }
        }
        return proxy;
    }
}
