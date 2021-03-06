package com.gnz48.zzt.repository.system;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gnz48.zzt.entity.system.User;

/**
 * @Description: 用户表DAO组件
 * @author JuFF_白羽
 * @date 2018年11月1日 上午11:45:10
 */
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * @Description: 根据用户名获取用户信息
	 * @author JuFF_白羽
	 * @param username 用户名（账号）
	 */
	User findByUsername(String username);

}
