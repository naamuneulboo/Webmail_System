package deu.cse.spring_webmail.control;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@PropertySource("classpath:/config.properties")
public class AddrsssController {

    @Value("${mysql.server.ip}")
    private String mysqlServerIP;
    @Value("${mysql.server.port}")
    private String mysqlServerPort;

    @GetMapping("/showtable")
    public String showTable(Model model){
        model.addAttribute("mysql_server_ip",this.mysqlServerIP);
        model.addAttribute("mysql_server_port",this.mysqlServerPort);
        log.info("mysql.server.ip = {}, mysql.server.port = {}",this.mysqlServerIP,this.mysqlServerPort);
        return "address/alladdress";
    }

    @GetMapping("/createadd")
    public String createAddress(Model model){
        model.addAttribute("mysql_server_ip",this.mysqlServerIP);
        model.addAttribute("mysql_server_port",this.mysqlServerPort);
        log.info("mysql.server.ip = {}, mysql.server.port = {}",this.mysqlServerIP,this.mysqlServerPort);
        return "address/createadd";
    }

    @PostMapping("/createadd")
    public String createAdd(Model model){
        model.addAttribute("mysql_server_ip",this.mysqlServerIP);
        model.addAttribute("mysql_server_port",this.mysqlServerPort);
        log.info("mysql.server.ip = {}, mysql.server.port = {}",this.mysqlServerIP,this.mysqlServerPort);
        return "address/createaddress";
    }
}
