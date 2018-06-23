package org.zj.yourbatis.dao;

import org.zj.yourbatis.annotation.Mapper;
import org.zj.yourbatis.annotation.Select;
import org.zj.yourbatis.bean.Student;

import java.util.List;

/**
 * Created by ZhangJun on 2018/6/22.
 */
@Mapper
public interface StudentMapper {
    @Select("select * from student")
    List<Student> findAll();
}
