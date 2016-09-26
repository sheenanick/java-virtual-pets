import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;

public class MonsterTest {
  private Monster testMonster;

  @Before
  public void intialize() {
    testMonster = new Monster("Squirtle", "Water", 1);
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void monster_instantiatesCorrectly_true() {
    assertEquals(true, testMonster instanceof Monster);
  }

  @Test
  public void Monster_instantiatesWithName_String() {
    assertEquals("Squirtle", testMonster.getName());
  }

  @Test
  public void Monster_instantiatesWithType_String() {
    assertEquals("Water", testMonster.getType());
  }

  @Test
  public void MOnster_instantiesWithTrainerId_int() {
    assertEquals(1, testMonster.getTrainerId());
  }

  @Test
  public void save_returnsTrueIfDescriptionsAretheSame() {
    testMonster.save();
    Monster savedMonster = Monster.all().get(0);
    assertEquals(savedMonster.getId(), testMonster.getId());
  }

  @Test
  public void find_returnsMonsterWithSameId_secondMonster() {
    testMonster.save();
    Monster secondMonster = new Monster("Pikachu", "Electric", 3);
    secondMonster.save();
    assertEquals(Monster.find(secondMonster.getId()), secondMonster);
  }

  @Test
  public void save_savesTrainerIdIntoDB_true() {
    Trainer testTrainer = new Trainer("Henry", "henry@henry.com");
    testTrainer.save();
    Monster testMonsterTwo = new Monster("Squirtle", "Water", testTrainer.getId());
    testMonsterTwo.save();
    Monster savedMonster = Monster.find(testMonsterTwo.getId());
    assertEquals(savedMonster.getTrainerId(), testTrainer.getId());
  }

  @Test
  public void monster_instantiatesWithHalfFullPlayLevel() {
    assertEquals(testMonster.getMonsterLevel(), (Monster.MAX_MONSTER_LEVEL / 2));
  }

  @Test
  public void monster_instantiatesWithFullHungerLevel() {
    assertEquals(testMonster.getHungerLevel(), (Monster.MAX_HUNGER_LEVEL / 2));
  }

  @Test
  public void monster_instantiatesWithFullHealthLevel() {
    assertEquals(testMonster.getHealthLevel(), (Monster.MAX_HEALTH_LEVEL));
  }

  @Test
  public void isAlive_confirmsMOnsterIsAliveIfAllLevelsAboveMinimum_true() {
    assertEquals(testMonster.isAlive(), true);
  }

  @Test
  public void depleteLevels_reducesHungerAndHealthLevels() {
    testMonster.depleteLevels();
    assertEquals(testMonster.getHungerLevel(), (Monster.MAX_HUNGER_LEVEL / 2) - 1);
    assertEquals(testMonster.getHealthLevel(), (Monster.MAX_HEALTH_LEVEL) - 1);
  }

  @Test
  public void battle_reducesHealthLevel() {
    testMonster.battle();
    assertEquals(testMonster.getHealthLevel(), (Monster.MAX_HEALTH_LEVEL) - 50);
  }

  @Test
  public void isAlive_recognizesMonsterIsDeadWhenLevelsReachMinimum_false() {
    for(int i = Monster.MIN_ALL_LEVELS; i <= Monster.MAX_HEALTH_LEVEL; i++) {
      testMonster.depleteLevels();
    }
    assertEquals(testMonster.isAlive(), false);
  }

  @Test
  public void train_increasesMonsterLevel() {
    testMonster.train();
    assertTrue(testMonster.getMonsterLevel() > (Monster.MAX_MONSTER_LEVEL / 2));
  }

  @Test
  public void feed_increasesMonsterHungerLevel() {
    testMonster.feed();
    assertTrue(testMonster.getHungerLevel() > (Monster.MAX_HUNGER_LEVEL / 2));
  }

  @Test
  public void medicate_increasesMonsterHealthLevel() {
    testMonster.battle();
    testMonster.medicate();
    assertEquals(51, testMonster.getHealthLevel());
  }

  @Test
  public void monster_hungerLevelCannotGoBeyondMaxValue() {
    for(int i = Monster.MIN_ALL_LEVELS; i <= (Monster.MAX_HUNGER_LEVEL + 2); i++) {
      try {
        testMonster.feed();
      } catch (UnsupportedOperationException exception) { }
    }
    assertTrue(testMonster.getHungerLevel() <= Monster.MAX_HUNGER_LEVEL);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void feed_throwsExceptionsIfHungerLevelIsAtMaxValue() {
    for(int i = Monster.MIN_ALL_LEVELS; i <= (Monster.MAX_HUNGER_LEVEL); i++) {
      testMonster.feed();
    }
  }

  @Test
  public void monster_healthLevelCannotGoBeyondMaxValue() {
    testMonster.battle();
    for(int i = Monster.MIN_ALL_LEVELS; i <= (Monster.MAX_HEALTH_LEVEL); i++) {
      try {
        testMonster.medicate();
      } catch (UnsupportedOperationException exception){ }
    }
    assertTrue(testMonster.getHealthLevel() <= Monster.MAX_HEALTH_LEVEL);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void medicate_throwsExceptionsIfHealthLevelIsAtMaxValue() {
    for(int i = Monster.MIN_ALL_LEVELS; i <= (Monster.MAX_HEALTH_LEVEL); i++) {
      testMonster.medicate();
    }
  }

  @Test
  public void monster_monsterLevelCannotGoBeyondMaxValue() {
    for(int i = Monster.MIN_ALL_LEVELS; i <= (Monster.MAX_MONSTER_LEVEL); i++) {
      try {
        testMonster.train();
      } catch (UnsupportedOperationException exception){ }
    }
    assertTrue(testMonster.getMonsterLevel() <= Monster.MAX_MONSTER_LEVEL);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void train_throwsExceptionsIfMonsterLevelIsAtMaxValue() {
    for(int i = Monster.MIN_ALL_LEVELS; i <= (Monster.MAX_MONSTER_LEVEL); i++) {
      testMonster.train();
    }
  }
}
