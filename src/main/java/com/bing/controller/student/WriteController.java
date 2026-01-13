package com.bing.controller.student;

import com.bing.VO.WriteText;
import com.bing.bean.Result;
import com.bing.common.ResourceService;
import com.bing.common.ResourceType;
import com.bing.observe.AnswerHandler;
import com.bing.service.pictureRecognition.PictureToText;
import com.bing.service.student.WriteUtil;
import com.bing.websocket.xfModel.XFWebSocketClientListener;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("write")
public class WriteController<T> {
    @Resource
    private Result<T> result;

    @Resource
    private WriteUtil writeUtil;

    @Resource
    private PictureToText pictureToText;

    @Autowired
    private ResourceService resource;
    /**
     * 智能写作模块
     *
     * 接受文字生成作文ai批改的结果
     * @param text
     * @return
     */
    @PostMapping ("result")
    public Result<List<String>> getWriteResultFromText(@RequestBody WriteText text){
        if(text.getText()==null) {
        return result.fail(null,"参数为空");
        }
        List<String> writeResult = writeUtil.dealWriteResult(1, text.getText());
        return result.success(writeResult);
    }

    /**
     * 智能写作模块
     *
     * 接受作文图片生成ai批改结果
     */
    @PostMapping("photo")
    public Result<List<String>> getWriteResultFromPhoto(@RequestParam(value = "file") MultipartFile multipartFile) throws IOException {
        String fileName = UUID.randomUUID().toString().trim().replace("-", "");
//        String relativePath="src/main/resources/picture";
        String name = multipartFile.getOriginalFilename();
        assert name != null;
        String type = name.substring(name.lastIndexOf(".") + 1, name.length());
        Path path = null;
        Path rootPath = null;
//        构建图片的根路径
        if(type.equals("jpg")){
            rootPath= resource.getResourcePath(ResourceType.JPG, "");//todo 先构建根路径
        }else if(type.equals("png")){
            rootPath= resource.getResourcePath(ResourceType.PNG, "");
        }
//        组装图片文件路径
        if (rootPath != null) {
            path=Paths.get(rootPath.toString(),fileName + "." + type);
        }
        String photoPath = null;
        if (path != null) {
            photoPath = path.toString();
            File photoFile=new File(photoPath);
            FileCopyUtils.copy(multipartFile.getBytes(),photoFile);
        }
//        String photoPath="H:\\EduPlatForm\\src\\main\\resources\\picture\\"+fileName+".png";
//            获取图片中的文字
        String text = pictureToText.photoToText(photoPath);
        List<String> writeResult = writeUtil.dealWriteResult(1, text);
        if (writeResult==null) return result.fail(null,"结果获取失败！");
        return result.success(writeResult);
    }

}
