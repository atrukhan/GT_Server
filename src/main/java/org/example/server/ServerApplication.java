package org.example.server;

import org.example.server.models.*;
import org.example.server.models.enums.ERole;
import org.example.server.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
public class ServerApplication implements CommandLineRunner {

	@Autowired
	RoleRepository roleRepository;
	@Autowired
	UserRepository userRepository;

	@Autowired
	DefaultLibraryRepository defaultLibraryRepository;

	@Autowired
	UserLibraryRepository userLibraryRepository;

	@Autowired
	CardRepository cardRepository;

	@Autowired
	TrainingRepository trainingRepository;

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Role userRole = new Role(ERole.ROLE_USER);
		Role adminRole = new Role(ERole.ROLE_ADMIN);
		Set<Role> roles = new HashSet<>();
		roles.add(userRole);
		roles.add(adminRole);
		roleRepository.save(userRole);
		roleRepository.save(adminRole);

		User user = userRepository.save(new User("Sasha", "purple@gmail.com","$2a$10$E114PTNEZr5U4GbC9sNBzemXA1wVgMBgzV8KpCOY60cN8SKDC.Yzu", true, roles)); //root



		DefaultLibrary ld1 = defaultLibraryRepository.save(new DefaultLibrary("Lib-1", "00000"));
		DefaultLibrary ld2 = defaultLibraryRepository.save(new DefaultLibrary("Lib-2", "11111"));



		UserLibrary userLibrary = userLibraryRepository.save(new UserLibrary("User lib", "code", user));

		Training t1 = new Training(1, 1, true, null, new Date(), userLibrary, null);
		Training t2 = new Training(3, 2, false, null, new Date(), userLibrary, null);
		Training t3 = new Training(7, 3, false, null, new Date(), userLibrary, null);

		List<Training> trainings = trainingRepository.saveAll(List.of(t1,t2,t3));

		List<Card> l1 = new ArrayList<>();
		l1.add(new Card("slim", null, "стройный", "I opened the slim brown envelope and found that it contained my account.\n" +
				"Я вскрыл тонкий коричневый конверт - там лежал счет.", null, ld1, null));
		l1.add(new Card("polite", null, "вежливый", "\"Sounds like a polite way of saying 'mind your own business.' \"\n" +
				"— Вежливый способ сказать \"не ваше это дело\"?", null, ld1, null));
		l1.add(new Card("adult", null, "взрослый", "Empty", null, ld1, null));
		cardRepository.saveAll(l1);

		List<Card> l2 = new ArrayList<>();
		l2.add(new Card("costs", null, "затраты", "You are free to make it cost whatever you find necessary.\n" +
				"Вы вольны определять стоимость строительства.", null, ld2, null));
		l2.add(new Card("each of us",  null,"каждый из нас", "He excused himself with a small bow that included each of us in turn.\n" +
				"Тот встал и извинился с легким поклоном, обращенным к каждому из нас понемногу.", null, ld2, null));
		l2.add(new Card("get tired", null, "уставать", "You get tired fighting with her.\n" +
				"С ней драться устанешь.", null, ld2, null));
		cardRepository.saveAll(l2);

		List<Card> l3 = new ArrayList<>();
		l3.add(new Card("slim", null, "стройный", "I opened the slim brown envelope and found that it contained my account.\\n\" +\n" +
				"\t\t\t\t\"Я вскрыл тонкий коричневый конверт - там лежал счет.", userLibrary, null, trainings.get(0)));
		l3.add(new Card("polite", null, "вежливый", "\\\"Sounds like a polite way of saying 'mind your own business.' \\\"\\n\" +\n" +
				"\t\t\t\t\"— Вежливый способ сказать \\\"не ваше это дело\\\"?", userLibrary, null, trainings.get(0)));
		l3.add(new Card("adult", null, "взрослый", "Empty", userLibrary, null, trainings.get(0)));
		l3 = cardRepository.saveAll(l3);

//		userLibrary = userLibraryRepository.findById(userLibrary.getId()).get();


		System.out.println("finish");
	}
}
