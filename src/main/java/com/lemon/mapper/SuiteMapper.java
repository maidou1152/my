package com.lemon.mapper;

import com.lemon.pojo.Suite;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kk
 * @since 2020-02-15
 */
public interface SuiteMapper extends BaseMapper<Suite> {

	//某个product下面的suite
	@Select("select * from suite where project_Id=#{projectId}")
	//延迟加载的请求数据   某个suite下面的cases
	@Results({@Result(column="id",property="id"),
		@Result(column="id",property="cases",many=@Many(select="com.lemon.mapper.CasesMapper.findAll"))
		})
	List<Suite> findSuiteAndReleadtedCasesBy(Integer projectId);
}
