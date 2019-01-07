package com.gnz48.zzt.vo;

import com.gnz48.zzt.entity.CommentMonitor;
import com.gnz48.zzt.entity.QQCommunity;

/**
 * @ClassName: CommentMonitorVO
 * @Description: 摩点评论监控配置VO类
 *               <p>
 *               包含了摩点评论监控和QQ表字段。
 * @author JuFF_白羽
 * @date 2018年8月9日 上午9:41:28
 */
public class CommentMonitorVO {

	/**
	 * 摩点项目评论监控配置
	 */
	private CommentMonitor commentMonitor;

	/**
	 * （yyh）QQ群信息
	 */
	private QQCommunity qqCommunity;

	public CommentMonitorVO() {
	}

	public CommentMonitorVO(CommentMonitor commentMonitor) {
		QQCommunity qqCommunity = new QQCommunity();
		this.commentMonitor = commentMonitor;
		this.qqCommunity = qqCommunity;
	}

	public CommentMonitorVO(QQCommunity qqCommunity) {
		CommentMonitor commentMonitor = new CommentMonitor();
		this.commentMonitor = commentMonitor;
		this.qqCommunity = qqCommunity;
	}

	public CommentMonitorVO(CommentMonitor commentMonitor, QQCommunity qqCommunity) {
		this.commentMonitor = commentMonitor;
		this.qqCommunity = qqCommunity;
	}

	public CommentMonitor getCommentMonitor() {
		return commentMonitor;
	}

	public void setCommentMonitor(CommentMonitor commentMonitor) {
		this.commentMonitor = commentMonitor;
	}

	public QQCommunity getQqCommunity() {
		return qqCommunity;
	}

	public void setQqCommunity(QQCommunity qqCommunity) {
		this.qqCommunity = qqCommunity;
	}

}
