package com.bing.controller.parents;


import com.bing.bean.Province;
import com.bing.dto.Response;
import com.bing.service.student.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/api/provinces")
public class MapController {

    private final MapService mapService;

    @Autowired
    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/province/1")
    public Response showProvince1(){
        return mapService.showProvince1();
    }

    @GetMapping
    public Response<List<Province>> getAllProvinces() {
        return Response.success(mapService.getAllProvinces());
    }

    @GetMapping("/name/{name}")
    public Response<Province> getProvinceByName(@PathVariable String name) {
        Province province = mapService.getProvinceByName(name);
        return province != null ? Response.success(province) : Response.error(404, "Province not found");
    }

    @PostMapping
    public Response<Void> addProvince(@RequestBody Province province) {
        mapService.addProvince(province);
        return Response.success(null);
    }

    @PutMapping
    public Response<Void> updateProvince(@RequestBody Province province) {
        mapService.updateProvince(province);
        return Response.success(null);
    }

    @DeleteMapping("/{name}")
    public Response<Void> deleteProvince(@PathVariable String name) {
        mapService.deleteProvince(name);
        return Response.success(null);
    }
} 