/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.control;

/**
 *
 * @author 이가연
 */
import deu.cse.spring_webmail.model.UserAdminAgent;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 사용자 ID 중복을 확인하는 컨트롤러 클래스입니다.
 */
@Slf4j
@PropertySource("classpath:/system.properties")
@Controller
public class CheckIDHandler {
/*
    InputStream is = null;
    OutputStream os = null;
    boolean isConnected = false;

    private final String EOL = "\r\n";

    @Value("${james.control.port}")
    private Integer JAMES_CONTROL_PORT;

    @PostMapping("/check_id.do")
    public String checkid(@RequestParam String id, RedirectAttributes attrs, HttpSession session) {
        log.debug("check_id.do: id = {}, port = {}", id, JAMES_CONTROL_PORT);

        try {
            // ID 중복 체크
            // CheckIDHandler 객체 생성
            CheckIDHandler checkIDHandler = new CheckIDHandler(); // 이 부분을 적절히 수정하여 CheckIDHandler 객체를 생성해야 합니다.

            if (checkIDHandler.isIdExists(id)) {
                attrs.addFlashAttribute("msg", "이미 사용 중인 ID입니다. 다른 ID를 선택해주세요.");
                return "redirect:/register";
            }
        } catch (Exception ex) {
            log.error("check_id.do: 시스템 접속에 실패했습니다. 예외 = {}", ex.getMessage());
        }

        return "redirect:/main_menu";
    }

    public boolean isIdExists(String userId) {
        boolean status = false;
        byte[] messageBuffer = new byte[1024];

        log.debug("isUserIdExists() called");
        if (!isConnected) {
            return status;
        }

        try {

            // 1: "verifyuser" command
            String verifyUserCommand = "verifyuser " + userId + EOL;
            os.write(verifyUserCommand.getBytes());

            // 2: response for "verifyuser" command
            java.util.Arrays.fill(messageBuffer, (byte) 0);
            is.read(messageBuffer);
            String recvMessage = new String(messageBuffer);
            log.debug(recvMessage);

            // 3: 확인된 사용자가 존재하는지 확인
            if (recvMessage.contains("exists")) {
                status = true; // ID가 이미 존재함
            } else {
                status = false; // ID가 존재하지 않음
            }
        } catch (Exception ex) {
            log.error("isUserIdExists 예외: {}", ex.getMessage());
            status = false;
        }

        return status;
    }
*/
}