package org.example.server;

import org.example.server.models.*;
import org.example.server.models.enums.ERole;
import org.example.server.repositories.*;
import org.example.server.services.UserLibService;
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

	@Autowired
	UserLibService userLibService;

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



		DefaultLibrary ld1 = defaultLibraryRepository.save(new DefaultLibrary("Еда", ""));
		ld1.setCode(userLibService.generateCode(0, ld1.getId()));
		defaultLibraryRepository.save(ld1);
		DefaultLibrary ld2 = defaultLibraryRepository.save(new DefaultLibrary("Одежда", ""));
		ld2.setCode(userLibService.generateCode(0, ld2.getId()));
		defaultLibraryRepository.save(ld2);
		DefaultLibrary ld3 = defaultLibraryRepository.save(new DefaultLibrary("Глаголы действия", ""));
		ld3.setCode(userLibService.generateCode(0, ld3.getId()));
		defaultLibraryRepository.save(ld3);


		UserLibrary userLibrary = userLibraryRepository.save(new UserLibrary("Мой набор - 1", "", user));
		userLibrary.setCode(userLibService.generateCode(1, userLibrary.getId()));
		userLibraryRepository.save(userLibrary);

		Training t1 = new Training(1, 1, true, null, new Date(), userLibrary, null);
		Training t2 = new Training(3, 2, false, null, new Date(), userLibrary, null);
		Training t3 = new Training(7, 3, false, null, new Date(), userLibrary, null);

		List<Training> trainings = trainingRepository.saveAll(List.of(t1,t2,t3));

		List<Card> l1 = new ArrayList<>();
		l1.add(new Card("bacon", null, "бекон", "", null, ld1, null));
		l1.add(new Card("beef", null, "говядина", "", null, ld1, null));
		l1.add(new Card("chicken", null, "курица", "", null, ld1, null));
		l1.add(new Card("ham", null, "ветчина", "", null, ld1, null));
		l1.add(new Card("liver", null, "печень", "", null, ld1, null));
		l1.add(new Card("salami", null, "салями", "", null, ld1, null));
		l1.add(new Card("pork", null, "свинина", "", null, ld1, null));
		l1.add(new Card("turkey", null, "индейка", "", null, ld1, null));
		l1.add(new Card("apple", null, "яблоко", "", null, ld1, null));
		l1.add(new Card("banana", null, "банан", "", null, ld1, null));
		l1.add(new Card("apricot", null, "абрикос", "", null, ld1, null));
		l1.add(new Card("blackberry", null, "ежевика", "", null, ld1, null));
		l1.add(new Card("blackcurrant", null, "черная смородина", "", null, ld1, null));
		l1.add(new Card("blueberry", null, "черника", "", null, ld1, null));
		cardRepository.saveAll(l1);

		List<Card> l2 = new ArrayList<>();
		l2.add(new Card("bow", null, "бант", "", null, ld2, null));
		l2.add(new Card("hat",  null,"шапка", "", null, ld2, null));
		l2.add(new Card("cape", null, "плащ", "", null, ld2, null));
		l2.add(new Card("belt", null, "пояс", "", null, ld2, null));
		l2.add(new Card("shoe", null, "обувь", "", null, ld2, null));
		l2.add(new Card("sock", null, "носок", "", null, ld2, null));
		l2.add(new Card("tie", null, "галстук", "", null, ld2, null));
		l2.add(new Card("vest", null, "жилет", "", null, ld2, null));
		l2.add(new Card("robe", null, "халат", "", null, ld2, null));
		l2.add(new Card("ring", null, "кольцо", "", null, ld2, null));
		l2.add(new Card("scarf", null, "шарф", "", null, ld2, null));
		l2.add(new Card("pants", null, "брюки", "", null, ld2, null));
		l2.add(new Card("suit", null, "костюм", "", null, ld2, null));
		l2.add(new Card("shirt", null, "рубашка", "", null, ld2, null));
		l2.add(new Card("jeans", null, "джинсы", "", null, ld2, null));
		cardRepository.saveAll(l2);

		List<Card> l3 = new ArrayList<>();
		l3.add(new Card("eat", null, "есть", "", null, ld3, null));
		l3.add(new Card("sew", null, "шить", "", null, ld3, null));
		l3.add(new Card("sing", null, "петь", "", null, ld3, null));
		l3.add(new Card("cut", null, "резать", "", null, ld3, null));
		l3.add(new Card("buy", null, "купить", "", null, ld3, null));
		l3.add(new Card("fly", null, "летать", "", null, ld3, null));
		l3.add(new Card("give", null, "давать", "", null, ld3, null));
		l3.add(new Card("wait", null, "ждать", "", null, ld3, null));
		l3.add(new Card("knit", null, "вязать", "", null, ld3, null));
		l3.add(new Card("read", null, "читать", "", null, ld3, null));
		cardRepository.saveAll(l3);

		List<Card> lu3 = new ArrayList<>();
		lu3.add(new Card("slim", null, "стройный", "", userLibrary, null, trainings.get(0)));
		lu3.add(new Card("polite", null, "вежливый", "", userLibrary, null, trainings.get(0)));
		lu3.add(new Card("adult", null, "взрослый", "", userLibrary, null, trainings.get(0)));
		lu3.add(new Card("think over", null, "обдумать", "", userLibrary, null, trainings.get(0)));
		lu3.add(new Card("grateful", null, "благодарный", "", userLibrary, null, trainings.get(0)));
		lu3.add(new Card("follow", null, "следовать", "", userLibrary, null, trainings.get(0)));
		lu3.add(new Card("invent", null, "изобретать", "", userLibrary, null, trainings.get(0)));
		lu3.add(new Card("wheel", null, "колесо", "", userLibrary, null, trainings.get(0)));
		lu3.add(new Card("forget", null, "забыть", "", userLibrary, null, trainings.get(0)));
		lu3.add(new Card("forgive", null, "прощать", "", userLibrary, null, trainings.get(0)));
		lu3.add(new Card("hurry up", null, "торопиться", "", userLibrary, null, trainings.get(0)));
		lu3 = cardRepository.saveAll(lu3);

//		userLibrary = userLibraryRepository.findById(userLibrary.getId()).get();


		System.out.println("finish");
	}
}
