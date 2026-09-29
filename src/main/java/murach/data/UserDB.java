package murach.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import murach.business.User;

public class UserDB {

    // Insert user
    public static void insert(User user) {

        EntityManager em =
                DBUtil.getEmFactory().createEntityManager();

        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            em.persist(user);

            trans.commit();

        } catch (Exception e) {

            if (trans.isActive()) {
                trans.rollback();
            }

            System.out.println("Error inserting user: " + e.getMessage());

        } finally {
            em.close();
        }
    }

    // Update user
    public static void update(User user) {

        EntityManager em =
                DBUtil.getEmFactory().createEntityManager();

        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            em.merge(user);

            trans.commit();

        } catch (Exception e) {

            if (trans.isActive()) {
                trans.rollback();
            }

            System.out.println("Error updating user: " + e.getMessage());

        } finally {
            em.close();
        }
    }

    // Delete user
    public static void delete(User user) {

        EntityManager em =
                DBUtil.getEmFactory().createEntityManager();

        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            User managedUser = em.merge(user);
            em.remove(managedUser);

            trans.commit();

        } catch (Exception e) {

            if (trans.isActive()) {
                trans.rollback();
            }

            System.out.println("Error deleting user: " + e.getMessage());

        } finally {
            em.close();
        }
    }

    // Find user by ID
    public static User getUserById(long userId) {

        EntityManager em =
                DBUtil.getEmFactory().createEntityManager();

        try {
            return em.find(User.class, userId);

        } finally {
            em.close();
        }
    }

    // Find user by email
    public static User selectUser(String email) {

        EntityManager em =
                DBUtil.getEmFactory().createEntityManager();

        String jpql =
                "SELECT u FROM User u WHERE u.email = :email";

        TypedQuery<User> query =
                em.createQuery(jpql, User.class);

        query.setParameter("email", email);

        try {

            return query.getSingleResult();

        } catch (NoResultException e) {

            return null;

        } finally {

            em.close();
        }
    }

    // Check whether email already exists
    public static boolean emailExists(String email) {

        User user = selectUser(email);

        return user != null;
    }
}