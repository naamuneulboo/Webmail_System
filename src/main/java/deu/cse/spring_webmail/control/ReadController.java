/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.control;

import deu.cse.spring_webmail.model.Pop3Agent;
import jakarta.mail.internet.MimeUtility;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Prof.Jong Min Lee
 */
@Controller
@PropertySource("classpath:/system.properties")
@Slf4j
public class ReadController {

    @Autowired
    private ServletContext ctx;
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;
    @Value("${file.download_folder}")
    private String DOWNLOAD_FOLDER;

    @GetMapping("/show_message")
    public String showMessage(@RequestParam Integer msgid, Model model) {
        log.debug("download_folder = {}", DOWNLOAD_FOLDER);

        Pop3Agent pop3 = new Pop3Agent();
        pop3.setHost((String) session.getAttribute("host"));
        pop3.setUserid((String) session.getAttribute("userid"));
        pop3.setPassword((String) session.getAttribute("password"));
        pop3.setRequest(request);

        String msg = pop3.getMessage(msgid);
        session.setAttribute("sender", pop3.getSender());  // 220612 LJM - added
        session.setAttribute("subject", pop3.getSubject());
        session.setAttribute("body", pop3.getBody());
        model.addAttribute("msg", msg);
        return "/read_mail/show_message";
    }

     @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("userid") String userId,
            @RequestParam("filename") String fileName) {
        log.debug("userid = {}, filename = {}", userId, fileName);
        try {
            log.debug("userid = {}, filename = {}", userId, MimeUtility.decodeText(fileName));
        } catch (UnsupportedEncodingException ex) {
            log.error("error");
        }

        // 1. 내려받기할 파일의 기본 경로 설정
        String basePath = ctx.getRealPath(DOWNLOAD_FOLDER) + File.separator + userId;

        // 2. 파일의 Content-Type 찾기
        Path path = Paths.get(basePath + File.separator + fileName);
        String contentType = null;
        try {
            contentType = Files.probeContentType(path);
            log.debug("File: {}, Content-Type: {}", path.toString(), contentType);
        } catch (IOException e) {
            log.error("downloadDo: 오류 발생 - {}", e.getMessage());
        }

        // 3. Http 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.builder("attachment").filename(fileName, StandardCharsets.UTF_8).build());
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);

        // 4. 파일을 입력 스트림으로 만들어 내려받기 준비
        Resource resource = null;
        try {
            resource = new InputStreamResource(Files.newInputStream(path));
        } catch (IOException e) {
            log.error("downloadDo: 오류 발생 - {}", e.getMessage());
        }
        if (resource == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    /*@GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("userid") String userId,
            @RequestParam("filename") String fileName) {
        log.debug("userid = {}, filename = {}", userId, fileName);
        try {
            log.debug("userid = {}, filename = {}", userId, MimeUtility.decodeText(fileName));
        } catch (UnsupportedEncodingException ex) {
            log.error("error");
        }

        // 1. 내려받기할 파일의 기본 경로 설정
        String basePath = ctx.getRealPath(DOWNLOAD_FOLDER) + File.separator + userId;

        // 2. 파일을 File 객체로 가져오기
        File file = new File(basePath, fileName);

        // 3. 파일이 존재하는지 확인
        if (!file.exists()) {
            log.error("File {} does not exist", file.getAbsolutePath());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // 4. Http 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.builder("attachment").filename(fileName, StandardCharsets.UTF_8).build());

        // 5. 파일을 리소스로 변환
        Resource resource = new FileSystemResource(file);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }*/


    /*@GetMapping("/delete_mail.do")
    public String deleteMailDo(@RequestParam("msgid") Integer msgId, RedirectAttributes attrs) {
        log.debug("delete_mail.do: msgid = {}", msgId);
        
        String host = (String) session.getAttribute("host");
        String userid = (String) session.getAttribute("userid");
        String password = (String) session.getAttribute("password");

        Pop3Agent pop3 = new Pop3Agent(host, userid, password);
        
        
        boolean deleteSuccessful = pop3.deleteMessage(msgId, true);
        if (deleteSuccessful) {
            attrs.addFlashAttribute("msg", "메시지 삭제를 성공하였습니다.");
        } else {
            attrs.addFlashAttribute("msg", "메시지 삭제를 실패하였습니다.");
        }
        
        return "redirect:main_menu";
    }*/
    @GetMapping("/delete_mail.do")
    public String deleteMailDo(@RequestParam("msgid") Integer msgId, RedirectAttributes attrs, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("delete_mail.do: msgid = {}", msgId);

        // HTML과 JavaScript로 팝업창을 띄우기 위한 코드
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<script type=\"text/javascript\">");
        out.println("if (confirm('정말 메일을 삭제하시겠습니까?')) {");
        out.println("  window.location.href = 'confirm_delete_mail.do?msgid=" + msgId + "';");
        out.println("} else {");
        out.println("  window.location.href = 'main_menu';");
        out.println("}");
        out.println("</script>");
        out.println("</body></html>");
        out.close();

        return null; // 추가적인 처리를 막기 위해 null 반환
    }

    @GetMapping("/confirm_delete_mail.do")
    public String confirmDeleteMail(@RequestParam("msgid") Integer msgId, RedirectAttributes attrs, HttpSession session) {
        log.debug("confirm_delete_mail.do: msgid = {}", msgId);

        String host = (String) session.getAttribute("host");
        String userid = (String) session.getAttribute("userid");
        String password = (String) session.getAttribute("password");

        Pop3Agent pop3 = new Pop3Agent(host, userid, password);

        boolean deleteSuccessful = pop3.deleteMessage(msgId, true);
        if (deleteSuccessful) {
            attrs.addFlashAttribute("msg", "메시지 삭제를 성공하였습니다.");
        } else {
            attrs.addFlashAttribute("msg", "메시지 삭제를 실패하였습니다.");
        }

        return "redirect:main_menu";
    }

}
