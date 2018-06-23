package org.zj.yourbatis.test;

import com.alibaba.druid.filter.AutoLoad;
import org.junit.jupiter.api.Test;
import org.omg.CORBA.ARG_OUT;
import org.zj.yourbatis.annotation.Autofired;
import org.zj.yourbatis.bean.Student;
import org.zj.yourbatis.core.Handle;
import org.zj.yourbatis.dao.StudentMapper;

import java.util.List;

/**
 * Created by ZhangJun on 2018/6/22.
 */
public class zhangjun {
    @Autofired
     StudentMapper studentMapper;

    public void n(){
        new Handle().handle();


        System.out.println(this);

        System.out.println(studentMapper.findAll().size()+"      我拿到数据了");
    }

    public zhangjun(){
        System.out.println("我被初始化:"+this);
    }

}
