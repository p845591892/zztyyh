package com.gnz48.zzt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gnz48.zzt.entity.RoomMonitor;
import com.gnz48.zzt.vo.RoomMonitorVO;

/**
 * 
 * Copyright: Copyright (c) 2018 LanRu-Caifu
 * 
 * @ClassName: RoomMonitorRepository.java
 * @Description: QQ群监控口袋房间表DAO组件
 *
 * @version: v1.0.0
 * @author: JuFF_白羽
 * @date: 2018年7月12日 下午11:41:45
 * 
 */
public interface RoomMonitorRepository extends JpaRepository<RoomMonitor, Long> {

	/**
	 * 
	 * @Function: RoomMonitorRepository.java
	 * @Description: 根据房间ID获取该房间的监控信息
	 *
	 * @param: 房间ID
	 * @return：返回QQ群监控口袋房间的VO类集合
	 *
	 * @version: v1.0.0
	 * @author: JuFF_白羽
	 */
	@Query(value = "select new com.gnz48.zzt.vo.RoomMonitorVO(r,q) from RoomMonitor r, com.gnz48.zzt.entity.QQCommunity q where r.communityId = q.id and r.roomId = ?1")
	List<RoomMonitorVO> findRoomMonitorAndQQCommunityByRoomId(Long roomId);

	/**
	 * @Title: updateKeywordById
	 * @Description: 根据配置ID修改过滤关键字
	 * @author JuFF_白羽
	 * @param id 监控配置ID
	 * @param keyword 监控过滤关键字
	 */
	@Transactional
	@Modifying
	@Query(value = "update RoomMonitor t set t.keywords = ?2 where t.id = ?1")
	int updateKeywordById(Long id, String keyword);
	
	/**
	 * @Title: deleteByCommunityId
	 * @Description: 删除QQ号对应的配置
	 * @author JuFF_白羽
	 * @param communityId QQ号
	 * @return int 受影响的行数
	 */
	@Transactional
	@Modifying
	@Query("delete from RoomMonitor t where t.communityId = ?1")
	int deleteByCommunityId(long communityId);

}
