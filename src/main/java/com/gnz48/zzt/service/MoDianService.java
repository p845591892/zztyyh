package com.gnz48.zzt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnz48.zzt.repository.modian.MoDianCommentRepository;
import com.gnz48.zzt.repository.modian.MoDianPoolProjectRepository;

/**
 * @Description: 摩点项目操作服务类
 * @author JuFF_白羽
 * @date 2018年9月17日 上午10:56:48
 */
@Service
@Transactional
public class MoDianService {
	
	/**
	 * 摩点集资项目表DAO组件
	 */
	@Autowired
	private MoDianPoolProjectRepository moDianPoolProjectRepository;
	
	/**
	 * 摩点评论表DAO组件
	 */
	@Autowired
	private MoDianCommentRepository moDianCommentRepository;

	/**
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @author JuFF_白羽
	 * @param id ID集合
	 */
	public Integer deleteMoDianPoolProject(String id) {
		String[] ids = id.split(",");
		for (String idStr : ids) {
			moDianCommentRepository.deleteByprojectId(Long.parseLong(idStr));
			moDianPoolProjectRepository.delete(Long.parseLong(idStr));
		}
		return 200;
	}

}
