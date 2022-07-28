package com.rahmanash.gettingstarted.apiservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.rahmanash.gettingstarted.apiservice.room.Room;
import com.rahmanash.gettingstarted.apiservice.room.RoomRepository;

@SpringBootTest
@AutoConfigureWebTestClient
public class ApiServiceApplicationTests {
	
	 @MockBean
	 private ApiService apiService;
	 
	@Test
    void testValidService(@Autowired WebTestClient webClient) throws JSONException {
		when(this.apiService.isValidService("dolby")).thenReturn(true);
		JSONObject response = new JSONObject();
		response.put("serviceId", "dolby");
		response.put("Valid", true);

        webClient
                .get().uri("/service/dolby")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(response.toString());
    }
	

}
