package com.hog.bigdata.controller;


import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONObject;
import com.hog.bigdata.service.DataSynManageService;
import com.hog.bigdata.utils.EasyExcelUtil;
import com.hog.bigdata.vo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//@Api(tags = {"数据导入管理"})
@CrossOrigin(origins = "http://localhost:8888", maxAge = 3600)
@Controller
public class DataOperatorController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataSynManageService dataSynManageService;


    /**
     * 主页
     *
     * @return
     */
//    @ApiOperation(value = "进入主页", notes = "进入主页")
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * 上次文件
     *
     * @param file    文件流
     * @param type    模块类型
     * @param purpose 用途
     * @return
     */
//    @ApiOperation(value = "上传文件", notes = "上传文件")
    @ResponseBody
    @PostMapping("/upload")
    public ResponseResult fileUpload(@RequestParam("file") MultipartFile file, @RequestParam("type") int type, Integer variety, Integer purpose, Float price) {
        ResponseResult<Object> result = new ResponseResult<>();
        try {
            List<List<String>> excelModelList = EasyExcelUtil.readExcelWithStringList(file.getInputStream(), ExcelTypeEnum.XLS);
            LOGGER.info("handler record is total {}", excelModelList.size());
            result.setData(dataSynManageService.syn(excelModelList, type, variety, purpose, price));
        } catch (Exception e) {
            result.setStatus(1);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }
}
