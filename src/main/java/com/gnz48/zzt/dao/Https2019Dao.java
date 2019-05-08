package com.gnz48.zzt.dao;

import org.apache.ibatis.annotations.Mapper;

import com.gnz48.zzt.entity.snh48.Member;

/**
 * 2019年版Https服务的Dao组件
 * @author shiro
 *
 */
@Mapper
public interface Https2019Dao {

	/**
	 * 根据id更新成员信息<p>
	 * 更新现有成员的，有变动可能的资料字段，当字段roomMonitor为空时默认不修改。<p>
	 * 参数字段： avatar, teamId, teamName, groupId, groupName, roomMonitor, roomName, topic.
	 * @param member 成员实体类com.gnz48.zzt.entity.snh48.Member
	 * @return 受影响的数据行数
	 */
	int updateMemberById(Member member);

}
