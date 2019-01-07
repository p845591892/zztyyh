package com.gnz48.zzt.vo;

import com.gnz48.zzt.entity.modian.MoDianComment;
import com.gnz48.zzt.entity.modian.MoDianPoolProject;

/**
 * @ClassName: MoDianCommentVO
 * @Description: 摩点评论VO类
 *               <p>
 *               包含摩点评论和摩点集资项目的所有字段。
 * @author JuFF_白羽
 * @date 2018年8月9日 上午11:13:42
 */
public class MoDianCommentVO {

	/**
	 * 摩点评论表
	 */
	private MoDianComment moDianComment;

	/**
	 * 摩点集资项目表
	 */
	private MoDianPoolProject moDianPoolProject;

	public MoDianCommentVO() {
	}

	public MoDianCommentVO(MoDianComment moDianComment) {
		MoDianPoolProject moDianPoolProject = new MoDianPoolProject();
		this.moDianComment = moDianComment;
		this.moDianPoolProject = moDianPoolProject;
	}

	public MoDianCommentVO(MoDianPoolProject moDianPoolProject) {
		MoDianComment moDianComment = new MoDianComment();
		this.moDianComment = moDianComment;
		this.moDianPoolProject = moDianPoolProject;
	}

	public MoDianCommentVO(MoDianComment moDianComment, MoDianPoolProject moDianPoolProject) {
		this.moDianComment = moDianComment;
		this.moDianPoolProject = moDianPoolProject;
	}

	public MoDianComment getMoDianComment() {
		return moDianComment;
	}

	public void setMoDianComment(MoDianComment moDianComment) {
		this.moDianComment = moDianComment;
	}

	public MoDianPoolProject getMoDianPoolProject() {
		return moDianPoolProject;
	}

	public void setMoDianPoolProject(MoDianPoolProject moDianPoolProject) {
		this.moDianPoolProject = moDianPoolProject;
	}

}
