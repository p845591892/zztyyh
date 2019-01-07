package com.gnz48.zzt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gnz48.zzt.entity.CommentMonitor;
import com.gnz48.zzt.vo.CommentMonitorVO;

/**
 * @ClassName: CommentMonitorRepostiory
 * @Description: 摩点项目评论监控配置表DAO组件
 * @author JuFF_白羽
 * @date 2018年8月9日 上午9:39:34
 */
public interface CommentMonitorRepostiory extends JpaRepository<CommentMonitor, Long> {

	/**
	 * @Title: findMoDianCommentAndQQCommunityByProjectId
	 * @Description: 根据摩点项目ID获取
	 * @author JuFF_白羽
	 * @param projectId 摩点项目ID
	 * @return List<CommentMonitorVO> 摩点评论监控配置VO类集合
	 */
	@Query(value = "select new com.gnz48.zzt.vo.CommentMonitorVO(c,q) from CommentMonitor c,com.gnz48.zzt.entity.QQCommunity q where c.communityId = q.id and c.projectId = ?1")
	List<CommentMonitorVO> findMoDianCommentAndQQCommunityByProjectId(Long projectId);

	/**
	 * @Title: deleteByCommunityId
	 * @Description: 删除QQ号对应的配置
	 * @author JuFF_白羽
	 * @param communityId QQ号
	 * @return int 受影响的行数
	 */
	@Transactional
	@Modifying
	@Query("delete from CommentMonitor t where t.communityId = ?1")
	int deleteByCommunityId(long communityId);

}
