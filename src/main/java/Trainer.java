import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class Trainer {
  private String name;
  private String email;
  private int id;

  public Trainer(String name, String email) {
    this.name = name;
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public int getId() {
    return id;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO trainers (name, email) VALUES (:name, :email)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("email", this.email)
        .executeUpdate()
        .getKey();
    }
  }

  @Override
  public boolean equals(Object otherTrainer){
    if (!(otherTrainer instanceof Trainer)) {
      return false;
    } else {
      Trainer newTrainer = (Trainer) otherTrainer;
      return this.getName().equals(newTrainer.getName()) &&
             this.getEmail().equals(newTrainer.getEmail());
    }
  }

  public static List<Trainer> all() {
  String sql = "SELECT * FROM trainers";
  try(Connection con = DB.sql2o.open()) {
   return con.createQuery(sql).executeAndFetch(Trainer.class);
    }
  }

  public static Trainer find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM trainers where id=:id";
      Trainer trainer = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Trainer.class);
      return trainer;
    }
  }

  public List<Monster> getMonsters() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM monsters where trainerId=:id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Monster.class);
    }
  }
}
