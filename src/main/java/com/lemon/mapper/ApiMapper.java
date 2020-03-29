package com.lemon.mapper;

import com.lemon.common.ApiListVO;
import com.lemon.common.ApiVO;
import com.lemon.pojo.Api;

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
public interface ApiMapper extends BaseMapper<Api> {

	//第二条查询语句
	@Select("Select * from api where api_classification_id=#{apiClassificationId}")
	public List<Api> findApi(Integer apiClassificationId);
	
	//两表连接操作 projectId下的api信息
	@Select("SELECT t1.*,t2.name classificationName from api t1,api_classification t2 where t2.project_id=#{projectId} and t1.api_classification_id=t2.id;")
	public List<ApiListVO> showApiListByProject(Integer projectId);
	
	//两表连接操作该接口分类下的api信息
	@Select("SELECT t1.*,t2.name classificationName from api t1,api_classification t2 where t2.id=#{apiclassificationId} and t1.api_classification_id=t2.id;")
	public List<ApiListVO> showApiListByClassification(Integer apiClassificationId);
	
	//两表连接 (api、 user）: api信息和创建者信息
	@Select("SELECT t1.*, t2.username createUsername  FROM api t1,`user` t2 WHERE t1.id =#{apiId} AND t1.create_user = t2.id;")
	//延迟加载的请求数据
	@Results({@Result(column="id",property="id"),
			@Result(column="id",property="requestParams",many=@Many(select="com.lemon.mapper.ApiRequestParamMapper.findAll"))
	})
	public ApiVO findApiViewVO(Integer apiId);
	
}
