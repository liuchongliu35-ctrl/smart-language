package com.bing.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

public  class MultipartFileToFile {

    public static String savePhotoFile(MultipartFile multipartFile,String targetDirPath){
        File photoFile=null;
        if(photoFile.equals("")||photoFile.getTotalSpace()<=0){
            return null;
        }else {
//            获取原文件的名字
            String originalFilename = multipartFile.getOriginalFilename();
//            获取文件格式
            String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uuid = UUID.randomUUID().toString().trim().replace("-", "");
            photoFile=new File(targetDirPath+File.separator+uuid+fileType);

            String absolutPath=null;
            try {
                absolutPath=photoFile.getCanonicalPath();
//                判断绝对路径里的文件夹是否存在
                String dir = absolutPath.substring(0, absolutPath.lastIndexOf(File.separator));
                File dirFile=new File(dir);
                if(!dirFile.exists()){
                    dirFile.mkdirs();
                }
                System.out.println(absolutPath);
                InputStream inputStream = multipartFile.getInputStream();
                inputStreamToFile(inputStream,photoFile);
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
       return uuid+fileType;
        }
    }

    public static void inputStreamToFile(InputStream inputStream, File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            int len=0;
            byte[] bytes=new byte[8192];
            while ((len=inputStream.read(bytes,0,8192))!=-1){
                fileOutputStream.write(bytes,0,len);
            }
            fileOutputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
