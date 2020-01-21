package com.hog.bigdata.mapper;

import com.hog.bigdata.entity.SlaughterQuarantine;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlaughterQuarantineMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SlaughterQuarantine record);

    int insertSelective(SlaughterQuarantine record);

    SlaughterQuarantine selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SlaughterQuarantine record);

    int updateByPrimaryKey(SlaughterQuarantine record);

    void batchInsert(@Param("quarantines") List<SlaughterQuarantine> slaughterQuarantines);
}