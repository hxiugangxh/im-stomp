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
}