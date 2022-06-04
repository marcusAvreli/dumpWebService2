package derbyClientWeb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(Application.class);


	protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        
        logger.info("APPLICATION_STARTED");
        response.setContentType("text/html;charset=UTF-8");
        
        PrintWriter out = null;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        
        try {
        
            out = response.getWriter();
        
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/testdb");
        
            con = ds.getConnection();
            st = con.createStatement();

            out.println("<html>");
            out.println("<head>");
            out.println("<title>SimpleServlet</title>");
            out.println("</head>");
            out.println("<body>");            
            
            rs = st.executeQuery("SELECT * FROM CARS");
        
            while (rs.next()) {
                out.print(rs.getInt(1));
                out.print(" ");
                out.print(rs.getString(2));
                out.print(" ");
                out.print(rs.getString(3));
                out.print("<br>");
            }

            out.println("</body>");
            out.println("</html>");

        } catch (NamingException | SQLException ex) {
        
        
            logger.error("error_occured",ex);
           

        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }

                if (con != null) {
                    con.close();
                }

                if (out != null) {
                    out.close();
                }
                
            } catch (SQLException ex) {
        
                ex.printStackTrace();
            }
        }
        logger.info("APPLICATION_FINISHED");
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


}