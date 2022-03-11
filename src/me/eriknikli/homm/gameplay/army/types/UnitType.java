package me.eriknikli.homm.gameplay.army.types;

import me.eriknikli.homm.data.ImageAsset;
import me.eriknikli.homm.gameplay.army.Unit;
import me.eriknikli.homm.utils.Range;

/**
 * Egységek típusát leíró osztály, minden egységtípusnak saját osztálya lesz
 * Lehetne ENUM is, de úgy látom, hogy a speciális képességeket külön osztályokkal könnyebb megvalósítani
 */
public abstract class UnitType {

    /**
     * @return név
     */
    public abstract String name();

    /**
     * @return leírás
     */
    public abstract String description();

    /**
     * @return ár "darabonként"
     */
    public abstract int price();

    /**
     * @return sebzés "darabonként"
     */
    public abstract Range damage();

    /**
     * @return max / kezdeti életerő "darabonként"
     */
    public abstract double maxHealth();

    /**
     * @return sebesség
     */
    public abstract int speed();

    /**
     * @return Ikonhoz kép, ami a mezőn látszódik
     */
    public abstract ImageAsset image();

    /**
     * Egységet készít ebből a típusból
     *
     * @param amount mennyiség
     * @return egy új egység adott mennyiséggel ezzel a típussal
     */
    public final Unit createUnit(int amount) {
        return new Unit(this, amount);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof UnitType) {
            return getClass().equals(obj.getClass());
        }
        return false;
    }
}
