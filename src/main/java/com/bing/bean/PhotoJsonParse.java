package com.bing.bean;

import lombok.Data;

@Data
public class PhotoJsonParse {
    private PhotoHeader header;
    private PhotoPayload payload;
}
