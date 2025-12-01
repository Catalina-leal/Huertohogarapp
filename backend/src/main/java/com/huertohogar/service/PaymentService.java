package com.huertohogar.service;

import com.huertohogar.model.dto.PaymentRequest;
import com.huertohogar.model.dto.PaymentResponse;
import com.huertohogar.model.entity.Order;
import com.huertohogar.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    
    private final OrderRepository orderRepository;
    
    public PaymentResponse processPayment(PaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + request.getOrderId()));
        
        if (!order.getTotalAmount().equals(request.getAmount())) {
            throw new RuntimeException("El monto no coincide con el pedido");
        }
        
        // Simulación de procesamiento de pago
        // En producción, aquí se integraría con pasarela de pagos real
        
        String paymentId = UUID.randomUUID().toString();
        String transactionId = "TXN-" + System.currentTimeMillis();
        
        // Simular diferentes resultados según método de pago
        String status;
        String message;
        
        switch (request.getPaymentMethod().toLowerCase()) {
            case "cash":
            case "transfer":
                status = "success";
                message = "Pago procesado exitosamente";
                order.setStatus(Order.OrderStatus.CONFIRMED);
                break;
            case "credit_card":
            case "debit_card":
                // Validación básica de tarjeta
                if (request.getCardNumber() != null && 
                    request.getCardNumber().length() >= 13 &&
                    request.getCvv() != null && 
                    request.getCvv().length() == 3) {
                    status = "success";
                    message = "Pago con tarjeta procesado exitosamente";
                    order.setStatus(Order.OrderStatus.CONFIRMED);
                } else {
                    status = "failed";
                    message = "Datos de tarjeta inválidos";
                }
                break;
            default:
                status = "failed";
                message = "Método de pago no válido";
        }
        
        if ("success".equals(status)) {
            orderRepository.save(order);
        }
        
        return new PaymentResponse(paymentId, status, transactionId, message);
    }
}
