package deu.cse.spring_webmail.control;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import deu.cse.spring_webmail.model.UserAdminAgent;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.apache.tomcat.util.http.fileupload.IOUtils;

@WebServlet(name = "IdCheckHandler", urlPatterns = {"/IdCheck.do"})
public class IdCheckHandler extends HttpServlet {

    private String server;
    private int port;
    private String ROOT_ID;
    private String ROOT_PASSWORD;
    private String ADMIN_ID;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");

        PrintWriter out = response.getWriter();

        String server = "127.0.0.1";
        int port = 4555;
        String ROOT_ID = "root";
        String ROOT_PASSWORD = "20212964";
        String ADMIN_ID = "admin";

        try {
            //클라이언트에서 id값을 받아옴
            ServletInputStream is = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String id = stringBuilder.toString();

            //사용자가 입력한 id가 사용가능한지 확인
            UserAdminAgent agent = new UserAdminAgent(server, port, this.getServletContext().getRealPath("."),ROOT_ID,ROOT_PASSWORD,ADMIN_ID);
            if (agent.verify(id)) {
                //이미존재하는 id
                out.write("duplicated");
            } else {
                //사용가능한 id
                out.write("ok");
            }
        } catch (Exception ex) {
            out.write("error" + ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
