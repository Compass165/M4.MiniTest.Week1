package repository;

import model.Payment;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class PaymentRepository implements IPaymentRepository{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Payment> findAll() {
        TypedQuery<Payment>query = em.createQuery("select c from Payment c", Payment.class);
        return query.getResultList();
    }

    @Override
    public Payment findById(Long id) {
        TypedQuery<Payment> query = em.createQuery("select c from Payment c where c.id=:id", Payment.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void save(Payment payment) {
        if (payment.getId() != null) {
            em.merge(payment);
        } else {
            em.persist(payment);
        }
    }

    @Override
    public void remove(Long id) {
        Payment payment = findById(id);
        if (payment != null) {
            em.remove(payment);
        }
    }
}
