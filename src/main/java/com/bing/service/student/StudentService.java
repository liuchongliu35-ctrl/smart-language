package com.bing.service.student;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bing.bean.Student;

public interface StudentService extends IService<Student> {

    /**
     * 初始化用户信息
     */
    public int iniUser(String userName,String language);
}
