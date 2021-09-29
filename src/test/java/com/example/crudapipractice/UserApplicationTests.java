package com.example.crudapipractice;

import com.example.crudapipractice.Model.User;
import com.example.crudapipractice.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserApplicationTests {
	@Autowired
	MockMvc mvc;

	@Autowired
	UserRepository newUserRepository;

	@Transactional
	@Rollback
	@Test
	void testGetRequestReturnsAllUsers() throws Exception {
		User user1 = new User();
		User user2 = new User();
		User user3 = new User();
		user1.setEmail("test1@test.com");
		user2.setEmail("test2@test.com");
		user3.setEmail("test3@test.com");
		this.newUserRepository.save(user1);
		this.newUserRepository.save(user2);
		this.newUserRepository.save(user3);

		String jsonOutput = String.format("[{\"id\":%d,\"email\":" +
				"\"test1@test.com\"},{\"id\":%d,\"email\":\"test2@test.com\"}," +
				"{\"id\":%d,\"email\":\"test3@test.com\"}]", user1.getId(), user2.getId(), user3.getId());

		this.mvc.perform(get("/users"))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonOutput));
	}

	@Transactional
	@Rollback
	@Test
	void testPostAddsNewUserToDatabase() throws Exception {
		String jsonInput = "{\"email\":\"test3@test.com\",\"password\":\"s00persekrit\"}";
		User user1 = new User();
		User user2 = new User();
		user1.setEmail("test1@test.com");
		user2.setEmail("test2@test.com");
		this.newUserRepository.save(user1);
		this.newUserRepository.save(user2);

		this.mvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonInput))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email", is("test3@test.com")));
	}

	@Transactional
	@Rollback
	@Test
	void testGetUserByPathIdReturnsSingleUser() throws Exception {
		User user1 = new User();
		User user2 = new User();
		User user3 = new User();
		user1.setEmail("test1@test.com");
		user2.setEmail("test2@test.com");
		user3.setEmail("test3@test.com");
		this.newUserRepository.save(user1);
		this.newUserRepository.save(user2);
		this.newUserRepository.save(user3);

		this.mvc.perform(get(String.format("/users/%d", user2.getId())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email", is("test2@test.com")));
	}

	@Transactional
	@Rollback
	@Test
	void testPatchModifiesUserInDatabase() throws Exception {
		User user1 = new User();
		User user2 = new User();
		User user3 = new User();
		user1.setEmail("test1@test.com");
		user2.setEmail("test2@test.com");
		user3.setEmail("test3@test.com");
		this.newUserRepository.save(user1);
		this.newUserRepository.save(user2);
		this.newUserRepository.save(user3);
		String jsonInput = "{\"email\":\"john@example.com\",\"password\":\"1234\"}";

		this.mvc.perform(patch(String.format("/users/%d", user2.getId()))
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonInput))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email", is("john@example.com")));
	}

	@Transactional
	@Rollback
	@Test
	void testDeleteMethodsRemovesFromDatabase() throws Exception {
		User user1 = new User();
		User user2 = new User();
		User user3 = new User();
		user1.setEmail("test1@test.com");
		user2.setEmail("test2@test.com");
		user3.setEmail("test3@test.com");
		this.newUserRepository.save(user1);
		this.newUserRepository.save(user2);
		this.newUserRepository.save(user3);

		this.mvc.perform(delete(String.format("/users/%d", user2.getId())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email", is("test2@test.com")));

		this.mvc.perform(get(String.format("/users/%d", user2.getId())))
				.andExpect(status().isOk())
				.andExpect( jsonPath("$.email").doesNotExist());
	}
}
