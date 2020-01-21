package com.hog.bigdata.mapper;

import com.hog.bigdata.entity.OriginQuarantine;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OriginQuarantineMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OriginQuarantine record);

    int insertSelective(OriginQuarantine record);

    OriginQuarantine selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OriginQuarantine record);

    int updateByPrimaryKey(OriginQuarantine record);

    void batchInsert(@Param("quarantines") List<OriginQuarantine> quarantines);
}