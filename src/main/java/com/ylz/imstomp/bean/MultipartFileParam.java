package com.ylz.imstomp.bean;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MultipartFileParam {

    private String uid;  // 用户id
    private String id;  //任务ID
    private int chunks; //总分片数量
    private int chunk; //当前为第几块分片
    private long size = 0L;  //当前分片大小
    private String name;  //文件名
    private MultipartFile file; //分片对象
    private String md5;  // MD5

}
