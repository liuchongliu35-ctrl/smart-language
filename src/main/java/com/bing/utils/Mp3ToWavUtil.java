package com.bing.utils;

import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class Mp3ToWavUtil {
    public static AudioFormat targetFormat = null;

    /**
     * mp3转pcm(8k 16bit)
     * @param mp3filepath
     * @param pcmfilepath
     * @return
     */
    public static boolean Mp3ToPcm(String mp3filepath, String pcmfilepath){
        try {
            //获取文件的音频流，pcm的格式
            AudioInputStream audioInputStream = getPcmAudioInputStream(mp3filepath);
            //将音频转化为  pcm的格式保存下来
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(pcmfilepath));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取MP3音频流
     * @param mp3filepath
     * @return
     */
    private static AudioInputStream getPcmAudioInputStream(String mp3filepath) {
        File mp3 = new File(mp3filepath);
        AudioInputStream audioInputStream = null;

        try {
            AudioInputStream in = null;
            //读取音频文件的类
            MpegAudioFileReader mp = new MpegAudioFileReader();
            in = mp.getAudioInputStream(mp3);
            AudioFormat baseFormat = in.getFormat();
            //设定输出格式为pcm格式的音频文件
            targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
                    baseFormat.getChannels(), baseFormat.getChannels()*16/8, baseFormat.getFrameRate(), false);
            //输出到音频
            audioInputStream = AudioSystem.getAudioInputStream(targetFormat, in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return audioInputStream;
    }
    /**
     * pcm(8k 16bit)转wav(16k 16bit)
     * @param pcmfilepath
     * @param wavfilepath
     * @throws IOException
     */
    public static void pcmToWav(String pcmfilepath,String wavfilepath) throws IOException {
        FileInputStream fis = new FileInputStream(pcmfilepath);
        byte channels = (byte) targetFormat.getChannels();
        int sampleRate = (int) targetFormat.getSampleRate();
        int byteRate = targetFormat.getFrameSize()*sampleRate*channels/8;
        int datalen = (int)fis.getChannel().size();
        ByteBuffer bb = ByteBuffer.allocate(44);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(new byte[] {'R','I','F','F'});//RIFF标记
        bb.putInt(datalen+44-8);//原始数据长度（不包含RIFF和本字段共8个字节）
        bb.put(new byte[] {'W','A','V','E'});//WAVE标记
        bb.put(new byte[] {'f','m','t',' '});//fmt标记
        bb.putInt(16);//“fmt”字段的长度，存储该子块的字节数（不含前面的Subchunk1ID和Subchunk1Size这8个字节）
        bb.putShort((short)1);//存储音频文件的编码格式，PCM其存储值为1
        bb.putShort((short) targetFormat.getChannels());//通道数，单通道(Mono)值为1，双通道(Stereo)值为2
        //采样率
        bb.putInt(sampleRate);
        //音频数据传送速率,采样率*通道数*采样深度/8。(每秒存储的bit数，其值=SampleRate * NumChannels * BitsPerSample/8)
        bb.putInt(byteRate);
        //块对齐/帧大小，NumChannels * BitsPerSample/8
        bb.putShort((short)(targetFormat.getChannels()*16/8));
        //pcm数据位数，一般为8,16,32等
        bb.putShort((short)16);
        bb.put(new byte[] {'d','a','t','a'});//data标记
        bb.putInt(datalen);//data数据长度
        byte[] header = bb.array();
        //wav头
//        for(int i=0;i<header.length;i++) {
//            System.out.printf("%02x ",header[i]);
//        }
        ByteBuffer wavbuff = ByteBuffer.allocate(44+datalen);
        wavbuff.put(header);
        byte[] temp = new byte[datalen];
        fis.read(temp);
        wavbuff.put(temp);
        byte[] wavbytes = wavbuff.array();
        FileOutputStream fos = new FileOutputStream(wavfilepath);
        fos.write(wavbytes);
        fos.flush();
        fos.close();
        fis.close();
        System.out.println("end");
    }
}
