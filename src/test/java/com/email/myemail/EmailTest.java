package com.email.myemail;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.junit.Test;

public class EmailTest {

    public static final String SMTPSERVER = "smtp.fastech.com.cn";
    public static final String SMTPPORT = "465";
    public static final String ACCOUT = "liangruo@fastech.com.cn";
    public static final String PWD = "Re095233";

    @Test
    public void testSendEmail() throws Exception {

        // 创建邮件配置
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp"); // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", SMTPSERVER); // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.port", SMTPPORT); 
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.auth", "true"); // 需要请求认证
        props.setProperty("mail.smtp.ssl.enable", "true");// 开启ssl


        // 根据邮件配置创建会话，注意session别导错包
        Session session = Session.getDefaultInstance(props);
        // 开启debug模式，可以看到更多详细的输入日志
        session.setDebug(true);
        //创建邮件
        MimeMessage message = createEmail(session);
        //获取传输通道
        Transport transport = session.getTransport();
        transport.connect(SMTPSERVER,ACCOUT, PWD);
        //连接，并发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();

    }


    public MimeMessage createEmail(Session session) throws Exception {
        // 根据会话创建邮件
        MimeMessage msg = new MimeMessage(session);
        // address邮件地址, personal邮件昵称, charset编码方式
        InternetAddress fromAddress = new InternetAddress(ACCOUT,
                "liangruo", "utf-8");
        // 设置发送邮件方
        msg.setFrom(fromAddress);
        InternetAddress receiveAddress = new InternetAddress(
                "liangruo@fastech.com.cn", "梁若", "utf-8");
        // 设置邮件接收方
        msg.setRecipient(RecipientType.TO, receiveAddress);
        Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH )+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String m = month + "";
		if (month < 10) {
		    m = "0" + m;
        }
        // 设置邮件标题
        msg.setSubject("研发日报-梁若-"+m+day, "utf-8");
        String footer = "\n\n\n\n           此致\n" +
                "敬礼\n" +
                "                                         梁若\n" +
                "*****************************************\n" +
                "上海泛宇信息技术有限公司\n" +
                "地址：上海市杨浦区控江路2063号五环大厦西座16楼\n" +
                "邮编：200092\n" +
                "电话：021-61670006/61670007\n" +
                "传真：021-61670009\n" +
                "\n" +
                "杭州海络研发中心\n" +
                "地址：杭州市滨江区西兴街道聚工路11号创伟科技园B座704室\n" +
                "邮编：310051\n" +
                "电话：0571-56073936\n" +
                "手机：13157186751\n" +
                "*****************************************";
        //邮件内容
        Path path = Paths.get("D:\\Documents\\Desktop\\日报.txt");
        byte[] bytes = Files.readAllBytes(path); //传入Path对象
        //之后可以根据字符集构建字符串
        String content = new String(bytes, "utf-8");



//        String content = "this is a test email.";





        msg.setText("\n " + m+day + " 工作内容：\n\n    "+content + "\n" + footer);
        // 设置显示的发件时间
        msg.setSentDate(new Date());
        // 保存设置
        msg.saveChanges();
        return msg;
    }
}
