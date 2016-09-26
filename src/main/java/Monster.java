import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;

public class Monster {
  private String name;
  private String type;
  private int monsterLevel;
  private int hungerLevel;
  private int healthLevel;
  private int trainerId;
  private int id;

  public static final int MAX_MONSTER_LEVEL = 100;
  public static final int MAX_HUNGER_LEVEL = 10;
  public static final int MAX_HEALTH_LEVEL = 100;
  public static final int MIN_ALL_LEVELS = 0;

  public Monster(String name, String type, int trainerId) {
    this.name = name;
    this.type = type;
    this.monsterLevel = MAX_MONSTER_LEVEL / 2;
    this.hungerLevel = MAX_HUNGER_LEVEL / 2;
    this.healthLevel = MAX_HEALTH_LEVEL;
    this.trainerId = trainerId;

  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public int getTrainerId() {
    return trainerId;
  }

  public int getId() {
    return id;
  }

  public int getMonsterLevel() {
    return monsterLevel;
  }

  public int getHungerLevel() {
    return hungerLevel;
  }

  public int getHealthLevel() {
    return healthLevel;
  }

  @Override
  public boolean equals(Object otherMonster){
    if (!(otherMonster instanceof Monster)) {
      return false;
    } else {
      Monster newMonster = (Monster) otherMonster;
      return this.getName().equals(newMonster.getName()) &&
        this.getType().equals(newMonster.getType()) &&
        this.getTrainerId() == newMonster.getTrainerId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO monsters (name, type, trainerid) VALUES (:name, :type, :trainerId)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("type", this.type)
        .addParameter("trainerId", this.trainerId)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Monster> all() {
    String sql = "SELECT * FROM monsters";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Monster.class);
    }
  }

  public static Monster find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM monsters where id=:id";
      Monster monster = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Monster.class);
      return monster;
    }
  }

  public boolean isAlive() {
    if (hungerLevel <= MIN_ALL_LEVELS ||
    monsterLevel <= MIN_ALL_LEVELS ||
    healthLevel  <= MIN_ALL_LEVELS) {
      return false;
    }
    return true;
  }

  public void depleteLevels() {
    hungerLevel--;
    healthLevel--;
  }

  public void train() {
    if (monsterLevel >= MAX_MONSTER_LEVEL) {
      throw new UnsupportedOperationException("You cannot level up your monster anymore!");
    }
    monsterLevel++;
  }

  public void feed() {
    if (hungerLevel >= MAX_HUNGER_LEVEL) {
      throw new UnsupportedOperationException("You cannot feed your monster anymore!");
    }
    hungerLevel++;
  }

  public void medicate() {
    if (healthLevel >= MAX_HEALTH_LEVEL) {
      throw new UnsupportedOperationException("You cannot heal this monster anymore!");
    }
    healthLevel++;
  }

  public void battle() {
    healthLevel-=50;
  }
}
