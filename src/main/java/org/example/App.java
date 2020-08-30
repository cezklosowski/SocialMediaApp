package org.example;


import javax.persistence.*;
import java.util.Scanner;

public class App
{
    public static void main( String[] args )
    {
        // otwarcie połączenia z bazą
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpa.hibernate");
        EntityManager entityManager = entityManagerFactory.createEntityManager();


        // wstępne dodanie użytkowników do bazy
        generateUsers(entityManager);


        // logowanie użytkownika
        logUser(entityManager);


        // zamknięcie połączenia z bazą
        entityManager.close();
        entityManagerFactory.close();
    }

    private static void generateUsers(EntityManager entityManager){
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

    }

    private static void logUser(EntityManager entityManager){
        System.out.println("Welcome! Please log in.");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter LOGIN:");
        String login = scanner.nextLine();
        System.out.println("Enter PASSWORD:");
        String password = scanner.nextLine();

        // sprawdzenie danych logowania
        entityManager.getTransaction().begin();

        // pobranie usera z bazy o podanym loginie
        try {
            TypedQuery<User> typedQuery = entityManager.createQuery(
                    "SELECT e FROM User e WHERE e.login = :login AND e.password = :password ", User.class);
            typedQuery.setParameter("login", login);
            typedQuery.setParameter("password", password);
            User user = typedQuery.getSingleResult();

            System.out.println("Logowanie poprawne!");
        }
        // jeżeli nie znajdzie usera w bazie o podanych danych
        catch (NoResultException ex){
            System.out.println("Błędne dane logowania!!!");
        }

        entityManager.getTransaction().commit();
    }
}
