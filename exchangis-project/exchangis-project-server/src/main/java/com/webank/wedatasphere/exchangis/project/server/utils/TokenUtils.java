/*
 * <<
 *  Davinci
 *  ==
 *  Copyright (C) 2016 - 2019 EDP
 *  ==
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  >>
 *
 */

package com.webank.wedatasphere.exchangis.project.server.utils;

import com.webank.wedatasphere.exchangis.project.server.common.Constants;
import com.webank.wedatasphere.exchangis.project.server.entity.TokenDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenUtils.class);

    /**
     * 自定义 token 私钥
     */
    @Value("${jwtToken.secret:Pa@ss@Word}")
    private String SECRET;

    /**
     * 默认 token 超时时间
     */
    @Value("${jwtToken.timeout:1800000}")
    private Long TIMEOUT;

    /**
     * 默认 jwt 生成算法
     */
    @Value("${jwtToken.algorithm:HS512}")
    private String ALGORITHM;


    /**
     * 根据 TokenDetail 实体生成Token
     *
     * @param tokenDetail
     * @return
     */
    public String generateToken(TokenDetail tokenDetail) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.TOKEN_USER_NAME, Constants.isEmpty(tokenDetail.getUsername()) ? Constants.EMPTY : tokenDetail.getUsername());
        claims.put(Constants.TOKEN_USER_PASSWORD, Constants.isEmpty(tokenDetail.getPassword()) ? Constants.EMPTY : tokenDetail.getPassword());
        claims.put(Constants.TOKEN_CREATE_TIME, System.currentTimeMillis());
        return generate(claims);
    }

    /**
     * 刷新token
     *
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        Claims claims = getClaims(token);
        claims.put(Constants.TOKEN_CREATE_TIME, System.currentTimeMillis());
        return generate(claims);
    }


    /**
     * 根据 TokenDetail 实体和自定义超时时长生成Token
     *
     * @param tokenDetail
     * @param timeOutMillis （毫秒）
     * @return
     */
    public String generateToken(TokenDetail tokenDetail, Long timeOutMillis) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.TOKEN_USER_NAME, Constants.isEmpty(tokenDetail.getUsername()) ? Constants.EMPTY : tokenDetail.getUsername());
        claims.put(Constants.TOKEN_USER_PASSWORD, Constants.isEmpty(tokenDetail.getPassword()) ? Constants.EMPTY : tokenDetail.getPassword());
        claims.put(Constants.TOKEN_CREATE_TIME, System.currentTimeMillis());

        Long expiration = Long.parseLong(claims.get(Constants.TOKEN_CREATE_TIME) + Constants.EMPTY) + timeOutMillis;

        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(claims.get(Constants.TOKEN_USER_NAME).toString())
                    .setExpiration(new Date(expiration))
                    .signWith(null != SignatureAlgorithm.valueOf(ALGORITHM) ?
                            SignatureAlgorithm.valueOf(ALGORITHM) :
                            SignatureAlgorithm.HS512, SECRET.getBytes("UTF-8"))
                    .compact();
        } catch (UnsupportedEncodingException ex) {
            LOGGER.warn(ex.getMessage());
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(claims.get(Constants.TOKEN_USER_NAME).toString())
                    .setExpiration(new Date(expiration))
                    .signWith(null != SignatureAlgorithm.valueOf(ALGORITHM) ?
                            SignatureAlgorithm.valueOf(ALGORITHM) :
                            SignatureAlgorithm.HS512, SECRET)
                    .compact();
        }
    }

    /**
     * 根据 TokenDetail 实体生成永久 Token
     *
     * @param tokenDetail
     * @return
     */
    public String generateContinuousToken(TokenDetail tokenDetail) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.TOKEN_USER_NAME, Constants.isEmpty(tokenDetail.getUsername()) ? Constants.EMPTY : tokenDetail.getUsername());
        claims.put(Constants.TOKEN_USER_PASSWORD, Constants.isEmpty(tokenDetail.getPassword()) ? Constants.EMPTY : tokenDetail.getPassword());
        claims.put(Constants.TOKEN_CREATE_TIME, System.currentTimeMillis());
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(claims.get(Constants.TOKEN_USER_NAME).toString())
                    .signWith(null != SignatureAlgorithm.valueOf(ALGORITHM) ?
                            SignatureAlgorithm.valueOf(ALGORITHM) :
                            SignatureAlgorithm.HS512, SECRET.getBytes("UTF-8"))
                    .compact();
        } catch (UnsupportedEncodingException ex) {
            LOGGER.warn(ex.getMessage());
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(claims.get(Constants.TOKEN_USER_NAME).toString())
                    .signWith(null != SignatureAlgorithm.valueOf(ALGORITHM) ?
                            SignatureAlgorithm.valueOf(ALGORITHM) :
                            SignatureAlgorithm.HS512, SECRET)
                    .compact();
        }
    }

    /**
     * 根据 clams生成token
     *
     * @param claims
     * @return
     */
    private String generate(Map<String, Object> claims) {
        Long expiration = Long.parseLong(claims.get(Constants.TOKEN_CREATE_TIME) + Constants.EMPTY) + TIMEOUT;
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(claims.get(Constants.TOKEN_USER_NAME).toString())
                    .setExpiration(new Date(expiration))
                    .signWith(null != SignatureAlgorithm.valueOf(ALGORITHM) ?
                            SignatureAlgorithm.valueOf(ALGORITHM) :
                            SignatureAlgorithm.HS512, SECRET.getBytes("UTF-8"))
                    .compact();
        } catch (UnsupportedEncodingException ex) {
            LOGGER.warn(ex.getMessage());
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(claims.get(Constants.TOKEN_USER_NAME).toString())
                    .setExpiration(new Date(expiration))
                    .signWith(null != SignatureAlgorithm.valueOf(ALGORITHM) ?
                            SignatureAlgorithm.valueOf(ALGORITHM) :
                            SignatureAlgorithm.HS512, SECRET)
                    .compact();
        }
    }

    /**
     * 解析 token 用户名
     *
     * @param token
     * @return
     */
    public String getUsername(String token) {
        String username;
        try {
            final Claims claims = getClaims(token);
            username = claims.get(Constants.TOKEN_USER_NAME).toString();
        } catch (Exception e) {
            LOGGER.warn("Jwt get user name failed : " , e);
            username = null;
        }
        return username;
    }

    /**
     * 解析 token 密码
     *
     * @param token
     * @return
     */
    public String getPassword(String token) {
        String password;
        try {
            final Claims claims = getClaims(token);
            password = claims.get(Constants.TOKEN_USER_PASSWORD).toString();
        } catch (Exception e) {
            LOGGER.warn("Jwt get user password failed : " , e);
            password = null;
        }
        return password;
    }

    /**
     * 获取token claims
     *
     * @param token
     * @return
     */
    private Claims getClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET.getBytes("UTF-8"))
                    .parseClaimsJws(token.startsWith(Constants.TOKEN_PREFIX) ?
                            token.substring(token.indexOf(Constants.TOKEN_PREFIX) + Constants.TOKEN_PREFIX.length()).trim() :
                            token.trim())
                    .getBody();
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(),e);
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.startsWith(Constants.TOKEN_PREFIX) ?
                            token.substring(token.indexOf(Constants.TOKEN_PREFIX) + Constants.TOKEN_PREFIX.length()).trim() :
                            token.trim())
                    .getBody();
        }
        return claims;
    }

    /**
     * 根据 TokenDetail 验证token
     *
     * @param token
     * @param tokenDetail
     * @return
     */
    public Boolean validateToken(String token, TokenDetail tokenDetail) {
        TokenDetail user = tokenDetail;
        String username = getUsername(token);
        String password = getPassword(token);
        return (username.equals(user.getUsername()) && password.equals(user.getPassword()) && !(isExpired(token)));
    }

    /**
     * 根据 用户名、密码 验证 token
     *
     * @param token
     * @param username
     * @param password
     * @return
     */
    public Boolean validateToken(String token, String username, String password) {
        String tokenUsername = getUsername(token);
        String tokenPassword = getPassword(token);
        return (username.equals(tokenUsername) && password.equals(tokenPassword) && !(isExpired(token)));
    }

    /**
     * 解析 token 创建时间
     *
     * @param token
     * @return
     */
    private Date getCreatedDate(String token) {
        Date created;
        try {
            final Claims claims = getClaims(token);
            created = new Date((Long) claims.get(Constants.TOKEN_CREATE_TIME));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    /**
     * 获取 token 超时时间
     *
     * @param token
     * @return
     */
    private Date getExpirationDate(String token) {
        Date expiration;
        try {
            final Claims claims = getClaims(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    /**
     * token 是否超时
     *
     * @param token
     * @return
     */
    private Boolean isExpired(String token) {
        final Date expiration = getExpirationDate(token);
        //超时时间为空则永久有效
        return null == expiration ? false : expiration.before(new Date(System.currentTimeMillis()));
    }

}
