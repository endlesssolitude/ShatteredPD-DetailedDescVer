package com.shatteredpixel.shatteredpixeldungeon.custom.challenges.mimic;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.CustomUtils;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Stone;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MimicForChallenge extends Mimic {
    {
        immunities.add(Grim.class);
        immunities.add(Corruption.class);
        immunities.add(ScrollOfRetribution.class);
        immunities.add(Paralysis.class);
        immunities.add(Terror.class);
        resistances.add(Amok.class);
        resistances.add(ScrollOfPsionicBlast.class);
        flying=true;
    }

    protected float level;
    private static final String LEVEL = "level";
    private static final String BASIC_MOD_FACTOR = "b_mod";
    private static final String TYPE_FACTOR = "type_f";
    private static final String ATTACK_FACTOR = "attack_factor";
    private static final String DEFENSE_FACTOR = "defense_factor";
    private static final String TRICK_FACTOR = "trick_factor";

    public static MimicForChallenge spawnAt( int pos, Item item ){
        return spawnAt( pos, Arrays.asList(item), MimicForChallenge.class);
    }

    public static MimicForChallenge spawnAt( int pos, Item item, Class mimicType ){
        return spawnAt( pos, Arrays.asList(item), mimicType);
    }

    public static MimicForChallenge spawnAt( int pos, List<Item> items ) {
        return spawnAt( pos, items, MimicForChallenge.class);
    }
    public static MimicForChallenge spawnAt(int pos, List<Item> items, Class mimicType ) {

        MimicForChallenge m;

        if(mimicType == GoldenMimicForChallenge.class){
            m = new GoldenMimicForChallenge();
        }else if(mimicType == CrystalMimicForChallenge.class){
            m = new CrystalMimicForChallenge();
        }else{
            m = new MimicForChallenge();
        }

        m.items = new ArrayList<>( items );

        m.setLevel( Dungeon.depth );
        m.pos = pos;

        //generate an extra reward for killing the mimic
        m.generatePrize();

        return m;
    }

    public int type = 0;
    protected static final int T_CHAOS = 0;
    protected static final int T_ATTACK = 1;
    protected static final int T_DEFEND = 2;
    protected static final int T_TRICK = 3;

    //BASIC attribute modification for mimic. Spawn one, occasionally two.
    //Mod level is between 0 and 7.
    protected int basicModFactor = 0;
    protected static final int BASE_HEALTH_MOVE = 0;
    protected static final int BASE_ATTACK_MOVE = 3;
    protected static final int BASE_EVASION_MOVE = 6;
    protected static final int BASE_ACCURACY_MOVE = 9;
    protected static final int BASE_MOVE_SPEED_MOVE = 12;
    protected static final int BASE_ATTACK_SPEED_MOVE = 15;

    protected void createType(){
        type = Random.chances(new float[]{40f, 20f, 10f, 20f});
    }

    protected int basicModLevel(){
        return Random.chances(new float[]{17,13,11,7})+Math.min(4, Math.round(level/6));
    }

    protected int basicPerkModLevel(){
        return Random.chances(new float[]{7,7,5,5})+Math.min(4, Math.round(level/6));
    }

    protected int maxBasicTags(){
        return 1;
    }

    protected void createBasicModFactor(){
        int maxMods = 1;

        int typeAbilityLevel = basicPerkModLevel();

        int created=0;
        switch (type){
            case T_ATTACK:
                basicModFactor += typeAbilityLevel<<BASE_ATTACK_MOVE;
                created += 1<<(BASE_ATTACK_MOVE/3);
                break;
            case T_DEFEND:
                basicModFactor += typeAbilityLevel<<BASE_HEALTH_MOVE;
                created += 1<<(BASE_HEALTH_MOVE/3);
                break;
            case T_TRICK:
                basicModFactor += typeAbilityLevel<<BASE_MOVE_SPEED_MOVE;
                created += 1<<(BASE_MOVE_SPEED_MOVE/3);
                break;
            case T_CHAOS: default: maxMods=maxBasicTags();
        }
        if(created>0){
            if(maxBasicTags()>1) {
                int sel;
                do {
                    sel = Random.Int(6);
                } while (((created >> sel) & 1) > 0);
                basicModFactor += basicModLevel() << (sel * 3);
            }
        }else {
            int modded = 0;
            int maxRoll = 6;
            //before resistances
            for (int i = 0; i < 6; ++i) {
                if (Random.Float() < (float) (maxMods - modded) / (maxRoll - i)) {
                    int modLevel = (Random.Int(4)==0) ? basicPerkModLevel():basicModLevel();
                    basicModFactor += modLevel << (3 * i);
                    modded++;
                }
            }
        }
    }

    //6 basic arguments
    protected float HealthModFactor(){
        int modlevel = (basicModFactor>>BASE_HEALTH_MOVE) & 0x7;
        return 1f + modlevel/16f + modlevel*modlevel / 112f;
    }
    protected float AttackModFactor(){
        int modlevel = (basicModFactor>>BASE_ATTACK_MOVE) & 0x7;
        return 1f + modlevel/14f + modlevel*modlevel / 98f;
    }
    protected float AccuracyModFactor(){
        int modlevel = (basicModFactor>>BASE_ACCURACY_MOVE) & 0x7;
        return 1f + 0.2f * (2<<(modlevel-1));
    }
    protected float EvasionModFactor(){
        int modlevel = (basicModFactor>>BASE_EVASION_MOVE) & 0x7;
        return 1f + 0.08f * (2<<(modlevel-1));
    }
    protected float MoveSpeedFactor(){
        int modlevel = (basicModFactor>>BASE_MOVE_SPEED_MOVE) & 0x7;
        return 1f + modlevel/12f + modlevel*modlevel / 84f;
    }
    protected float AttackSpeedFactor(){
        int modlevel = (basicModFactor>>BASE_ATTACK_SPEED_MOVE) & 0x7;
        return 1f + modlevel/14f + modlevel*modlevel / 98f;
    }

    protected int attackMod = 0;
    protected int defendMod = 0;
    protected int trickMod = 0;
    protected int resistMod = 0;

    private static final int RES_MELEE = 0;
    private static final int RES_MISSILE = 2;
    private static final int RES_MAGIC = 4;

    private static final int ATK_ARMOR_PIERCE = 0;
    private static final int ATK_BERSERK = 2;
    private static final int ATK_SUPPRESS = 4;
    private static final int ATK_COMBO = 6;
    private static final int ATK_ROOT = 8;
    private static final int ATK_CURSE = 10;
    private static final int ATK_FROZEN = 12;
    private static final int ATK_FIRE = 14;

    private static final int DEF_HIGH_DAMAGE = 0;
    private static final int DEF_LOW_DAMAGE = 2;
    private static final int DEF_DEFENSE_COPY = 4;
    private static final int DEF_COMBO_RESIST = 6;
    private static final int DEF_VAMPIRE = 8;
    private static final int DEF_PUSH_BACK = 10;
    private static final int DEF_PERIODIC_SHIELD = 12;
    private static final int DEF_NEGATIVE_IMMUNE = 14;

    private static final int TRK_RANGE = 0;
    private static final int TRK_DISAPPEAR = 2;
    private static final int TRK_DEGRADE = 4;
    private static final int TRK_ALERT = 6;
    private static final int TRK_SUMMON = 8;
    private static final int TRK_SCAN = 10;
    private static final int TRK_CHARGE_EATER = 12;
    private static final int TRK_THROW = 14;

    protected int maxSpecialTags(){
        if(level<5) return 1;
        else if(level<10) return 2;
        else return 3;
    }
    //We are much more offensive on distributing high-level perks to improve interest and difficulty, and provide richer reward in early run.
    protected void createSpecialModFactor(){

        int reslevel = Random.chances(new float[]{50f - level, 30f - level/2, 20f + level/2, 10f + level});
        switch(Random.Int(3)){
            case 0: resistMod += reslevel << RES_MELEE; break;
            case 1: resistMod += reslevel << RES_MISSILE; break;
            case 2: resistMod += reslevel << RES_MAGIC; break;
        }

        if(type != T_CHAOS) {
            int attackLevel = perkLevel();
            final int[] selected = new int[]{Random.Int(8), Random.Int(8), Random.Int(8)};
            int defenseLevel = perkLevel();
            int trickLevel = perkLevel();

            int another;
            switch (type) {
                case T_ATTACK:
                    do {
                        another = Random.Int(8);
                    } while (selected[0] == another);
                    attackMod += attackLevel << (2 * selected[0]);
                    if(maxSpecialTags()>1) attackMod += uniquePerkLevel() << (2*another);
                    if(maxSpecialTags()>2) defendMod += defenseLevel << (2 * selected[1]);

                    break;
                case T_DEFEND:
                    do {
                        another = Random.Int(8);
                    } while (selected[1] == another);
                    defendMod += uniquePerkLevel() << (2*another);
                    if(maxSpecialTags()>1) defendMod += defenseLevel << (2 * selected[1]);
                    if(maxSpecialTags()>2) trickMod += trickLevel << (2 * selected[2]);
                    break;
                case T_TRICK: default:
                    do {
                        another = Random.Int(8);
                    } while (selected[2] == another);
                    trickMod += trickLevel << (2 * selected[2]);
                    if(maxSpecialTags()>1) trickMod += uniquePerkLevel() << (2*another);
                    if(maxSpecialTags()>2) attackMod += attackLevel << (2 * selected[0]);
            }
        }else{
            final int total = maxSpecialTags();
            int modded = 0;
            for(int i=0;i<24;++i){
                if(Random.Float()<(float)(total-modded)/(24-i)){
                    int perkLevel = (Random.Int(3)>0)? perkLevel():uniquePerkLevel();
                    if(i<8){
                        attackMod += perkLevel <<(2*i);
                    }else if(i<16){
                        defendMod += perkLevel <<(2*i-16);
                    }else{
                        trickMod += perkLevel <<(2*i-32);
                    }
                    modded++;
                }
            }
        }
    }

    private int perkLevel(){
        return Random.chances(new float[]{60f - level, 45f - level / 2, 30f + level / 2, 20f + level});
    }

    private int uniquePerkLevel(){
        return Random.chances(new float[]{40f - level, 40f - level / 2, 40f + level / 2, 40f + level});
    }

    protected float berserkDamageFactor(){
        int modlevel = (attackMod>>ATK_BERSERK)&0x3;
        float lose = 1f-(float)this.HP/this.HT;
        return 1f+modlevel*lose*lose*0.45f;
    }

    protected float suppressDamageFactor(){
        int modlevel = (attackMod>>ATK_SUPPRESS)&0x3;
        float left = (float)this.HP/this.HT;
        return 1f+modlevel*left*left*0.27f;
    }
    //add damage = dr(enemy) * multiplier
    protected float highDefenseAddDamageMultiplier(){
        int modlevel = (attackMod>>ATK_ARMOR_PIERCE) &0x3;
        if(modlevel == 0){
            return 0f;
        }
        return 0.5f*modlevel-0.2f;
    }

    protected int attacked=0;
    protected float comboDamageFactor(){
        int modlevel = (attackMod>>ATK_COMBO)&0x3;
        return 1f+modlevel*attacked*0.1f;
    }

    protected void rootProc(Char enemy){
        int modlevel = (attackMod>>ATK_ROOT)&0x3;
        if(modlevel>0) {
            if (Random.Float() < 0.4f + 0.12f*modlevel) {
                Buff.prolong(enemy, Roots.class, 2 * modlevel);
            }
        }
    }

    protected void curseAttackProc(Char enemy){
        int modlevel = (attackMod>>ATK_CURSE)&0x3;
        if(modlevel>0) {
            if (Random.Int(5 - modlevel) == 0) {
                float duration = 2 << modlevel;
                Buff.prolong(enemy, Weakness.class, duration);
                Buff.prolong(enemy, Blindness.class, duration);
                Buff.prolong(enemy, Hex.class, duration);
                Buff.affect(enemy, Vulnerable.class, duration);
            }
        }
    }

    protected void freezingAttackProc(Char enemy){
        int modlevel = (attackMod>>ATK_FROZEN)&0x3;
        if(modlevel>0) {
            if (Random.Float() < 0.4f + 0.12f*modlevel) {
                float duration = modlevel * 2.5f - 1f;
                Buff.prolong(enemy, Chill.class, duration);
            }
        }
    }

    protected void fireAttackProc(Char enemy){
        int modlevel = (attackMod>>ATK_FIRE)&0x3;
        if(modlevel>0) {
            if(Random.Int(5-modlevel)==0) {
                Buff.affect(enemy, Burning.class);
            }
        }
    }

    protected float MeleeResistanceFactor() {
        int modlevel = (resistMod>>RES_MELEE) & 0x3;
        return 0.225f*modlevel;
    }

    protected float MissileResistanceFactor() {
        int modlevel = (resistMod>>RES_MISSILE)&0x3;
        return 0.3f*modlevel;
    }

    protected float MagicalResistanceFactor(){
        int modlevel = (resistMod>>RES_MAGIC)&0x3;
        return 0.3f*modlevel;
    }

    protected float defenseCopyFactor(){
        int modlevel = (defendMod>>DEF_DEFENSE_COPY)&0x3;
        return 0.5f*modlevel;
    }

    protected float ComboResistanceFactor(int combo){
        int modlevel = (defendMod>>DEF_COMBO_RESIST)&0x3;
        if(modlevel>0) {
            return 1f - modlevel / (3f * modlevel) * (1f - (float) Math.pow(0.75 - 0.05 * modlevel, combo));
        }
        return 1f;
    }

    protected int limitMaxDamage(int damage){
        int modlevel = (defendMod>>DEF_HIGH_DAMAGE)&0x3;
        if(modlevel <= 0){
            return damage;
        }
        int limit = Math.round((float)this.HT/(modlevel*2+1));
        if(damage > limit){
            return Math.round(0.2f*(damage-limit)+limit);
        }else{
            return damage;
        }
    }

    protected int limitMinDamage(int damage){
        int modlevel = (defendMod>>DEF_LOW_DAMAGE)&0x3;
        if(modlevel == 0){
            return damage;
        }
        int limit = Math.round(this.HT*(0.08f*modlevel-0.01f*modlevel*modlevel));
        if(damage<limit){
            return 1;
        }else{
            return damage;
        }
    }

    protected void vampireProc(int damage){
        int modlevel = (defendMod>>DEF_VAMPIRE)&0x3;
        if(modlevel>0){
            this.HP = Math.min(this.HT, Math.round(this.HP + damage*0.25f*modlevel));
            sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
        }
    }

    protected int pushBackProc(Char enemy, int damage){
        int modlevel = (defendMod>>DEF_PUSH_BACK)&0x3;
        if(modlevel>0){
            if(Random.Int(2)==0) {
                CustomUtils.throwChar(enemy, new Ballistica(enemy.pos, 2*enemy.pos - this.pos, Ballistica.MAGIC_BOLT), 1 + modlevel );
                damage = damage/5;
            }
        }
        return damage;
    }

    protected int shieldProc(int damage){
        int modlevel = (defendMod>>DEF_PERIODIC_SHIELD)&0x3;
        if(modlevel>0) {
            if (getHit % (6-modlevel) == 0) {
                return 1;
            }
        }
        return damage;
    }

    protected int attackRange(){
        int modlevel = (trickMod>>TRK_RANGE)&0x3;
        switch (modlevel){
            case 3: return 6;
            case 2: return 3;
            case 1: return 2;
            case 0: default: return 1;
        }
    }

    protected void disappearProc(){
        int modlevel = (trickMod>>TRK_DISAPPEAR)&0x3;
        if(modlevel>0){
            if(Random.Int(17-modlevel*4)==0){
                flying=false;
                new TeleportationTrap().set(this.pos).activate();
                flying=true;
            }
        }
    }

    protected void degradeProc(Char enemy){
        int modlevel = (trickMod>>TRK_DEGRADE)&0x3;
        if(modlevel>0){
            if(Random.Int(8)==0){
                float duration = (Random.Int(1000)==0)? 999999f : 1<<modlevel;
                Buff.prolong(enemy, Degrade.class, duration);
            }
        }
    }

    protected void alertProc(Char enemy){
        int modlevel = (trickMod>>TRK_ALERT)&0x3;
        if(modlevel>0){
            for(Mob m: Dungeon.level.mobs.toArray(new Mob[0])){
                if(m.alignment == Alignment.ENEMY) {
                    if (Random.Int(5 - modlevel) == 0) {
                        m.beckon(enemy.pos);
                    }
                }
            }
        }
    }

    protected void summonProc(){
        int modlevel = (trickMod>>TRK_SUMMON)&0x3;
        if(modlevel>0){
            int nMobs = modlevel;

            ArrayList<Integer> candidates = new ArrayList<>();

            for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                int p = this.pos + PathFinder.NEIGHBOURS8[i];
                if (Actor.findChar( p ) == null && (Dungeon.level.passable[p] || Dungeon.level.avoid[p])) {
                    candidates.add( p );
                }
            }

            ArrayList<Integer> respawnPoints = new ArrayList<>();

            while (nMobs > 0 && candidates.size() > 0) {
                int index = Random.index( candidates );

                respawnPoints.add( candidates.remove( index ) );
                nMobs--;
            }

            ArrayList<Mob> mobs = new ArrayList<>();

            for (Integer point : respawnPoints) {
                Mob mob = Dungeon.level.createMob();
                while (Char.hasProp(mob, Char.Property.LARGE) && !Dungeon.level.openSpace[point]){
                    mob = Dungeon.level.createMob();
                }
                if (mob != null) {
                    mob.state = mob.WANDERING;
                    mob.pos = point;
                    GameScene.add(mob, 2f);
                    mobs.add(mob);
                }
            }

            for (Mob mob : mobs){
                ScrollOfTeleportation.appear(mob, mob.pos);
                Dungeon.level.occupyCell(mob);
            }
        }
    }

    protected void scanProcess(){
        int modLevel = (trickMod>>TRK_SCAN)&0x3;
        if(modLevel>0){
            if(this.alignment == Alignment.ENEMY){
                if(Dungeon.level.distance(this.pos, Dungeon.hero.pos)<=3*modLevel-1){
                    Buff.detach(Dungeon.hero, Invisibility.class);
                }
            }
        }
    }

    protected void chargeEatProc(Char enemy){
        if(!(enemy instanceof Hero )) return;
        int modLevel = (trickMod>>TRK_CHARGE_EATER)&0x3;
        if(modLevel>0){
            for(Item i: ((Hero) enemy).belongings){
                if(i instanceof Wand){
                    ((Wand)i).curCharges=Math.max(((Wand)i).curCharges-modLevel, 0);
                }
            }
            Item.updateQuickslot();
        }
    }

    protected void throwProc(Char enemy){
        int modlevel = (trickMod>>TRK_THROW)&0x3;
        if(modlevel>0) {
            if(Random.Int(2)==0) {
                //trace a ballistica to our target (which will also extend past them
                Ballistica trajectory = new Ballistica(this.pos, enemy.pos, Ballistica.STOP_TARGET);
                //trim it to just be the part that goes past them
                trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
                CustomUtils.throwChar(enemy, trajectory, 2 * modlevel);
            }
        }
    }

    @Override
    public void setLevel(int level){
        this.level = level;
        createType();
        createBasicModFactor();
        createSpecialModFactor();
        adjustStats();
    }

    protected void adjustStats(){
        HP = HT = Math.round((6+level*6)*HealthModFactor());
        defenseSkill = Math.round((2 + level/2)*EvasionModFactor());
        baseSpeed = MoveSpeedFactor();
        enemySeen = true;
    }

    @Override
    protected boolean act(){
        scanProcess();
        return super.act();
    }

    @Override
    public void stopHiding(){

        summonProc();
        super.stopHiding();
    }

    @Override
    protected boolean canAttack( Char enemy ){
        int modlevel = (trickMod>>TRK_RANGE)&0x3;
        if(modlevel>0){
            //can see, and in range
            return ((Dungeon.level.distance(this.pos, enemy.pos)<=attackRange()) && this.fieldOfView[enemy.pos])
                    || super.canAttack(enemy);
        }
        return super.canAttack(enemy);
    }

    @Override
    public float attackDelay(){
        return super.attackDelay()/AttackSpeedFactor();
    }

    @Override
    public int attackSkill( Char target ) {
        if (target != null && alignment == Alignment.NEUTRAL && target.invisible <= 0){
            return INFINITE_ACCURACY;
        } else {
            return Math.round((6 + level*1.2f)*AccuracyModFactor());
        }
    }
    @Override
    public int damageRoll() {
        float base = (alignment == Alignment.NEUTRAL? Random.NormalFloat( 2 + 2*level, 3 + 9*level/4) : Random.NormalFloat( 1 + level, 3 + 9*level/4));
        base = base * AttackModFactor() * berserkDamageFactor() * suppressDamageFactor() * comboDamageFactor();
        return Math.round(base);
    }
    //NO dr, for we have resistance arguments and defensive perks.
    @Override
    public int drRoll() {
        return 0;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc( enemy, damage );
        int dr  = enemy.drRoll();
        int postDamage = damage;
        postDamage += Math.round(dr*highDefenseAddDamageMultiplier());

        curseAttackProc(enemy);

        freezingAttackProc(enemy);

        rootProc(enemy);

        fireAttackProc(enemy);

        degradeProc(enemy);

        vampireProc(damage);

        chargeEatProc(enemy);

        throwProc(enemy);

        attacked++;

        return super.attackProc(enemy, postDamage);
    }
    @Override
    public int defenseProc(Char enemy, int damage) {

        damage -= Math.round(enemy.drRoll()*defenseCopyFactor());
        if(damage<0) damage = 0;

        float meleeRes = MeleeResistanceFactor();
        float missileRes = MissileResistanceFactor();

        if(enemy instanceof Hero){
            if(((Hero) enemy).belongings.weapon instanceof MeleeWeapon) {
                damage = Math.round(damage * (1f - meleeRes));
            }else if(((Hero) enemy).belongings.weapon instanceof MissileWeapon || ((Hero) enemy).belongings.weapon instanceof SpiritBow.SpiritArrow){
                damage = Math.round(damage * (1f - missileRes));
            }
        }

        return super.defenseProc(enemy,damage);
    }
    //count total times of damage it has taken
    protected int getHit = 0;
    @Override
    public void damage(int dmg, Object src) {
        if (state == PASSIVE){
            alignment = Alignment.ENEMY;
            stopHiding();
        }
        dmg=shieldProc(dmg);
        float magicalRes = MagicalResistanceFactor();
        if(src instanceof Wand || src instanceof Buff || src instanceof Blob || src instanceof Scroll || src instanceof Stone){
            dmg = Math.round(dmg * (1f - magicalRes));
        }

        dmg = Math.round(dmg*ComboResistanceFactor(getHit));
        dmg = limitMinDamage(dmg);
        dmg = limitMaxDamage(dmg);
        if(dmg<0) dmg = 0;
        if(!(src instanceof Buff || src instanceof Blob)){
            getHit++;
        }
        if(dmg>1) disappearProc();
        if(dmg>1) alertProc(enemy);
        dmg=pushBackProc(enemy, dmg);
        super.damage(dmg, src);
    }

    @Override
    public synchronized void add( Buff buff ) {
        if(buff.type== Buff.buffType.NEGATIVE) {
            int modlevel = (defendMod >> DEF_NEGATIVE_IMMUNE) & 0x3;
            if (modlevel > 0) {
                if (Random.Int(4 - modlevel) == 0  || Random.Int(5-modlevel) == 0) {
                    return;
                }
            }
        }
        super.add(buff);
    }

    protected float basicModPower(int level){
        if(level>0 && level<8){
            return 1f + level/14f + level*level / 98f;
        }
        return 1f;
    }

    protected float specialModPower(int level){
        switch (level){
            case 0: default: return 1f;
            case 1: return 1.25f;
            case 2: return 1.6f;
            case 3: return 2f;
        }
    }

   public float showPower() {
       float power = 1.1f;//base power of flying
        for(int i=0;i<6;++i){
            power *= basicModPower((basicModFactor>>(3*i))&0x7);
        }
        int lvl;
       for(int i=0; i<3; ++i){
           lvl = (resistMod>>(2*i))&0x3;
           power*=specialModPower(lvl);
       }

       for(int i=0; i<8; ++i){
           lvl = (attackMod>>(2*i))&0x3;
           power*=specialModPower(lvl);
       }

       for(int i=0; i<8; ++i){
           lvl = (defendMod>>(2*i))&0x3;
           power*=specialModPower(lvl);
       }

       for(int i=0; i<8; ++i){
           lvl = (trickMod>>(2*i))&0x3;
           power*=specialModPower(lvl);
       }
       return power;
   }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( LEVEL, level );
        bundle.put( BASIC_MOD_FACTOR, basicModFactor);
        bundle.put( TYPE_FACTOR, type);
        bundle.put( ATTACK_FACTOR, attackMod);
        bundle.put( DEFENSE_FACTOR, defendMod);
        bundle.put( TRICK_FACTOR, trickMod);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        level = bundle.getFloat( LEVEL );
        basicModFactor = bundle.getInt(BASIC_MOD_FACTOR);
        type = bundle.getInt(TYPE_FACTOR);
        attackMod = bundle.getInt(ATTACK_FACTOR);
        defendMod = bundle.getInt(DEFENSE_FACTOR);
        trickMod = bundle.getInt(TRICK_FACTOR);
        adjustStats();
        super.restoreFromBundle(bundle);
    }

    @Override
    protected void generatePrize(){
        Item reward = null;
        float power = showPower();
        //power = Math.min(9 + level/2, power);
        if(power<2.3f){
            if(Random.Int(4)<3){
                do {
                    reward = (Random.Int(3)!=0)?
                            ((Random.Int(2)==0)?    (Generator.random(Generator.Category.POTION))   :   (Generator.random(Generator.Category.SCROLL)))
                            :(new Gold().random());
                } while (reward == null || Challenges.isItemBlocked(reward));
                if(!(reward instanceof Gold)) reward.quantity(1);
                items.add(reward);
            }
        }
        else if(power <4.6f){
            if(Random.Int(6)<5){
                do {
                    reward = (Random.Int(3)!=0)?
                            ((Random.Int(2)==0)?    (Generator.random(Generator.Category.POTION))   :   (Generator.random(Generator.Category.SCROLL)))
                            :(new Gold().random());
                } while (reward == null || Challenges.isItemBlocked(reward));
                if(!(reward instanceof Gold)) reward.quantity(1);
                items.add(reward);
            }
            if(Random.Int(6)<2){
                do {
                    reward = (Random.Int(2)==0)?    (Generator.random(Generator.Category.POTION))   :   (Generator.random(Generator.Category.SCROLL));
                } while (reward == null || Challenges.isItemBlocked(reward));
                reward.quantity(1);
                items.add(reward);
            }
            if(Random.Int(4)==0) {
                do {
                    switch(Random.Int(4)){
                        case 1: case 2:
                            reward = Generator.randomMissile().quantity(Random.Int(1,4));
                            break;
                        case 3:
                            reward = Generator.random(Generator.Category.SEED);
                            break;
                        case 0:
                            reward =  Generator.random(Generator.Category.STONE);
                            break;
                    }
                } while (reward == null || Challenges.isItemBlocked(reward));
                items.add(reward);
            }
        }else if(power < 7.9f){
            do{
                reward = (Random.Int(2)==0)?    (Generator.random(Generator.Category.POTION))   :   (Generator.random(Generator.Category.SCROLL));
            }while (reward == null || Challenges.isItemBlocked(reward));

            if(Random.Int(3)==0) reward.quantity(2);
            items.add(reward);

            if(Random.Int(3)==0) {
                do {
                    switch(Random.Int(3)){
                        case 1:
                            reward = Generator.randomMissile().quantity(Random.Int(1,4));
                            break;
                        case 2:
                            reward = Generator.random(Generator.Category.SEED);
                            break;
                        case 0:
                            reward =  Generator.random(Generator.Category.STONE);
                            break;
                    }
                } while (reward == null || Challenges.isItemBlocked(reward));
                items.add(reward);
            }
            if(Random.Int(5)==0){
                do{
                    switch(Random.Int(4)){
                        case 0:
                            reward = Generator.randomArmor();
                            break;
                        case 1:
                            reward = Generator.randomWeapon();
                            break;
                        case 2:
                            reward = Generator.random(Generator.Category.RING);
                            break;
                        case 3:
                            reward = Generator.random(Generator.Category.WAND);
                    }
                }while (reward == null || Challenges.isItemBlocked(reward));
                if(reward.isUpgradable()){
                    reward.upgrade();
                    if(Random.Int(8)==0){
                        reward.upgrade();
                    }
                    if(Random.Int(8)==0){
                        reward.upgrade();
                    }
                }
                items.add(reward);
            }
        }else{
            do {
                switch(Random.Int(2)){
                    case 1:
                        reward = Generator.random(Generator.Category.SEED);
                        break;
                    case 0: default:
                        reward =  Generator.random(Generator.Category.STONE);
                        break;
                }
            } while (reward == null || Challenges.isItemBlocked(reward));
            reward.quantity(2);
            items.add(reward);

            if(Random.Int(4)==0) {reward = new StoneOfEnchantment();reward.quantity(1);items.add(reward);}
            if(Random.Int(4)==0) {reward = new ScrollOfTransmutation();reward.quantity(1);items.add(reward);}
            if(Random.Int(4)==0) {reward = new PotionOfExperience();reward.quantity(1);items.add(reward);}
            if(Random.Int(6)==0){
                do{
                    switch(Random.Int(3)){
                        case 0:
                            reward = Generator.randomWeapon();
                            break;
                        case 1:
                            reward = Generator.random(Generator.Category.RING);
                            break;
                        case 2:
                            reward = Generator.random(Generator.Category.WAND);
                    }
                }while (reward == null || Challenges.isItemBlocked(reward));
                if(reward.isUpgradable()) {
                    reward.cursed = false;
                    if (power > 12.8f) {
                        reward.level(Random.Int(4, 7));
                    } else if (power > 9.9f) {
                        reward.level(Random.Int(3, 6));
                    } else {
                        reward.level(Random.Int(2, 5));
                    }
                }
                items.add(reward);
            }
        }
    }

    @Override
    public String description(){
        return super.description() + ModifierDesc();
    }

    protected String ModifierDesc(){
        StringBuilder desc = new StringBuilder("\n");

        int lvl;
        lvl = (basicModFactor>>BASE_HEALTH_MOVE)&0x7;
        if(lvl>0) desc.append(Messages.get(this, "health", lvl));
        lvl = (basicModFactor>>BASE_ATTACK_MOVE)&0x7;
        if(lvl>0) desc.append(Messages.get(this, "attack", lvl));
        lvl = (basicModFactor>>BASE_ACCURACY_MOVE)&0x7;
        if(lvl>0) desc.append(Messages.get(this, "accuracy", lvl));
        lvl = (basicModFactor>>BASE_EVASION_MOVE)&0x7;
        if(lvl>0) desc.append(Messages.get(this, "evasion", lvl));
        lvl = (basicModFactor>>BASE_MOVE_SPEED_MOVE)&0x7;
        if(lvl>0) desc.append(Messages.get(this, "move_speed", lvl));
        lvl = (basicModFactor>>BASE_ATTACK_SPEED_MOVE)&0x7;
        if(lvl>0) desc.append(Messages.get(this, "attack_speed", lvl));

        for(int i=0; i<3; ++i){
            lvl = (resistMod>>(2*i))&0x3;
            if(lvl>0) desc.append(Messages.get(this, "resist_"+ (i + 1), lvl));
        }

        for(int i=0; i<8; ++i){
            lvl = (attackMod>>(2*i))&0x3;
            if(lvl>0) desc.append(Messages.get(this, "attack_"+(i + 1), lvl));
        }

        for(int i=0; i<8; ++i){
            lvl = (defendMod>>(2*i))&0x3;
            if(lvl>0) desc.append(Messages.get(this, "defend_"+(i + 1), lvl));
        }

        for(int i=0; i<8; ++i){
            lvl = (trickMod>>(2*i))&0x3;
            if(lvl>0) desc.append(Messages.get(this, "trick_"+(i + 1), lvl));
        }


        return desc.toString();
    }
}