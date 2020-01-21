package com.hog.bigdata.mapper;

import com.hog.bigdata.entity.PigDispatching;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PigDispatchingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PigDispatching record);

    int insertSelective(PigDispatching record);

    PigDispatching selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PigDispatching record);

    int updateByPrimaryKey(PigDispatching record);

    void batchInsert(@Param("dispatchings") List<PigDispatching> dispatchings);
}