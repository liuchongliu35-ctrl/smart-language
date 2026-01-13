package com.bing.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bing.bean.WordList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Mapper
public interface WordMapper extends BaseMapper<WordList> {

    Integer  deleteByWordId(@Param("wordId") Integer wordId);

    int selecMax(@Param("cidList") List<Integer> cidList);
}
