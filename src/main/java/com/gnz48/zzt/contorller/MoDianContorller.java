package com.gnz48.zzt.contorller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gnz48.zzt.entity.modian.MoDianPoolProject;
import com.gnz48.zzt.repository.modian.MoDianPoolProjectRepository;
import com.gnz48.zzt.service.MoDianService;
import com.gnz48.zzt.vo.ResultVO;

/**
 * @Description: 摩点项目控制层类
 * @author JuFF_白羽
 * @date 2018年9月17日 上午10:25:25
 */
@RestController
@RequestMapping("/modian")
public class MoDianContorller {

	/**
	 * 摩点集资项目表DAO组件
	 */
	@Autowired
	private MoDianPoolProjectRepository moDianPoolProjectRepository;
	
	/**
	 * 摩点项目操作服务类
	 */
	@Autowired
	private MoDianService moDianService;

	/**
	 * @Description: 新增一条摩点项目
	 * @author JuFF_白羽
	 * @param id URL中的ID
	 */
	@PostMapping("/add")
	public ResultVO addMoDianPoolProject(Long id) {
		ResultVO result = new ResultVO();
		if (id != null) {
			MoDianPoolProject moDianPoolProject = moDianPoolProjectRepository.save(new MoDianPoolProject(id));
			if (moDianPoolProject.getId() == id) {
				result.setStatus(200);
			} else {
				result.setStatus(500);
				result.setCause("新增失败");
			}
		} else {
			result.setStatus(400);
			result.setCause("ID不能为空");
		}
		return result;
	}
	
	/**
	 * @Description: 删除摩点项目
	 * @author JuFF_白羽
	 * @param id ID集合
	 */
	@PostMapping("/delete")
	public ResultVO deleteMoDianPoolProject(String id) {
		ResultVO result = new ResultVO();
		if (id != null) {
			result.setStatus(moDianService.deleteMoDianPoolProject(id));
		} else {
			result.setStatus(400);
			result.setCause("ID不能为空");
		}
		return result;
	}

}
