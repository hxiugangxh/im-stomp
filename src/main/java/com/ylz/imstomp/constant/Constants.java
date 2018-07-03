package com.ylz.imstomp.constant;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    /**
     * SessionId
     */
    public static final String SESSIONID = "sessionid";

    /**
     * Session对象Key, 用户id
     */
    public static final String SKEY_ACCOUNT_ID = "accountId";

    /**
     * 单聊地址
     */
    public static final String SINGLE_CHAT_DES = "/queue/notifications";

    /**
     * 单聊地址-已读广播
     */
    public static final String SINGLE_CHAT_READ_DES = "/queue/read";

    /**
     * 群聊地址
     */
    public static final String GROUP_CHAT_DES = "/topic/getResponse";

    /**
     * 群聊地址
     */
    public static final String SINGLE_CHAT_READ_ROOM_DES = "/queue/roomRead";

    /**
     * 在线信息广播
     */
    public static final String USER_ONLINE_INFO = "/topic/online";

    /**
     * 异常信息统一头信息<br>
     * 非常遗憾的通知您,程序发生了异常
     */
    public static final String Exception_Head = "程序异常";
    /**
     * 缓存键值
     */
    public static final Map<Class<?>, String> cacheKeyMap = new HashMap<>();
    /**
     * 保存文件所在路径的key，eg.FILE_MD5:1243jkalsjflkwaejklgjawe
     */
    public static final String FILE_MD5_KEY = "FILE_MD5:";
    /**
     * 保存上传文件的状态
     */
    public static final String FILE_UPLOAD_STATUS = "FILE_UPLOAD_STATUS";
}