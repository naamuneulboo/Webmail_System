package deu.cse.spring_webmail.control;

import deu.cse.spring_webmail.model.Pop3Agent;
import deu.cse.spring_webmail.model.UserAdminAgent;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author 이가연
 */
@Controller
@PropertySource("classpath:/system.properties")
@Slf4j
public class SignUpHandler {

    @Autowired
    private ServletContext ctx;
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;

    @Value("${root.id}")
    private String ROOT_ID;
    @Value("${root.password}")
    private String ROOT_PASSWORD;
    @Value("${admin.id}")
    private String ADMINISTRATOR;  //  = "admin";
    @Value("${james.control.port}")
    private Integer JAMES_CONTROL_PORT;
    @Value("${james.host}")
    private String JAMES_HOST;

    /*
    @PostMapping("/user_signup.do")
    public String userSignup(@RequestParam String id, @RequestParam String password,
            RedirectAttributes attrs, HttpSession session) {
        log.debug("add_user.do: id = {}, password = {}, port = {}",
                id, password, JAMES_CONTROL_PORT);

        try {
            String cwd = ctx.getRealPath(".");
            UserAdminAgent agent = new UserAdminAgent(JAMES_HOST, JAMES_CONTROL_PORT, cwd,
                    ROOT_ID, ROOT_PASSWORD, ADMINISTRATOR);

            // if (addUser successful)
            if (agent.addUser(id, password)) {
                attrs.addFlashAttribute("msg", String.format("%s 님 회원가입에 성공하였습니다.", id));
                // 로그인 수행
                loginAfterSignup(id, password, attrs, session);
            } else {
                attrs.addFlashAttribute("msg", String.format("%s 님 회원가입에 실패하였습니다.", id));
            }
        } catch (Exception ex) {
            log.error("user_signup.do: 시스템 접속에 실패했습니다. 예외 = {}", ex.getMessage());
        }

        return "redirect:/main_menu";
    }
     */

    @PostMapping("/user_signup.do")
    public String userSignup(@RequestParam String id, @RequestParam String password,
            RedirectAttributes attrs, HttpSession session) {
        log.debug("add_user.do: id = {}, password = {}, port = {}",
                id, password, JAMES_CONTROL_PORT);

        try {
            String cwd = ctx.getRealPath(".");
            UserAdminAgent agent = new UserAdminAgent(JAMES_HOST, JAMES_CONTROL_PORT, cwd,
                    ROOT_ID, ROOT_PASSWORD, ADMINISTRATOR);

            // if (addUser successful)
            if (agent.addUser(id, password)) {
                attrs.addFlashAttribute("msg", String.format("%s 님 회원가입에 성공하였습니다.", id));

                // 회원가입 후 자동으로 로그인 처리
                String host = (String) session.getAttribute("host");
                Pop3Agent pop3Agent = new Pop3Agent(host, id, password);
                boolean isLoginSuccess = pop3Agent.validate();

                if (isLoginSuccess) {
                    // 로그인 성공 시 세션에 사용자 정보 설정
                    session.setAttribute("userid", id);
                    session.setAttribute("password", password);

                    // 관리자 여부에 따라 메시지 설정
                    if (isAdmin(id)) {
                        attrs.addFlashAttribute("msg", "관리자로 로그인하였습니다.");
                    } else {
                        attrs.addFlashAttribute("msg", "로그인에 성공하였습니다.");
                    }
                } else {
                    attrs.addFlashAttribute("msg", "로그인에 실패하였습니다.");
                }
            } else {
                attrs.addFlashAttribute("msg", String.format("%s 님 회원가입에 실패하였습니다.", id));
            }
        } catch (Exception ex) {
            log.error("user_signup.do: 시스템 접속에 실패했습니다. 예외 = {}", ex.getMessage());
        }

        return "redirect:/main_menu";
    }

    private void loginAfterSignup(String id, String password, RedirectAttributes attrs, HttpSession session) {
        String host = (String) session.getAttribute("host");

        // Check the login information is valid using Pop3Agent.
        Pop3Agent pop3Agent = new Pop3Agent(host, id, password);
        boolean isLoginSuccess = pop3Agent.validate();

        // Now call the correct page according to its validation result.
        if (isLoginSuccess) {
            if (isAdmin(id)) {
                // HttpSession 객체에 userid를 등록해 둔다.
                session.setAttribute("userid", id);
                attrs.addFlashAttribute("msg", "회원가입 및 로그인에 성공하였습니다.");
            } else {
                // HttpSession 객체에 userid와 password를 등록해 둔다.
                session.setAttribute("userid", id);
                session.setAttribute("password", password);
                attrs.addFlashAttribute("msg", "회원가입 및 로그인에 성공하였습니다.");
            }
        } else {
            attrs.addFlashAttribute("msg", "로그인에 실패하였습니다.");
        }
    }

    protected boolean isAdmin(String userid) {
        boolean status = false;

        if (userid.equals(this.ADMINISTRATOR)) {
            status = true;
        }

        return status;
    }
}