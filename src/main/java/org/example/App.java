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
        try {
            int postNumber = scanner.nextInt();
            scanner.nextLine();

            TypedQuery<Post> typedQuery = entityManager.createQuery(
                    "SELECT p FROM Post p WHERE p.id = :id", Post.class);
            typedQuery.setParameter("id", postNumber);
            Post post = typedQuery.getSingleResult();

            System.out.println("Post nr " + postNumber + ":");

            System.out.println(post.getText());

            System.out.println();
            System.out.println("Choose option:");

            String option = "";
            do {
                System.out.println("1. Back to menu");
                System.out.println("2. Read comments");

                // jeżeli ten użytkownik napisał ten post
                if (user.getId() == post.getUserID()) {
                    System.out.println("3. Edit post.");
                    System.out.println("4. Delete post.");
                }

                option = scanner.nextLine();

                switch (option) {
                    case "1":
                        break;
                    case "2":
                        readComment(entityManager, post, user);
                        break;
                    case "3":
                        if (user.getId() == post.getUserID()) {
                            editPost(entityManager, post);
                        }
                        break;
                    case "4":
                        if (user.getId() == post.getUserID()) {
                            deletePost(entityManager, post);
                        }
                        break;
                }
            } while (!option.equals("1") && !option.equals("4"));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("There is no post with the given number");
        } catch (NoResultException e) {
            System.out.println("There is no post with the given number");
        }
    }

    private static void editPost(EntityManager entityManager, Post post) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("You're editing the post.");

        String option = "";
        do {
            System.out.println("Choose option:");
            System.out.println("1. Edit title");
            System.out.println("2. Edit text.");
            System.out.println("3. Cancel. Back to menu.");
            option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.println("Enter new title");
                    String newTitle = scanner.nextLine();

                    entityManager.getTransaction().begin();
                    post.setTitle(newTitle);
                    entityManager.getTransaction().commit();
                    break;
                case "2":
                    System.out.println("Enter new text");
                    String newText = scanner.nextLine();

                    entityManager.getTransaction().begin();
                    post.setText(newText);
                    entityManager.getTransaction().commit();
                    break;
                case "3":
                    break;
            }
        } while (!option.equals("3"));
    }

    private static void deletePost(EntityManager entityManager, Post post) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Do you want to delete the post? y/n");
        String ifDelete = scanner.nextLine();
        if (ifDelete.equals("y")) {
            entityManager.getTransaction().begin();
            entityManager.remove(post);
            entityManager.getTransaction().commit();
            System.out.println("Post has been deleted");
        } else {
            System.out.println("Post has not been deleted");
        }

    }

    private static void readComment(EntityManager entityManager, Post post, User user) {
        Scanner scanner = new Scanner(System.in);

        TypedQuery<Comment> typedQuery = entityManager.createQuery(
                "SELECT c FROM Comment c WHERE c.postID = :id", Comment.class);
        typedQuery.setParameter("id", post.getId());
        List<Comment> comments = typedQuery.getResultList();

        if (comments.size() == 0) {
            System.out.println("This post has no comments yet");
        } else {
            System.out.println("All comments:");
            comments.stream()
                    .forEach(comment -> System.out.println(comment));
            System.out.println();
        }

        System.out.println();

        String option = "";
        do {
            System.out.println("Choose option:");
            System.out.println("1. Add comment");
            System.out.println("2. ");
            System.out.println("3. Back to menu.");
            option = scanner.nextLine();

            switch (option) {
                case "1":
                    addComment(entityManager, user, post);
                    break;
                case "2":

                    break;
                case "3":
                    break;
            }
        } while (!option.equals("3"));


    }

    private static void addComment(EntityManager entityManager, User user, Post post) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("You're adding a new comment.");
        System.out.println("Enter text [Enter]:");
        String text = scanner.nextLine();


        Comment comment = new Comment();
        comment.setText(text);
        comment.setPostID(post.getId());
        comment.setUserID(user.getId());

        entityManager.getTransaction().begin();
        entityManager.persist(comment);
        entityManager.getTransaction().commit();

        System.out.println("Comment added!");
    }


}

