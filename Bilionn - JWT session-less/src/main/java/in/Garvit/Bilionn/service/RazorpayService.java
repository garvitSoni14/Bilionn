package in.Garvit.Bilionn.service;

import com.razorpay.RazorpayException;
import in.Garvit.Bilionn.io.RazorpayOrderResponse;

public interface RazorpayService {
    RazorpayOrderResponse createOrder(Double amount, String currency) throws RazorpayException;
}
