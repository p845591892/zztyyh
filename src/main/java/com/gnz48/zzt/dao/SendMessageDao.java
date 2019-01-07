package com.gnz48.zzt.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gnz48.zzt.entity.modian.MoDianComment;

/**
 * @Description: 发送消息的服务的DAO组件
 * @author JuFF_白羽
 * @date 2018年10月22日 下午6:22:43
 */
@Mapper
public interface SendMessageDao {
	
	List<MoDianComment> findMoDianCommentByProjectId(Map<String, Object> param);
	
}
