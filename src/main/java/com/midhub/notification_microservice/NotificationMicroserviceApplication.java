package com.midhub.notification_microservice;

import jakarta.mail.Session;
import jakarta.mail.Transport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class NotificationMicroserviceApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(NotificationMicroserviceApplication.class, args);
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		String username = "testeando432@gmail.com";
		String password = "cdsj lysn iutf rmvd";

		try {
			Session session = Session.getInstance(props);
			Transport transport = session.getTransport("smtp");
			transport.connect(username, password);
			System.out.println("✅ Conexión SMTP exitosa.");
			transport.close();
		} catch (Exception e) {
			System.err.println("❌ Error de conexión SMTP: " + e.getMessage());
		}
	}

}
