/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

import jakarta.mail.Address;
import jakarta.mail.FetchProfile;
import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;
import java.util.Properties;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author skylo
 */
@Slf4j
@NoArgsConstructor        // 기본 생성자 생성
public class Pop3Agent {
    @Getter @Setter private String host;
    @Getter @Setter private String userid;
    @Getter @Setter private String password;
    @Getter @Setter private Store store;
    @Getter @Setter private String excveptionType;
    @Getter @Setter private HttpServletRequest request;
    
    // 220612 LJM - added to implement REPLY
    @Getter private String sender;
    @Getter private String subject;
    @Getter private String body;
    
    public Pop3Agent(String host, String userid, String password) {
        this.host = host;
        this.userid = userid;
        this.password = password;
    }
    
    public boolean validate() {
        boolean status = false;

        try {
            status = connectToStore();
            store.close();
        } catch (Exception ex) {
            log.error("Pop3Agent.validate() error : " + ex);
            status = false;  // for clarity
        } finally {
            return status;
        }
    }

    public boolean deleteMessage(int msgid, boolean really_delete) {
        boolean status = false;

        if (!connectToStore()) {
            return status;
        }

        try {
            // Folder 설정
//            Folder folder = store.getDefaultFolder();
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);

            // Message에 DELETED flag 설정
            Message msg = folder.getMessage(msgid);
            msg.setFlag(Flags.Flag.DELETED, really_delete);

            // 폴더에서 메시지 삭제
            // Message [] expungedMessage = folder.expunge();
            // <-- 현재 지원 안 되고 있음. 폴더를 close()할 때 expunge해야 함.
            folder.close(true);  // expunge == true
            store.close();
            status = true;
        } catch (Exception ex) {
            log.error("deleteMessage() error: {}", ex.getMessage());
        } finally {
            return status;
        }
    }

    public String getMessageList(int page, int size) {
        String result = "";
        Message[] messages = null;

        if (!connectToStore()) {  // 3.1
            log.error("POP3 connection failed!");
            return "POP3 연결이 되지 않아 메일 목록을 볼 수 없습니다.";
        }

        try {
            // 메일 폴더 열기
            Folder folder = store.getFolder("INBOX");  // 3.2
            folder.open(Folder.READ_ONLY);  // 3.3
            
            //전체 메일 수
            int total = folder.getMessageCount();
            
            if (total == 0) {
            //받은 메일이 없을 경우
                StringBuilder buffer = new StringBuilder();
                buffer.append("<strong>받은 메일이 없습니다.</strong>");
                result = buffer.toString();
                return buffer.toString();
            }
            
            //페이징을 위한 범위 계산
            int start = (page -1)* size + 1;
            int end = Math.min(start + size - 1, total);
            log.info("start={}, end={}",start, end);
            //int startIndex = (page-1)*size+1;
            
            // 현재 수신한 메시지 모두 가져오기
            messages = folder.getMessages(start, end);      // 3.4
            FetchProfile fp = new FetchProfile();
            // From, To, Cc, Bcc, ReplyTo, Subject & Date
            fp.add(FetchProfile.Item.ENVELOPE);
            folder.fetch(messages, fp);

            MessageFormatter formatter = new MessageFormatter(userid,total,start, end);  //3.5
            result = formatter.getMessageTable(messages);   // 3.6

            folder.close(true);  // 3.7
            store.close();       // 3.8
        } catch (Exception ex) {
            log.error("Pop3Agent.getMessageList() : exception = {}", ex.getMessage());
            result = "Pop3Agent.getMessageList() : exception = " + ex.getMessage();
        } finally {
            return result;
        }
    }

    public String getMessage(int n) {
        String result = "POP3  서버 연결이 되지 않아 메시지를 볼 수 없습니다.";

        if (!connectToStore()) {
            log.error("POP3 connection failed!");
            return result;
        }

        try {
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            Message message = folder.getMessage(n);

            MessageFormatter formatter = new MessageFormatter(userid);
            formatter.setRequest(request);  // 210308 LJM - added
            result = formatter.getMessage(message);
            sender = formatter.getSender();  // 220612 LJM - added
            subject = formatter.getSubject();
            body = formatter.getBody();

            folder.close(true);
            store.close();
        } catch (Exception ex) {
            log.error("Pop3Agent.getMessageList() : exception = {}", ex);
            result = "Pop3Agent.getMessage() : exception = " + ex;
        } finally {
            return result;
        }
    }

    private boolean connectToStore() {
        boolean status = false;
        Properties props = System.getProperties();
        // https://jakarta.ee/specifications/mail/2.1/apidocs/jakarta.mail/jakarta/mail/package-summary.html
        props.setProperty("mail.pop3.host", host);
        props.setProperty("mail.pop3.user", userid);
        props.setProperty("mail.pop3.apop.enable", "false");
        props.setProperty("mail.pop3.disablecapa", "true");  // 200102 LJM - added cf. https://javaee.github.io/javamail/docs/api/com/sun/mail/pop3/package-summary.html
        props.setProperty("mail.debug", "false");
        props.setProperty("mail.pop3.debug", "false");

        Session session = Session.getInstance(props);
        session.setDebug(false);

        try {
            store = session.getStore("pop3");
            store.connect(host, userid, password);
            status = true;
        } catch (Exception ex) {
            log.error("connectToStore 예외: {}", ex.getMessage());
        } finally {
            return status;
        }
    }
    
     public String getMyOwnMessages(int page, int size) {
        String result = "";
        Message[] messages = null;

        if (!connectToStore()) {
            log.error("POP3 connection failed!");
            return "POP3 연결이 되지 않아 메일 목록을 볼 수 없습니다.";
        }

        try {
            // 메일 폴더 열기
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            
            //전체 메일 수
            int total = getMyMessagesTotal();
            log.info("messageArray.length={}",total);
            //페이징을 위한 범위 계산
            int start = (page -1)* size + 1;
            int end = Math.min(start + size - 1, total);
            log.info("To.Me start={}, end={}",start, end);
            
            // 현재 수신한 메시지 모두 가져오기
            messages = folder.getMessages(start,end);
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            folder.fetch(messages, fp);

            List<Message> myMessages = new ArrayList<>();
            for (Message message : messages) {
                Address[] fromAddresses = message.getFrom();
                Address[] toAddresses = message.getRecipients(Message.RecipientType.TO);

                if (fromAddresses != null && toAddresses != null) {
                    for (Address from : fromAddresses) {
                        for (Address to : toAddresses) {
                            if (from.toString().equals(to.toString()) && from.toString().equals(userid)) {
                                myMessages.add(message);
                            }
                        }
                    }
                }
            }
            Message[] myMessagesArray = myMessages.toArray(new Message[0]);
            
            MessageFormatter formatter = new MessageFormatter(userid, total, start, end);
            result = formatter.getMessageTable(myMessagesArray);
            log.info("ownMessage Array={}",myMessagesArray);

            folder.close(true);
            store.close();
        } catch (Exception ex) {
            log.error("Pop3Agent.getMyOwnMessages() : exception = {}", ex.getMessage());
            result = "Pop3Agent.getMyOwnMessages() : exception = " + ex.getMessage();
        } finally {
            return result;
        }
    }
    
    public int getTotalMessageCount() {
        if (!connectToStore()) {
            log.error("POP3 connection failed!");
            return 0;
        }

        try {
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            int total = folder.getMessageCount();
            folder.close(true);
            store.close();
            return total;
        } catch (Exception ex) {
            log.error("Pop3Agent.getTotalMessageCount() : exception = {}", ex.getMessage());
            return 0;
        }
    }
    


    public int getMyMessagesTotal() {
        String result = "";
        Message[] messages = null;
    
        if (!connectToStore()) {
            log.error("POP3 connection failed!");
            return 0;
        }
        
        try {
            // 메일 폴더 열기
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            // 현재 수신한 메시지 모두 가져오기
            messages = folder.getMessages();
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            folder.fetch(messages, fp);

            List<Message> myMessages = new ArrayList<>();
            for (Message message : messages) {
                Address[] fromAddresses = message.getFrom();
                Address[] toAddresses = message.getRecipients(Message.RecipientType.TO);

                if (fromAddresses != null && toAddresses != null) {
                    for (Address from : fromAddresses) {
                        for (Address to : toAddresses) {
                            if (from.toString().equals(to.toString()) && from.toString().equals(userid)) {
                                myMessages.add(message);
                            }
                        }
                    }
                }
            }
            Message[] myMessagesArray = myMessages.toArray(new Message[0]);
            //전체 메일 수
            int total = myMessagesArray.length;
            return total;
        } catch (Exception ex) {
            log.error("Pop3Agent.getTotalMessageCount() : exception = {}", ex.getMessage());
            return 0;
    }
    }
}