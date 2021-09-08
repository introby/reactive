package by.intro.server.service;

import by.intro.server.model.game.*;
import by.intro.server.model.game.Character;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Random;

@Service
public class GameService {

    public Mono<Character> createCharacter(FactionEnum faction, String name) {
        Random random = new Random();
        int gold = random.nextInt(4000) + 1000;
        int crystal = random.nextInt(70) + 30;
        GreetingsBonus greetingsBonus = new GreetingsBonus(gold, crystal);
        Character character = switch (faction) {
            case ORC -> new Orc(greetingsBonus, name);
            case HUMAN -> new Human(greetingsBonus, name);
            case UNDEAD -> new Undead(greetingsBonus, name);
        };
        return Mono.just(character);
    }
}
