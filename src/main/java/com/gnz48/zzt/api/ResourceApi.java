package com.gnz48.zzt.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gnz48.zzt.repository.QQCommunityRepository;
import com.gnz48.zzt.repository.QuartzConfigRepository;
import com.gnz48.zzt.repository.RoomMonitorRepository;
import com.gnz48.zzt.repository.modian.MoDianPoolProjectRepository;
import com.gnz48.zzt.repository.snh48.MemberRepository;
import com.gnz48.zzt.repository.weibo.WeiboUserRepository;
import com.gnz48.zzt.service.ResourceManagementService;
import com.gnz48.zzt.service.SystemManageService;
import com.gnz48.zzt.vo.ResultVO;

/**
 * @ClassName: ResourceManagementApi
 * @Description: 资源的数据接口
 *               <p>
 *               存放的接口均为获取数据的接口，该接口不需要登录验证。
 * @author JuFF_白羽
 * @date 2018年7月19日 下午2:28:56
 */
@RestController
@RequestMapping("/resource")
public class ResourceApi {

	/**
	 * 成员表DAO组件
	 */
	@Autowired
	private MemberRepository memberRepository;

	/**
	 * QQ群监控口袋房间表DAO组件
	 */
	@Autowired
	private RoomMonitorRepository roomMonitorRepository;

	/**
	 * 资源管理模块的服务类
	 */
	@Autowired
	private ResourceManagementService resourceManagementService;

	/**
	 * QQ群信息表DAO组件
	 */
	@Autowired
	private QQCommunityRepository qqCommunityRepository;

	/**
	 * 摩点集资项目表DAO组件
	 */
	@Autowired
	private MoDianPoolProjectRepository moDianPoolProjectRepository;

	/**
	 * 微博用户表DAO组件
	 */
	@Autowired
	private WeiboUserRepository weiboUserRepository;

	/**
	 * 定时任务配置表DAO组件
	 */
	@Autowired
	private QuartzConfigRepository quartzConfigRepository;

	/**
	 * 系统配置的服务类
	 */
	@Autowired
	private SystemManageService systemManageService;

	/**
	 * @Description: 获取成员信息列表
	 * @author JuFF_白羽
	 */
	@GetMapping("/meber")
	public ResultVO getMemberList() {
		ResultVO result = new ResultVO();
		result.setStatus(200);
		result.setData(memberRepository.findAll());
		return result;
	}

	/**
	 * @Description: 获取成员房间的监控信息列表
	 * @author JuFF_白羽
	 */
	@GetMapping("/meber/room-monitor/{roomId}")
	public ResultVO getRoomMonitorTableHtml(@PathVariable Long roomId) {
		ResultVO result = new ResultVO();
		result.setStatus(200);
		result.setData(resourceManagementService.getRoomMonitorTableHtml(roomId));
		return result;
	}

	/**
	 * @Description: 获取成员房间的新增弹窗内容
	 * @author JuFF_白羽
	 */
	@GetMapping("/meber/add-monitor-layer/{roomId}")
	public ResultVO getMeberAddMonitorLayerHtml(@PathVariable Long roomId) {
		ResultVO result = new ResultVO();
		result.setStatus(200);
		result.setData(resourceManagementService.getMeberAddMonitorLayerHtml(roomId));
		return result;
	}

	/**
	 * @Description: 获取指定配置中的筛选关键字
	 * @author JuFF_白羽
	 */
	@GetMapping("/room-monitor/{id}/keyword")
	public ResultVO getRoomMonitorKeyword(@PathVariable Long id) {
		ResultVO result = new ResultVO();
		result.setStatus(200);
		result.setData(roomMonitorRepository.findOne(id));
		return result;
	}

	/**
	 * @Description: 获取QQ群信息列表
	 * @author JuFF_白羽
	 */
	@GetMapping("/qqCommunity")
	public ResultVO getQQCommunityList() {
		ResultVO result = new ResultVO();
		result.setStatus(200);
		result.setData(qqCommunityRepository.findAll());
		return result;
	}

	/**
	 * @Description: 获取摩点项目信息列表
	 * @author JuFF_白羽
	 */
	@GetMapping("/modian")
	public ResultVO getMoDianList() {
		ResultVO result = new ResultVO();
		result.setStatus(200);
		result.setData(moDianPoolProjectRepository.findAll());
		return result;
	}

	/**
	 * @Description: 获取摩点项目的监控信息列表
	 * @author JuFF_白羽
	 */
	@GetMapping("/modian/comment-monitor/{projectId}")
	public ResultVO getCommentMonitorTableHtml(@PathVariable Long projectId) {
		ResultVO result = new ResultVO();
		result.setStatus(200);
		result.setData(resourceManagementService.getCommentMonitorTableHtml(projectId));
		return result;
	}

	/**
	 * @Description: 获取摩点项目监控的新增弹窗内容
	 * @author JuFF_白羽
	 */
	@GetMapping("/modian/add-monitor-layer/{projectId}")
	public ResultVO getModianAddMonitorLayerHtml(@PathVariable Long projectId) {
		ResultVO result = new ResultVO();
		result.setStatus(200);
		result.setData(resourceManagementService.getModianAddMonitorLayerHtml(projectId));
		return result;
	}

	/**
	 * @Description: 获取微博用户列表
	 * @author JuFF_白羽
	 */
	@GetMapping("/weibo")
	public ResultVO getWeiboList() {
		ResultVO result = new ResultVO();
		result.setStatus(200);
		result.setData(weiboUserRepository.findAll());
		return result;
	}

	/**
	 * @Description: 获取微博动态的监控信息列表
	 * @author JuFF_白羽
	 */
	@GetMapping("/weibo/dynamic-monitor/{userId}")
	public ResultVO getDynamicMonitorTableHtml(@PathVariable Long userId) {
		ResultVO result = new ResultVO();
		result.setStatus(200);
		result.setData(resourceManagementService.getDynamicMonitorTableHtml(userId));
		return result;
	}

	/**
	 * @Description: 获取摩点项目监控的新增弹窗内容
	 * @author JuFF_白羽
	 */
	@GetMapping("/weibo/add-monitor-layer/{userId}")
	public ResultVO getWeiboAddMonitorLayerHtml(@PathVariable Long userId) {
		ResultVO result = new ResultVO();
		result.setStatus(200);
		result.setData(resourceManagementService.getWeiboAddMonitorLayerHtml(userId));
		return result;
	}

	/**
	 * @Description: 获取轮询任务的配置列表
	 * @author JuFF_白羽
	 */
	@GetMapping("/quartz-config")
	public ResultVO getQuartzConfingList() {
		ResultVO result = new ResultVO();
		result.setStatus(200);
		result.setData(quartzConfigRepository.findAll());
		return result;
	}

	/**
	 * @Description: 获取系统用户列表
	 * @author JuFF_白羽
	 */
	@GetMapping("/system-user")
	public ResultVO getSystemUserList() {
		ResultVO result = new ResultVO();
		result.setStatus(200);
		result.setData(systemManageService.getUsers());
		return result;
	}

	/**
	 * @Description: 获取系统角色列表
	 * @author JuFF_白羽
	 */
	@GetMapping("/system-role")
	public ResultVO getSystemRoleList() {
		ResultVO result = new ResultVO();
		result.setStatus(200);
		result.setData(systemManageService.getRoles());
		return result;
	}

	/**
	 * @Description: 获取用户未拥有的角色列表
	 * @author JuFF_白羽
	 */
	@GetMapping("/system-user/role/havent/{uid}")
	public ResultVO getSystemUserHaventRole(@PathVariable Long uid) {
		ResultVO result = new ResultVO();
		result.setStatus(200);
		result.setData(systemManageService.getHaventRolesByUid(uid));
		return result;
	}

	/**
	 * @Description: 获取用户拥有的角色列表
	 * @author JuFF_白羽
	 */
	@GetMapping("/system-user/role/have/{uid}")
	public ResultVO getSystemUserHaveRole(@PathVariable Long uid) {
		ResultVO result = new ResultVO();
		result.setStatus(200);
		result.setData(systemManageService.getHaveRolesByUid(uid));
		return result;
	}

	/**
	 * @Description: 获取角色未拥有的资源列表
	 * @author JuFF_白羽
	 */
	@GetMapping("/system-role/permission/havent")
	public ResultVO getSystemRoleHaventPermission(Long rid) {
		ResultVO result = new ResultVO();
		result.setStatus(200);
		result.setData(systemManageService.getHaventPermissionsByRid(rid));
		return result;
	}

	/**
	 * @Description: 获取角色已拥有的资源列表
	 * @author JuFF_白羽
	 */
	@GetMapping("/system-role/permission/have")
	public ResultVO getSystemRoleHavePermission(Long rid) {
		ResultVO result = new ResultVO();
		result.setStatus(200);
		result.setData(systemManageService.getHavePermissionsByRid(rid));
		return result;
	}

	/**
	 * @Description: 获取资源列表
	 * @author JuFF_白羽
	 */
	@GetMapping("/system-permission")
	public ResultVO getSystemPermission(Long id, Long pid) {
		ResultVO result = new ResultVO();
		result.setStatus(200);
		result.setData(systemManageService.getSystemPermission(id, pid));
		return result;
	}

}
