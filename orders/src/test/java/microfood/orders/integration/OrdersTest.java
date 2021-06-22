package microfood.orders.integration;


import static org.junit.Assert.assertEquals;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.verify.VerificationTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;

import lombok.extern.slf4j.Slf4j;
import microfood.orders.dtos.OrderDTO;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integrationtest")
@Transactional
@Slf4j
public class OrdersTest {
    private ClientAndServer mockServer;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    TestRestTemplate restTemplate;

    @BeforeAll
    public void startServer() {
        mockServer = startClientAndServer(1080);

    }

    @AfterAll
    public void stopServer() {
        mockServer.stop();
    }

    private void prepare() throws IOException {
        URL url = Resources.getResource("restaurant_menu.json");
        String response = Resources.toString(url, StandardCharsets.UTF_8);
        HttpRequest request = request()
                .withMethod("GET")
                .withPath("/restaurants/cc81f348-e739-43ec-b973-56ea41843b74/menu");
        new MockServerClient("localhost", 1080)
                .when(request).respond(response().withStatusCode(200).withBody(response)
                .withContentType(org.mockserver.model.MediaType.APPLICATION_JSON));

        String ticketResponse = Resources.toString(Resources.getResource("ticket_base.json"), StandardCharsets.UTF_8);
        HttpRequest ticketRequest = request()
                .withMethod("POST")
                .withPath("/tickets/cc81f348-e739-43ec-b973-56ea41843b74");
        new MockServerClient("localhost", 1080)
                .when(ticketRequest).respond(response().withStatusCode(201).withBody(ticketResponse)
                .withContentType(org.mockserver.model.MediaType.APPLICATION_JSON));
    }

    private void verify() {
        new MockServerClient("localhost", 1080)
                .verify(
                        request().withMethod("GET")
                                .withPath("/restaurants/cc81f348-e739-43ec-b973-56ea41843b74/menu"),
                        VerificationTimes.exactly(1));
        new MockServerClient("localhost", 1080)
                .verify(
                        request().withMethod("POST")
                                .withPath("/tickets/cc81f348-e739-43ec-b973-56ea41843b74"),
                        VerificationTimes.exactly(1));
    }

    @Test
    public void testOrderCreation() throws IOException {
        prepare();
        URL url = Resources.getResource("order.json");
        OrderDTO orderDTO = objectMapper.readValue(url, OrderDTO.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OrderDTO> entity = new HttpEntity<>(orderDTO, headers);
        ResponseEntity<OrderDTO> order = restTemplate.exchange("/orders", HttpMethod.POST, entity, OrderDTO.class);
        verify();
        String orderPath = String.format("/orders/%s", order.getBody().getOrderId());
        ResponseEntity<OrderDTO> foundOrder = restTemplate.exchange(orderPath, HttpMethod.GET, entity, OrderDTO.class);
        assertEquals(order.getBody().getOrderId(), foundOrder.getBody().getOrderId());
    }
}
