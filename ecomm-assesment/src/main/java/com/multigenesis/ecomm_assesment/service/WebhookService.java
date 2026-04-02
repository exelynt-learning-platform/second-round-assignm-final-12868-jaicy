package com.multigenesis.ecomm_assesment.service;

import com.stripe.model.Event;

public interface WebhookService {
	 void handleEvent(Event event);
}
