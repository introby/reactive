package by.intro.server.model.game;

public final class Undead extends Character {

    public Undead(GreetingsBonus bonus, String name) {
        super(name, FactionEnum.UNDEAD, "Magic Resistance", bonus);
    }
}
