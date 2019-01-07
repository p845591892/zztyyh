package com.gnz48.zzt.vo;

/**
 * @Description: 资源VO类
 *               <P>
 *               用于代替资源实体类接收参数。
 * @author JuFF_白羽
 * @date 2018年12月6日 上午11:22:41
 */

/**
 * @ClassName: PermissionVO
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author lcy
 * @date 2018年12月6日 下午5:31:45
 */
public class PermissionVO {

	private Long id;// 主键.

	private String name;// 名称.

	private String resourceType;// 资源类型，[menu|button]

	private String url;// 资源路径.

	private String permission; // 权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view

	private Long parentId; // 父编号

	private String parentIds; // 父编号列表

	private Boolean available = Boolean.FALSE;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	@Override
	public String toString() {
		return "PermissionVO [id=" + id + ", name=" + name + ", resourceType=" + resourceType + ", url=" + url
				+ ", permission=" + permission + ", parentId=" + parentId + ", parentIds=" + parentIds + ", available="
				+ available + "]";
	}

}
