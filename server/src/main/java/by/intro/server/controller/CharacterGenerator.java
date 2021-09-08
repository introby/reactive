package by.intro.server.controller;

import by.intro.server.model.game.Character;
import by.intro.server.model.game.FactionEnum;
import by.intro.server.service.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController(value = "/api/v1/game")
public class CharacterGenerator {

    private final GameService gameService;

    public CharacterGenerator(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public Mono<Character> createCharacter(FactionEnum faction, String name) {
        return gameService.createCharacter(faction, name);
    }
}
