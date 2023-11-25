package org.example.server;

import org.example.server.models.ERole;
import org.example.server.models.Role;
import org.example.server.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication implements CommandLineRunner {

	@Autowired
	RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Role userRole = new Role(ERole.ROLE_USER);
		Role adminRole = new Role(ERole.ROLE_ADMIN);
		roleRepository.save(userRole);
		roleRepository.save(adminRole);
	}
}
