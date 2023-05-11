package tech.ada.games.jokenpo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tech.ada.games.jokenpo.dto.PlayerDto;
import tech.ada.games.jokenpo.response.AuthResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class PlayerControllerTest extends AbstractBaseTest {

    private final String baseUri = "/api/v1/jokenpo/player";
    private AuthResponse authResponse;

    @BeforeEach
    void beforeAll() {
        this.populateDatabase();
        this.authResponse = this.loginAsF1rstPlayer();
    }

    @Test
    void createPlayerTest() throws Exception {
        PlayerDto player = PlayerDto.builder()
            .name("player1")
            .username("username1")
            .password("1234")
            .build();

        final MockHttpServletResponse response =
                mvc.perform(post(baseUri + "/create")
                                .content(asJsonString(player))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", authResponse.getAccessToken()))
                        .andDo(print())
                        .andReturn().getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    void findAllPlayersTest() throws Exception {

        final MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.get(baseUri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authResponse.getAccessToken()))
                .andDo(print())
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getContentAsString());

    }
 
    @Test
    void findPlayersByNameTest() throws Exception {

        final MockHttpServletResponse response = mvc.perform(get(baseUri + "/player1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authResponse.getAccessToken()))
                .andDo(print())
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getContentAsString());
    }
 
     @Test
     void deletePlayerWithSuccessTest() throws Exception {

         final MockHttpServletResponse response = mvc.perform(delete(baseUri + "/1")
                 .contentType(MediaType.APPLICATION_JSON)
                 .header("Authorization", authResponse.getAccessToken()))
                 .andDo(print())
                 .andReturn().getResponse();

         assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
     }

}