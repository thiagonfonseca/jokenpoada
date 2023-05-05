package tech.ada.games.jokenpo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import tech.ada.games.jokenpo.dto.PlayerDto;
import tech.ada.games.jokenpo.exception.DataConflictException;
import tech.ada.games.jokenpo.exception.DataNotFoundException;
import tech.ada.games.jokenpo.model.Player;
import tech.ada.games.jokenpo.service.PlayerService;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
abstract class BasePlayerTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected PlayerService playerService;

    protected Optional<Player> createPlayerIfNotExists(final String username, final String password) {
        try {
            final Player player = playerService.findByPlayer(username);
            return Optional.ofNullable(player);
        } catch (DataNotFoundException exception) {
            return this.createPlayer(username, password);
        }
    }

    private Optional<Player> createPlayer(final String username, final String password) {
        final PlayerDto playerDto = this.buildPlayerDto(username, password);
        try {
            playerService.createPlayer(playerDto);
        } catch (DataConflictException exception) {
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(playerService.findByPlayer(playerDto.getUsername()));
        } catch (DataNotFoundException e) {
            return Optional.empty();
        }
    }

    private PlayerDto buildPlayerDto(final String username, final String password) {
        final PlayerDto playerDto = new PlayerDto();
        playerDto.setUsername(username);
        playerDto.setName(username + 1);
        playerDto.setPassword(password);
        return playerDto;
    }
}