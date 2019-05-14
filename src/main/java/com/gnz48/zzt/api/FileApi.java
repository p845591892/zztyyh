package com.gnz48.zzt.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.gnz48.zzt.util.FileUtil;
import com.gnz48.zzt.vo.ResultVO;

/**
 * 文件操作控制层<p>
 * 提供文件相关的接口，包括上传、下载等。
 * @author shiro
 *
 */
@RestController
@RequestMapping("/file")
public class FileApi {
	
//	private static final Logger log = LoggerFactory.getLogger(FileApi.class);
	
	/**
	 * 上传文件存储根目录
	 */
	@Value("${web.upload-path}")
	private String uploadPath;
	
	/**
	 * java文件的16进制文件头
	 */
	public static final String JAVA_CODE = "7061636B";
	
	/**
	 * @Description: 测试
	 * @author JuFF_白羽
	 */
	@GetMapping("/test")
	public ModelAndView toRoomMessage(ModelAndView mav) {
		mav.setViewName("/test");
		return mav;
	}

	/**
	 * 上传头像
	 */
	@PostMapping("/upload/avatar")
	public ResultVO uploadAvatar(@RequestParam(name = "avatar") MultipartFile multipartFile) {
		ResultVO result = new ResultVO();
		if (multipartFile == null) {
			result.setStatus(404);
			result.setCause("文件上传失败");
		} else if (multipartFile.isEmpty()) {
			result.setStatus(400);
			result.setCause("文件内容不能为空");
		} else {
			String path = FileUtil.getPath(uploadPath, FileUtil.AVATAR);
			try {
				path = FileUtil.saveFile(path, multipartFile.getInputStream(), multipartFile.getOriginalFilename(), true);
				result.setStatus(200);
				result.setCause("success");
				result.setData(path);
			} catch (IOException e) {
				result.setStatus(500);
				result.setCause("error");
				result.setData(e.getMessage());
			}
		}
		return result;
	}

	/**
	 * 上传定时任务job类
	 */
	@PostMapping("/upload/job")
	public ResultVO uploadQuartz(@RequestParam(name = "job") MultipartFile multipartFile) {
		ResultVO result = new ResultVO();
		if (multipartFile == null) {
			result.setStatus(404);
			result.setCause("文件上传失败");
		} else if (multipartFile.isEmpty()) {
			result.setStatus(400);
			result.setCause("文件内容不能为空");
		} else if (!FileUtil.getFileHeader(multipartFile).equals(JAVA_CODE)) {
			result.setStatus(400);
			result.setCause("请上传java文件");
		} else {
			String path = FileUtil.getPath(uploadPath, FileUtil.JAVA);
			try {
				path = FileUtil.saveFile(path, multipartFile.getInputStream(), multipartFile.getOriginalFilename(), true);
				result.setStatus(200);
				result.setCause("success");
				result.setData(path);
			} catch (IOException e) {
				result.setStatus(500);
				result.setCause("error");
				result.setData(e.getMessage());
			}
		}
		return result;
	}
	
}
