package com.bing.config;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class XFConfig {
    private String hostUrl="https://spark-api.xf-yun.com/v3.1/chat";
    private String apiId="d9fbbb1c";
    private String apiSecret="OTA1MTMyMTRhM2FmZjQ5NTdjMmRkOGEy";
    private String apiKey="c7abf9f94d555f2f7ade1ec137e11a13";
    private Integer maxResponseTime=30;
}
