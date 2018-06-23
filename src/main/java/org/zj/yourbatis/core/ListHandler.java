package org.zj.yourbatis.core;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhangJun on 2018/6/22.
 */
public class ListHandler<T> {
    public List<T> handle(Class c, ResultSet resultSet) throws Exception {
        Object obj=c.newInstance();
        List<T> result=new ArrayList<>();
       BeanHandler<T> beanHandler=new BeanHandler<>();
        while(resultSet.next()){
            System.out.println("添加到bean中");
            result.add(beanHandler.convertToBean(obj,resultSet));
        }
        return result;
    }
}
