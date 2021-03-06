package me.eriknikli.homm.scenes.components.prep;

import me.eriknikli.homm.HoMM;
import me.eriknikli.homm.gameplay.AIHero;
import me.eriknikli.homm.gameplay.Hero;
import me.eriknikli.homm.scenes.GameScene;
import me.eriknikli.homm.scenes.PrepScene;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Itt látható a jelenlegi állapota a hősnek
 */
public class CurrentStatusPanel extends JPanel {

    private final UnitListPanel units;
    private final SpellsListPanel spells;
    private final JLabel gold;
    private final ImproveSkillPanel skills;
    private final JButton startBtn;
    private Hero hero;

    public CurrentStatusPanel(Hero hero, PrepScene scene) {
        this.hero = hero;
        setLayout(new GridBagLayout());
        var c = new GridBagConstraints();
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.weightx = c.weighty = 0;
        c.insets = new Insets(5, 5, 5, 5);
        var lbl = new JLabel(hero.name);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setFont(new Font("Times New Roman", Font.BOLD, 40));
        lbl.setHorizontalTextPosition(SwingConstants.CENTER);
        c.anchor = GridBagConstraints.PAGE_START;
        add(lbl, c);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.weightx = c.weighty = 0;
        c.anchor = GridBagConstraints.PAGE_START;
        add(gold = new JLabel("Gold: " + hero.gold()), c);
        gold.setHorizontalAlignment(SwingConstants.CENTER);
        gold.setHorizontalTextPosition(SwingConstants.CENTER);
        gold.setFont(new Font("Arial", Font.BOLD, 15));

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        // c.weightx = 1;
        add(skills = new ImproveSkillPanel(hero), c);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        //c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.PAGE_START;
        c.weighty = 1;
        c.weightx = 1;
        c.insets = new Insets(10, 10, 10, 10);
        add(units = new UnitListPanel(hero), c);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 1;
        // c.weighty = 1;
        //c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(10, 10, 10, 10);
        add(spells = new SpellsListPanel(hero), c);
        var help = new JLabel("Hover here to check your enemy");
        help.setFont(new Font("Arial", Font.ITALIC, 15));
        help.setToolTipText(((AIHero) scene.ai()).helpTxt());
        help.setHorizontalAlignment(SwingConstants.CENTER);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;

        add(help, c);
        var randomBtn = new JButton("Random Hero");
        randomBtn.addActionListener(e -> {
            hero.reset();
            hero.random();
        });
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 5;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        add(randomBtn, c);

        var resetBtn = new JButton("Reset Hero");
        resetBtn.addActionListener(e -> {
            hero.reset();
        });
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 5;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        add(resetBtn, c);

        startBtn = new JButton("Start Battle");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(startBtn, c);
        startBtn.addActionListener(e -> {
            EventQueue.invokeLater(() -> {
                HoMM.game().setScene(new GameScene(scene.player(), scene.ai()));
            });
        });
        startBtn.setEnabled(false);
        _update();
    }

    public void _update() {
        units._update();
        spells._update();
        skills._update();
        gold.setText("Gold: " + hero.gold());
        startBtn.setEnabled(hero.units().size() > 0);
    }

}
