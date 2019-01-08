package com.gnz48.zzt.vo;

/**
 * @Description: 用户VO类
 *               <p>
 *               用于代替用户实体接收参数。
 * @author JuFF_白羽
 * @date 2018年11月28日 上午11:43:18
 */
public class UserVO {

	private Long id;

	private String username;// 帐号

	private String nickname;// 名称（昵称或者真实姓名，不同系统不同定义）

	private String password; // 密码;

	private String salt;// 加密密码的盐

	private String email;// 邮箱

	private byte state;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

}
