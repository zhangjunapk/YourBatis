package org.zj.yourbatis.core;

import java.lang.reflect.Field;
import java.sql.ResultSet;

/**
 * Created by ZhangJun on 2018/6/22.
 */
public class BeanHandler<T> {
    public T convertToBean(Object c, ResultSet resultSet) throws Exception {
        Object o = c;

        System.out.println(o+"     需要数据的类");

        //遍历所有字段
        for(Field field:c.getClass().getDeclaredFields()){

            System.out.println(c+"      "+field+"     字段");

            System.out.println("       >"+field);
            field.setAccessible(true);
            field.set(o,getVal(field,resultSet));
        }
        return (T) o;
    }

    private Object getVal(Field field, ResultSet resultSet) {

        System.out.println(field.getType().getName()+"       类型");

        try{
            if(field.getType().getName().contains("String")){
                return resultSet.getString(field.getName());
            }
            if(field.getType().getName().contains("Integer")){
                return resultSet.getInt(field.getName());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
