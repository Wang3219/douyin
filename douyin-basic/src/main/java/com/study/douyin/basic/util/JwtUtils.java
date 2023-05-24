package com.study.douyin.basic.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author: weiyi.wang1999@qq.com
 * @create: 2023-05-19 16:13
 * @Description:
 */
@Slf4j
public class JwtUtils {

    //指定一个token过期时间（毫秒）
    private static final long EXPIRE_TIME = 30 * 60 * 1000;  // 30min
    private static final String JWT_TOKEN_SECRET_KEY = "douyin-simple";

    public static String createJwtTokenByUser(int userId) {
        String secret = JWT_TOKEN_SECRET_KEY;

        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);    //使用密钥进行哈希
        // 附带username信息的token
        return JWT.create()
                .withClaim("userId", userId)
                .withExpiresAt(date)  //过期时间
                .sign(algorithm);     //签名算法
    }


    /**
     * 校验token是否正确
     */
    public static boolean verifyTokenOfUser(String token) {
        log.info("verifyTokenOfUser");

        //根据密钥生成JWT效验器
        Algorithm algorithm = Algorithm.HMAC256(JWT_TOKEN_SECRET_KEY);
        JWTVerifier verifier = JWT.require(algorithm)
                .withClaim("userId", getUserId(token))//从不加密的消息体中取出username
                .build();
        try {
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception){
            return false;
        }
    }

    /**
     * 在token中获取到username信息
     */
    public static int getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asInt();
        } catch (JWTDecodeException e) {
            return 0;
        }
    }

    /**
     * 判断是否过期
     * 因为没有安卓源码修改，所以无法进行过期判断、续期以及失效要求用户重新登录等操作
     */
    public static boolean isExpire(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().getTime() < System.currentTimeMillis();
    }

}
