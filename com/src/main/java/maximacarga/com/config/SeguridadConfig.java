package maximacarga.com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SeguridadConfig {
	@Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt es el más usado y el mismo que usa tu compañero
        return new BCryptPasswordEncoder();
    }

}
