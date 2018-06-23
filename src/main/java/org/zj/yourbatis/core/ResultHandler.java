package org.zj.yourbatis.core;

import org.zj.yourbatis.annotation.Select;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Map;

/**
 * Created by ZhangJun on 2018/6/22.
 */
//处理一个类的所有方法,实例化后放到容器里
public class ResultHandler {
    public void handle(DataSource dataSource, Map<String, Object> map, Class c) throws Exception {
        for(Method method:c.getDeclaredMethods()){
            if(!method.isAnnotationPresent(Select.class)){
                continue;
            }

            String sql=method.getAnnotation(Select.class).value();

            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);



            //如果方法返回值是List
            if(method.getReturnType().isArray()){

            }
        }
    }
}
