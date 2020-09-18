package ru.job4j.todo.servlets;

import ru.job4j.todo.model.User;
import ru.job4j.todo.store.HbnStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User user = User.of(req.getParameter("name"), req.getParameter("password"), req.getParameter("email"));
        HbnStore.instOf().addUser(user);
        HttpSession sc = req.getSession();
        sc.setAttribute("user", user);
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
