package by.intro.server.model.game;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public sealed class Character
        permits Orc, Human, Undead {

    private String name;
    private FactionEnum faction;
    private String factionAbility;
    private GreetingsBonus bonus;
}


