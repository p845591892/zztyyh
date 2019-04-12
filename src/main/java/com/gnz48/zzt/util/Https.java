package com.gnz48.zzt.util;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gnz48.zzt.confing.MyX509TrustManager;
import com.gnz48.zzt.exception.RuleException;

/**
 * @ClassName: Https
 * @Description: Https操作的工具类
 * @author JuFF_白羽
 * @date 2018年6月12日 上午10:39:42
 */
public class Https {

	private static final Logger log = LoggerFactory.getLogger(Https.class);

	private String url;

	private Map<String, String> params;

	private String dataType;

	private String result;

	private Map<String, String> requestPropertys;

	private String payloadJson;

	public Https() {
	}

	public Https(String url, String dataType, Map<String, String> params, Map<String, String> requestPropertys,
			String payloadJson) {
		this.url = url;
		this.dataType = dataType;
		this.params = params;
		this.requestPropertys = requestPropertys;
		this.payloadJson = payloadJson;
	}

	/**
	 * @Title: getUrl
	 * @Description: 获取当前使用的URL
	 * @author JuFF_白羽
	 * @return String
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @Title: setUrl
	 * @Description: 设置URL
	 * @author JuFF_白羽
	 * @param url
	 *            请求地址
	 * @return Https
	 */
	public Https setUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * @Title: getParams
	 * @Description: 获取当前使用的参数
	 * @author JuFF_白羽
	 * @return Map<String,String>
	 */
	public Map<String, String> getParams() {
		return params;
	}

	/**
	 * @Title: setParams
	 * @Description: 设置参数
	 * @author JuFF_白羽
	 * @param params
	 *            参数键值对
	 * @return Https
	 */
	public Https setParams(Map<String, String> params) {
		this.params = params;
		return this;
	}

	/**
	 * @Title: setDataType
	 * @Description: 设置请求方式
	 *               <p>
	 *               可用"POST","GET"
	 * @author JuFF_白羽
	 * @param dataType
	 * @return Https 返回类型
	 */
	public Https setDataType(String dataType) {
		this.dataType = dataType;
		return this;
	}

	/**
	 * @Title: setRequestProperty
	 * @Description: 设置请求头
	 * @author JuFF_白羽
	 * @param requestPropertys
	 *            请求头的Map
	 * @return Https 返回类型
	 */
	public Https setRequestProperty(Map<String, String> requestPropertys) {
		this.requestPropertys = requestPropertys;
		return this;
	}

	/**
	 * @Title: setPayloadJson
	 * @Description: 设置需要用流写入的json参数
	 *               <p>
	 *               该方法是为了无法用url?携带参数的请求而编写的，使用输出流将参数写入后台请求中。
	 * @author JuFF_白羽
	 * @param payloadJson
	 *            json字符串
	 * @return Https 返回类型
	 */
	public Https setPayloadJson(String payloadJson) {
		this.payloadJson = payloadJson;
		return this;
	}

	/**
	 * @Title: send
	 * @Description: 发送请求
	 * @author JuFF_白羽
	 * @return String 返回类型
	 */
	public String send() {
		validateRule(this.url);
		StringBuffer buffer = null;
		try {
			// 以SSL的规则创建SSLContext
			SSLContext sslContext = SSLContext.getInstance("SSL");
			TrustManager[] tm = { new MyX509TrustManager() };
			// 初始化
			sslContext.init(null, tm, new SecureRandom());
			// 获取SSLSocketFactory对象
			SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
			URL url = new URL(installParams(this.url, this.params));
			HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			// 请求不使用缓存
			urlConn.setUseCaches(false);
			// 设置请求头
			if (requestPropertys != null) {
				for (Map.Entry<String, String> entry : requestPropertys.entrySet()) {
					urlConn.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			// 设置请求方式
			urlConn.setRequestMethod(this.dataType);
			// 设置当前实例使用的SSLSoctetFactory
			urlConn.setSSLSocketFactory(sslSocketFactory);
			// 设置需要用流写入的请求参数
			if (payloadJson != null && !payloadJson.equals("")) {
				OutputStreamWriter writer = new OutputStreamWriter(urlConn.getOutputStream(), "UTF-8");
				writer.write(payloadJson);
				writer.close();
			}
			urlConn.connect();
			// 读取服务器端返回的内容
			InputStream is = urlConn.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			buffer = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				buffer.append(line);
			}
		} catch (NoSuchAlgorithmException e) {
			log.info(e.toString());
		} catch (KeyManagementException e) {
			log.info(e.toString());
		} catch (MalformedURLException e) {
			log.info(e.toString());
		} catch (IOException e) {
			log.info(e.toString());
		} finally {
			new Https(null, null, null, null, null);// 清空参数
		}
		return buffer.toString();
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	/**
	 * @Title: validateRule
	 * @Description: 对传入的URL进行必要的校验
	 * @author JuFF_白羽
	 * @param url
	 *            请求地址
	 */
	public static void validateRule(String url) {
		if (url.equals("")) {
			throw new RuleException("url不能为空！");
		}
		if (!url.startsWith("http://") && !url.startsWith("https://")) {
			throw new RuleException("url的格式不正确！");
		}
	}

	/**
	 * @Title: installParams
	 * @Description: 为请求地址增加参数
	 * @author JuFF_白羽
	 * @param url
	 *            请求地址
	 * @param params
	 *            装有参数的Map
	 * @return String 用?带参数的url
	 */
	private static String installParams(String url, Map<String, String> params) {
		if (params == null) {
			return url;
		}
		int flag = 1;
		url += "?";
		for (Map.Entry<String, String> param : params.entrySet()) {
			if (flag > 1) {
				url += "&";
			}
			url = url + param.getKey() + "=" + param.getValue();
			flag += 1;
		}
		return url;
	}

	/**
	 * @Description: 根据图片网络地址,发送https请求获取一个Image对象
	 * @author JuFF_白羽
	 * @return Image 图形图像超类
	 * @throws IOException 
	 */
	public Image getImage() throws IOException {
		validateRule(this.url);
		InputStream inputStream = null;
		try {
			HttpURLConnection connection = null;
			connection = (HttpsURLConnection) new URL(this.url).openConnection();
			connection.setReadTimeout(10000);
			connection.setConnectTimeout(10000);
			connection.setRequestMethod("GET");
			if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
				inputStream = connection.getInputStream();
				return ImageIO.read(inputStream);
			} else {
				log.info("请求网络图片失败：{}", connection.getResponseCode());
			}
		} catch (IOException e) {
			log.info(e.toString());
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return null;
	}

}
