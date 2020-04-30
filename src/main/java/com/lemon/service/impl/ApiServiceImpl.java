package com.lemon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.lemon.common.ApiListVO;
import com.lemon.common.ApiRunResult;
import com.lemon.common.ApiVO;
import com.lemon.mapper.ApiMapper;
import com.lemon.pojo.Api;
import com.lemon.pojo.ApiRequestParam;
import com.lemon.service.ApiService;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kk
 * @since 2020-02-15
 */
@Service
public class ApiServiceImpl extends ServiceImpl<ApiMapper, Api> implements ApiService {

	@Autowired
	ApiMapper apiMaper;
	
	@Override
	public List<ApiListVO> showApiListByProject(Integer projectId) {
		
		return apiMaper.showApiListByProject(projectId);
	}

	@Override
	public List<ApiListVO> showApiListByClassification(Integer apiClassificationId) {
		
		return apiMaper.showApiListByClassification(apiClassificationId);
	}

	@Override
	public ApiVO findApiViewVO(Integer apiId) {
		return apiMaper.findApiViewVO(apiId);
	}

	@Override
	public ApiRunResult run(ApiVO apiRunVO) throws JsonProcessingException  {
		//远程调用
		RestTemplate restTemplate=new RestTemplate();
		//url
		String url=apiRunVO.getHost()+apiRunVO.getUrl();
		//method
		String method=apiRunVO.getMethod();
		//参数
		List<ApiRequestParam> list=apiRunVO.getRequestParams();
		//头
		LinkedMultiValueMap<String, String> headers=new LinkedMultiValueMap<String,String>();
		//体
		LinkedMultiValueMap<String, String> bodyParams=new LinkedMultiValueMap<String,String>();
		String paramStr="?";
		String jsonStr=null;
		for (ApiRequestParam apiRequestParam : list) {
			if(apiRequestParam.getType()==3){
				headers.add(apiRequestParam.getName(), apiRequestParam.getValue());
			}
			else if(apiRequestParam.getType()==1){
				//参数拼接在地址栏的请求
				paramStr+=apiRequestParam.getName()+"="+apiRequestParam.getValue()+"&";
			}
			else if(apiRequestParam.getType()==2) {
				//body 2 4  注意此时，type==1没有处理
				bodyParams.add(apiRequestParam.getName(), apiRequestParam.getValue());
			}
			else if (apiRequestParam.getType()==4) {
				jsonStr=apiRequestParam.getValue();
			}
			if(!("?".equals(paramStr))){
				paramStr=paramStr.substring(0, paramStr.lastIndexOf("&"));
			}
			
		}
		HttpEntity httpEntity=null;
		ResponseEntity response=null;
		ApiRunResult apiRunResult=new ApiRunResult();
		try{
		if("get".equalsIgnoreCase(method)){
			httpEntity=new HttpEntity(headers);
			response=restTemplate.exchange(url+paramStr,HttpMethod.GET,httpEntity,String.class);
		}else if("post".equalsIgnoreCase(method)){
			//type=4
			if (jsonStr!="") {
				httpEntity=new HttpEntity(jsonStr,headers);		
				response=restTemplate.exchange(url,HttpMethod.POST,httpEntity,String.class);
			}
			//type=2
			else{
				httpEntity=new HttpEntity(bodyParams,headers);		
				response=restTemplate.exchange(url,HttpMethod.POST,httpEntity,String.class);
			}
		}
		HttpHeaders headersResult;
		if(response!=null){
			apiRunResult.setStatusCode(response.getStatusCodeValue()+"");
			headersResult=response.getHeaders();
		//将java转json
		//apiRunResult.setHeaders(new ObjectMapper().writeValueAsString(headersResult));
		apiRunResult.setHeaders(JSON.toJSONString(headersResult));
		apiRunResult.setBody(response.getBody().toString());
		}
		}
		catch(HttpStatusCodeException e){
			//注意此时有异常调用情况  往往没有体
			if(e.getStatusCode()!=null){
				apiRunResult.setStatusCode(e.getStatusCode().toString());
			}
			apiRunResult.setHeaders(JSON.toJSONString(e.getResponseHeaders()));
			apiRunResult.setBody(e.getResponseBodyAsString());	
		}
		return apiRunResult;
	}
}