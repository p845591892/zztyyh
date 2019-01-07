package com.gnz48.zzt.repository.weibo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gnz48.zzt.entity.weibo.WeiboUser;

/**
 * @ClassName: UserRepository
 * @Description: 微博用户表DAO组件
 * @author JuFF_白羽
 * @date 2018年8月1日 上午10:45:19
 */
public interface WeiboUserRepository extends JpaRepository<WeiboUser, Long> {

}
