/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.control;

import deu.cse.spring_webmail.model.SmtpAgent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 메일 쓰기를 위한 제어기
 *
 * @author Prof.Jong Min Lee
 */
@Controller
@PropertySource("classpath:/system.properties")
@Slf4j
public class WriteController {

    @Value("${file.upload_folder}")
    private String UPLOAD_FOLDER;
    @Value("${file.max_size}")
    private String MAX_SIZE;

    @Autowired
    private ServletContext ctx;
    @Autowired
    private HttpSession session;

    @GetMapping("/write_mail")
    public String writeMail() {
        log.debug("write_mail called...");
        session.removeAttribute("sender");  // 220612 LJM - 메일 쓰기 시는 
        return "write_mail/write_mail";
    }

    /* @PostMapping("/write_mail.do")
    public String writeMailDo(@RequestParam String to, @RequestParam String cc,
            @RequestParam String subj, @RequestParam String body,
            @RequestParam(name = "files") MultipartFile[] upFiles,
            RedirectAttributes attrs) {
        log.debug("write_mail.do: to = {}, cc = {}, subj = {}, body = {}, files count = {}",
                to, cc, subj, body, upFiles.length);

        // 파일 업로드 처리
        for (MultipartFile upFile : upFiles) {
            if (!upFile.isEmpty()) {
                String basePath = ctx.getRealPath(UPLOAD_FOLDER);
                String fileName = upFile.getOriginalFilename(); // 첨부 파일의 이름 가져오기
                log.debug("{} 파일을 {} 폴더에 저장...", fileName, basePath); //첨부파일명 로그
                log.debug("{} 파일을 {} 폴더에 저장...", upFile.getOriginalFilename(), basePath);
                File f = new File(basePath + File.separator + upFile.getOriginalFilename());
                try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f))) {
                    bos.write(upFile.getBytes());
                } catch (IOException e) {
                    log.error("upload.do: 오류 발생 - {}", e.getMessage());
                }
            }
        }

        boolean sendSuccessful = sendMessage(to, cc, subj, body, upFiles);
        if (sendSuccessful) {
            attrs.addFlashAttribute("msg", "메일 전송이 성공했습니다.");
        } else {
            attrs.addFlashAttribute("msg", "메일 전송이 실패했습니다.");
        }

      
        return "redirect:/main_menu";
    }*/
    @PostMapping("/write_mail.do")
    public String writeMailDo(@RequestParam String to, @RequestParam String cc,
            @RequestParam String subj, @RequestParam String body,
            @RequestParam(name = "files") MultipartFile[] upFiles,
            RedirectAttributes attrs) {
        log.debug("write_mail.do: to = {}, cc = {}, subj = {}, body = {}, files count = {}",
                to, cc, subj, body, upFiles.length);

        // 파일 업로드 처리
        for (MultipartFile upFile : upFiles) {
            if (!upFile.isEmpty()) {
                String basePath = ctx.getRealPath(UPLOAD_FOLDER);
                log.debug("{} 파일을 {} 폴더에 저장...", upFile.getOriginalFilename(), basePath);
                File f = new File(basePath + File.separator + upFile.getOriginalFilename());
                try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f))) {
                    bos.write(upFile.getBytes());
                } catch (IOException e) {
                    log.error("upload.do: 오류 발생 - {}", e.getMessage());
                }
            }
        }

        // 메일 전송 처리
        boolean sendSuccessful = sendMessage(to, cc, subj, body, upFiles);
        if (sendSuccessful) {
            attrs.addFlashAttribute("msg", "메일 전송이 성공했습니다.");
        } else {
            attrs.addFlashAttribute("msg", "메일 전송이 실패했습니다.");
        }
/*
        // 첨부 파일 목록을 모델에 추가하여 뷰로 전달
        List<String> attachments = Arrays.stream(upFiles)
                .filter(file -> !file.isEmpty())
                .map(MultipartFile::getOriginalFilename)
                .collect(Collectors.toList());
        model.addAttribute("attachments", attachments);
*/
        return "redirect:/main_menu";
    }

    private boolean sendMessage(String to, String cc, String subject, String body, MultipartFile[] upFiles) {
        boolean status = false;

        String host = (String) session.getAttribute("host");
        String userid = (String) session.getAttribute("userid");

        SmtpAgent agent = new SmtpAgent(host, userid);
        agent.setTo(to);
        agent.setCc(cc);
        agent.setSubj(subject);
        agent.setBody(body);

        for (MultipartFile upFile : upFiles) {
            String fileName = upFile.getOriginalFilename();
            if (fileName != null && !"".equals(fileName)) {
                log.debug("sendMessage: 파일({}) 첨부 필요", fileName);
                File f = new File(ctx.getRealPath(UPLOAD_FOLDER) + File.separator + fileName);
                agent.addAttachment(f.getAbsolutePath());
            }
        }

        if (agent.sendMessage()) {
            status = true;
        }
        return status;
    }

}
