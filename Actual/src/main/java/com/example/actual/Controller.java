package com.example.actual;

import db.DBManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


/**
 * Created by forest on 5/17/2018.
 */

public class Controller extends HttpServlet {
    HttpSession currentSession;
    int userId;

    public void writePuzzle(PrintWriter printWriter, String puzzleHtml) {
        User user = (User) currentSession.getAttribute("user");
        printWriter.println("<html>");
        printWriter.println("<head>");
        printWriter.println("<title>Puzzle</title>");
        printWriter.println("<link rel='stylesheet' type='text/css' href='style.css'>");
        printWriter.println("<script src=\"https://code.jquery.com/jquery-3.6.0.min.js\" integrity=\"sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=\" crossorigin=\"anonymous\"></script>");
        printWriter.println("<script src=\"https://code.jquery.com/ui/1.12.1/jquery-ui.min.js\" integrity=\"sha256-VazP97ZCwtekAsvgPBSUwPFKdrwD3unUfSGVYrahUqU=\" crossorigin=\"anonymous\"></script>");
        printWriter.println("<script src=\"ajax-utils.js\" defer></script> ");
        printWriter.println("</head>");
        printWriter.println("<body>");
        printWriter.println("<div id='score'>");
        printWriter.println("</div>");
        printWriter.println("<h2 style=\"text-align: center\"> Hello " + user.getUsername() + "!</h2>");
        printWriter.println("<form  class=\"logout\" action=\"LoginController\" method=\"get\">\n" +
                "    <input style=\"width: 60px; height: 30px;\" type=\"submit\" value=\"Logout\" id=\"logout\"/>\n" +
                "</form>");
        printWriter.println("<div id='puzzle'>");
        printWriter.println(puzzleHtml);
        printWriter.println("</div>");
        printWriter.println("<div id='status'>");
        printWriter.println("</div>");
        printWriter.println("</body>");
        printWriter.println("</html>");
    }
    public void doGet(HttpServletRequest req, HttpServletResponse servletResponse) throws IOException {

            currentSession = req.getSession();
            userId = (Integer) currentSession.getAttribute("userID");

            servletResponse.setContentType("text/html");
            DBManager db = new DBManager();
            writePuzzle(servletResponse.getWriter(), db.getPuzzle(userId));

    }

    public void doPost(HttpServletRequest req, HttpServletResponse servletResponse) throws IOException {
        currentSession = req.getSession();
        userId = (int) currentSession.getAttribute("userID");

        servletResponse.setContentType("text/html");
        DBManager db = new DBManager();
        db.resetGame(userId);
        writePuzzle(servletResponse.getWriter(), db.getPuzzle(userId));
    }

    public void doPut(HttpServletRequest req, HttpServletResponse servletResponse) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));

        String data = br.readLine();
        System.out.println(data);
        Map<String, String> params = new HashMap<>();
        Stream.of(data.split("&", 2)).forEach((par) -> {
            String[] arr = par.split("=", 2);
            params.put(arr[0], arr[1]);
        });
        int id1 = Integer.parseInt(params.get("id1"));
        int id2 = Integer.parseInt(params.get("id2"));

        DBManager db = new DBManager();
        db.swap(id1, id2, userId);
        servletResponse.getWriter().println(db.getPuzzle(userId));
    }
}
