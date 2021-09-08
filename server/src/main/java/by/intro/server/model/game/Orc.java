package by.intro.server.model.game;

public final class Orc extends Character {

    public Orc(GreetingsBonus bonus, String name) {
        super(name, FactionEnum.ORC, "Hardness", bonus);
    }
}
