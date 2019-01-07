package com.gnz48.zzt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnz48.zzt.dao.SystemManageDao;
import com.gnz48.zzt.entity.system.Permission;
import com.gnz48.zzt.entity.system.Role;
import com.gnz48.zzt.entity.system.User;
import com.gnz48.zzt.repository.system.PermissionRepository;
import com.gnz48.zzt.repository.system.RoleRepository;
import com.gnz48.zzt.vo.PermissionVO;
import com.gnz48.zzt.vo.RoleVO;
import com.gnz48.zzt.vo.UserVO;

/**
 * @Description: 系统配置的服务类
 *               <p>
 *               提供系统配置相关的服务。
 * @author JuFF_白羽
 * @date 2018年11月27日 下午3:35:06
 */
@Service
@Transactional
public class SystemManageService {

	private static final Logger log = LoggerFactory.getLogger(SystemManageService.class);

	/**
	 * 系统配置的DAO组件
	 */
	@Autowired
	private SystemManageDao systemManageDao;

	/**
	 * 角色表DAO组件
	 */
	@Autowired
	private RoleRepository roleRepository;

	/**
	 * 权限资源表DAO组件
	 */
	@Autowired
	private PermissionRepository permissionRepository;

	/**
	 * @Description: 获取系统用户列表
	 * @author JuFF_白羽
	 */
	public List<User> getUsers() {
		return systemManageDao.findUser();
	}

	/**
	 * @Description: 修改用户信息
	 * @author JuFF_白羽
	 */
	public int updateUser(UserVO user) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", user.getId());
		param.put("nickname", user.getNickname());
		param.put("state", user.getState());
		try {
			return systemManageDao.updateUserById(param);
		} catch (Exception e) {
			log.info(e.toString());
			return 0;
		}
	}

	/**
	 * @Description: 获取系统角色列表
	 * @author JuFF_白羽
	 */
	public List<Role> getRoles() {
		return systemManageDao.findRole();
	}

	/**
	 * @Description: 新增角色
	 * @author JuFF_白羽
	 */
	public int addRole(RoleVO role) {
		Role param = new Role();
		param.setAvailable(true);
		param.setDescription(role.getDescription());
		param.setRole(role.getRole());
		try {
			roleRepository.save(param);
			return 1;
		} catch (Exception e) {
			log.info(e.toString());
			return 0;
		}
	}

	/**
	 * @Description: 根据用户ID获取用户未拥有的角色列表
	 * @author JuFF_白羽
	 * @param uid
	 *            用户ID
	 */
	public List<Role> getHaventRolesByUid(Long uid) {
		return systemManageDao.findHaventRoleByUid(uid);
	}

	/**
	 * @Description: 根据用户ID获取用户拥有的角色列表
	 * @author JuFF_白羽
	 * @param uid
	 *            用户ID
	 */
	public List<Role> getHaveRolesByUid(Long uid) {
		return systemManageDao.findHaveRoleByUid(uid);
	}

	/**
	 * @Description: 根据用户ID，为用户赋予新角色
	 * @author JuFF_白羽
	 * @param uid
	 *            用户ID
	 * @param rids
	 *            角色ID数组
	 */
	public int addUserRole(Long uid, String rids) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("uid", uid);
		param.put("rids", rids.split(","));
		return systemManageDao.insertUserRole(param);
	}

	/**
	 * @Description: 根据用户ID，为用户撤销已赋予的角色
	 * @author JuFF_白羽
	 * @param uid
	 *            用户ID
	 * @param rids
	 *            角色ID数组
	 */
	public int deleteUserRole(Long uid, String rids) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("uid", uid);
		param.put("rids", rids.split(","));
		return systemManageDao.deleteUserRole(param);
	}

	/**
	 * @Description: 根据角色ID，修改角色信息
	 * @author JuFF_白羽
	 * @param role
	 *            角色实体类
	 */
	public int updateRole(RoleVO role) {
		if (role.getId() == null) {
			return 0;
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", role.getId());
		param.put("role", role.getRole());
		param.put("description", role.getDescription());
		param.put("available", role.getAvailable());
		return systemManageDao.updateRole(param);
	}

	/**
	 * @Description: 根据角色ID删除一条角色
	 * @author JuFF_白羽
	 * @param id
	 *            角色ID
	 */
	public int deleteRole(Long id) {
		try {
			roleRepository.delete(id);
			return 1;
		} catch (Exception e) {
			log.info(e.toString());
			return 0;
		}
	}

	/**
	 * @Description: 根据角色ID，获取角色未被赋权的URL或BUTTON
	 * @author JuFF_白羽
	 * @param rid
	 *            角色ID
	 * @return List<Permission> 资源列表
	 */
	public List<Permission> getHaventPermissionsByRid(Long rid) {
		return systemManageDao.findHaventPermissionsByRid(rid);
	}

	/**
	 * @Description: 根据角色ID，获取角色已获赋权的URL或BUTTON
	 * @author JuFF_白羽
	 * @param rid
	 *            角色ID
	 * @return List<Permission> 资源列表
	 */
	public List<Permission> getHavePermissionsByRid(Long rid) {
		return systemManageDao.findHavePermissionsByRid(rid);
	}

	/**
	 * @Description: 根据角色ID，为角色赋予新的资源权限
	 * @author JuFF_白羽
	 * @param rid
	 *            角色ID
	 * @param pids
	 *            资源ID集合
	 * @return int 新增数据条数
	 */
	public int addRolePermission(Long rid, String pids) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("rid", rid);
		param.put("pids", pids.split(","));
		try {
			return systemManageDao.insertRolePermission(param);
		} catch (Exception e) {
			log.info(e.toString());
			return 0;
		}
	}

	/**
	 * @Description: 根据角色ID，撤销角色已获权限的资源
	 * @author JuFF_白羽
	 * @param rid
	 *            角色ID
	 * @param pids
	 *            资源ID集合
	 * @return int 删除数据条数
	 */
	public int deleteRolePermission(Long rid, String pids) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("rid", rid);
		param.put("pids", pids.split(","));
		try {
			return systemManageDao.deleteRolePermission(param);
		} catch (Exception e) {
			log.info(e.toString());
			return 0;
		}
	}

	/**
	 * @Description: 获取权限资源列表
	 * @author JuFF_白羽
	 * @return List<Permission> 权限资源列表
	 */
	public List<Permission> getSystemPermission(Long id, Long pid) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("pid", pid);
		return systemManageDao.findPermission(param);
	}

	/**
	 * @Description: 新增资源
	 * @author JuFF_白羽
	 * @param permission
	 *            资源对象
	 */
	public int addPermission(PermissionVO vo) {
		Permission permission = new Permission();
		permission.setName(vo.getName());
		permission.setParentId(vo.getParentId());
		permission.setPermission(vo.getPermission());
		permission.setResourceType(vo.getResourceType());
		permission.setUrl(vo.getUrl());
		try {
			System.out.println(vo.toString());
			permissionRepository.save(permission);
			return 1;
		} catch (Exception e) {
			log.info(e.toString());
			return 0;
		}
	}

	/**
	 * @Description: 修改一条资源信息
	 * @author JuFF_白羽
	 * @param permission
	 *            资源对象
	 */
	public int updatePermission(PermissionVO permission) {
		if (permission.getId() == null) {
			return 0;
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", permission.getId());
		param.put("name", permission.getName());
		param.put("resourceType", permission.getResourceType());
		param.put("url", permission.getUrl());
		param.put("permission", permission.getPermission());
		param.put("parentId", permission.getParentId());
		return systemManageDao.updatePermission(param);
	}

	/**
	 * @Description: 删除一条资源
	 * @author JuFF_白羽
	 * @param id
	 *            资源ID
	 */
	public int deletePermission(Long id) {
		try {
			permissionRepository.delete(id);
			return 1;
		} catch (Exception e) {
			log.info(e.toString());
			return 0;
		}
	}

}
