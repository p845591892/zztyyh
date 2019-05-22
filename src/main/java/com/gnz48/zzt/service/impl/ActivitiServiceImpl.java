package com.gnz48.zzt.service.impl;

import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnz48.zzt.entity.system.Role;
import com.gnz48.zzt.repository.system.RoleRepository;
import com.gnz48.zzt.repository.system.UserRepository;
import com.gnz48.zzt.service.ActivitiService;

/**
 * 流程服务接口实现类
 * @author shiro
 */
@Service
@Transactional
public class ActivitiServiceImpl implements ActivitiService {
	
	private static final Logger log = LoggerFactory.getLogger(ActivitiServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public void syncdata() {
		// 同步user
		List<com.gnz48.zzt.entity.system.User> userList = userRepository.findAll();
		User au = null;
		for (com.gnz48.zzt.entity.system.User user : userList) {
			au = new UserEntity();
			au.setId(String.valueOf(user.getId()));
			au.setFirstName(user.getNickname());
			au.setEmail(user.getEmail());
			identityService.deleteUser(au.getId());
			identityService.saveUser(au);
		}
		// 同步role
		List<Role> roleList = roleRepository.findAll();
		Group group = null;
		for (Role role : roleList) {
			group = new GroupEntity();
			group.setId(String.valueOf(role.getId()));
			group.setName(role.getDescription());
			identityService.deleteGroup(group.getId());
			identityService.saveGroup(group);
			// 同步user-role关联
			List<com.gnz48.zzt.entity.system.User> users = role.getUsers();
			for (com.gnz48.zzt.entity.system.User user : users) {
				identityService.deleteMembership(String.valueOf(user.getId()), String.valueOf(role.getId()));
				identityService.createMembership(String.valueOf(user.getId()), String.valueOf(role.getId()));
			}
		}
		
		log.info("成功同步用户角色信息到activiti表");
	}
	
	

}
