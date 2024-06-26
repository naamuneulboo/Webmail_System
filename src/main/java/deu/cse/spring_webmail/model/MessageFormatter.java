/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

import jakarta.mail.Message;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author skylo
 */
@Slf4j
@RequiredArgsConstructor
public class MessageFormatter {

    @NonNull
    private String userid;  // 파일 임시 저장 디렉토리 생성에 필요
    private HttpServletRequest request = null;

    private int totalMessages;
    private int startIndex;
    private int endIndex;
    
    // 220612 LJM - added to implement REPLY
    @Getter
    private String sender;
    @Getter
    private String subject;
    @Getter
    private String body;

    
     public MessageFormatter(String userid, int totalMessages, int startIndex, int endIndex) {
        this.userid = userid;
        this.totalMessages = totalMessages;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }
     
    public String getMessageTable(Message[] messages) {
        StringBuilder buffer = new StringBuilder();

        // 메시지 제목 보여주기
        buffer.append("<table>");  // table start
        buffer.append("<tr> "
                + " <th> No. </td> "
                + " <th> 보낸 사람 </td>"
                + " <th> 제목 </td>     "
                + " <th> 보낸 날짜 </td>   "
                + " <th> 삭제 </td>   "
                + " </tr>");

         for (int i = 0; i < messages.length; i++) { 
            MessageParser parser = new MessageParser(messages[i], userid);
            parser.parse(false);  // envelope 정보만 필요
            
            //인덱스 번호 내림차순
            //int actualIndex = totalMessages - (startIndex + (messages.length -1 -i)) +1;
            int actualIndex = startIndex + i;
            // 메시지 헤더 포맷
            // 추출한 정보를 출력 포맷 사용하여 스트링으로 만들기
            buffer.append("<tr> "
                    + " <td id=no>" + actualIndex + " </td> "
                    + " <td id=sender>" + parser.getFromAddress() + "</td>"
                    + " <td id=subject> "
                    + " <a href=show_message?msgid=" + (startIndex + i) + " title=\"메일 보기\"> "
                    + parser.getSubject() + "</a> </td>"
                    + " <td id=date>" + parser.getSentDate() + "</td>"
                    + " <td id=delete>"
                    + "<a onclick=\"return confirm('정말로 삭제하시겠습니까?')\" href=delete_mail.do"
                    + "?msgid=" + (startIndex+i) + "> 삭제 </a>" + "</td>"
                    + " </tr>");
        }
        buffer.append("</table>");

        return buffer.toString();
//        return "MessageFormatter 테이블 결과";
    }

    
    public String getMessage(Message message) {
        StringBuilder buffer = new StringBuilder();

        MessageParser parser = new MessageParser(message, userid, request);
        parser.parse(true);

        sender = parser.getFromAddress();
        subject = parser.getSubject();
        body = parser.getBody();

        buffer.append("<strong>보낸 사람:</strong> " + parser.getFromAddress() + " <br>");
        buffer.append("<strong>받은 사람:</strong> " + parser.getToAddress() + " <br>");
        buffer.append("<strong>Cc &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; :</strong> " + parser.getCcAddress() + " <br>");
        buffer.append("<strong>보낸 날짜:</strong> " + parser.getSentDate() + " <br>");
        buffer.append("<strong>제 &nbsp;&nbsp;&nbsp;  목:</strong> " + parser.getSubject() + " <br> <hr>");

        buffer.append(parser.getBody());

        List<String> attachedFiles = parser.getAttachmentFileNames();
        if (!attachedFiles.isEmpty()) {
            buffer.append("<br> <hr> <strong>첨부파일:</strong> <br>");
            for (String attachedFile : attachedFiles) {
                buffer.append("<a href=download"
                        + "?userid=" + this.userid
                        + "&filename=" + attachedFile.replaceAll(" ", "%20")
                        + " target=_top> " + attachedFile + "</a> <br>");
            }
        }

        return buffer.toString();
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}
