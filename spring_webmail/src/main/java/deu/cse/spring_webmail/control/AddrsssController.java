package deu.cse.spring_webmail.control;


import deu.cse.spring_webmail.model.AddrBookManager;
import deu.cse.spring_webmail.model.AddrBookRow;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@PropertySource("classpath:/system.properties")
public class AddrsssController {

    @Value("${mysql.server.ip}")
    private String mysqlServerIP;
    @Value("${mysql.server.port}")
    private String mysqlServerPort;
    @Autowired
    private Environment env;

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
    
    
    
    @GetMapping("/delete_addrbook")
    public String deleteAddressBook(){
        return "address/delete_addrbook"; // 해당하는 뷰의 이름을 반환합니다.
    }
    
    @PostMapping("/delete.do")
     public String deleteAddressBook(@RequestParam String email, @RequestParam String name,
             @RequestParam String phone, Model model){
         String userName = env.getProperty("spring.datasource.username");
         String password = env.getProperty("spring.datasource.password");
         String jdbcDriver = env.getProperty("spring.datasource.driver-class-name");
         
         AddrBookManager manager = new AddrBookManager(mysqlServerIP,
            mysqlServerPort,userName,password,jdbcDriver);
         manager.delRow(email, name, phone);
         
         List<AddrBookRow> dataRows = manager.getAllRows();
         model.addAttribute("dataRows", dataRows);
         
         return "redirect:/showtable"; 
     }
    
    @GetMapping("/update_addrbook")
    public String updateAddressBook(){
        return "address/update_addrbook"; // 해당하는 뷰의 이름을 반환합니다.
    }
    
    @PostMapping("/update.do")
     public String updateAddressBook(@RequestParam String email, @RequestParam String name,
             @RequestParam String phone, Model model){
         String userName = env.getProperty("spring.datasource.username");
         String password = env.getProperty("spring.datasource.password");
         String jdbcDriver = env.getProperty("spring.datasource.driver-class-name");
         
         AddrBookManager manager = new AddrBookManager(mysqlServerIP,
            mysqlServerPort,userName,password,jdbcDriver);
         manager.updRow(email, name, phone);
         
         List<AddrBookRow> dataRows = manager.getAllRows();
         model.addAttribute("dataRows", dataRows);
         
         return "redirect:/showtable"; 
     }
    
}
