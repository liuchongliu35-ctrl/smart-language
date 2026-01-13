package com.bing.observe;

import com.bing.bean.JsonParse;

public interface AnswerObserver {
    void onAnswerReceived(JsonParse answer);
}
