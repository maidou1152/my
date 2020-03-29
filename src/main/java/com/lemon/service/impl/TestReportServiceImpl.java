package com.lemon.service.impl;

import com.lemon.pojo.ApiRequestParam;
import com.lemon.pojo.TestReport;
import com.lemon.pojo.TestRule;
import com.lemon.pojo.User;
import com.lemon.common.ApiRunResult;
import com.lemon.common.CaseEditVO;
import com.lemon.common.ReportVO;
import com.lemon.mapper.TestReportMapper;
import com.lemon.service.TestReportService;

import lombok.val;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.soap.SOAPBinding.Use;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kk
 * @since 2020-02-15
 */
@Service
public class TestReportServiceImpl extends ServiceImpl<TestReportMapper, TestReport> implements TestReportService {

	@Autowired
	TestReportMapper testReportMapper;
	
	@Override
	public List<TestReport> run(Integer suiteId) {
		List<TestReport> reportList=new ArrayList<>();
		//1、先根据suiteId 查询
		List<CaseEditVO> list=testReportMapper.findCaseEditVosUnderSuite(suiteId);
		//2、 for list
		for (CaseEditVO caseEditVo : list) {
			//远程调用api执行
			TestReport report=runAndGetReport(caseEditVo);
			reportList.add(report);
		}
		//3.对test_report 先删除再插入
		testReportMapper.deleteReport(suiteId);
		this.saveBatch(reportList);
		return reportList;
		
		
	}

	TestReport runAndGetReport(CaseEditVO caseEditVo) {
		TestReport testReport= new TestReport();
		//远程调用
				RestTemplate restTemplate=new RestTemplate();
				//url
				String url=caseEditVo.getHost()+caseEditVo.getUrl();
				//method
				String method=caseEditVo.getMethod();
				//参数
				List<ApiRequestParam> list=caseEditVo.getRequestParams();
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
				try{
				if("get".equalsIgnoreCase(method)){
					httpEntity=new HttpEntity(headers);
					response=restTemplate.exchange(url+paramStr,HttpMethod.GET,httpEntity,String.class);
				}else if("post".equalsIgnoreCase(method)){
					//type=4
					if (jsonStr!="") {
						httpEntity=new HttpEntity(jsonStr,headers);	
						testReport.setRequestBody(JSON.toJSONString(jsonStr));
						response=restTemplate.exchange(url,HttpMethod.POST,httpEntity,String.class);
					}
					//type=2
					else{
						httpEntity=new HttpEntity(bodyParams,headers);	
						testReport.setRequestBody(JSON.toJSONString(bodyParams));
						response=restTemplate.exchange(url,HttpMethod.POST,httpEntity,String.class);
					}
					testReport.setCaseId(caseEditVo.getId());
					testReport.setRequestUrl(url);
					testReport.setRequestHeaders(JSON.toJSONString(headers));
					testReport.setResponseHeaders(JSON.toJSONString(response.getHeaders()));
					testReport.setResponseBody(response.getBody().toString());
					testReport.setPassFlag(asserByTestRule(response.getBody().toString(),caseEditVo.getTestRules()));
					
				}
				}
				catch(HttpStatusCodeException e)
				{
					e.printStackTrace();
				}
		return testReport;
	}
	/**
	 * 判断规则是否验证通过
	 * @param responseBody
	 * @param testRules
	 * @return
	 */
	String asserByTestRule(String responseBody,List<TestRule> testRules){
		boolean flag=true;
		for (TestRule testRule : testRules) {
			Object value=JSONPath.read(responseBody,testRule.getExpression());//$.name
			String actual=(String) value;
			String op=testRule.getOperator();
			if (op.equals("=")) {
				if (!value.equals(testRule.getExpression())) {
					flag=false;
				}
			}else {
				if (!actual.contains(testRule.getExpected())) {
					flag=false;
				}
			}	
		}
		if (!flag) {
			return "不通过";
		}
		return "通过";
	}

	@Override
	public TestReport finbByCaseId(Integer caseId) {
		return testReportMapper.findByCaseId(caseId);
	}

	@Override
	public ReportVO getReport(Integer suiteId) {
		ReportVO report=testReportMapper.getReport(suiteId);
		User user=(User) SecurityUtils.getSubject().getPrincipal();
		String username=user.getUsername();
		//设置用户名
		report.setUsername(username);
		//设置生成时间
		report.setCreateReportTime(new Date());
		return report;
	}


}
