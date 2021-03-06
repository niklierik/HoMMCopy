package me.eriknikli.homm.gameplay.spells;

import me.eriknikli.homm.data.ImageAsset;
import me.eriknikli.homm.data.Registry;
import me.eriknikli.homm.gameplay.army.Unit;

/**
 * Feltámasztás, vagy gyógyítás spell
 */
public class Resurrect extends Spell {


    @Override
    public String name() {
        return "Resurrect";
    }

    @Override
    public int price() {
        return 120;
    }

    @Override
    public int mana() {
        return 6;
    }

    @Override
    public String desc() {
        return "It can heal units depending on your Magic Power. (50 * Magic Power)";
    }

    @Override
    public double power(int skill) {
        return (int) (50 * skill);
    }

    @Override
    public ImageAsset icon() {
        return Registry.I_RESURRECT;
    }

    @Override
    public void cast(Unit selected, Unit target) {
        target.heal(power(selected.hero().skill(skill())), true);
    }
}
