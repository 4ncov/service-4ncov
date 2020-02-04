package com.ncov.module.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncov.module.NCoVApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {NCoVApplication.class})
@ActiveProfiles("test")
class FileControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void should_return_date_and_image_url_and_message_when_upload_image() throws Exception {
        String responseJson = mockMvc.perform(MockMvcRequestBuilders
                .multipart("/api/images")
                .file(new MockMultipartFile("image", "original-filename.jpeg", MediaType.APPLICATION_OCTET_STREAM_VALUE, new byte[]{}))
                .param("category", "身份验证"))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode jsonNode = new ObjectMapper().readTree(responseJson);
        assertEquals("Image uploaded.", jsonNode.get("message").asText());
        assertEquals("https://oss.com/images/身份验证-4cccf5ed-8e04-42b7-a8a1-0fce2cdb1b0e.png", jsonNode.get("data").get("url").asText());
        assertTrue(jsonNode.get("data").hasNonNull("gmtCreated"));
    }
}