package controller;

import model.Payment;
import model.PaymentForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import service.IPaymentService;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class PaymentController {
    @Autowired
    private IPaymentService paymentService;
    @Value("${file-upload}")
    private String fileUpload;

    @GetMapping("/payments")
    public ModelAndView listPayment() {
        List<Payment> payments = paymentService.findAll();
        ModelAndView modelAndView = new ModelAndView("/payment/list");
        modelAndView.addObject("payments", payments);
        return modelAndView;
    }

    @GetMapping("/create-payment")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/payment/create");
        modelAndView.addObject("paymentForm", new PaymentForm());
        return modelAndView;
    }

    @PostMapping("/create-payment")
    public ModelAndView savePayment(@ModelAttribute("payment") PaymentForm paymentForm) {
        MultipartFile multipartFile = paymentForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(paymentForm.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Payment payment = new Payment(paymentForm.getId(), paymentForm.getName(),
                paymentForm.getPrice(), paymentForm.getDescription(), fileName);
        paymentService.save(payment);
        ModelAndView modelAndView = new ModelAndView("/payment/create");
        modelAndView.addObject("paymentForm", new PaymentForm());
        modelAndView.addObject("message", "New payment created successfully");
        return modelAndView;
    }

    @GetMapping("/edit-payment/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        Payment payment = paymentService.findById(id);
        if (payment != null) {
            ModelAndView modelAndView = new ModelAndView("/payment/edit");
            modelAndView.addObject("payment", payment);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/edit-payment")
    public ModelAndView updatePayment(@ModelAttribute("payment") Payment payment) {
        paymentService.save(payment);
        ModelAndView modelAndView = new ModelAndView("/payment/edit");
        modelAndView.addObject("payment", payment);
        modelAndView.addObject("message", "Payment updated successfully");
        return modelAndView;
    }

    @GetMapping("/delete-payment/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        Payment payment = paymentService.findById(id);
        if (payment != null) {
            ModelAndView modelAndView = new ModelAndView("/payment/delete");
            modelAndView.addObject("payment", payment);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/delete-payment")
    public String deletePayment(@ModelAttribute("payment") Payment payment) {
        paymentService.remove(payment.getId());
        return "redirect:payments";
    }

}
