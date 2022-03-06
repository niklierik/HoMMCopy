package me.eriknikli.homm.gameplay.army;

import me.eriknikli.homm.gameplay.army.types.UnitType;

public class Unit {


    private final UnitType type;
    private final int amount;

    public Unit(UnitType type, int a) {
        this.type = type;
        amount = a;
    }

    public int amount() {
        return amount;
    }

    public UnitType type() {
        return type;
    }

}