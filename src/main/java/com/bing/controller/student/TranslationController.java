package com.bing.controller.student;

import com.bing.bean.ResponseData;
import com.bing.bean.Result;
import com.bing.common.ResourceService;
import com.bing.common.ResourceType;
import com.bing.service.pictureRecognition.PictureToText;
import com.bing.service.student.TranslationService;
import com.bing.service.translation.MachineTranslate;
import com.bing.utils.Mp3ToWavUtil;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("translate")
public class TranslationController<T> {

//    private static String audioPath;
    @Resource
    private Result<T> result;

    @Resource
    private TranslationService translation;

    @Resource
    private PictureToText pictureToText;

    @Resource
    private MachineTranslate machineTranslate;

    @Autowired
    private ResourceService resource;

    /**
     * 机器翻译模块
     *
     * 接受语音转文字，获取文字的翻译返回
     */
    @PostMapping("audio")
    public Result<String> getTranslationFromAudio(@RequestParam("file") MultipartFile multipartFile, @Param(value = "lang") String lang) {
        try {
            if (multipartFile != null && multipartFile.getContentType().startsWith("audio/")) {

//                audioPath = "E:\\EduPlatForm\\src\\main\\resources\\mp3path\\";
                String newName=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//                String pcmPath="E:\\EduPlatForm\\src\\main\\resources\\pcmpath"+newName+".pcm";
//                String wavPath="E:\\EduPlatForm\\src\\main\\resources\\wavpath"+newName+".wav";
                String pcmRootPath = resource.getResourcePath(ResourceType.PCMPATH, "").toString();//todo 先获取根路径
                String pcmPath=Paths.get(pcmRootPath,newName + ".pcm").toString();

                String wavRootPath = resource.getResourcePath(ResourceType.WAVPATH, "").toString();//todo 先获取根路径
                String wavPath = Paths.get(wavRootPath, newName + ".wav").toString();

//                 获取文件的后缀
                String name = multipartFile.getOriginalFilename();
                String subffix = name.substring(name.lastIndexOf(".") + 1, name.length());
//                FileCopyUtils.copy(multipartFile.getBytes(),audioFile);
                Path tmp3Rootpath = resource.getResourcePath(ResourceType.TMP3, "");//todo 先获取根路径
                String path=Paths.get(tmp3Rootpath.toString(),newName + "." + subffix).toString();
                File file = new File(path);
                System.out.println(file.getName());
                //将音频文件复制过来,存到本地
                multipartFile.transferTo(file);
//                将mp3文件转为wav文件！！！！
                System.out.println(file.getPath());
                Mp3ToWavUtil.Mp3ToPcm(file.getPath(),pcmPath);
                Mp3ToWavUtil.pcmToWav(pcmPath,wavPath);
                if(file.exists()){
//                    调用语音翻译接口
                    String translationText = translation.audioTranslation(wavPath,lang);
                    return result.success(translationText);
                }
            }else {
                return result.fail(null,"音频格式不正确！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.fail(null,"音频文件为空！");
    }

    /**
     * 机器翻译模块
     */
    @PostMapping("photo")
    @ResponseBody
    public Result getTranslationFromPhoto(@Param(value = "from") String from,@Param(value = "to") String to, @RequestParam("image") MultipartFile multipartFile) throws IOException
    {
        if (from==null||to==null) return result.fail(null,"语言参数不能为空");
        String text=null;
        String fileName = UUID.randomUUID().toString().trim().replace("-", "");
        String name = multipartFile.getOriginalFilename();
        String fix = name.substring(name.indexOf(".") + 1, name.length());
//        用相对路径
//        String relativePath="src/main/resources/picture";
        Path resourcePath;
        String photoPath= null;
        if(fix.equals("jpg")){
            resourcePath = resource.getResourcePath(ResourceType.JPG, "");//todo 先获取根路径
            photoPath=Paths.get(resourcePath.toString(),fileName + "." + fix).toString();//todo 再去组装文件路径
        }else if(fix.equals("png")){
            resourcePath = resource.getResourcePath(ResourceType.PNG, "");//todo 先获取根路径
            photoPath=Paths.get(resourcePath.toString(),fileName + "." + fix).toString();//todo 再去组装文件路径
        }

//            mp3Path="H:\\EduPlatForm\\src\\main\\resources\\MP3\\"+soundName;  src/main/resources/picture
//        String photoPath="H:\\EduPlatForm\\src\\main\\resources\\picture\\"+fileName+"."+fix;
//            获取图片中的文字//  目前只能识别中英文，所以from 只能是 en或者 cn
//        if(from.equals("en")||from.equals("cn")){
        File photoFile= null;
        if (photoPath != null) {
            photoFile = new File(photoPath);
            FileCopyUtils.copy(multipartFile.getBytes(),photoFile);
            text = pictureToText.photoToTextWithMoreLanguage(photoPath);
            System.out.println(text);
        }
//        FileCopyUtils.copy(multipartFile.getBytes(),photoFile);
//            text = pictureToText.photoToTextWithMoreLanguage(photoPath);
//            System.out.println(text);
//        }
//        else {
//            return result.fail(null,"目前只支持中英文识别！");
//        }
//            翻译图片中的文字//to 可以是正在学习的语言
            ResponseData translation = machineTranslate.getTranslation(from, to, text);
            if(translation!=null){
                return result.success(translation.getData().getResult().getTrans_result().getDst());
            }

        return result.fail(null,"图片上传失败，无法获取地址！");
    }

    /**
     * 机器翻译模块
     */
    @GetMapping("text")
    public Result getTranslationFromText(@Param(value = "text") String text,@Param(value = "from") String from,@Param(value = "to") String to ){
        if(text!=null&&from!=null&&to!=null){
            ResponseData translation = machineTranslate.getTranslation(from, to, text);
            return result.success(translation.getData().getResult()
                    .getTrans_result().getDst());
        }
        return result.fail(null,"参数不为空！");
    }
}
