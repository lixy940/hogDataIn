package com.hog.bigdata.mapper;

import com.hog.bigdata.entity.FarmerManagement;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FarmerManagementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FarmerManagement record);

    int insertSelective(FarmerManagement record);

    FarmerManagement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FarmerManagement record);

    int updateByPrimaryKey(FarmerManagement record);

    List<FarmerManagement> findAllFarmer();

    void batchInsert(@Param("farmerManagements") List<FarmerManagement> farmerManagements);
}