package com.ylz.imstomp.constant;

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
    public static final String SINGLE_CHAT_DES_READ = "/queue/read";

    /**
     * 群聊地址
     */
    public static final String GROUP_CHAT_DES = "/topic/getResponse";

    /**
     * 在线信息广播
     */
    public static final String USER_ONLINE_INFO = "/topic/online";
}