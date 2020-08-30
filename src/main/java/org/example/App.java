package org.example;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        // otwarcie połączenia z bazą
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpa.hibernate");
        EntityManager entityManager = entityManagerFactory.createEntityManager();


        // wstępne dodanie użytkowników do bazy
        DataProvider.generateData(entityManager);


        // logowanie użytkownika
        User user = logUser(entityManager);

        // menu
        menu(entityManager, user);


        // zamknięcie połączenia z bazą
        entityManager.close();
        entityManagerFactory.close();
    }


    private static User logUser(EntityManager entityManager) {
        Scanner scanner = new Scanner(System.in);
        Boolean loginFlag = false;
        User user = new User();

        while (!loginFlag) {
            System.out.println("Welcome! Please log in.");


            System.out.println("Enter LOGIN:");
            String login = scanner.nextLine();
            System.out.println("Enter PASSWORD:");
            String password = scanner.nextLine();

            // sprawdzenie danych logowania
            entityManager.getTransaction().begin();

            // pobranie usera z bazy o podanym loginie
            try {
                TypedQuery<User> typedQuery = entityManager.createQuery(
                        "SELECT u FROM User u WHERE u.login = :login AND u.password = :password ", User.class);
                typedQuery.setParameter("login", login);
                typedQuery.setParameter("password", password);
                user = typedQuery.getSingleResult();

                loginFlag = true;
                System.out.println("Login is correct!");
            }
            // jeżeli nie znajdzie usera w bazie o podanych danych
            catch (NoResultException ex) {
                System.out.println("Incorrect LOGIN or PASSWORD!!!");
                System.out.println("Try again!");
                System.out.println("..................................");
                System.out.println();
            }

            entityManager.getTransaction().commit();
        }
        return user;
    }

    private static void menu(EntityManager entityManager, User user) {
        Scanner scanner = new Scanner(System.in);

        String option = "";

        do {
            System.out.println("MENU");
            System.out.println("1. Add new post");
            System.out.println("2. Read the post");
            System.out.println("3. Exit");
            option = scanner.nextLine();

            switch (option) {
                case "1":
                    addPost(entityManager, user);
                    break;
                case "2":
                    readPost(entityManager, user);
                    break;
            }

        } while (!option.equals("3"));


    }

    private static void addPost(EntityManager entityManager, User user) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("You're adding a new post.");
        System.out.println("Enter title:");
        String title = scanner.nextLine();
        System.out.println("Type here and confirm with [Enter]:");
        String text = scanner.nextLine();

        Post post = new Post();
        post.setTitle(title);
        post.setText(text);
        post.setUserID(user.getId());

        entityManager.getTransaction().begin();
        entityManager.persist(post);
        entityManager.getTransaction().commit();

        System.out.println("Post added!");

    }

    private static void readPost(EntityManager entityManager, User user) {
        Scanner scanner = new Scanner(System.in);

        TypedQuery<Post> query = entityManager.createQuery(
                "SELECT p FROM Post p", Post.class);
        List<Post> posts = query.getResultList();

        System.out.println("All posts:");
        posts.stream()
                .forEach(post -> System.out.println(post));
        System.out.println();

        System.out.println("Which post do you want to read?");
        int postNumber = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Post nr " + postNumber + ":");
        System.out.println(posts.get(postNumber - 1).getText());

        System.out.println();
        System.out.println("Choose option:");

        String option = "";
        do {
            System.out.println("1. Back to menu");

            // jeżeli ten użytkownik napisał ten post
            if (user.getId() == posts.get(postNumber - 1).getUserID()) {
                System.out.println("2. Edit post.");
                System.out.println("3. Delete post.");
            }

            option = scanner.nextLine();

            switch (option) {
                case "1":
                    break;
                case "2":
                    if (user.getId() == posts.get(postNumber - 1).getUserID()) {
                        editPost(entityManager, user);
                    }
                    break;
                case "3":
                    if (user.getId() == posts.get(postNumber - 1).getUserID()) {
                        deletePost(entityManager, user);
                    }
                    break;
            }

        } while (!option.equals("1"));
    }

    private static void editPost(EntityManager entityManager, User user) {

    }

    private static void deletePost(EntityManager entityManager, User user) {

    }


}

