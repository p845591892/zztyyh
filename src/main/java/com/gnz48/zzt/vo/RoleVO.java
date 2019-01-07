package com.gnz48.zzt.vo;

/**
 * @Description: 角色VO类
 *               <p>
 *               用于代替角色实体类接收参数。
 * @author JuFF_白羽
 * @date 2018年11月28日 上午11:40:26
 */
public class RoleVO {

	private Long id; // 编号

	private String role; // 角色标识程序中判断使用,如"admin",这个是唯一的:

	private String description; // 角色描述,UI界面显示使用

	private Boolean available = Boolean.FALSE; // 是否可用,如果不可用将不会添加给用户

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

}
