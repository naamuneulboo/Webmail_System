/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deu.cse.spring_webmail.model;

import com.sun.mail.smtp.SMTPMessage;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeUtility;
import java.io.File; 
import java.util.Properties;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j; 

/**
 *
 * @author jongmin
 */
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SmtpAgent {

    @Getter @Setter  protected String host = null;
    @Getter @Setter  protected String userid = null;
    @Getter @Setter protected String to = null;
    @Getter @Setter protected String cc = null;
    @Getter @Setter protected String subj = null;
    @Getter @Setter protected String body = null;
    private List<String> attachments = new ArrayList<>();

    public SmtpAgent(String host, String userid) {
        this.host = host;
        this.userid = userid;
    }

    public void addAttachment(String filePath) {
        this.attachments.add(filePath);
    }

    public boolean sendMessage() {
        boolean status = false;

        Properties props = System.getProperties();
        props.put("mail.debug", false);
        props.put("mail.smtp.host", this.host);
        log.debug("SMTP host : {}", props.get("mail.smtp.host"));

        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(false);

        try {
            SMTPMessage msg = new SMTPMessage(session);
            msg.setFrom(new InternetAddress(this.userid));

            if (this.to.indexOf(';') != -1) {
                this.to = this.to.replaceAll(";", ",");
            }
            msg.setRecipients(Message.RecipientType.TO, this.to);

            if (this.cc.length() > 1) {
                if (this.cc.indexOf(';') != -1) {
                    this.cc = this.cc.replaceAll(";", ",");
                }
                msg.setRecipients(Message.RecipientType.CC, this.cc);
            }

            msg.setSubject(this.subj);
            msg.setHeader("User-Agent", "LJM-WM/0.1");

            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setText(this.body);

            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp);

            for (String filePath : attachments) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                DataSource source = new FileDataSource(filePath);
                attachmentPart.setDataHandler(new DataHandler(source));

                int index = filePath.lastIndexOf(File.separator);
                String fileName = filePath.substring(index + 1);
                attachmentPart.setFileName(MimeUtility.encodeText(fileName, "UTF-8", "B"));

                mp.addBodyPart(attachmentPart);
            }

            msg.setContent(mp);
            Transport.send(msg);

            for (String filePath : attachments) {
                File f = new File(filePath);
                if (!f.delete()) {
                    log.error("{}: 파일 삭제가 제대로 안 됨.", filePath);
                }
            }
            status = true;
        } catch (Exception ex) {
            log.error("sendMessage() error: {}", ex);
        } 
        return status;
    }
}
