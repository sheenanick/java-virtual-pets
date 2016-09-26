import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class TrainerTest {


  @Rule
  public DatabaseRule database = new DatabaseRule();



  @Test
  public void trainer_instantiatesCorrectly_true() {
    Trainer testTrainer = new Trainer("Henry", "henry@henry.com");
    assertEquals(true, testTrainer instanceof Trainer);
  }

  @Test
  public void getName_trainerInstantiatesWithName_Henry() {
    Trainer testTrainer = new Trainer("Henry", "henry@henry.com");
    assertEquals("Henry", testTrainer.getName());
  }

  @Test
  public void getName_trainerInstantiatesWithEmail_String() {
    Trainer testTrainer = new Trainer("Henry", "henry@henry.com");
    assertEquals("henry@henry.com", testTrainer.getEmail());
  }

  @Test
  public void equals_returnsTrueIfNameAndEmailAreSame_true() {
    Trainer firstTrainer = new Trainer("Henry", "henry@henry.com");
    Trainer anotherTrainer = new Trainer("Henry", "henry@henry.com");
    assertTrue(firstTrainer.equals(anotherTrainer));
  }

  @Test
  public void save_insertsObjectIntoDatabase_Trainer() {
    Trainer testTrainer = new Trainer("Henry", "henry@henry.com");
    testTrainer.save();
    assertTrue(Trainer.all().get(0).equals(testTrainer));
  }

  @Test
  public void all_returnsAllInstancesOfTrainer_true() {
    Trainer firstTrainer = new Trainer("Henry", "henry@henry.com");
    firstTrainer.save();
    Trainer secondTrainer = new Trainer("Harriet", "harriet@harriet.com");
    secondTrainer.save();
    assertEquals(true, Trainer.all().get(0).equals(firstTrainer));
    assertEquals(true, Trainer.all().get(1).equals(secondTrainer));
  }

  @Test
  public void save_assignsIdToObject() {
    Trainer testTrainer = new Trainer("Henry", "henry@henry.com");
    testTrainer.save();
    Trainer savedTrainer = Trainer.all().get(0);
    assertEquals(testTrainer.getId(), savedTrainer.getId());
  }

  @Test
  public void find_returnsTrainerWithSameId_secondTrainer() {
    Trainer firstTrainer = new Trainer("Henry", "henry@henry.com");
    firstTrainer.save();
    Trainer secondTrainer = new Trainer("Harriet", "harriet@harriet.com");
    secondTrainer.save();
    assertEquals(Trainer.find(secondTrainer.getId()), secondTrainer);
  }

  @Test
  public void getMonsters_retrievesAllMonstersFromDatabase_monstersList() {
    Trainer testTrainer = new Trainer("Henry", "henry@henry.com");
    testTrainer.save();
    Monster firstMonster = new Monster("Squirtle", "Water", testTrainer.getId());
    firstMonster.save();
    Monster secondMonster = new Monster("Pikachu", "Elecric", testTrainer.getId());
    secondMonster.save();
    Monster[] monsters = new Monster[] { firstMonster, secondMonster };
    assertTrue(testTrainer.getMonsters().containsAll(Arrays.asList(monsters)));
  }

}
