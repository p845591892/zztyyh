package com.gnz48.zzt.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gnz48.zzt.exception.RuleException;
import com.gnz48.zzt.jsoup.LinkTypeData;
import com.gnz48.zzt.jsoup.Rule;

/**
 * 
 * Copyright: Copyright (c) 2018 LanRu-Caifu
 * 
 * @ClassName: ExtractService.java
 * @Description: 核心的查询类
 *
 * @version: v1.0.0
 * @author: JuFF_白羽
 * @date: 2018年5月29日 下午9:48:55
 *
 * Modification History: Date Author Version Description
 * ---------------------------------------------------------*
 * 
 */
@Deprecated
public class ExtractService {

	public static List<LinkTypeData> extract(Rule rule) {

		// 进行对rule的必要校验
		validateRule(rule);

		List<LinkTypeData> datas = new ArrayList<LinkTypeData>();
		LinkTypeData data = null;
		try {
			/**
			 * 解析rule
			 */
			String url = rule.getUrl();
			String[] params = null;//rule.getParams();
			String[] values = rule.getValues();
			String resultTagName = rule.getResultTagName();
			int type = rule.getType();
			int requestType = rule.getRequestMoethod();

			Connection conn = Jsoup.connect(url);
			conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			conn.header("Accept-Encoding", "gzip, deflate, sdch");
			conn.header("Accept-Language", "zh-CN,zh;q=0.8");
			conn.header("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
			// 设置查询参数
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					conn.data(params[i], values[i]);
				}
			}

			// 设置请求类型
			Document doc = null;
			switch (requestType) {
			case Rule.GET:
				doc = conn.timeout(100000).get();
				break;
			case Rule.POST:
				doc = conn.timeout(100000).post();
				break;
			}

			// 处理返回数据
			Elements results = new Elements();
			switch (type) {
			case Rule.CLASS:
				results = doc.getElementsByClass(resultTagName);
				break;
			case Rule.ID:
				Element result = doc.getElementById(resultTagName);
				results.add(result);
				break;
			case Rule.SELECTION:
				results = doc.select(resultTagName);
				break;
			default:
				// 当resultTagName为空时默认去body标签
				if (resultTagName.equals("")) {
					results = doc.getElementsByTag("body");
				}
			}

			for (Element result : results) {
				Elements links = result.getElementsByTag("a");

				for (Element link : links) {
					// 必要的筛选
					String linkHref = link.attr("href");
					String linkText = link.text();

					data = new LinkTypeData();
					data.setLinkHref(linkHref);
					data.setLinkText(linkText);

					datas.add(data);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return datas;
	}

	/**
	 * 对传入的参数进行必要的校验
	 */
	private static void validateRule(Rule rule) {
		String url = rule.getUrl();
		if (url.equals("")) {
			throw new RuleException("url不能为空！");
		}
		if (!url.startsWith("http://") && !url.startsWith("https://")) {
			throw new RuleException("url的格式不正确！");
		}

//		if (rule.getParams() != null && rule.getValues() != null) {
//			if (rule.getParams().length != rule.getValues().length) {
//				throw new RuleException("参数的键值对个数不匹配！");
//			}
//		}
	}

}
