package me.eriknikli.homm.gameplay.army;

import me.eriknikli.homm.gameplay.Hero;
import me.eriknikli.homm.gameplay.army.types.UnitType;
import me.eriknikli.homm.scenes.components.game.Tile;

import java.util.HashSet;

/**
 * Egy egység
 */
public class Unit {

    /**
     * Az egység típusa
     */
    private final UnitType type;
    /**
     * Mennyi katona van az adott egységben
     */
    private int amount;
    /**
     * Életereje az egységnek
     */
    private double health;

    /**
     * Mező ahol található
     */
    private Tile tile;

    /**
     * Ennek a hősnek a birtokában van
     */
    private Hero hero;

    /**
     * Kezdő életereje a hősnek
     */
    private double startHP;

    /**
     * Meg volt-e támadva
     */
    private boolean wasAttacked = false;

    /**
     * Létrehoz egy új egységet
     *
     * @param type az egyésg típusa, Registry.UT_*
     * @param a    mennyiség
     * @throws IllegalArgumentException ha az amount nem pozitív
     */
    public Unit(UnitType type, int a) {
        this.type = type;
        amount = a;
        health = type().maxHealth() * a;
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be negative or 0.");
        }
    }

    /**
     * Ha elindul egy kör, akkor lesz meghívva ez a függvény
     */
    public void onTurnStarted() {
        wasAttacked = false;
        type().onStartTurn(this);
    }

    /**
     * @return a katonák száma az egységben
     */
    public int amount() {
        return amount;
    }

    /**
     * Hozzáad ennyi katonát az egységhez
     *
     * @param amount katonák száma
     */
    public void addAmount(int amount) {
        this.amount += amount;
        this.health += amount * type.maxHealth();
    }

    /**
     * @return az egység típusa
     */
    public UnitType type() {
        return type;
    }

    /**
     * Lesebzi az egységet
     *
     * @param dam a sebzés mértéke
     */
    public void dealDamage(double dam) {
        health -= dam;
        amount = (int) (health / type().maxHealth());
        if (health <= 0) {
            die();
        }
        health = Math.max(health, 0);
    }

    /**
     * Meghal az adott egység, és a program "feltakarítja" a maradékait (pl. mezőről eltünteti és a hőstől is, stb...)
     */
    public void die() {

    }

    /**
     * @return él-e még ez az egység?
     */
    public boolean isAlive() {
        return health <= 0;
    }

    /**
     * @return a hős jelenlegi életerejét
     */
    public double health() {
        return health;
    }

    /**
     * @return a hőst
     */
    public Hero hero() {
        return hero;
    }

    /**
     * @return meg volt-e támadva az adott egyésg ebben a körben? Minden kör elején visszaáll false-ra
     */
    public boolean wasAttacked() {
        return wasAttacked;
    }

    /**
     * @return tud-e visszatámadni?
     */
    public boolean canCounterAttack() {
        return type().canCounterAttack(this);
    }

    /**
     * @return lehetséges célpontok
     */
    public HashSet<Unit> validTargets() {
        return type().validTargets(this);
    }

    /**
     * @return a mezőt, amin a hős van
     */
    public Tile tile() {
        return tile;
    }

    /**
     * @param who az egység
     * @return megnézi, hogy az adott egység egy csapatba van-e a másikkal (ha nem, akkor lehet, hogy who null, ha nem, akkor viszont egyértelműen ellenfelek)
     */
    public boolean isWith(Unit who) {
        if (who == null) {
            return false;
        }
        return who.hero() == hero();
    }

    /**
     * Gyógyul a hős
     */
    public void heal(double amount, boolean canRevive) {
        health += amount;
        health = Math.min(startHP, health);
        if (!canRevive) {
            health = Math.min(health, this.amount * type.maxHealth());
        } else {
            this.amount = (int) (health / type.maxHealth());
        }

    }
}
