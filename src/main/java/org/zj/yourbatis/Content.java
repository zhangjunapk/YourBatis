package org.zj.yourbatis;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.zj.yourbatis.bean.Student;
import org.zj.yourbatis.core.Handle;
import org.zj.yourbatis.core.JdkMethodInvocation;
import org.zj.yourbatis.core.ListHandler;
import org.zj.yourbatis.dao.StudentMapper;
import org.zj.yourbatis.test.zhangjun;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by ZhangJun on 2018/6/22.
 */
public class Content {
    public static void main(String[] args) throws Exception {

        Handle handle1 = new Handle();
        handle1.handle();
        Map<String, Object> instanceMap = handle1.instanceMap;


        DruidDataSource druidDataSource=new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/yourbatis");
        druidDataSource.setUsername("root");
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");

        System.out.println(druidDataSource);

        JdkMethodInvocation jdkMethodInvocation=new JdkMethodInvocation(StudentMapper.class,druidDataSource);
        Object o = Proxy.newProxyInstance(jdkMethodInvocation.getClass().getClassLoader(), new Class[]{StudentMapper.class}, jdkMethodInvocation);

        System.out.println(jdkMethodInvocation.getClass().getClassLoader()+"           "+StudentMapper.class.isInterface()+"   "+jdkMethodInvocation);

        StudentMapper studentMapper= (StudentMapper) o;
        List<Student> all = studentMapper.findAll();
        System.out.println(all.size()+"    返回结果大小");

        for(Method m:zhangjun.class.getMethods()){
            System.out.println("       一样的吧"+instanceMap.get("org.zj.yourbatis.test.zhangjun"));
        }



        DruidPooledConnection connection = druidDataSource.getConnection();
        Statement statement = connection.createStatement();

        String sql="select * from student";
        ResultSet resultSet = statement.executeQuery(sql);

        List<Student> handle = new ListHandler<Student>().handle(Student.class, resultSet);
        for(Student student:handle){
            System.out.println(student);
        }
    }


}
