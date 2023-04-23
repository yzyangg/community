package com.yzy.community.contant;

/**
 * @author: yzy
 **/
public interface RedisConst {
    String LOGIN_CODE_KEY = "login:code:";
    String LOGIN_USER_KEY = "login:token:";
    String LIKED_KEY = "liked:";
    Long LOGIN_USER_TTL = 30L;
    String COMMENT = "comment:";
    String POST = "post:";

    String FOLLOWEE_KEY = "followee:";
    String FOLLOWER_KEY = "follower:";
}
