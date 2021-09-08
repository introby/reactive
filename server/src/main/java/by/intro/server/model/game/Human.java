package by.intro.server.model.game;

public final class Human extends Character {

    public Human(GreetingsBonus bonus, String name) {
        super(name, FactionEnum.HUMAN, "Health Regeneration", bonus);
    }
}
