package in.Garvit.Bilionn.controller;

import com.razorpay.RazorpayException;
import in.Garvit.Bilionn.io.OrderResponse;
import in.Garvit.Bilionn.io.PaymentRequest;
import in.Garvit.Bilionn.io.PaymentVerificationRequest;
import in.Garvit.Bilionn.io.RazorpayOrderResponse;
import in.Garvit.Bilionn.service.OrderService;
import in.Garvit.Bilionn.service.RazorpayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final RazorpayService razorpayService;
    private final OrderService orderService;


    @PostMapping("/create-order")
    @ResponseStatus(HttpStatus.CREATED)
    public RazorpayOrderResponse createRazorpayOrder(@RequestBody PaymentRequest request) throws RazorpayException {
        return razorpayService.createOrder(request.getAmount(), request.getCurrency());
    }

    @PostMapping("/verify")
    public OrderResponse verifyPayment(@RequestBody PaymentVerificationRequest request){
    return orderService.verifyPayment(request);
    }


}
