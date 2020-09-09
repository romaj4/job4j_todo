package ru.job4j.todo.servlets;

import com.google.gson.Gson;
import ru.job4j.todo.store.HbnStore;
import ru.job4j.todo.store.Store;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ItemsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Store store = HbnStore.instOf();
        String json = new Gson().toJson(store.findAll());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
