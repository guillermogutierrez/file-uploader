package org.guillermo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ComponentScan(basePackages = "org.guillermo.file")
class ZipFileApplicationTests {

	private static String FILE_NAME = "test.txt";

	private static String FILE_PARAM = "files";

	private static String FILE_CONTENT = "Testing the multiple upload endpoint";

	private static String END_POINT = "/multiple-upload";
	
	@Autowired
	private MockMvc mockMvc;

	@TestConfiguration
	static class TestConfig {
		@Bean
		public MockMvc mockMvc(WebApplicationContext webApplicationContext) {

			return MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		}
	}

	@Test
	public void whenFileUploaded_thenVerifyStatus() throws Exception {

		mockMvc.perform(multipart(END_POINT).file(createMockMultipartFile())).andExpect(status().isOk());
	}

	@Test
	public void whenFileUploaded_thenVerifyContent() throws Exception {

		JSONObject responseJSON = new JSONObject(mockMvc.perform(multipart(END_POINT).file(createMockMultipartFile()))
				.andReturn().getResponse().getContentAsString());

		JSONAssert.assertEquals("{fileOutcomes=[{\"fileName\":\"" + FILE_NAME + "\",\"resultCode\":\"FILE_STORED\"}]}",
				responseJSON, JSONCompareMode.LENIENT);

	}

	@Test
	public void testDownloadFile() throws Exception {

		JSONObject responseJSON = new JSONObject(mockMvc.perform(multipart(END_POINT).file(createMockMultipartFile()))
				.andReturn().getResponse().getContentAsString());

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/download/" + responseJSON.get("id"))
						.contentType(MediaType.APPLICATION_OCTET_STREAM)).andReturn();

		assertEquals(200, result.getResponse().getStatus());
		assertEquals(Boolean.TRUE, result.getResponse().getContentAsString().contains(FILE_NAME));

	}

	private MockMultipartFile createMockMultipartFile() {
		return new MockMultipartFile(FILE_PARAM, FILE_NAME, MediaType.TEXT_PLAIN_VALUE, FILE_CONTENT.getBytes());
	}

}
