package com.gnz48.zzt.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gnz48.zzt.dao.ResourceManagementDao;
import com.gnz48.zzt.entity.QQCommunity;
import com.gnz48.zzt.entity.snh48.Member;
import com.gnz48.zzt.entity.snh48.RoomMessage;
import com.gnz48.zzt.repository.CommentMonitorRepostiory;
import com.gnz48.zzt.repository.DynamicMonitorRepository;
import com.gnz48.zzt.repository.QQCommunityRepository;
import com.gnz48.zzt.repository.RoomMonitorRepository;
import com.gnz48.zzt.repository.snh48.MemberRepository;
import com.gnz48.zzt.repository.snh48.RoomMessageRepository;
import com.gnz48.zzt.util.StringUtil;
import com.gnz48.zzt.vo.CommentMonitorVO;
import com.gnz48.zzt.vo.DynamicMonitorVO;
import com.gnz48.zzt.vo.MemberVO;
import com.gnz48.zzt.vo.RoomMonitorVO;
import com.gnz48.zzt.vo.snh48.RoomMessageVO;

/**
 * @ClassName: ResourceManagementService
 * @Description: 资源管理模块的服务类
 * @author JuFF_白羽
 * @date 2018年7月25日 上午9:45:20
 */
@Service
@Transactional
public class ResourceManagementService {

	@Autowired
	private RoomMonitorRepository roomMonitorRepository;

	@Autowired
	private QQCommunityRepository qqCommunityRepository;

	@Autowired
	private CommentMonitorRepostiory commentMonitorRepostiory;

	@Autowired
	private DynamicMonitorRepository dynamicMonitorRepository;

	@Autowired
	private ResourceManagementDao resourceManagementDao;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private RoomMessageRepository roomMessageRepository;

	/**
	 * @Description: 获取成员房间的监控信息列表的HTML
	 * @author JuFF_白羽
	 * @param roomId 房间ID
	 * @return String Table的DetailView中的HTML内容
	 */
	public String getRoomMonitorTableHtml(Long roomId) {
		List<RoomMonitorVO> vos = roomMonitorRepository.findRoomMonitorAndQQCommunityByRoomId(roomId);
		/* 用拿到的数据构造table */
		StringBuffer sb = new StringBuffer();
		sb.append("<div class=\"content-panel\">");
		sb.append("<table class=\"table table-striped table-advance table-hover\">");
		sb.append("<h4><i class=\"fa fa-angle-right\"></i> 监控配置</h4>");
		sb.append("<hr>");
		sb.append("<thead>");
		sb.append("<tr>");
		sb.append("<th class=\"col-md-4\"><i class=\"fa fa-qq\"></i> QQ（群）名</th>");
		sb.append("<th class=\"col-md-2\"><i class=\"fa fa-qq\"></i> QQ（群）号</th>");
		sb.append("<th class=\"col-md-5\"><i class=\"fa fa-filter\"></i> 关键字筛选</th>");
		sb.append("<th class=\"col-md-1\"><button class=\"btn btn-success btn-xs\" onclick=\"showAddMonitor(this,"
				+ roomId + ")\"><i class=\"fa fa-plus-circle fa-lg\"></i> 新增</button></th>");
		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
		/* tbody start */
		for (RoomMonitorVO vo : vos) {
			long id = vo.getRoomMonitor().getId();
			long qq = vo.getQqCommunity().getId();// QQ号
			String qqName = vo.getQqCommunity().getCommunityName();// QQ（群）名称
			String keywords = vo.getRoomMonitor().getKeywords();// 关键字
			if (keywords == null) {
				keywords = "无关键字筛选";
			}
			sb.append("<tr>");
			sb.append("<td>" + qqName + "</td>");
			sb.append("<td>" + qq + "</td>");
			sb.append("<td>" + keywords + "</td>");
			sb.append("<td>");
			sb.append("<button class=\"btn btn-primary btn-xs\" onclick=\"updateKeyword(" + id
					+ ")\"><i class=\"fa fa-pencil\"></i></button>");
			sb.append("<button class=\"btn btn-danger btn-xs\" onclick=\"deleteMonitor(" + id
					+ ")\"><i class=\"fa fa-trash-o\"></i></button>");
			sb.append("</td>");
			sb.append("</tr>");
		}
		/* tbody end */
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</div>");
		return sb.toString();
	}

	/**
	 * @Description: 获取成员房间监控的新增弹窗内容
	 * @author JuFF_白羽
	 * @param roomId 房间ID
	 * @return String 新增弹窗的HTML
	 */
	public String getMeberAddMonitorLayerHtml(Long roomId) {
		List<QQCommunity> qqCommunitys = qqCommunityRepository.findByNotInIdAndRoomId(roomId);
		/* 用拿到的数据构造Layer */
		StringBuffer sb = new StringBuffer();
		sb.append("<form id=\"monitor-form\" class=\"form-horizontal style-form\">");
		sb.append("<div class=\"form-group\">");
		sb.append("<div class=\"col-sm-1\"></div>");
		sb.append("<label class=\"col-sm-2 control-label\">关键字：</label>");
		sb.append("<div class=\"col-sm-7\">");
		sb.append("<textarea class=\"form-control\" rows=\"3\" name=\"keyword\"></textarea>");
		sb.append("<span class=\"help-block\">对监控的消息进行关键字筛选，只发送包含关键字的消息。（关键字用逗号隔开，为空则不做筛选）</span>");
		sb.append("</div>");
		sb.append("</div>");
		sb.append("<div class=\"form-group\">");
		sb.append("<div class=\"col-sm-1\"></div>");
		sb.append("<label class=\"col-sm-2 control-label\">发送目标：</label>");
		sb.append("<div class=\"col-sm-7\">");
		sb.append("<select class=\"form-control\" name=\"communityId\">");
		/* select start */
		for (QQCommunity qqCommunity : qqCommunitys) {
			long qq = qqCommunity.getId();
			String qqName = qqCommunity.getCommunityName();
			sb.append("<option value=\"" + qq + "\">" + qqName + "(" + qq + ")</option>");
		}
		/* select end */
		sb.append("</select>");
		sb.append(
				"<span class=\"help-block\">没有你想要的QQ号？到<a href=\"/resource-management/qq-table\">QQ列表</a>进行操作</span>");
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</form>");
		return sb.toString();
	}

	/**
	 * @Description: 获取摩点项目的监控信息列表
	 * @author JuFF_白羽
	 * @param projectId 摩点项目ID
	 * @return String 摩点集资记录监控列表的HTML
	 */
	public String getCommentMonitorTableHtml(Long projectId) {
		List<CommentMonitorVO> vos = commentMonitorRepostiory.findMoDianCommentAndQQCommunityByProjectId(projectId);
		/* 用拿到的数据构造table */
		StringBuffer sb = new StringBuffer();
		sb.append("<div class=\"content-panel\">");
		sb.append("<table class=\"table table-striped table-advance table-hover\">");
		sb.append("<h4><i class=\"fa fa-angle-right\"></i> 监控配置</h4>");
		sb.append("<hr>");
		sb.append("<thead>");
		sb.append("<tr>");
		sb.append("<th class=\"col-md-4\"><i class=\"fa fa-qq\"></i> QQ（群）名</th>");
		sb.append("<th class=\"col-md-2\"><i class=\"fa fa-qq\"></i> QQ（群）号</th>");
		sb.append("<th class=\"col-md-1\"><button class=\"btn btn-success btn-xs\" onclick=\"showAddMonitor(this,"
				+ projectId + ")\"><i class=\"fa fa-plus-circle fa-lg\"></i> 新增</button></th>");
		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
		/* tbody start */
		for (CommentMonitorVO vo : vos) {
			long id = vo.getCommentMonitor().getId();
			long qq = vo.getQqCommunity().getId();// QQ号
			String qqName = vo.getQqCommunity().getCommunityName();// QQ（群）名称
			sb.append("<tr>");
			sb.append("<td>" + qqName + "</td>");
			sb.append("<td>" + qq + "</td>");
			sb.append("<td>");
			sb.append("<button class=\"btn btn-danger btn-xs\" onclick=\"deleteMonitor(" + id
					+ ")\"><i class=\"fa fa-trash-o\"></i></button>");
			sb.append("</td>");
			sb.append("</tr>");
		}
		/* tbody end */
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</div>");
		return sb.toString();
	}

	/**
	 * @Description: 获取摩点项目监控的新增弹窗内容
	 * @author JuFF_白羽
	 * @param projectId 摩点项目ID
	 * @return String 新增弹窗的HTML
	 */
	public String getModianAddMonitorLayerHtml(Long projectId) {
		List<QQCommunity> qqCommunitys = qqCommunityRepository.findByNotInIdAndProjectId(projectId);
		/* 用拿到的数据构造Layer */
		StringBuffer sb = new StringBuffer();
		sb.append("<form id=\"monitor-form\" class=\"form-horizontal style-form\">");
		sb.append("<div class=\"form-group\">");
		sb.append("<div class=\"col-sm-1\"></div>");
		sb.append("<label class=\"col-sm-2 control-label\">发送目标：</label>");
		sb.append("<div class=\"col-sm-7\">");
		sb.append("<select class=\"form-control\" name=\"communityId\">");
		/* select start */
		for (QQCommunity qqCommunity : qqCommunitys) {
			long qq = qqCommunity.getId();
			String qqName = qqCommunity.getCommunityName();
			sb.append("<option value=\"" + qq + "\">" + qqName + "(" + qq + ")</option>");
		}
		/* select end */
		sb.append("</select>");
		sb.append(
				"<span class=\"help-block\">没有你想要的QQ号？到<a href=\"/resource-management/qq-table\">QQ列表</a>进行操作</span>");
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</form>");
		return sb.toString();
	}

	/**
	 * @Description: 获取微博动态监控列表
	 * @author JuFF_白羽
	 * @param userId 微博用户ID
	 * @return String 微博动态监控列表的HTML
	 */
	public String getDynamicMonitorTableHtml(Long userId) {
		List<DynamicMonitorVO> vos = dynamicMonitorRepository.findDynamicMonitorAndQQCommunityByUserId(userId);
		StringBuffer sb = new StringBuffer();
		sb.append("<div class=\"content-panel\">");
		sb.append("<table class=\"table table-striped table-advance table-hover\">");
		sb.append("<h4><i class=\"fa fa-angle-right\"></i> 监控配置</h4>");
		sb.append("<hr>");
		sb.append("<thead>");
		sb.append("<tr>");
		sb.append("<th class=\"col-md-4\"><i class=\"fa fa-qq\"></i> QQ（群）名</th>");
		sb.append("<th class=\"col-md-2\"><i class=\"fa fa-qq\"></i> QQ（群）号</th>");
		sb.append("<th class=\"col-md-1\"><button class=\"btn btn-success btn-xs\" onclick=\"showAddMonitor(this,"
				+ userId + ")\"><i class=\"fa fa-plus-circle fa-lg\"></i> 新增</button></th>");
		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
		/* tbody start */
		for (DynamicMonitorVO vo : vos) {
			long id = vo.getDynamicMonitor().getId();
			long qq = vo.getQqCommunity().getId();// QQ号
			String qqName = vo.getQqCommunity().getCommunityName();// QQ（群）名称
			sb.append("<tr>");
			sb.append("<td>" + qqName + "</td>");
			sb.append("<td>" + qq + "</td>");
			sb.append("<td>");
			sb.append("<button class=\"btn btn-danger btn-xs\" onclick=\"deleteMonitor(" + id
					+ ")\"><i class=\"fa fa-trash-o\"></i></button>");
			sb.append("</td>");
			sb.append("</tr>");
		}
		/* tbody end */
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</div>");
		return sb.toString();
	}

	/**
	 * @Description: 获取微博动态监控的新增弹窗内容
	 * @author: JuFF_白羽
	 * @date: 2018年9月21日 下午10:22:44
	 */
	public String getWeiboAddMonitorLayerHtml(Long userId) {
		List<QQCommunity> qqCommunitys = qqCommunityRepository.findByNotInIdAndUserId(userId);
		/* 用拿到的数据构造Layer */
		StringBuffer sb = new StringBuffer();
		sb.append("<form id=\"monitor-form\" class=\"form-horizontal style-form\">");
		sb.append("<div class=\"form-group\">");
		sb.append("<div class=\"col-sm-1\"></div>");
		sb.append("<label class=\"col-sm-2 control-label\">发送目标：</label>");
		sb.append("<div class=\"col-sm-7\">");
		sb.append("<select class=\"form-control\" name=\"communityId\">");
		/* select start */
		for (QQCommunity qqCommunity : qqCommunitys) {
			long qq = qqCommunity.getId();
			String qqName = qqCommunity.getCommunityName();
			sb.append("<option value=\"" + qq + "\">" + qqName + "(" + qq + ")</option>");
		}
		/* select end */
		sb.append("</select>");
		sb.append(
				"<span class=\"help-block\">没有你想要的QQ号？到<a href=\"/resource-management/qq-table\">QQ列表</a>进行操作</span>");
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</form>");
		return sb.toString();
	}

	/**
	 * @Description: 分页获取口袋房间消息
	 * @author JuFF_白羽
	 * @param pageNumber 当前页数
	 * @param pageSize   每页数据条数
	 * @return PageInfo<RoomMessage> 返回PageInfo的口袋房间消息表
	 */
	public PageInfo<RoomMessageVO> getRoomMessage(Integer pageNumber, Integer pageSize) {
		PageHelper.startPage(pageNumber, pageSize);
		List<RoomMessageVO> roomMessages = resourceManagementDao.findRoomMessage();
		PageInfo<RoomMessageVO> pageInfo = new PageInfo<RoomMessageVO>(roomMessages);
		return pageInfo;
	}

	/**
	 * 分页获取成员列表
	 * 
	 * @param pageNumber 当前页数
	 * @param pageSize   每页条数
	 * @param vo         匹配参数对象
	 * @return Page<Member> 返回Page的成员列表
	 */
	public Page<Member> getMembers(Integer pageNumber, Integer pageSize, MemberVO vo) {
		// 查询条件
		Specification<Member> specification = new Specification<Member>() {

			@Override
			public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				String groupName = vo.getGroupName();
				String teamName = vo.getTeamName();
				String roomMonitor = vo.getRoomMonitor();
				String name = vo.getName();
				String abbr = vo.getAbbr();

				List<Predicate> predicates = new ArrayList<Predicate>();

				if (!StringUtil.isEmpty(groupName) && !groupName.equals("ALL")) {// 匹配团体名称
					Predicate predicate = cb.equal(root.get("groupName").as(String.class), groupName.trim());
					predicates.add(predicate);
				}
				if (!StringUtil.isEmpty(teamName) && !teamName.equals("ALL")) {// 匹配队伍名称
					Predicate predicate = cb.equal(root.get("teamName").as(String.class), teamName.trim());
					predicates.add(predicate);
				}
				if (!StringUtil.isEmpty(roomMonitor) && !roomMonitor.equals("ALL")) {// 匹配房间监控状态

					Predicate predicate = cb.equal(root.get("roomMonitor").as(Integer.class),
							Integer.parseInt(roomMonitor.trim()));
					predicates.add(predicate);
				}
				if (!StringUtil.isEmpty(name)) {// 模糊匹配成员名
					Predicate predicate = cb.like(root.get("name").as(String.class), "%" + name.trim() + "%");
					predicates.add(predicate);
				}
				if (!StringUtil.isEmpty(abbr)) {// 模糊匹配成员名拼音缩写
					Predicate predicate = cb.like(root.get("abbr").as(String.class), "%" + abbr.trim()+ "%");
					predicates.add(predicate);
				}

				return cb.and(predicates.toArray(new Predicate[0]));
			}
		};
		// 分页
		Pageable pageable = new PageRequest(pageNumber - 1, pageSize);

		Page<Member> members = null;
		try {
			members = memberRepository.findAll(specification, pageable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return members;
	}

	/**
	 * 分页获取口袋房间消息列表
	 * 
	 * @param pageNumber 当前页数
	 * @param pageSize   每页条数
	 * @param vo         房间消息参数对象
	 * @return Page<RoomMessage> 返回Page的房间消息列表
	 */
	public Page<RoomMessage> getRoomMessages(Integer pageNumber, Integer pageSize, RoomMessageVO vo) {
		// 查询条件
		Specification<RoomMessage> specification = new Specification<RoomMessage>() {

			@Override
			public Predicate toPredicate(Root<RoomMessage> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				String msgObject = vo.getMsgObject();

				List<Predicate> eqPredicates = new ArrayList<Predicate>();
				
				if (!StringUtil.isEmpty(msgObject) && !msgObject.equals("ALL")) {
					Predicate predicate = cb.equal(root.get("messageObject").as(String.class), msgObject.trim());
					eqPredicates.add(predicate);
				}
				
				if (StringUtil.isEmpty(vo.getSearchText())) {
					return cb.and(eqPredicates.toArray(new Predicate[0]));
				}
				
				String searchText = vo.getSearchText().trim();
				
				List<Predicate> orPredicates = new ArrayList<Predicate>();
				
				Predicate predicate1 = cb.like(root.get("senderName").as(String.class), "%" + searchText.trim() + "%");
				Predicate predicate2 = cb.like(root.get("msgContent").as(String.class), "%" + searchText.trim() + "%");
				orPredicates.add(predicate1);
				orPredicates.add(predicate2);
					
				return query.where(cb.and(eqPredicates.toArray(new Predicate[0])),
													cb.or(orPredicates.toArray(new Predicate[0]))
												).getRestriction();
			}
		};
		// 分页排序
		Sort sort = new Sort(Direction.DESC, "msgTime");
		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort);
		
		Page<RoomMessage> roomMessages = null;
		try {
			roomMessages = roomMessageRepository.findAll(specification, pageable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return roomMessages;
	}

}
