package Controllers;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/testForm")
public class TestDBServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<h1>Form Test Results</h1>");
        
        String[] tags = request.getParameterValues("tags");
        out.println("<p>Tags parameter values:");
        if (tags != null) {
            out.println("<ul>");
            for (String tag : tags) {
                out.println("<li>" + tag + "</li>");
            }
            out.println("</ul>");
        } else {
            out.println("null</p>");
        }
        
        out.println("<p>New tags: " + request.getParameter("newTags") + "</p>");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<h1>Test Form</h1>");
        out.println("<form method='post'>");
        out.println("<input type='checkbox' name='tags' value='1'> Tag 1<br>");
        out.println("<input type='checkbox' name='tags' value='2'> Tag 2<br>");
        out.println("<input type='checkbox' name='tags' value='3'> Tag 3<br>");
        out.println("New tags: <input type='text' name='newTags'><br>");
        out.println("<input type='submit' value='Test'>");
        out.println("</form>");
    }
}