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

package com.webank.wedatasphere.exchangis.project.server.common;

/**
 * 常量
 */
public class Constants  {

    public static final String BASE_API_PATH = "/api/rest_s/v1/exchangis";


    public static final String RESTFUL_BASE_PATH ="/exchangis/";


    public static final String EMPTY = "";

    /**
     * Token 前缀
     */
    public static final String TOKEN_PREFIX = "Bearer";

    /**
     * Token header名称
     */
    public static final String TOKEN_HEADER_STRING = "Authorization";

    /**
     * Token 用户名
     */
    public static final String TOKEN_USER_NAME = "token_user_name";

    /**
     * Token 密码
     */
    public static final String TOKEN_USER_PASSWORD = "token_user_password";

    /**
     * Token 创建时间
     */
    public static final String TOKEN_CREATE_TIME = "token_create_time";


    public static final String USER_TICKET_ID_STRING = "bdp-user-ticket-id";


    public static boolean isEmpty(String value) {
        return isEmpty((CharSequence)value);
    }

    public static boolean isEmpty(CharSequence value) {
        return value == null || value.length() == 0;
    }
}
