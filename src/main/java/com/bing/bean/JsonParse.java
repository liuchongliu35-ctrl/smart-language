package com.bing.bean;

import lombok.Data;

/**
 *将header和payLoad组装在一个对象里只用创建一次对象就可以调用两个实体类的
 */
@Data
public class JsonParse {
    private Header header;
    private PayLoad payLoad;
}
