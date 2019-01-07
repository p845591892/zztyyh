package com.gnz48.zzt.repository.modian;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gnz48.zzt.entity.modian.MoDianComment;
import com.gnz48.zzt.vo.MoDianCommentVO;

/**
 * 
 * @ClassName: MoDianCommentRepository
 * @Description: 摩点评论表DAO组件
 * @author JuFF_白羽
 * @date 2018年8月8日 下午3:29:56
 */
public interface MoDianCommentRepository extends JpaRepository<MoDianComment, Long> {

	/**
	 * @Description: 根据发送状态按评论时间升序获取摩点评论集合
	 * @author JuFF_白羽
	 * @param isSend
	 *            是否发送：1未发送，2已发送
	 * @return List<MoDianComment> 摩点评论集合
	 */
	List<MoDianComment> findByIsSendOrderByBackerDateAsc(int isSend);

	/**
	 * @Description: 根据发送状态按评论时间升序获取摩点评论和对应摩点项目集合
	 * @author JuFF_白羽
	 * @param isSend
	 *            是否发送：1未发送，2已发送
	 * @return List<MoDianCommentVO> 摩点评论VO类集合
	 */
	@Query(value = "select new com.gnz48.zzt.vo.MoDianCommentVO(c,p) from MoDianComment c, com.gnz48.zzt.entity.modian.MoDianPoolProject p where c.projectId = p.id and c.isSend = ?1 order by c.backerDate asc")
	List<MoDianCommentVO> findMoDianCommentAndMoDianPoolProjectBySendQQ(int isSend);

	/**
	 * @Description: 根据项目ID删除对应的集资记录
	 * @author JuFF_白羽
	 * @param id
	 *            项目ID
	 * @return int 受影响的行数
	 */
	@Transactional
	@Modifying
	@Query("delete from MoDianComment t where t.projectId = ?1")
	int deleteByprojectId(long id);

	/**
	 * @Description: 获取指定日期后的摩点集资信息总数量
	 * @author JuFF_白羽
	 * @param date
	 *            指定时间
	 */
	@Query("select count(t) from MoDianComment t where t.backerDate >= ?1")
	Integer countGreaterDate(Date date);

}
