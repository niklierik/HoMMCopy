package me.eriknikli.homm.gameplay.spells;

import me.eriknikli.homm.data.ImageAsset;
import me.eriknikli.homm.data.Registry;
import me.eriknikli.homm.gameplay.army.Unit;

/**
 * Villámcsapás képesség
 */
public class Shock extends Spell {


    @Override
    public String name() {
        return "Shock";
    }

    @Override
    public int price() {
        return 60;
    }

    @Override
    public int mana() {
        return 5;
    }

    @Override
    public String desc() {
        return "It damages one unit depending on your Magic Power. (Magic Power * 30)";
    }

    @Override
    public double power(int skill) {
        return (int) (30 * skill);
    }

    @Override
    public ImageAsset icon() {
        return Registry.I_SHOCK;
    }

    @Override
    public void cast(Unit selected, Unit target) {
        target.getDamage(power(selected.hero().skill(skill())), false);
    }
}
