package styx.studio.dex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class DesktopCliApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesktopCliApplication.class, args);
	}

}
