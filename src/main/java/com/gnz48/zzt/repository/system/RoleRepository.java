package com.gnz48.zzt.repository.system;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gnz48.zzt.entity.system.Role;

/**
 * @Description: 角色表DAO组件
 * @author JuFF_白羽
 * @date 2018年11月28日 下午5:01:20
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

}
