package ru.job4j.todo.store;

import ru.job4j.todo.model.Item;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.function.Function;

public class HbnStore implements Store {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private HbnStore() {
    }

    private static final class Lazy {
        private static final Store INST = new HbnStore();
    }

    public static Store instOf() {
        return HbnStore.Lazy.INST;
    }

    private <T> T apply(final Function<Session, T> command) {
        final Session session = this.sf.openSession();
        session.beginTransaction();
        T rsl = command.apply(session);
        session.getTransaction().commit();
        session.close();
        return rsl;
    }

    @Override
    public Item add(Item item) {
        this.apply(session -> session.save(item));
        return item;
    }

    @Override
    public boolean replace(Integer id, Item item) {
        boolean rst = this.findById(id) != null;
        if (rst) {
            this.apply(session -> {
                session.update(item);
                return null;
            });
        }
        return rst;
    }

    @Override
    public boolean delete(Integer id) {
        boolean rst = false;
        Item item = this.findById(id);
        if (item != null) {
            this.apply(session -> {
                session.delete(item);
                return null;
            });
            rst = true;
        }
        return rst;
    }

    @Override
    public List<Item> findAll() {
        return this.apply(session -> session.createQuery("from ru.job4j.todo.model.Item order by id").list());
    }

    @Override
    public Item findById(Integer id) {
        return this.apply(session -> session.get(Item.class, id));
    }

    @Override
    public User addUser(User user) {
        this.apply(session -> session.save(user));
        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        return this.apply(session -> session.byNaturalId(User.class).using("email", email).load());
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(this.registry);
    }
}
