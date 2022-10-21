package service;

import model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import repository.IPaymentRepository;

import java.util.List;

public class PaymentService implements IPaymentService{
    @Autowired
    private IPaymentRepository paymentRepository;

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment findById(Long id) {
        return paymentRepository.findById(id);
    }

    @Override
    public void save(Payment payment) {
        paymentRepository.save(payment);
    }

    @Override
    public void remove(Long id) {
        paymentRepository.remove(id);
    }
}
