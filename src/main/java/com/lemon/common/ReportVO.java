package com.lemon.common;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lemon.pojo.TestReport;

import lombok.Data;

@Data
public class ReportVO {

	private Integer id;
	private String name;//套件名

	private String username;//测试人
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date createReportTime;//测试时间
	
	private int totalCase;//总用例数、计算通过率
	private int success;//成功通过数
	private int failures;//失败数
	//跳过的用例数--就是没有执行的用例数
	
	private List<CaseListVO> caseList;//生成时间

	/**
	 * 获得执行用例总数
	 * @return
	 */
	public int getTotalCase(){
		return caseList.size();
	}
	
	/**
	 * 获得成功数
	 * @return
	 */
	public int getSuccesses(){
		int count1=0;
		int count2=0;
		for (CaseListVO caseListVO : caseList) {
			TestReport report=caseListVO.getTestReport();
			if (report!=null) {
				if (report.getPassFlag().equals("通过")) {
					count1++;
				}else{
					count2++;
				}
			}
		}
		this.success=count1;
		this.failures=count2;
		return success;
	}
	
	/**
	 * 获得失败数
	 * @return
	 */
	public int getFailures(){
		return failures;
	}
}