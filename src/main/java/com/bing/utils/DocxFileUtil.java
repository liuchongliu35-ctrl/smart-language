package com.bing.utils;

import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.xmlbeans.XmlException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DocxFileUtil {
    private static String docxPath;

    public DocxFileUtil(String docxPath) {
        this.docxPath = docxPath;
    }

    public DocxFileUtil(){
    }

    public  String dealDocx() throws IOException, OpenXML4JException, XmlException {
        String text="";
        if(docxPath.endsWith("doc")){
            FileInputStream inputStream = new FileInputStream(new File(docxPath));
            WordExtractor extractor = new WordExtractor(inputStream);
            text = extractor.getText();
        }

        if(docxPath.endsWith("docx")){
            OPCPackage opcPackage = POIXMLDocument.openPackage(docxPath);
            XWPFWordExtractor extractor = new XWPFWordExtractor(opcPackage);
            text = extractor.getText();
        }else {
            System.out.println("处理的文件不是word文件");
        }
        return text;
    }
}
