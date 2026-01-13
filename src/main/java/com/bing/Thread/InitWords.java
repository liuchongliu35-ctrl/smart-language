package com.bing.Thread;


import com.bing.bean.Book;
import com.bing.service.XHmodelUse.GetResult;
import com.bing.service.XHmodelUse.XHImpl.GetResultImpl;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;


@Component
public class InitWords {

    private static GetResult getResult = new GetResultImpl();
    public void InitWord(Book book,List<String> types) throws InterruptedException {
        HashMap<String, String> wordMap = new HashMap<>();
        for(String type:types){
            InitWordThread wordThread = new InitWordThread(book.getUid(),book.getLanguage(),type);
            Thread thread = new Thread(wordThread);
            thread.start();
        }
    }
    }
