package com.multigenesis.ecomm_assesment.service;

import org.springframework.stereotype.Service;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.EventDataObjectDeserializer;

@Service
public class WebhookServiceImpl implements WebhookService {

    @Override
    public void handleEvent(Event event) {

        switch (event.getType()) {

            case "payment_intent.succeeded":
                handlePaymentSuccess(event);
                break;

            case "payment_intent.payment_failed":
                handlePaymentFailure(event);
                break;

            default:
                System.out.println("Unhandled event type: " + event.getType());
        }
    }

    private void handlePaymentSuccess(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

        if (dataObjectDeserializer.getObject().isPresent()) {
            PaymentIntent paymentIntent = (PaymentIntent) dataObjectDeserializer.getObject().get();

            System.out.println("Payment succeeded: " + paymentIntent.getId());
        }
    }

    private void handlePaymentFailure(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

        if (dataObjectDeserializer.getObject().isPresent()) {
            PaymentIntent paymentIntent = (PaymentIntent) dataObjectDeserializer.getObject().get();

            System.out.println("Payment failed: " + paymentIntent.getId());
        }
    }
}
