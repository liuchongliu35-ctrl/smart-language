package com.bing.controller.student;

import com.bing.VO.PrimerWords;
import com.bing.bean.Result;
import com.bing.bean.ResultCodeEnum;
import com.bing.common.ResourceService;
import com.bing.common.ResourceType;
import com.bing.exception.SoundException;
import com.bing.service.soundSynthesis.XFSoundSynthesis;
import com.bing.service.student.PrimerService;
import com.bing.utils.PathUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

@CrossOrigin
@RequestMapping("student")
@RestController
public class PrimerController<T> {
    private static String mp3Path=null;
    private static final Logger log= LoggerFactory.getLogger(PrimerController.class);
    @Autowired
    private PrimerService primerService;

    @Autowired
    private XFSoundSynthesis xfSoundSynthesis;

    @Autowired
    private ResourceService resource;

    @Autowired
    private Result<T> result;
    /**
     * 语音获取模块
     *
     * 涉及查询操作，查询单词的音频地址
     * word是前端需要获得的单词的语音文件，该controller中主要是从数据库中获取该单词的语音地址
     * @param word
     */
    @GetMapping("mp3")
    public void sendMp3File(@Param(value = "word") String word,@Param(value = "cid") Integer cid,HttpServletRequest request, HttpServletResponse response,@Param(value = "lang") String lang,@Param(value = "soundName") String soundName) throws IOException {
//        方式一：从数据库中获取音频(word:获取音标的音频//word+uid获取单词本中的音频)
        String wordSoundPath = null;
        File mp3Music=null;
        if (word != null && lang == null) {
//            Termite
//            69
//            src\main\resources\MP3\1720788014137.mp3
            // todo 从数据库中获取该单词的语音地址,这是音标发音
            String wavePathByWord = primerService.getWavePathByWord(word);
//            提取文件名
            String name = PathUtil.getName(wavePathByWord);
            mp3Path = resource.getResourcePath(ResourceType.MP3, name).toString();
            mp3Music = new File(mp3Path);
            if (wavePathByWord.equals("")) {
//            throw new NullPointerException("未找到指定的单词！");
                log.error("未找到指定的" + word);

            }
//        方式二：纯靠音频路径获取音频数据
        } else if (word != null && lang != null) {
            if (cid == null) throw new SoundException("cid不能为空");
//            实时生成音频
//            todo 先根据word和cid查找该单词是否有音频,这是单词发音
            wordSoundPath = primerService.checkWavePath(word, cid);
            if (wordSoundPath == null) {
                String mp3Path = xfSoundSynthesis.baseSoundSynthesis(word, lang);
                File mp3Name = new File(mp3Path);
                mp3Path = resource.getResourcePath(ResourceType.MP3, mp3Name.getName()).toString();
                System.out.println("新的音频路径"+mp3Path);
//                更新单词的音频
                primerService.addSound(word, cid, mp3Path);
                mp3Music = new File(mp3Path);
                System.out.println(mp3Music);
            } else {
//                提取文件名
                String mp3Name = PathUtil.getName(wordSoundPath);
                mp3Path = resource.getResourcePath(ResourceType.MP3, mp3Name).toString();
                System.out.println("音频路径"+mp3Path);
                mp3Music = new File(mp3Path);
                System.out.println(mp3Music);
            }
//            soundName用于返回情景对话的音频
        } else if (soundName != null) {
//            "src/main/resources/MP3/"+soundName;
            mp3Path = resource.getResourcePath(ResourceType.MP3, soundName).toString();
            System.out.println("音频路径"+mp3Path);
            mp3Music = new File(mp3Path);
            System.out.println(mp3Music);
        }
//        判断该文件是否存在

        if (!mp3Music.exists() && wordSoundPath != null) {
//            如果不存在就在线生成音频更新到数据库中
            String mp3Path = xfSoundSynthesis.baseSoundSynthesis(word, lang);
            File mp3Name = new File(mp3Path);
            mp3Path = resource.getResourcePath(ResourceType.MP3, mp3Name.getName()).toString();
//                更新单词的音频
            primerService.addSound(word, cid, mp3Path);
            System.out.println("新的音频路径"+mp3Path);
            mp3Music = new File(mp3Path);
            System.out.println(mp3Music);
//            log.error(mp3Path+"文件不存在，请求重新操作！");
////            return result.fail(null,"该单词的音频文件不存在");
//            throw new SoundException(mp3Path+"文件不存在，请求重新操作！");
        }
//        mp3Music= new File(mp3Path);
//        if (!mp3Music.exists()) {
//            log.error(mp3Path+"文件不存在，请求重新操作！");
//            throw new SoundException(mp3Path+"文件不存在，请求重新操作！");
//        }
//        确定要下载的长度，end-start
        String range = request.getHeader("Range");
        long start = 0;
        long end = mp3Music.length() - 1;
        if (range != null && range.contains("bytes=") && range.contains("-")) {
            range = range.substring(range.lastIndexOf("=") + 1).trim();
            String[] ranges = range.split("-");
            try {
                if (ranges.length == 1) {
                    if (range.startsWith("-")) {
                        end = Long.parseLong(ranges[0]);
                    } else if (range.endsWith("-")) {
                        start = Long.parseLong(ranges[0]);
                    }
                } else if (ranges.length == 2) {
                    start = Long.parseLong(ranges[0]);
                    end = Long.parseLong(ranges[1]);
                }
            } catch (NumberFormatException e) {
                start = 0;
                end = mp3Music.length() - 1;
            }
        }
//要下载的长度
        long contentLength = end - start + 1;
//        文件名
        String fileName = mp3Music.getName();
//        文件类型
        String fileType = request.getServletContext().getMimeType(fileName);
        response.setHeader("Accept-Ranges", "bytes");
        response.setStatus(206);
        response.setContentType(fileType);
        response.setHeader("Content-Type", fileType);
        response.setHeader("Content-Disposition", "inline;filename=sound.mp3");
        response.setHeader("Content-Length", String.valueOf(contentLength));
        //坑爹地方三：Content-Range，格式为
        // [要下载的开始位置]-[结束位置]/[文件总大小]
        response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + mp3Music.length());

//      开始发送文件
        BufferedOutputStream bos = null;
//        RandomAccessFile该类允许你读取(read，mode是“r”)或写入（write，mode是“w”）数据
        RandomAccessFile randomAccessFile = null;
        long transmitted = 0;
        try {
//
//          r表示读取的意思
            randomAccessFile = new RandomAccessFile(mp3Music, "r");
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] bytes = new byte[4096];
            int len = 0;
//            seek表示将文件的读取位置移到start的位置
            randomAccessFile.seek(start);
//            transmitted+len表示已下载的进度加上本次即将要下载的长度len，如果没有该判断会超出
            while ((transmitted + len) <= contentLength && (len = randomAccessFile.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
                transmitted += len;
                Thread.sleep(100);
            }
//            用来处理不满足transmitted+len<=contentLength条件时，未下载的部分
            if (transmitted < contentLength) {
                len = randomAccessFile.read(bytes, 0, (int) (contentLength - transmitted));
                bos.write(bytes, 0, len);
                transmitted += len;
            }
            bos.flush();
            response.flushBuffer();
            randomAccessFile.close();
            System.out.println("下载完毕" + start + "-" + end + ":" + transmitted);
        } catch (ClientAbortException e) {
            System.out.println("用户停止下载" + start + "-" + end + ":" + transmitted);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        return result.success(null);
    }

    /**
     * 音标模块：获取指定语音的所有基础发音和音标
     * @param lang
     * @return
     */
//    查询指定语言的音标数据，包括音标的写法、音频，读法等数据
    @GetMapping("primerWords")
    public Result<List<PrimerWords>> getPrimerWords(@Param(value = "lang") String lang){
        List<PrimerWords> pWords= primerService.getAllByLanguage(lang);
        if(pWords==null) return result.build(null, ResultCodeEnum.FAIL);
        return result.success(pWords);
    }
}
