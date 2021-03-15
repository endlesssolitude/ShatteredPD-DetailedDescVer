package com.shatteredpixel.shatteredpixeldungeon.custom.testmode.generator;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.TomeOfMastery;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.AlchemicalCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ArcaneCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlink;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfIntuition;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greatsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed;
import com.shatteredpixel.shatteredpixeldungeon.plants.Dreamfoil;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss;
import com.shatteredpixel.shatteredpixeldungeon.plants.Starflower;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class LazyTest extends TestGenerator {
    {
        image = ItemSpriteSheet.EBONY_CHEST;
    }

    @Override
    public void execute(Hero hero, String action){
        if(action.equals(AC_GIVE)){
            new PotionOfExperience().quantity(100).identify().collect();
            new PotionOfFrost().quantity(100).identify().collect();
            new PotionOfHaste().quantity(100).identify().collect();
            new PotionOfHealing().quantity(100).identify().collect();
            new PotionOfInvisibility().quantity(100).identify().collect();
            new PotionOfLevitation().quantity(100).identify().collect();
            new PotionOfLiquidFlame().quantity(100).identify().collect();
            new PotionOfMindVision().quantity(100).identify().collect();
            new PotionOfParalyticGas().quantity(100).identify().collect();
            new PotionOfPurity().quantity(100).identify().collect();
            new PotionOfStrength().quantity(100).identify().collect();
            new PotionOfToxicGas().quantity(100).identify().collect();

            new ScrollOfIdentify().quantity(100).identify().collect();
            new ScrollOfLullaby().quantity(100).identify().collect();
            new ScrollOfMagicMapping().quantity(100).identify().collect();
            new ScrollOfMirrorImage().quantity(100).identify().collect();
            new ScrollOfRage().quantity(100).identify().collect();
            new ScrollOfRecharging().quantity(100).identify().collect();
            new ScrollOfRemoveCurse().quantity(100).identify().collect();
            new ScrollOfRetribution().quantity(100).identify().collect();
            new ScrollOfTeleportation().quantity(100).identify().collect();
            new ScrollOfTerror().quantity(100).identify().collect();
            new ScrollOfTransmutation().quantity(100).identify().collect();
            new ScrollOfUpgrade().quantity(100).identify().collect();

            PlateArmor plateArmor = new PlateArmor();
            plateArmor.level(15);
            plateArmor.identify().collect();

            Greatsword greatsword = new Greatsword();
            greatsword.level(15);
            greatsword.identify().collect();

            new Gold().quantity(36888).doPickUp(hero);

            new Pasty().quantity(100).collect();

            new Food().quantity(100).collect();

            new Dart().quantity(777).collect();

            new Bomb().quantity(777).collect();

            new TomeOfMastery().collect();

            new Honeypot().quantity(777).collect();

            new StoneOfIntuition().quantity(777).collect();

            new Torch().quantity(777).identify().collect();

            new AlchemicalCatalyst().quantity(100).collect();

            new ArcaneCatalyst().quantity(100).collect();

            new StoneOfBlast().quantity(4444).collect();

            new StoneOfBlink().quantity(4444).collect();

            new Blindweed.Seed().quantity(100).identify().collect();
            new Dreamfoil.Seed().quantity(100).identify().collect();
            new Earthroot.Seed().quantity(100).identify().collect();
            new Fadeleaf.Seed().quantity(100).identify().collect();
            new Firebloom.Seed().quantity(100).identify().collect();
            new Icecap.Seed().quantity(100).identify().collect();
            new Rotberry.Seed().quantity(100).identify().collect();
            new Sorrowmoss.Seed().quantity(100).identify().collect();
            new Starflower.Seed().quantity(100).identify().collect();
            new Stormvine.Seed().quantity(100).identify().collect();
            new Sungrass.Seed().quantity(100).identify().collect();
            new Swiftthistle.Seed().quantity(100).identify().collect();

            detach(hero.belongings.backpack);
        }
    }
}
