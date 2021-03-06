package me.eriknikli.homm.gameplay;

import me.eriknikli.homm.HoMM;
import me.eriknikli.homm.data.Registry;
import me.eriknikli.homm.gameplay.army.Unit;
import me.eriknikli.homm.gameplay.army.types.UnitType;
import me.eriknikli.homm.gameplay.spells.Spell;
import me.eriknikli.homm.scenes.components.game.GameBoard;
import me.eriknikli.homm.utils.RNG;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Hős, abstract, mert csak PlayerHero és AIHero létezhet, "sima" hős nem
 */
public abstract class Hero {

    /**
     * Név
     */
    public final String name;
    /**
     * Jelenlegi arany
     */
    private int gold;

    /**
     * Unitjai a hősnek
     */
    private final HashSet<Unit> units = new HashSet<>();

    /**
     * Spellek, amiket ismer a hős
     */
    private final HashSet<Spell> spells = new HashSet<>();

    /**
     * Skillek
     */
    private final HashMap<Skill, Integer> skills = new HashMap<>();

    /**
     * Megtanult skillek száma eddig
     */
    private int learntSkills = 0;

    /**
     * Kezdő gold
     */
    private final int startGold;


    /**
     * Mana mennyisége
     */
    private int mana;

    /**
     * Tud-e képességet castolni még?
     */
    private boolean canCastSpell = true;
    private Spell casting;

    /**
     * Hőst hoz létre, magába nem használható
     *
     * @param name      a hős neve
     * @param startGold a hős ennyi golddal kezd
     */
    protected Hero(String name, int startGold) {
        this.name = name;
        this.startGold = startGold;
        reset();
    }

    public void reset() {
        setGold(startGold);
        skills.clear();
        units.clear();
        spells.clear();
        for (Skill s : Skill.values()) {
            skills.put(s, 1);
        }
        learntSkills = 0;
        learnSpell(Registry.S_ATTACK);
        try {
            HoMM.update();
        } catch (Exception e) {
        }
    }

    /**
     * Meccs kezdetekor fut le
     */
    public void onStartMatch() {
        mana = skill(Skill.KNOWLEDGE) * 10;
    }


    /**
     * @return jelenlegi aranymennyiség
     */
    public int gold() {
        return gold;
    }

    /**
     * @return a unitjai a hősnek
     */
    public HashSet<Unit> units() {
        return units;
    }

    /**
     * Hozzáadja az adott unit-ot a hőshöz
     * Ha van hasonló típus már akkor merge-li
     */
    public void addUnit(Unit unit) {
        for (Unit u : units()) {
            if (u.type().equals(unit.type())) {
                u.addAmount(unit.amount());
                return;
            }
        }
        units.add(unit);
    }

    /**
     * @return ha sikerült megtanulnia a képességet
     */
    public boolean learnSpell(Spell s) {
        if (!knowsSpell(s)) {
            spells.add(s);
            return true;
        } else {
            return false;
        }
    }

    public boolean knowsSpell(Spell o) {
        for (Spell s : spells) {
            if (o.equals(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param type az egység típusa amit keresünk
     * @return visszaadja az adott típusú egyésg objektumot, amelyet a hős birtokol, null ha még ilyen nem létezik
     */
    public Unit unitOf(UnitType type) {
        for (var u : units) {
            if (u.type().equals(type)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Beállít adott mennyiségű aranyat ennek a hősnek. A gold soha nem megy 0 alá, és nem történik változás, ha ezt szeretnénk tenni.
     * (Tehát setGold(-1)-nek érdemi hatása nincs, nem lesz az aktuális gold se 0, se -1)
     *
     * @param amount mennyiség, amit szeretnénk beállítani
     * @return true ha amount >= 0, false ha amount < 0
     */
    public boolean setGold(int amount) {
        if (amount < 0) {
            return false;
        }
        gold = amount;
        return true;
    }

    /**
     * Hozzáad a jelenlegi aranyhoz
     *
     * @param amount a mennyiség, amit szeretnénk hozzáadni, lehet negatív is
     * @return sikerült-e adott mennyiségű aranyat hozzáadni, ha negatívba mennénk át, akkor false és érdemi változás nem történik
     */
    public boolean addGold(int amount) {
        return setGold(gold() + amount);
    }

    /**
     * Kivon a jelenlegi aranyból, shortcut {@code addGold(-amount)}-ra
     *
     * @param amount a mennyiség, amennyit szeretnénk hozzáadni, lehet negatív is
     * @return sikerült-e adott mennyiségű aranyat levonnunk, ha negatívba menne át, akkor nem történik semmi
     */
    public boolean subtractGold(int amount) {
        return addGold(-amount);
    }

    /**
     * @param gold aranymennyiség
     * @return van-e ennyi aranya ennek a hősnek?
     */
    public boolean canAfford(int gold) {
        return this.gold >= gold;
    }

    /**
     * Megtanult spell árának visszakérése, és annak elfelejtése
     *
     * @param s a spell
     */
    public void unlearnSpell(Spell s) {
        spells.remove(s);
    }

    /**
     * Egy adott egység kitörlése a hős egységei közül
     *
     * @param u az egység
     */
    public void removeUnit(Unit u) {
        units.remove(u);
    }

    /**
     * @param s az adott skill
     * @return mekkora szintű ezen a skillen, >=1-nek kell lennie
     */
    public int skill(Skill s) {
        if (s == null) {
            return 0;
        }
        return skills.getOrDefault(s, 0);
    }

    /**
     * @return Tud-e skillt fejleszteni
     */
    public boolean canImprove(Skill s) {
        return canAfford(nextSkillPrice()) && skill(s) < 10;
    }

    /**
     * @param s a skill
     * @return Tud-e az adott skillből visszavenni
     */
    public boolean canDeprove(Skill s) {
        return skill(s) > 1;
    }

    /**
     * Skillből vesz vissza eggyel és visszaadja a pénzt
     *
     * @param s a skill
     */
    public void decreaseSkill(Skill s) {
        if (canDeprove(s)) {
            skills.put(s, skill(s) - 1);
            learntSkills--;
            addGold(nextSkillPrice());
        }
        HoMM.update();
    }

    /**
     * Javít az adott skillből ha tud, és visszaadja az aranyat
     *
     * @param s a skill
     */
    public void increaseSkill(Skill s) {
        if (canImprove(s)) {
            skills.put(s, skill(s) + 1);
            subtractGold(nextSkillPrice());
            learntSkills++;
        }
        HoMM.update();
    }

    public int nextSkillPrice() {
        return skillPriceN(learntSkills);
    }

    private int skillPriceN(int n) {
        if (n == 0) {
            return 5;
        }
        return (int) Math.ceil(skillPriceN(n - 1) * 1.1);
    }

    public void resetSkills() {
        for (Skill s : Skill.values()) {
            while (canDeprove(s)) {
                decreaseSkill(s);
            }
        }
    }

    public void random() {
        if (startGold < 2000) {


            int n = 0;
            while (n < 10000 && canAfford(1)) {
                n++;
                int i = RNG.randomInt(3);
                switch (i) {
                    case 0:
                        if (learnSpell(RNG.randomElement(Registry.spells()))) {
                            n--;
                        }
                        break;
                    case 1:
                        int amount = RNG.randomInt(1, 30);
                        var type = RNG.randomElement(Registry.uTypes());
                        if (subtractGold(amount * type.price())) {
                            addUnit(new Unit(type, amount));
                            n--;
                        }
                        break;
                    case 2:
                        var s = RNG.randomElement(Skill.values());
                        if (canImprove(s)) {
                            increaseSkill(s);
                            n--;
                        }
                        break;
                }
            }
            while (canAfford(Registry.UT_FARMER.price())) {
                addUnit(new Unit(Registry.UT_FARMER, 1));
            }
            HoMM.update();
        }
    }

    public abstract void theirTurn(GameBoard board, Unit which);

    public int getMana() {
        return mana;
    }


    public void setMana(int mana) {
        this.mana = mana;
    }

    public HashSet<Spell> spells() {
        return spells;
    }

    public Color color() {
        if (this instanceof PlayerHero) {
            return new Color(8, 68, 6);
        }
        return new Color(91, 0, 0);
    }


    public void onStartRound() {
        canCastSpell = true;
    }

    public void onCastSpell() {
        canCastSpell = false;
        HoMM.update();
    }

    public boolean canCastSpell() {
        return canCastSpell;
    }

    public void setCastingSpell(Spell s) {
        this.casting = s;
    }

    public Spell casting() {
        return casting;
    }

    public String helpTxt() {
        var skills = "";
        for (Skill s : Skill.values()) {
            skills += "<li>";
            skills += "<strong>" + s.display() + "</strong>" + ": " + skill(s);
            skills += "</li>";
        }
        var spells = "";
        for (Spell s : Registry.spells()) {
            if (knowsSpell(s)) {
                spells += "<li>";
                spells += s.name();
                spells += "</li>";
            }
        }
        var units = "";
        for (var unit : units()) {
            units += "<li>";
            units += "<strong>" + unit.type().name() + "</strong>: " + unit.amount();
            units += "</li>";
        }
        return "<html>" +
                "<h1>" +
                name +
                "</h1>" +
                "<h3>Skills</h3>" +
                "<ul>" +
                skills +
                "</ul>" +
                "<h3>Spells</h3>" +
                "<ul>" +
                spells +
                "</ul>" +
                "<h3>Units</h3>" +
                "<ul>" +
                units +
                "</ul>" +
                "</html>";
    }
}
