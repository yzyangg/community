package com.yzy.community;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.yzy.community.utils.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: yzy
 **/
@SpringBootTest
public class testToken {


    @Test
    public void test01() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "yzy");
        String token = JWTUtil.genToken(map);
        System.out.println(token);


        DecodedJWT decodedJWT = JWTUtil.parseToken(token);
        System.out.println(decodedJWT.getClaim("name").asString());
    }

}
