package in.Garvit.Bilionn.service;

import in.Garvit.Bilionn.io.OrderRequest;
import in.Garvit.Bilionn.io.OrderResponse;
import in.Garvit.Bilionn.io.PaymentVerificationRequest;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);
    void deleteOrder(String orderId);
    List<OrderResponse> getLatestOrders();
    OrderResponse verifyPayment(PaymentVerificationRequest request);

    Double sumSalesByDate(LocalDate Date);

    Long countByOrderDate(LocalDate date);

    List<OrderResponse> findRecentOrders();
}
