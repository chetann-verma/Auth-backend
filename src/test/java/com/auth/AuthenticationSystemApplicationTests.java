package com.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
@SpringBootTest
class AuthenticationSystemApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void registerLoginAndProfileFlowWorks() throws Exception {
		String registerBody = """
				{
				  \"name\": \"Test User\",
				  \"email\": \"test@example.com\",
				  \"password\": \"Password123\"
				}
				""";

		mockMvc.perform(post("/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(registerBody))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.token").isNotEmpty())
				.andExpect(jsonPath("$.email").value("test@example.com"));

		String loginBody = """
				{
				  \"email\": \"test@example.com\",
				  \"password\": \"Password123\"
				}
				""";

		MvcResult loginResult = mockMvc.perform(post("/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(loginBody))
				.andExpect(status().isOk())
				.andReturn();

		JsonNode loginJson = objectMapper.readTree(loginResult.getResponse().getContentAsString());
		String token = loginJson.get("token").asText();

		mockMvc.perform(get("/profile")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("test@example.com"))
				.andExpect(jsonPath("$.name").value("Test User"));

		mockMvc.perform(get("/feed")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));

		String updateBody = """
				{
				  \"name\": \"Updated User\",
				  \"email\": \"updated@example.com\"
				}
				""";

		mockMvc.perform(put("/profile")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(updateBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Updated User"))
				.andExpect(jsonPath("$.email").value("updated@example.com"));
	}

	@Test
	void profileRequiresAuthentication() throws Exception {
		mockMvc.perform(get("/profile"))
				.andExpect(status().isForbidden());

		mockMvc.perform(get("/feed"))
				.andExpect(status().isForbidden());

		mockMvc.perform(put("/profile")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"A\",\"email\":\"a@example.com\"}"))
				.andExpect(status().isForbidden());
	}

}
