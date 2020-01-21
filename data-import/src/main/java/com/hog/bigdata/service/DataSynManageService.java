package com.hog.bigdata.service;

import java.util.List;

public interface DataSynManageService {


    /**
     * 数据同步
     *
     * @param dataList
     * @param type 业务类型
     * @param variety 种类
     * @param purpose 用途
     */
    String syn(List<List<String>> dataList, int type,Integer variety, Integer purpose,Float price);

}
