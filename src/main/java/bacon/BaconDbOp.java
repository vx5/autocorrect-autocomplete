package bacon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import dijkstra.DijkstraDbOp;

/**
 * @author vx5
 *
 *         Class that handles point for all interactions with the SQLite
 *         databases used for this project, including interactions required for
 *         Dijkstra's algorithm processing.
 */
public class BaconDbOp implements DijkstraDbOp<ActorVertex, FilmEdge> {
  // Stores connection to be used to access database
  private Connection conn;
  // Stores Cache for each unique, repeated database access
  private Cache<String, String> actorNameIdCache;
  private Cache<String, String> actorIdNameCache;
  private Cache<String, HashSet<String>> actorIdFilmIdsCache;
  private Cache<String, HashSet<String>> filmIdActorIdsCache;
  private Cache<String, String> filmIdNameCache;
  // Stores default settings for Caches
  private final int maxCacheSize = 500;
  private final int cacheMinutes = 3;

  /**
   * Constructor that initializes connection to SQL database, and all Caches.
   */
  public BaconDbOp() {
    // Initializes connection as null
    conn = null;
    // Initializes all caches
    actorNameIdCache = CacheBuilder.newBuilder().maximumSize(maxCacheSize)
        .expireAfterWrite(cacheMinutes, TimeUnit.MINUTES).build();
    actorIdNameCache = CacheBuilder.newBuilder().maximumSize(maxCacheSize)
        .expireAfterWrite(cacheMinutes, TimeUnit.MINUTES).build();
    actorIdFilmIdsCache = CacheBuilder.newBuilder().maximumSize(maxCacheSize)
        .expireAfterWrite(cacheMinutes, TimeUnit.MINUTES).build();
    filmIdActorIdsCache = CacheBuilder.newBuilder().maximumSize(maxCacheSize)
        .expireAfterWrite(cacheMinutes, TimeUnit.MINUTES).build();
    filmIdNameCache = CacheBuilder.newBuilder().maximumSize(maxCacheSize)
        .expireAfterWrite(cacheMinutes, TimeUnit.MINUTES).build();
  }

  /**
   * Returns whether a database has been loaded so far.
   *
   * @return true if a database has been loaded, false if not
   */
  public boolean hasDb() {
    return conn != null;
  }

  /**
   * Sets the appropriate SQL database given a file path to it.
   *
   * @param dbPath file path to SQL database
   * @throws ClassNotFoundException if there is error in making JDBC setting
   * @throws SQLException           if problem with SQL setup occurs
   */
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
    // Check for case of missing name (database cleaning error)
    if (from.getName().length() == 0 || to.getName().length() == 0) {
      return false;
    }
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

  /**
   * Uses the database to convert an actor's name to their id.
   *
   * @param name actor's name to be used
   * @return String for of actor's id
   * @throws SQLException if there is error in interactions with SQL database
   */
  public String actorNameToId(String name) throws SQLException {
    // Checks whether value is cached, uses if possible
    String cachedId = actorNameIdCache.getIfPresent(name);
    if (cachedId != null) {
      return cachedId;
    }
    // If not, then proceed with SQL request
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
    // Obtains actor's id
    String actorId = rs.getString(1);
    // Cache the result if new
    actorNameIdCache.put(name, actorId);
    // Close all SQL interactions and return id
    rs.close();
    prep.close();
    return actorId;
  }

  /**
   * Uses the database to convert an actor's id to their name.
   *
   * @param id String of id to be converted
   * @return String form of relevant actor's name
   * @throws SQLException if there is error in interactions with the database
   */
  public String actorIdToName(String id) throws SQLException {
    // Checks whether value is cached, access if possible
    String cachedName = actorIdNameCache.getIfPresent(id);
    if (cachedName != null) {
      return cachedName;
    }
    // If not, then proceed with SQL request
    PreparedStatement prep = conn
        .prepareStatement("SELECT * FROM actor WHERE id=?;");
    prep.setString(1, id);
    ResultSet rs = prep.executeQuery();
    // Id was not found
    if (!rs.next()) {
      throw new SQLException("actor with ID \"" + id + "\" could not be found");
    }
    String name = rs.getString(2);
    // Stores new request result's in cache
    actorIdNameCache.put(id, name);
    // Close all SQL interactions and return name
    rs.close();
    prep.close();
    return name;
  }

  /**
   * Uses database to return all films (by id) that an actor (by id) was in.
   *
   * @param actorId id of actor to be used for basis of search
   * @return HashSet of the ids of films that the actor was in
   * @throws SQLException if there is error in interactions with the database
   */
  public HashSet<String> actorIdToFilmIds(String actorId) throws SQLException {
    // Checks whether value is cached, access if necessary
    HashSet<String> cachedIds = actorIdFilmIdsCache.getIfPresent(actorId);
    if (cachedIds != null) {
      return cachedIds;
    }
    // Get all the film IDs that that actor was in
    PreparedStatement prep = conn
        .prepareStatement("SELECT * FROM actor_film WHERE actor=?;");
    prep.setString(1, actorId);
    ResultSet rs = prep.executeQuery();
    HashSet<String> filmIds = new HashSet<String>();
    while (rs.next()) {
      filmIds.add(rs.getString(2));
    }
    // Store new found value in cache
    actorIdFilmIdsCache.put(actorId, filmIds);
    // Close all SQL interactions and return ids
    rs.close();
    prep.close();
    return filmIds;
  }

  /**
   * Uses database to find all actors (by id) who were in a given film (by id).
   *
   * @param filmId id of film to be used as basis of search
   * @return HashSet of the ids of actors in the given film
   * @throws SQLException if there is an error in interacting with the database
   */
  public HashSet<String> filmIdToActorIds(String filmId) throws SQLException {
    // Checks whether value is cached, access if necessary
    HashSet<String> cachedIds = filmIdActorIdsCache.getIfPresent(filmId);
    if (cachedIds != null) {
      return cachedIds;
    }
    // Looks for all actors for a given film
    PreparedStatement prep = conn
        .prepareStatement("SELECT * FROM actor_film WHERE film=?;");
    prep.setString(1, filmId);
    ResultSet rs = prep.executeQuery();
    HashSet<String> actorIds = new HashSet<String>();
    // Get all actorIds
    while (rs.next()) {
      actorIds.add(rs.getString(1));
    }
    // Store new search results in the cache
    filmIdActorIdsCache.put(filmId, actorIds);
    // Close all SQL interactions and return ids
    rs.close();
    prep.close();
    return actorIds;
  }

  /**
   * Uses database to find a film's name, given its id.
   *
   * @param id String form of film id to be used
   * @return String form of given film's name
   * @throws SQLException if there is error in interactions with database
   */
  public String filmIdToName(String id) throws SQLException {
    // Checks whether value is cached, uses if possible
    String cachedName = filmIdNameCache.getIfPresent(id);
    if (cachedName != null) {
      return cachedName;
    }
    // Looks for a given film's name
    PreparedStatement prep = conn
        .prepareStatement("SELECT * FROM film WHERE id=?;");
    prep.setString(1, id);
    ResultSet rs = prep.executeQuery();
    rs.next();
    String filmName = rs.getString(2);
    // Replace empty name with the id itself
    if (filmName.length() == 0) {
      filmName = id;
    }
    // Stores newly found value in cache, if necessary
    filmIdNameCache.put(id, filmName);
    // Close all SQL interactions and return name
    rs.close();
    prep.close();
    return filmName;
  }

  /**
   * Returns all actor names in the database.
   *
   * @return HashSet of all actors' names in the database, in String form
   * @throws SQLException if there is error in interactions with database
   */
  public HashSet<String> getActors() throws SQLException {
    // Makes database request to get and store all actor's name
    PreparedStatement prep = conn.prepareStatement("SELECT name FROM actor;");
    ResultSet rs = prep.executeQuery();
    HashSet<String> actorNames = new HashSet<String>();
    while (rs.next()) {
      actorNames.add(rs.getString(1));
    }
    // Close all SQL interactions and return names
    rs.close();
    prep.close();
    return actorNames;
  }

  @Override
  public ActorVertex makeVertex(String id, float dist) {
    try {
      // Attempts to make new vertex
      String name = actorIdToName(id);
      return new ActorVertex(name, id, dist);
    } catch (SQLException e) {
      // This should never be reached. Ids should never be not present in the
      // table, since this method call itself is used with relation to this
      // table. There are certain ids for which there is no assigned name, but
      // an empty String is then returned by actorIdToName(), not an Exception.
      // That case is managed in intervertex validity.
      return null;
    }
  }

}
