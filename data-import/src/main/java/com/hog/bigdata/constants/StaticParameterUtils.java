package com.hog.bigdata.constants;


import com.hog.bigdata.utils.DBPropertyReaderUtils;

/**
 * Author：MR LIS，2019/10/21
 * Copyright(C) 2019 All rights reserved.
 */
public final class StaticParameterUtils {


    /**
     * token header
     */
    public static final String HTTP_HEADER_TOKEN = "";


    public static final String SERVER_PORT = DBPropertyReaderUtils.getProValue("server.port");
    public static final int ORIGIN_QUARANTINE = 1;
    public static final int SLAUGHTER_QUARANTINE = 2;
    public static final int HOG_DISPATCHING = 3;

    public static final int FARMERS_MANAGE = 4;
}
