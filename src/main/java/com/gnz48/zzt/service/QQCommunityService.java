package com.gnz48.zzt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnz48.zzt.entity.QQCommunity;
import com.gnz48.zzt.repository.CommentMonitorRepostiory;
import com.gnz48.zzt.repository.DynamicMonitorRepository;
import com.gnz48.zzt.repository.QQCommunityRepository;
import com.gnz48.zzt.repository.RoomMonitorRepository;

/**
 * @ClassName: QQCommunityService
 * @Description: QQ列表操作服务类
 * @author JuFF_白羽
 * @date 2018年8月10日 下午3:38:44
 */
@Service
@Transactional
public class QQCommunityService {

	/**
	 * （yyh）QQ群信息表DAO组件
	 */
	@Autowired
	private QQCommunityRepository qqCommunityRepository;
	
	/**
	 * 摩点项目评论监控配置表DAO组件
	 */
	@Autowired
	private CommentMonitorRepostiory commentMonitorRepostiory;
	
	/**
	 * 微博动态监控配置表DAO组件
	 */
	@Autowired
	private DynamicMonitorRepository dynamicMonitorRepository;
	
	/**
	 * QQ群监控口袋房间表DAO组件
	 */
	@Autowired
	private RoomMonitorRepository roomMonitorRepository;

	/**
	 * @Description: 新增一条QQ信息
	 * @author JuFF_白羽
	 */
	public int addQQCommunity(QQCommunity qqCommunity) {
		if (qqCommunity.getId() == null) {
			return 1;
		}
		if (qqCommunity.getCommunityName() == null || qqCommunity.getCommunityName().equals("")) {
			return 2;
		}
		QQCommunity qq = qqCommunityRepository.findOne(qqCommunity.getId());
		if (qq == null) {
			qq = qqCommunityRepository.save(qqCommunity);
			return 200;
		} else {
			return 3;
		}
	}

	/**
	 * @Description: 修改一条QQ信息
	 * @author JuFF_白羽
	 */
	public int updateQQCommunity(QQCommunity qqCommunity) {
		if (qqCommunity.getId() == null) {
			return 1;
		}
		if (qqCommunity.getCommunityName() == null || qqCommunity.getCommunityName().equals("")) {
			return 2;
		}
		qqCommunityRepository.save(qqCommunity);
		return 200;
	}

	/**
	 * @Description: 删除QQ信息以及关联信息
	 * @author JuFF_白羽
	 */
	public int deleteQQCommunity(String id) {
		String[] ids = id.split(",");
		for (String idStr : ids) {
			commentMonitorRepostiory.deleteByCommunityId(Long.parseLong(idStr));
			dynamicMonitorRepository.deleteByCommunityId(Long.parseLong(idStr));
			roomMonitorRepository.deleteByCommunityId(Long.parseLong(idStr));
			qqCommunityRepository.delete(Long.parseLong(idStr));
		}
		return 200;
	}

}
