package account.controllers;

import account.entity.employee.Payment;
import account.entity.employee.PaymentRequest;
import account.entity.employee.PaymentMessageResponse;
import account.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/acct")
@Validated
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payments")
    public PaymentMessageResponse addPaymentUser(@RequestBody List<@Valid PaymentRequest> employees) {
        return paymentService.addPaymentEmployee(employees);

    }

    @PutMapping("/payments/")
    public void updatePaymentUser() {
    }
    @GetMapping("/all")
    public List<Payment> geAllPayments(){
        return paymentService.findAll();
    }
}
