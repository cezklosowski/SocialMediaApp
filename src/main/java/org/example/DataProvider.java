package org.example;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class DataProvider {
    public static void generateData(EntityManager entityManager){

        // użytkownicy
        User user1 = new User();
        user1.setLogin("login1");
        user1.setPassword("password1");

        User user2 = new User();
        user2.setLogin("login2");
        user2.setPassword("password2");

        User user3 = new User();
        user3.setLogin("login3");
        user3.setPassword("password3");

        User user4 = new User();
        user4.setLogin("login4");
        user4.setPassword("password4");

        entityManager.getTransaction().begin();
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.persist(user4);
        entityManager.getTransaction().commit();

        // posty
        Post post1 = new Post();
        post1.setTitle("Tytuł postu 1");
        post1.setText("Treść postu 1, napisana przez użytkownika 1.");
        post1.setUserID(user1.getId());

        Post post2 = new Post();
        post2.setTitle("Tytuł postu 2");
        post2.setText("Treść postu 2, napisana przez użytkownika 1.");
        post2.setUserID(user1.getId());

        Post post3 = new Post();
        post3.setTitle("Tytuł postu 3");
        post3.setText("Treść postu 3, napisana przez użytkownika 2.");
        post3.setUserID(user2.getId());

        Post post4 = new Post();
        post4.setTitle("Tytuł postu 4");
        post4.setText("Treść postu 4, napisana przez użytkownika 3.");
        post4.setUserID(user3.getId());

        Post post5 = new Post();
        post5.setTitle("Tytuł postu 5");
        post5.setText("Treść postu 5, napisana przez użytkownika 3.");
        post5.setUserID(user3.getId());

        Post post6 = new Post();
        post6.setTitle("Tytuł postu 6");
        post6.setText("Treść postu 6, napisana przez użytkownika 4.");
        post6.setUserID(user4.getId());



        entityManager.getTransaction().begin();
        entityManager.persist(post1);
        entityManager.persist(post2);
        entityManager.persist(post3);
        entityManager.persist(post4);
        entityManager.persist(post5);
        entityManager.persist(post6);
        entityManager.getTransaction().commit();


        TypedQuery<Post> query = entityManager.createQuery(
                "SELECT p FROM Post p", Post.class);
        List<Post> posts = query.getResultList();

        // komentarze
        Comment comment1 = new Comment();
        comment1.setText("Komentarz 1 do Postu 1, napisany przez użytkownika 1");
        comment1.setPostID(posts.get(0).getId());
        comment1.setUserID(user1.getId());

        Comment comment2 = new Comment();
        comment2.setText("Komentarz 2 do Postu 1, napisany przez użytkownika 2");
        comment2.setPostID(posts.get(0).getId());
        comment2.setUserID(user2.getId());

        Comment comment3 = new Comment();
        comment3.setText("Komentarz 3 do Postu 2, napisany przez użytkownika 3");
        comment3.setPostID(posts.get(1).getId());
        comment3.setUserID(user3.getId());

        entityManager.getTransaction().begin();
        entityManager.persist(comment1);
        entityManager.persist(comment2);
        entityManager.persist(comment3);
        entityManager.getTransaction().commit();


    }


}
