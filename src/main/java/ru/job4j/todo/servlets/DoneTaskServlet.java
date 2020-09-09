package ru.job4j.todo.servlets;

import ru.job4j.todo.model.Item;
import ru.job4j.todo.store.HbnStore;
import ru.job4j.todo.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DoneTaskServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        int id = Integer.parseInt(req.getParameter("id"));
        Store store = HbnStore.instOf();
        Item item = store.findById(id);
        item.setDone(Boolean.parseBoolean(req.getParameter("done")));
        store.replace(id, item);
    }
}