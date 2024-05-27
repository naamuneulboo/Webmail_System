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

    @Getter
    @Setter
    protected String host = null;
    @Getter
    @Setter
    protected String userid = null;
    @Getter
    @Setter
    protected String to = null;
    @Getter
    @Setter
    protected String cc = null;
    @Getter
    @Setter
    protected String subj = null;
    @Getter
    @Setter
    protected String body = null;
    private List<String> attachments = new ArrayList<>();

    public SmtpAgent(String host, String userid) {
        this.host = host;
        this.userid = userid;
    }

    public void addAttachment(String filePath) {
        this.attachments.add(filePath);
    }

    public boolean sendMessage() {
        // 메서드의 성공 여부를 저장하는 변수
        boolean status = false;

        // SMTP 설정을 위한 프로퍼티 객체 생성
        Properties props = System.getProperties();
        props.put("mail.debug", false); // 디버그 모드 비활성화
        props.put("mail.smtp.host", this.host); // SMTP 호스트 설정
        log.debug("SMTP host : {}", props.get("mail.smtp.host"));

        // SMTP 세션 생성
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(false); // 세션 디버그 모드 비활성화

        try {
            // 이메일 메시지 객체 생성
            SMTPMessage msg = new SMTPMessage(session);
            msg.setFrom(new InternetAddress(this.userid)); // 발신자 설정

            // 수신자 주소 설정 (세미콜론을 쉼표로 변경)
            if (this.to.indexOf(';') != -1) {
                this.to = this.to.replaceAll(";", ",");
            }
            msg.setRecipients(Message.RecipientType.TO, this.to);

            // 참조자 주소 설정 (있을 경우)
            if (this.cc.length() > 1) {
                if (this.cc.indexOf(';') != -1) {
                    this.cc = this.cc.replaceAll(";", ",");
                }
                msg.setRecipients(Message.RecipientType.CC, this.cc);
            }

            // 메일 제목과 헤더 설정
            msg.setSubject(this.subj);
            msg.setHeader("User-Agent", "LJM-WM/0.1");

            // 본문 내용 설정
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setText(this.body);

            // 멀티파트 객체 생성 (본문과 첨부파일 포함)
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp);

            // 첨부파일을 처리하기 위한 스레드 목록 생성
            List<Thread> attachmentThreads = new ArrayList<>();

            // 각 첨부파일에 대해 스레드 생성 및 시작
            for (String filePath : attachments) {
                Thread attachmentThread = new Thread(() -> {
                    try {
                        MimeBodyPart attachmentPart = new MimeBodyPart();
                        DataSource source = new FileDataSource(filePath);
                        attachmentPart.setDataHandler(new DataHandler(source));

                        // 파일 이름 설정 (UTF-8 인코딩)
                        int index = filePath.lastIndexOf(File.separator);
                        String fileName = filePath.substring(index + 1);
                        attachmentPart.setFileName(MimeUtility.encodeText(fileName, "UTF-8", "B"));

                        // 멀티파트 객체에 첨부파일 추가 (동기화 블록 사용)
                        synchronized (mp) {
                            mp.addBodyPart(attachmentPart);
                        }
                    } catch (Exception e) {
                        // 첨부파일 추가 중 에러 발생 시 로그 출력
                        log.error("Error adding attachment: {}", filePath, e);
                    }
                });

                // 스레드 목록에 추가하고 스레드 시작
                attachmentThreads.add(attachmentThread);
                attachmentThread.start();
            }

            // 모든 첨부파일 처리 스레드가 종료될 때까지 대기
            for (Thread thread : attachmentThreads) {
                thread.join();
            }

            // 이메일 메시지의 콘텐츠 설정
            msg.setContent(mp);

            // 이메일 전송
            Transport.send(msg);

            // 첨부파일 삭제 시도
            for (String filePath : attachments) {
                File f = new File(filePath);
                if (!f.delete()) {
                    // 파일 삭제 실패 시 로그 출력
                    log.error("{}: 파일 삭제가 제대로 안 됨.", filePath);
                }
            }

            // 메서드 성공 여부 설정
            status = true;
        } catch (Exception ex) {
            // 예외 발생 시 로그 출력
            log.error("sendMessage() error: {}", ex);
        }

        // 메서드의 성공 여부 반환
        return status;
    }
     

}
