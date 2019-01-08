package com.gnz48.zzt.util;

import java.io.File;
import java.io.StringWriter;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * @Description: 邮件操作工具类
 *               <p>
 *               提供对邮件操作的封装方法，例如发送邮件。由于使用到的拓展接口org.springframework.mail.javamail.JavaMailSender
 *               需要从Spring中获取Bean，因此该工具类中的静态方法只适用于Spring启动了的环境下。
 * @author JuFF_白羽
 * @date 2019年1月2日 下午3:12:00
 */
public class Mail {

	private static final Logger log = LoggerFactory.getLogger(Mail.class);

	/**
	 * @Description: 发送仅包含文本的邮件
	 *               <p>
	 *               remark: 该方法只适用于Spring启动了的环境下。
	 * @author JuFF_白羽
	 * @param subject
	 *            邮件主题
	 * @param text
	 *            邮件文本内容
	 * @param tos
	 *            目标邮箱
	 */
	public static void sendTextMail(String subject, String text, String... tos) {

		if (tos == null) {
			return;
		}

		// 获取发送者邮箱
		String from = SpringUtil.getBean(MailProperties.class).getUsername();
		for (String to : tos) {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(from);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(text);

			// 获得拓展接口来发送邮件
			SpringUtil.getBean(JavaMailSender.class).send(message);

			log.info("成功发送一条文本邮件到{}", to);
		}

	}

	/**
	 * @Description: 发送带附件的文本邮件
	 *               <p>
	 *               如果附件为空则发送普通的文本邮件。
	 *               <p>
	 *               remark: 该方法只适用于Spring启动了的环境下。
	 * @author JuFF_白羽
	 * @param subject
	 *            邮件主题
	 * @param text
	 *            文本内容
	 * @param to
	 *            目标邮箱
	 * @param files
	 *            附件
	 */
	public static void sendFileMail(String subject, String text, String to, File... files) {

		if (to == null || to.equals("")) {
			return;
		}

		if (files == null) {
			sendTextMail(subject, text, to);
			return;
		}

		// 获取拓展接口
		JavaMailSender javaMailSender = SpringUtil.getBean(JavaMailSender.class);
		// 创建MimeMessage
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		// 获取发送者邮箱
		String from = SpringUtil.getBean(MailProperties.class).getUsername();

		try {
			// 用MimeMessageHelper构造复杂邮件
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(from);
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(text);
			// 嵌入资源
			for (File file : files) {
				FileSystemResource fileSystemResource = new FileSystemResource(file);
				mimeMessageHelper.addAttachment(file.getName(), fileSystemResource);
			}
		} catch (MessagingException e) {
			log.info(e.toString());
			return;
		}

		javaMailSender.send(mimeMessage);

		log.info("成功发送一条附件邮件到{}", to);

	}

	/**
	 * @Description: 发送嵌入静态资源的邮件
	 *               <p>
	 *               资源不能为空；html中引用资源时使用【cid:】。例如资源为weixin.jpg，则静态嵌入方式为
	 *               ：src="cid:weixin"。cid引用的key需要和文件名一致，且不能为中文。
	 *               <p>
	 *               remark: 该方法只适用于Spring启动了的环境下。
	 * @author JuFF_白羽
	 * @param subject
	 *            邮件主题
	 * @param text
	 *            HTML
	 * @param to
	 *            目标邮箱
	 * @param files
	 *            资源
	 */
	public static void sendInlineMail(String subject, String text, String to, File... files) {

		if (to == null || to.equals("") || files == null) {
			return;
		}

		// 获取拓展接口
		JavaMailSender javaMailSender = SpringUtil.getBean(JavaMailSender.class);
		// 创建MimeMessage
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		// 获取发送者邮箱
		String from = SpringUtil.getBean(MailProperties.class).getUsername();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(from);
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(text, true);
			// 嵌入资源
			for (File file : files) {
				// 去掉文件后缀，只使用文件名作key
				String key = file.getName();
				int ex = key.lastIndexOf(".");
				key = key.substring(0, ex);
				FileSystemResource fileSystemResource = new FileSystemResource(file);
				mimeMessageHelper.addInline(key, fileSystemResource);
			}
		} catch (MessagingException e) {
			log.info(e.toString());
			return;
		}

		javaMailSender.send(mimeMessage);

		log.info("成功发送一条嵌入静态资源邮件到{}", to);

	}

	/**
	 * @Description: 使用模板发送邮件
	 *               <p>
	 *               利用模板，将模块参数替换后的内容发送到指定邮箱中。模块参数中的key要和模板中的变量名对应一致。例如模板中含有变量${username}，模块参数的key=username。
	 *               <p>
	 *               模板路径参数template需要填写完整路径，例如/templates/template.vm。
	 *               <p>
	 *               remark: 该方法只适用于Spring启动了的环境下。
	 * @author JuFF_白羽
	 * @param subject
	 *            主题
	 * @param template
	 *            模板路径
	 * @param model
	 *            模块参数
	 * @param tos
	 *            目标邮箱
	 */
	public static void sendTemplateMail(String subject, String template, VelocityContext model, String... tos) {

		if (tos == null || template == null || template.equals("")) {
			return;
		}

		// 获取拓展接口
		JavaMailSender javaMailSender = SpringUtil.getBean(JavaMailSender.class);
		// 创建MimeMessage
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		// 获取发送者邮箱
		String from = SpringUtil.getBean(MailProperties.class).getUsername();
		for (String to : tos) {
			try {
				MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
				mimeMessageHelper.setFrom(from);
				mimeMessageHelper.setTo(to);
				mimeMessageHelper.setSubject(subject);

				StringWriter stringWriter = new StringWriter();
				// 合并模板并将呈现的流放入编写器中
				SpringUtil.getBean(VelocityEngine.class).mergeTemplate(template, "UTF-8", model, stringWriter);

				mimeMessageHelper.setText(stringWriter.toString(), true);
			} catch (Exception e) {
				log.info(e.toString());
				return;
			}

			javaMailSender.send(mimeMessage);
			
			log.info("成功发送一条模板邮件到{}", to);

		}

	}

}
