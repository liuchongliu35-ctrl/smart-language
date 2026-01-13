package com.bing.service.student;


import com.bing.bean.Province;
import com.bing.dto.Response;

import java.util.List;

public interface MapService {
    List<Province> getAllProvinces();
    Province getProvinceByCode(String code);
    Province getProvinceByName(String name);
    void updateProvince(Province province);
    void deleteProvince(String name);
    void addProvince(Province province);
    Response showProvince1();
} 