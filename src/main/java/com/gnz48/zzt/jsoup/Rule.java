package com.gnz48.zzt.jsoup;

import java.util.Map;

/**
 * 
 * Copyright: Copyright (c) 2018 LanRu-Caifu
 * 
 * @ClassName: Rule.java
 * @Description: 用于指定查询url,method,keys等的一个规则类
 *
 * @version: v1.0.0
 * @author: JuFF_白羽
 * @date: 2018年5月23日 上午12:21:28
 *
 *        Modification History: Date Author Version Description
 *        ---------------------------------------------------------*
 * 
 */
public class Rule {

	/**
	 * 链接
	 */
	private String url;

	/**
	 * 参数集合
	 */
	private String[] keys;
	/**
	 * 参数对应的值
	 */
	private String[] values;
	/**
	 * 请求类型
	 */
	private String dataType;
	/**
	 * 参数Map集合
	 */
	private Map<String, String> params;

	/**
	 * 对返回的HTML，第一次过滤所用的标签，请先设置type
	 */
	private String resultTagName;

	/**
	 * CLASS / ID / SELECTION 设置resultTagName的类型，默认为ID
	 */
	private int type = ID;

	/**
	 * GET / POST 请求的类型，默认GET
	 */
	private int requestMoethod = GET;

	public final static int GET = 0;
	public final static int POST = 1;

	public final static int CLASS = 0;
	public final static int ID = 1;
	public final static int SELECTION = 2;

	public Rule() {
	}

	public Rule(String url, String[] keys, String[] values, String resultTagName, int type, int requestMoethod) {
		super();
		this.url = url;
		this.keys = keys;
		this.values = values;
		this.resultTagName = resultTagName;
		this.type = type;
		this.requestMoethod = requestMoethod;
	}

	public Rule(String url, String[] keys, String[] values, String dataType) {
		super();
		this.url = url;
		this.keys = keys;
		this.values = values;
		this.dataType = dataType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

	public String getResultTagName() {
		return resultTagName;
	}

	public void setResultTagName(String resultTagName) {
		this.resultTagName = resultTagName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRequestMoethod() {
		return requestMoethod;
	}

	public void setRequestMoethod(int requestMoethod) {
		this.requestMoethod = requestMoethod;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String[] getKeys() {
		return keys;
	}

	public void setKeys(String[] keys) {
		this.keys = keys;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

}
