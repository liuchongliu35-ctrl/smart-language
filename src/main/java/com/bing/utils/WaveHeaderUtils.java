package com.bing.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class WaveHeaderUtils {
    public final char[] fileID = {'R', 'I', 'F', 'F'};
    public int fileLength;
    public char[] wavTag = {'W', 'A', 'V', 'E'};
    public char[] FmtHdrID = {'f', 'm', 't', ' '};
    public int FmtHdrLeth;
    public short FormatTag;
    public short Channels;
    public int SamplesPerSec;
    public int AvgBytesPerSec;
    public short BlockAlign;
    public short BitsPerSample;
    public char[] DataHdrID = {'d', 'a', 't', 'a'};
    public int DataHdrLeth;

    private void writeChar(ByteArrayOutputStream bos,char[] ch){
    for(char t:ch){
        bos.write(t);
    }
    }

    private void writeInt(ByteArrayOutputStream bos,int n) throws IOException {
        byte[] bytes=new byte[4];
        bytes[3]=(byte) (n>>24);
        bytes[2]=(byte) ((n<<8)>>24);
        bytes[1]=(byte) ((n<<16)>>24);
        bytes[0]=(byte) ((n<<24)>>24);
        bos.write(bytes);
    }

    private void writeShort(ByteArrayOutputStream bos,int n) throws IOException {
        byte[] sh=new byte[2];
        sh[1]=(byte) ((n<<16)>>24);
        sh[0]=(byte) ((n<<24)>>24);
        bos.write(sh);
    }
//该方法将header的头部信息先暂时存放到 ByteArrayOutputStream中，再将 ByteArrayOutputStream转成byte数组返回
    public byte[] getHeader() throws IOException {
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
//        下面的操作是将头部的参数变为二进制的形式
        writeChar(bos,fileID);
        writeInt(bos,fileLength);
        writeChar(bos,wavTag);
        writeChar(bos,FmtHdrID);
        writeInt(bos,FmtHdrLeth);
        writeShort(bos,FormatTag);
        writeShort(bos,Channels);
        writeInt(bos,SamplesPerSec);
        writeInt(bos,AvgBytesPerSec);
        writeShort(bos,BlockAlign);
        writeShort(bos,BitsPerSample);
        writeChar(bos,DataHdrID);
        writeInt(bos,DataHdrLeth);
        bos.flush();
        byte[] byteArray = bos.toByteArray();
        bos.close();
        return byteArray;
    }
}
