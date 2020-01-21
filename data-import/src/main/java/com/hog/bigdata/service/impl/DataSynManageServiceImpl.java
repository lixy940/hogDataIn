package com.hog.bigdata.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hog.bigdata.constants.StaticParameterUtils;
import com.hog.bigdata.entity.*;
import com.hog.bigdata.mapper.*;
import com.hog.bigdata.service.DataSynManageService;
import com.hog.bigdata.utils.MyDateUtils;
import com.hog.bigdata.utils.PhoneNoGeneratorUtil;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DataSynManageServiceImpl implements DataSynManageService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private FarmerManagementMapper farmerManagementMapper;

    @Autowired
    private OriginQuarantineMapper originQuarantineMapper;
    @Autowired
    private SlaughterQuarantineMapper slaughterQuarantineMapper;

    @Autowired
    private PigDispatchingMapper pigDispatchingMapper;

    private Map<String, SysUser> userMap = new HashMap<>();
    private Map<String, FarmerManagement> farmerManagementMap = new HashMap<>();


    @PostConstruct
    void init() {
        List<SysUser> sysUsers = sysUserMapper.selectSpecialUsers();
        sysUsers.stream().forEach(sysUser -> {
            userMap.put(sysUser.getName(), sysUser);
        });
        List<FarmerManagement> farmerAll = farmerManagementMapper.findAllFarmer();
        farmerAll.stream().forEach(k -> {
            if (StringUtils.isNotBlank(k.getPhone()))
                farmerManagementMap.put(k.getFarmerName(), k);
        });

    }

    @Override
    public String syn(List<List<String>> dataList, int type, Integer variety, Integer purpose, Float price) {
        if (null == dataList || dataList.isEmpty()) {
            return null;
        }
        Map<String, Integer> map = new HashMap<>();
        StringBuffer builder = new StringBuffer();
        builder.append("解析出的总记录为").
                append("【").
                append(dataList.size())
                .append("】")
                .append("，")
                .append("其中，可用数据且成功导入数据量为")
                .append("【");

        switch (type) {
            case StaticParameterUtils.ORIGIN_QUARANTINE:
                builder.append(handlerOrigin(dataList, purpose, price));
                break;
            case StaticParameterUtils.SLAUGHTER_QUARANTINE:
                builder.append(handlerSlaughter(dataList, variety, price));
                break;
            case StaticParameterUtils.HOG_DISPATCHING:
                builder.append(handlerDispatching(dataList, variety, purpose));
                break;
            case StaticParameterUtils.FARMERS_MANAGE:
                builder.append(handlerFarmerInfo(dataList));
                break;
            default:
                throw new RuntimeException("不符合要求的业务类型");
        }
        builder.append("】");
        return builder.toString();
    }

    private int handlerOrigin(List<List<String>> dataList, int purpose, Float price) {
        List<OriginQuarantine> originQuarantines = new ArrayList<>();
        OriginQuarantine model;
        int size = dataList.size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            //第一行不要
            if (i == 0) {
                continue;
            }

            List<String> strings = dataList.get(i);
            model = new OriginQuarantine();
            model.setQuarantineDate(MyDateUtils.parseDate(strings.get(0), "yyyy-MM-dd HH:mm:ss"));
            model.setYongTu(purpose);
            model.setOwnerName(strings.get(1));
            String pigNumStr = strings.get(4);
            model.setPigNum(Integer.parseInt(pigNumStr.substring(0, pigNumStr.indexOf("头"))));
            model.setCarNum(strings.get(10));
            //查询养殖户手机号，如果有就直接取，没有就生成
            FarmerManagement farmerManagement = farmerManagementMap.get(model.getOwnerName());
            if (farmerManagement != null) {
                model.setOwnerPhone(farmerManagement.getPhone());
            } else {
                model.setOwnerPhone(PhoneNoGeneratorUtil.getMultiPhoneNo());
            }
            //单价根
            model.setUnitPrice(String.valueOf(price));
            //二期镇名称
            String town = getTown(strings.get(5));
            model.setProducingArea(town);
            model.setTownName(town);

            List<String> areaList = getAreaSplitAddr(strings.get(8));
            model.setProvince(areaList.get(0));
            model.setCity(areaList.get(1));
            model.setDistrict(areaList.get(2));
            model.setTown(areaList.get(3));

            SysUser sysUser = userMap.get(town);
            model.setCreateBy(sysUser.getId());
            model.setUpdateBy(sysUser.getId());

            Date currentDate = new Date();

            model.setCreateDate(currentDate);
            model.setUpdateDate(currentDate);

            String currentDateStr = MyDateUtils.getCurrentDateStr("yyyy-MM-dd HH:mm:ss:SSS");
            String code = currentDateStr.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
            model.setCode(code);

            originQuarantines.add(model);
            if (originQuarantines.size() >= 500) {
                count += originQuarantines.size();
                originQuarantineMapper.batchInsert(originQuarantines);
                originQuarantines.clear();
            }
        }
        count += originQuarantines.size();
        originQuarantineMapper.batchInsert(originQuarantines);
        LOGGER.info("handlerOrigin 插入完成 {}", JSONObject.toJSONString(originQuarantines.size()));
        return count;
    }

    private int handlerSlaughter(List<List<String>> dataList, Integer variety, Float price) {
        List<SlaughterQuarantine> slaughterQuarantines = new ArrayList<>();
        SlaughterQuarantine model;
        int count = 0;
        int size = dataList.size();
        for (int i = 0; i < size; i++) {
            //第一行不要
            if (i == 0) {
                continue;
            }
            List<String> strings = dataList.get(i);
            model = new SlaughterQuarantine();
            model.setQuarantineDate(MyDateUtils.parseDate(strings.get(0), "yyyy-MM-dd HH:mm:ss"));
            /**
             *  猪的种类 1猪肉 2 产品
             */
            model.setType(variety);

            model.setOwnerName(strings.get(1));
            //猪数量*90
            String pigNumStr = strings.get(4);
            model.setWeight(String.valueOf(Integer.parseInt(pigNumStr.substring(0,pigNumStr.indexOf("头"))) * 90));
            model.setCarNum(strings.get(10));
            //查询养殖户手机号，如果有就直接取，没有就生成
            FarmerManagement farmerManagement = farmerManagementMap.get(model.getOwnerName());
            if (farmerManagement != null) {
                model.setOwnerPhone(farmerManagement.getPhone());
            } else {
                model.setOwnerPhone(PhoneNoGeneratorUtil.getMultiPhoneNo());
            }
            //单价根据情况生成，看是否传入
            model.setUnitPrice(String.valueOf(price));

            //产地对于excel目的地数据
            List<String> areaList = getAreaSplitAddr(strings.get(8));
            model.setProducingProvince(areaList.get(0));
            model.setProducingCity(areaList.get(1));
            model.setProducingDistrict(areaList.get(2));


            /**
             * 目的地划分：80%对应重庆主城区，20%对应荣昌区内（镇街主要分布昌元和昌州，占百分之70）
             */
            List<String> list = randomArea();
            model.setDestinationProvince(list.get(0));
            model.setDestinationCity(list.get(1));
            model.setDestinationDistrict(list.get(2));
            //目的地镇名称,获取对应的填报用户
            String newTown = list.get(3);
            model.setDestinationTown(newTown);

            //二期镇名称
            String town = getTown(strings.get(5));
            model.setTownName(town);
            SysUser sysUser = userMap.get(town);
            model.setCreateBy(sysUser.getId());
            model.setUpdateBy(sysUser.getId());

            Date currentDate = new Date();

            model.setCreateDate(currentDate);
            model.setUpdateDate(currentDate);

            String currentDateStr = MyDateUtils.getCurrentDateStr("yyyy-MM-dd HH:mm:ss:SSS");
            String code = currentDateStr.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
            model.setCode(code);

            slaughterQuarantines.add(model);
            if (slaughterQuarantines.size() >= 800) {
                count += slaughterQuarantines.size();
                slaughterQuarantineMapper.batchInsert(slaughterQuarantines);
                slaughterQuarantines.clear();
            }
        }
        count += slaughterQuarantines.size();
        slaughterQuarantineMapper.batchInsert(slaughterQuarantines);
        LOGGER.info("handlerSlaughter 插入完成:{}", JSONObject.toJSONString(slaughterQuarantines.size()));
        return count;
    }

    private int handlerDispatching(List<List<String>> dataList, Integer variety, int purpose) {

        List<PigDispatching> dispatchings = new ArrayList<>();
        int count = 0;
        PigDispatching model;
        int size = dataList.size();
        for (int i = 0; i < size; i++) {
            List<String> strings = dataList.get(i);

            if (!"到达".equals(strings.get(12))) {
                continue;
            }

            model = new PigDispatching();
            model.setDispatchingDate(MyDateUtils.parseDate(strings.get(11), "yyyy-MM-dd HH:mm:ss"));
            model.setType(variety);
            model.setUsedTo(purpose);
            /*
             *种类  1-猪 2-产品
             *种类：如果是猪，则是头，如果是产品则用重量
             */
            if (variety == 1) {
                model.setPigNum((int) Float.parseFloat(strings.get(10)));
            } else {
                model.setWeight(String.valueOf((int) Float.parseFloat(strings.get(10))));
            }

            //产地，使用启运地
            List<String> pAddrList = getAreaSplitAddr(strings.get(4));
            model.setProducingProvince(pAddrList.get(0));
            model.setProducingCity(pAddrList.get(1));
            model.setProducingDistrict(pAddrList.get(2));

            //目的地
            List<String> dAddrList = getAreaSplitAddr(strings.get(5));
            model.setDestinationProvince(dAddrList.get(0));
            model.setDestinationCity(dAddrList.get(1));
            model.setDestinationDistrict(dAddrList.get(2));
            model.setDestinationTown(dAddrList.get(3));

            //二期镇名称
            String town = dAddrList.get(3);
            model.setTownName(town);

            SysUser sysUser = userMap.get(town);
            model.setCreateBy(sysUser.getId());
            model.setUpdateBy(sysUser.getId());
            Date currentDate = new Date();
            model.setCreateDate(currentDate);
            model.setUpdateDate(currentDate);

            String currentDateStr = MyDateUtils.getCurrentDateStr("yyyy-MM-dd HH:mm:ss:SSS");
            String code = currentDateStr.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
            model.setCode(code);

            dispatchings.add(model);

            if (dispatchings.size() >= 500) {
                count += dispatchings.size();
                pigDispatchingMapper.batchInsert(dispatchings);
                dispatchings.clear();
            }

        }
        count += dispatchings.size();
        pigDispatchingMapper.batchInsert(dispatchings);
        LOGGER.info("handlerDispatching 插入完成:{}", JSONObject.toJSONString(dispatchings.size()));
        return count;
    }

    /***
     * 养殖户数据导入
     * @param dataList
     */
    private int handlerFarmerInfo(List<List<String>> dataList) {
        List<FarmerManagement> farmerManagements = new ArrayList<>();
        int count = 0;
        FarmerManagement model;
        int size = dataList.size();
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                continue;
            }
            List<String> strings = dataList.get(i);
            model = new FarmerManagement();
            model.setFarmerName(strings.get(0));
            model.setPhone(strings.get(1));
            model.setProvince(strings.get(2));
            model.setCity(strings.get(3));
            model.setDistrict(strings.get(4));
            model.setTown(strings.get(5));
            //二期镇名称
            model.setTownName(strings.get(5));
            model.setAddressDetails(strings.get(6));
            model.setPigCunlan(Integer.valueOf(strings.get(7)));

            SysUser sysUser = userMap.get(model.getTownName());
            model.setCreateBy(sysUser.getId());
            model.setUpdateBy(sysUser.getId());
            Date currentDate = new Date();
            model.setCreateDate(currentDate);
            model.setUpdateDate(currentDate);

            farmerManagements.add(model);

            if (farmerManagements.size() >= 500) {
                count += farmerManagements.size();
                farmerManagementMapper.batchInsert(farmerManagements);
                farmerManagements.clear();
            }
        }
        count += farmerManagements.size();
        farmerManagementMapper.batchInsert(farmerManagements);
        LOGGER.info("handlerFarmerInfo 插入完成:{}", JSONObject.toJSONString(farmerManagements.size()));
        return count;
    }

    /**
     * 直辖市
     */
    private static List<String> SPECIAL_CITY_LIST = Stream.of("北京", "上海", "天津", "重庆").collect(Collectors.toList());

    /**
     * 自治区
     */
    private static List<String> AUTONOMY_REGION_LIST = Stream.of("内蒙","新疆","广西","宁夏","西藏").collect(Collectors.toList());
    /**
     * 主城区
     */
    private static List<String> MAIN_CITY_LIST = Stream.of("渝中区", "江北区", "南岸区", "九龙坡区", "沙坪坝区", "大渡口区", "北碚区", "渝北区", "巴南区").collect(Collectors.toList());

    /*
    荣昌街道
     */
    private static List<String> STREET_LIST = Stream.of("峰高", "昌元", "昌州", "广顺", "安富", "双河").collect(Collectors.toList());

    private static List<String> TOWN_LIST = Stream.of("万灵", "清江", "盘龙", "仁义", "吴家", "观胜", "清升", "直升", "清流", "铜鼓", "荣隆", "河包", "龙集", "远觉", "古昌").collect(Collectors.toList());

    /*
     * 街道分类，单独处理
     */
    private static List<String> STREET_1_LIST = Stream.of("昌元", "昌州").collect(Collectors.toList());
    private static List<String> STREET_2_LIST = Stream.of("峰高", "广顺", "安富", "双河").collect(Collectors.toList());

    private static List<String> TOWN_2_LIST = new ArrayList<String>() {
        {
            addAll(TOWN_LIST);
            addAll(STREET_2_LIST);
        }
    };

    /**
     * 获取填报单位及二期镇
     *
     * @param str
     * @return
     */
    private String getTown(String str) {
        String addr = str.replaceAll("\n", "");
        for (String s : STREET_LIST) {
            if (addr.contains(s)) {
                return s + AreaFieldEnum.STREET.getName();
            }
        }
        for (String s : TOWN_LIST) {
            if (addr.contains(s)) {
                return s + AreaFieldEnum.TOWN.getName();
            }
        }

        return "荣昌" + AreaFieldEnum.DISTRINCT.getName();
    }

    /**
     * 获取省市区镇
     *
     * @param str
     * @return
     */
    private List<String> getAreaSplitAddr(String str) {
        String addr = str.replaceAll("市辖区", "").replaceAll("自治区","").replaceAll("\n", "");
        List<String> areaList = new ArrayList<>();
        String prex2Char = addr.substring(0, 2);
        String province = null;
        String city = null;
        String district = null;
        String town = null;
        try {
            //是否为直辖市
            if (SPECIAL_CITY_LIST.contains(prex2Char)) {
                province = prex2Char + AreaFieldEnum.CITY.getName();
                city = province;
            } else if(AUTONOMY_REGION_LIST.contains(prex2Char)){//是否为自治区
                province =prex2Char+AreaFieldEnum.AUTONOMY.getName();
                if (addr.indexOf(AreaFieldEnum.CITY.getName()) != -1) {
                    city = addr.substring(addr.indexOf(prex2Char) + 1, addr.indexOf(AreaFieldEnum.CITY.getName()) + 1);
                }else{
                    city = addr.substring(addr.indexOf(prex2Char) + 1, addr.indexOf(AreaFieldEnum.STATE.getName()) + 1);
                }
            }else {
                province = addr.substring(0, addr.indexOf(AreaFieldEnum.PROVINCE.getName()) + 1);
                city = addr.substring(addr.indexOf(AreaFieldEnum.PROVINCE.getName()) + 1, addr.indexOf(AreaFieldEnum.CITY.getName()) + 1);
            }

            String districtStr = addr.substring(addr.indexOf(AreaFieldEnum.CITY.getName()) + 1);

            String townStr = "";
            //区县区分
            if (districtStr.contains(AreaFieldEnum.COUNTY.getName())) {
                district = districtStr.substring(0, districtStr.indexOf(AreaFieldEnum.COUNTY.getName()) + 1);
                townStr = districtStr.substring(districtStr.indexOf(AreaFieldEnum.COUNTY.getName()) + 1);
            } else {
                district = districtStr.substring(0, districtStr.indexOf(AreaFieldEnum.DISTRINCT.getName()) + 1);
                townStr = districtStr.substring(districtStr.indexOf(AreaFieldEnum.DISTRINCT.getName()) + 1);
            }

            //判断是否为镇，还是街道，如果解析不了，直接跳过
            if (townStr.contains(AreaFieldEnum.TOWN.getName())) {
                String townE = townStr.substring(0, townStr.indexOf(AreaFieldEnum.TOWN.getName()));
                town = townE + (STREET_LIST.contains(townE) ? AreaFieldEnum.STREET.getName() : AreaFieldEnum.TOWN.getName());
            } else {
                town = townStr.substring(0, townStr.indexOf(AreaFieldEnum.STREET.getName())) + AreaFieldEnum.STREET.getName();
            }
        } catch (Exception e) {
            LOGGER.error("此地区解析异常：{}", e.getStackTrace());
        }
        areaList.add(province);
        areaList.add(city);
        areaList.add(district);
        areaList.add(town);

        return areaList;

    }


    /**
     * 自动生成区域
     *
     * @return
     */
    private List<String> randomArea() {
        List<String> list = new ArrayList<>();
        String province = "重庆市";
        String city = "重庆市";
        String district = null;
        String town = null;
        int num = RandomUtils.nextInt(1, 101);
        if (num > 20) {
            district = MAIN_CITY_LIST.get(num % MAIN_CITY_LIST.size());
        } else {
            district = "荣昌区";

            if (num > 0 && num < 15) {
                town = STREET_1_LIST.get(num % STREET_1_LIST.size()) + "街道";
            } else {
                int townNum = RandomUtils.nextInt(1, 101);
                town = TOWN_2_LIST.get(townNum % TOWN_2_LIST.size());
                town = STREET_2_LIST.contains(town) ? town + "街道" : town + "镇";
            }
        }

        list.add(province);
        list.add(city);
        list.add(district);
        list.add(town);
//        LOGGER.info((JSONObject.toJSONString(list)));
        return list;

    }


    enum AreaFieldEnum {
        PROVINCE("省"),
        AUTONOMY("自治区"),
        CITY("市"),
        STATE("州"),
        DISTRINCT("区"),
        COUNTY("县"),
        TOWN("镇"),
        STREET("街道"),
        ;

        AreaFieldEnum(String name) {
            this.name = name;
        }

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
//        System.out.println(JSONObject.toJSONString(getAreaSplitAddr("重庆市荣昌区昌元街道武城矿区社区")));
//        System.out.println(JSONObject.toJSONString(getAreaSplitAddr("重庆市市辖区荣昌区昌元街道")));
//        System.out.println(JSONObject.toJSONString(getAreaSplitAddr("重庆市市辖区荣昌区双河镇")));
//        System.out.println(JSONObject.toJSONString(getAreaSplitAddr("重庆市荣昌县双河镇白玉社区")));
//        System.out.println(JSONObject.toJSONString(getAreaSplitAddr("重庆市荣昌县双河街道白玉社区")));
//        System.out.println(JSONObject.toJSONString(getAreaSplitAddr("重庆市荣昌区昌元街道虹桥社区")));
//        System.out.println(JSONObject.toJSONString(getAreaSplitAddr("重庆市市辖区荣昌区吴家镇")));
//        System.out.println(getTown("昌元街道动物检疫申报点"));
//        System.out.println(getTown("荣昌县峰高街道动物检疫申报点"));
//        System.out.println(getTown("荣昌县峰高镇动物检疫申报点"));
//        System.out.println(getTown("荣昌县吴家镇动物检疫申报点"));
//        for (int i = 0; i <100 ; i++) {
//
//            randomArea();
//        }
    }
}
