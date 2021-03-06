package me.eriknikli.homm.test;

import me.eriknikli.homm.data.Registry;
import me.eriknikli.homm.gameplay.Difficulty;
import me.eriknikli.homm.gameplay.PlayerHero;
import me.eriknikli.homm.gameplay.army.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Leteszteli, hogy a Unit addolás jól működik, merge-el ha kell, ha kell akkor hozzáadja a listához, mint új objektum a unit-ot
 * Szélsőséges esetek: amikor már létezik adott egység, vagy ha nem létezik adott egység, akkor visszatér-e nullal?
 */
public class AddUnitsTest {


    @Test()
    @DisplayName("Add Units Test")
    public void test() {
        PlayerHero hero = new PlayerHero("Test János", Difficulty.NO_LIMIT);
        Unit u1 = new Unit(Registry.UT_ARCHER, 100);
        Unit u2 = new Unit(Registry.UT_ARCHER, 200);
        Unit u3 = new Unit(Registry.UT_FARMER, 500);
        Unit u4 = new Unit(Registry.UT_GRIFFIN, 500);
        Unit u5 = new Unit(Registry.UT_GRIFFIN, 300);
        hero.addUnit(u1);
        hero.addUnit(u2);
        hero.addUnit(u3);
        hero.addUnit(u4);
        hero.addUnit(u5);
        Assertions.assertEquals(500, hero.unitOf(Registry.UT_FARMER).amount());
        Assertions.assertEquals(300, hero.unitOf(Registry.UT_ARCHER).amount());
        Assertions.assertEquals(800, hero.unitOf(Registry.UT_GRIFFIN).amount());
        Assertions.assertNull(hero.unitOf(Registry.UT_PRIEST));
        Assertions.assertNull(hero.unitOf(Registry.UT_SWORDSMAN));
    }

}
