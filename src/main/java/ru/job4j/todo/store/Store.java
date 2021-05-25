package ru.job4j.todo.store;

import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.util.List;

public interface Store extends AutoCloseable {

    Item add(Item item, String[] ids);

    boolean replace(Integer id, Item item);

    boolean delete(Integer id);

    List<Item> findAll();

    Item findById(Integer id);

    User addUser(User user);

    User findUserByEmail (String email);

    List<Category> allCategories();
}
