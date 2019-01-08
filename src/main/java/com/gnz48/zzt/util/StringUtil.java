package com.gnz48.zzt.util;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @ClassName: StringUtil
 * @Description: 字符工具类
 *               <p>
 *               包含各种处理字符的静态方法。
 * @author JuFF_白羽
 * @date 2018年7月11日 下午9:08:31
 */
public class StringUtil extends StringUtils{

	/**
	 * @Title: removeNonBmpUnicode
	 * @Description: 过滤掉特殊编码的文字表情，例如emoji表情
	 * @author JuFF_白羽
	 * @param str
	 *            需要过滤的字符串
	 * @return String 过滤后的字符串
	 */
	public static String removeNonBmpUnicode(String str) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (isPunctuation(c) || isUserDefined(c))
				continue;
			else {
				if (!isChinese(c)) {
					str = str.replace(c, '?');
				}
			}
		}
		return str;
	}

	private static boolean isChinese(char c) {
		Character.UnicodeScript sc = Character.UnicodeScript.of(c);
		if (sc == Character.UnicodeScript.HAN) {
			return true;
		}
		return false;
	}

	public static boolean isPunctuation(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if ( // punctuation, spacing, and formatting characters
		ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				// symbols and punctuation in the unified Chinese, Japanese and
				// Korean script
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				// fullwidth character or a halfwidth character
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				// vertical glyph variants for east Asian compatibility
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
				// vertical punctuation for compatibility characters with the
				// Chinese Standard GB 18030
				|| ub == Character.UnicodeBlock.VERTICAL_FORMS
				// ascii
				|| ub == Character.UnicodeBlock.BASIC_LATIN) {
			return true;
		} else {
			return false;
		}
	}

	private static Boolean isUserDefined(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.NUMBER_FORMS || ub == Character.UnicodeBlock.ENCLOSED_ALPHANUMERICS
				|| ub == Character.UnicodeBlock.LETTERLIKE_SYMBOLS || c == '\ufeff' || c == '\u00a0')
			return true;
		return false;
	}

	/**
	 * @Description: shiro密码处理
	 *               <p>
	 *               使用MD5方式加密，获得密码的密文。
	 * @author JuFF_白羽
	 * @param salt
	 *            盐
	 * @param password
	 *            密码明文
	 * @return String MD5加密后的密码
	 */
	public static String shiroMd5(String salt, String password) {
		String hashAlgorithmName = "MD5";// 加密方式
		ByteSource saltByte = ByteSource.Util.bytes(salt);// 盐值
		int hashIterations = 1024;// 加密1024次
		SimpleHash hash = new SimpleHash(hashAlgorithmName, password, saltByte, hashIterations);
		return hash.toString();
	}

//	/**
//	 * @Description: 指定字符串拼接数组，获得新的字符串
//	 *               <p>
//	 *               <blockquote>
//	 *               <table cellpadding=1 cellspacing=0 summary="Split examples
//	 *               showing regex and result">
//	 *               <tr>
//	 *               <th>Regex</th>
//	 *               <th>Array</th>
//	 *               <th>Result</th>
//	 *               </tr>
//	 *               <tr>
//	 *               <td align=center>d</td>
//	 *               <td>{ "ba", "an", "foo" }</td>
//	 *               <td>"badandfood"</td>
//	 *               </tr>
//	 *               </table>
//	 *               </blockquote>
//	 * @author JuFF_白羽
//	 * @param array
//	 *            拼接的数组
//	 * @param regex
//	 *            拼接使用的字符
//	 * @return String 返回连接好的字符串
//	 */
//	public static <T> String join(T[] array, String regex) {
//		String result = "";
//		for (int i = 0; i < array.length; i++) {
//			if (i + 1 < array.length) {
//				result = result + String.valueOf(array[i]) + regex;
//			} else {
//				result = result + String.valueOf(array[i]);
//			}
//		}
//		return result;
//	}

}
