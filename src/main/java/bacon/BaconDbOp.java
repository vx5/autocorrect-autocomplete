package bacon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;

import dijkstra.DijkstraDbOp;

public class BaconDbOp implements DijkstraDbOp<ActorVertex, FilmEdge> {
  // Stores connection to be used to access database
  private Connection conn;

  public BaconDbOp() {
    conn = null;
  }

  public boolean hasDb() {
    return conn != null;
  }

  public void setSqlDb(String dbPath)
      throws ClassNotFoundException, SQLException {
    // NOTE: this section is taken from the Databases lab
    // this line loads the driver manager class, and must be
    // present for everything else to work properly
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + dbPath;
    conn = DriverManager.getConnection(urlToDB);
    // these two lines tell the database to enforce foreign
    // keys during operations, and should be present
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys = ON;");
  }

  @Override
  public boolean validNeighbors(ActorVertex from, ActorVertex to) {
    // Bacon's criteria for valid neighbors is that the last initial of the from
    // node (donor)'s name is the same as the first initial of the to node
    // (recipient)'s name
    String[] nameArray = from.getName().split(" ");
    String lastName = nameArray[nameArray.length - 1];
    char donorRelChar = lastName.charAt(0);
    // Get the first initial of the recipient
    char recipientRelChar = to.getName().charAt(0);
    // Check for character equality
    return donorRelChar == recipientRelChar;
  }

  @Override
  public void giveNeighbors(ActorVertex origin) throws Exception {
    String actorId = origin.getId();
    HashSet<String> filmIds = actorIdToFilmIds(actorId);
    Iterator<String> i = filmIds.iterator();
    while (i.hasNext()) {
      String filmId = i.next();
      HashSet<String> actorIds = filmIdToActorIds(filmId);
      // Identifies film's name
      String filmName = filmIdToName(filmId);
      // For each film id, get all the actor IDs
      float plusDist = 1 / (float) actorIds.size();
      Iterator<String> j = actorIds.iterator();
      while (j.hasNext()) {
        // Creates new Edge
        FilmEdge newEdge = new FilmEdge(filmName, filmId, plusDist, origin);
        // Stores neighbor actor ID
        String neighborId = j.next();
        // If the received ID is the same as the origins, move on
        if (neighborId.contentEquals(origin.getId())) {
          continue;
        }
        // Creates new Vertex
        String neighborName = actorIdToName(neighborId);
        ActorVertex newVert = new ActorVertex(neighborName, neighborId,
            origin.getDist() + newEdge.getWeight());
        // Adds vertex to given edge
        newEdge.setTail(newVert);
        // Adds edge as previous of vertex
        newVert.setPrevEdge(newEdge);
        // Adds edge to vertex
        origin.addEdge(newEdge);
      }
    }
  }

  public String actorNameToId(String name) throws SQLException {
    // Gets actor's ID
    PreparedStatement prep = conn
        .prepareStatement("SELECT * FROM actor WHERE name=?;");
    prep.setString(1, name);
    ResultSet rs = prep.executeQuery();
    // Actor was not found
    if (!rs.next()) {
      throw new SQLException(
          "actor with name \"" + name + "\" could not be found");
    }
    String actorId = rs.getString(1);
    rs.close();
    prep.close();
    return actorId;
  }

  public String actorIdToName(String id) throws SQLException {
    PreparedStatement prep = conn
        .prepareStatement("SELECT * FROM actor WHERE id=?;");
    prep.setString(1, id);
    ResultSet rs = prep.executeQuery();
    // Id was not found
    if (!rs.next()) {
      throw new SQLException("actor with ID \"" + id + "\" could not be found");
    }
    String name = rs.getString(2);
    rs.close();
    prep.close();
    return name;
  }

  public HashSet<String> actorIdToFilmIds(String actorId) throws SQLException {
    // Get all the film IDs that that actor was in
    PreparedStatement prep = conn
        .prepareStatement("SELECT * FROM actor_film WHERE actor=?;");
    prep.setString(1, actorId);
    ResultSet rs = prep.executeQuery();
    HashSet<String> filmIds = new HashSet<String>();
    while (rs.next()) {
      filmIds.add(rs.getString(2));
    }
    rs.close();
    prep.close();
    return filmIds;
  }

  public HashSet<String> filmIdToActorIds(String filmId) throws SQLException {
    PreparedStatement prep = conn
        .prepareStatement("SELECT * FROM actor_film WHERE film=?;");
    prep.setString(1, filmId);
    ResultSet rs = prep.executeQuery();
    HashSet<String> actorIds = new HashSet<String>();
    while (rs.next()) {
      actorIds.add(rs.getString(1));
    }
    rs.close();
    prep.close();
    return actorIds;
  }

  private String filmNameToId(String name) throws SQLException {
    PreparedStatement prep = conn
        .prepareStatement("SELECT * FROM film WHERE name=?;");
    prep.setString(1, name);
    ResultSet rs = prep.executeQuery();
    rs.next();
    String filmId = rs.getString(1);
    rs.close();
    prep.close();
    return filmId;
  }

  public String filmIdToName(String id) throws SQLException {
    PreparedStatement prep = conn
        .prepareStatement("SELECT * FROM film WHERE id=?;");
    prep.setString(1, id);
    ResultSet rs = prep.executeQuery();
    rs.next();
    String filmName = rs.getString(2);
    rs.close();
    prep.close();
    return filmName;
  }

  @Override
  public ActorVertex makeVertex(String id, float dist) {
    try {
      String name = actorIdToName(id);
      return new ActorVertex(name, id, dist);
    } catch (SQLException e) {
      return null;
    }
  }

}
