package com.project.notificationservice;

import brave.Tracer;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceApplication {

	private final ObservationRegistry observationRegistry;
	private final Tracer tracer;

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

	@KafkaListener(topics = "notificationTopic")
	public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
		log.info("i am here");
		Observation.createNotStarted("on-message", this.observationRegistry).observe(() -> {
			log.info("Got message <{}>", orderPlacedEvent);
			log.info("TraceId- {}, Received Notification for Order - {}", this.tracer.currentSpan().context().traceId(),
					orderPlacedEvent.getOrderNumber());
		});
		// send out an email notification
	}
}
