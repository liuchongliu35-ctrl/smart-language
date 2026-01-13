package com.bing.service.student.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bing.bean.Student;
import com.bing.mapper.StudentMapper;
import com.bing.service.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public int iniUser(String userName, String language) {
        Student student = new Student();
        student.setAccount(userName);
        student.setLanguage(language);
        student.setPassword(String.valueOf(new Random().nextInt(900000) + 100000));
        int insert = studentMapper.insert(student);
        if(insert==1){
//            返回被插入的用户的uid
            QueryWrapper<Student> qw=new QueryWrapper<>();
            qw.select("uid").eq("password",student.getPassword());
            Student student1 = studentMapper.selectOne(qw);
            return student1.getUid();
        }
        return -1;
    }
}
