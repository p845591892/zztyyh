package com.gnz48.zzt.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件操作工具类
 * 
 * @author shiro
 */
public class FileUtil extends org.apache.velocity.texen.util.FileUtil {

	private static final Logger log = LoggerFactory.getLogger(FileUtil.class);
	
	/**
	 * java文件的16进制文件头
	 */
	public static final String JAVA_CODE = "7061636B";

	/**
	 * 头像相对路径
	 */
	public static final String AVATAR = "image{}avatar{}";

	/**
	 * java文件相对路径
	 */
	public static final String JAVA = "java{}job{}";

	/**
	 * 将文件存储到本地
	 * <p>
	 * autoName为true，将以 yyyyMMddHHmmss + 4位随机数字/字符 的格式生成新的存储的文件名，否则按fileNmae存储。
	 * 
	 * @param path     存储文件的绝对路径
	 * @param is       输入流
	 * @param fileName 原文件名(包括后缀)
	 * @param autoName 是否自动生成文件名，推荐使用true
	 * @return 储存成功返回文件的完整路径，失败返回空串
	 */
	public static String saveFile(String path, InputStream is, String fileName, boolean autoName) {
		if (autoName) {
			String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
			fileName = DateUtil.getDate(new Date(), "yyyyMMddHHmmss")// 时间
					+ VerifyCodeUtils.generateVerifyCode(4)// 随机数字字符
					+ extension;// 拓展名
		}
		return saveFile(path, is, fileName);
	}

	/**
	 * 将文件存储到本地
	 * 
	 * @param path 储存路径
	 * @param is   文件
	 * @param fn   存储文件名称
	 * @return 储存成功返回文件的完整路径，失败返回空串
	 */
	private static String saveFile(String path, InputStream is, String fn) {
		OutputStream os = null;
		log.info("path = [{}], fn = [{}]", path, fn);
		try {
			byte[] bs = new byte[1024];
			int size;
			// 判断是否存在该路径
			log.info("判断是否存在该路径：{}", path);
			File tempFile = getFile(path);
			// 声明输出流
			log.info("声明输出流的地址：{}", tempFile.getPath() + fn);
			os = new FileOutputStream(tempFile.getPath() + File.separator + fn);
			// 开始写入
			while ((size = is.read(bs)) != -1) {
				os.write(bs, 0, size);
			}
			// 获取新的完整路径
			path = tempFile.getPath() + File.separator + fn;
			return path;

		} catch (IOException e) {
			log.info("文件{}写入本地发生IOException", fn);
		} catch (Exception e) {
			log.info("文件{}写入本地发生错误：{}", fn, e.getMessage());
		} finally {
			try {
				os.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			path = "";
		}
		return path;

	}

	/**
	 * 获取存放文件的目录路径
	 * <p>
	 * 根据当前系统以及application中配置的web.load-path路径，自动生成存的放完整路径。
	 * <p>
	 * 所包含的类型相对路径详见本类的静态变量。
	 * 
	 * @param uploadPath 上传文件存放的根目录
	 * @param typePath   各类型相对目录
	 * @return 完整的绝对路径
	 */
	public static String getPath(String uploadPath, String typePath) {
		File file = getFile(uploadPath);
		uploadPath = file.getPath();
		typePath = typePath.replace("{}", File.separator);
		if (uploadPath.endsWith(File.separator)) {
			uploadPath += typePath;
		} else {
			uploadPath = uploadPath + File.separator + typePath;
		}
		return uploadPath;
	}

	/**
	 * 根据路径获取目录对象
	 * <p>
	 * 对该path进行判断，若该目录不存在则新建一个。
	 * 
	 * @param path 路径
	 * @return File对象
	 */
	public static File getFile(String path) {
		File tempFile = new File(path);
		if (!tempFile.exists()) {
			log.info(mkdir(path));
		}
		return tempFile;
	}

	/**
	 * 读取文件的十六进制的文件头
	 * @param file 上传的文件载体
	 * @return 十六进制的文件头
	 */
	public static String getFileHeader(MultipartFile file) {
		InputStream is = null;
		String value = null;
		try {
			is = file.getInputStream();
			byte[] b = new byte[4];
			is.read(b, 0, b.length);
			value = bytesToHexString(b);
		} catch (Exception e) {
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
		return value;
	}

	private static String bytesToHexString(byte[] src) {
		StringBuilder builder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		String hv;
		for (int i = 0; i < src.length; i++) {
			hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
			if (hv.length() < 2) {
				builder.append(0);
			}
			builder.append(hv);
		}
		return builder.toString();
	}

}
