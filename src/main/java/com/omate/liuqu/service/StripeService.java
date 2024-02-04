package com.omate.liuqu.service;

import com.omate.liuqu.dto.PaymentIntentDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.stripe.Stripe;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    // 其他涉及Stripe的方法
    public PaymentIntentDTO createPaymentIntent(Long amount, String currency) {
        Stripe.apiKey = stripeApiKey;
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency)
                // Add more configuration as needed
                .build();

        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // 创建并返回DTO
            return new PaymentIntentDTO(
                    paymentIntent.getId(),
                    paymentIntent.getAmount(),
                    paymentIntent.getCurrency(),
                    paymentIntent.getStatus(),
                    paymentIntent.getCreated()
            );
        } catch (StripeException e) {
            // Log the exception (consider using a logger instead of System.out.println)
            System.out.println("Stripe API error: " + e.getMessage());
            // Handle the exception by throwing it or returning a custom response
            throw new RuntimeException("Error creating payment intent", e);
        }
    }
}
