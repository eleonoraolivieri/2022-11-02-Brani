package it.polito.tdp.itunes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.itunes.model.Adiacenza;
import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Artist;
import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.MediaType;
import it.polito.tdp.itunes.model.Playlist;
import it.polito.tdp.itunes.model.Track;

public class ItunesDAO {
	
	public List<Album> getAllAlbums(){
		final String sql = "SELECT * FROM Album";
		List<Album> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Album(res.getInt("AlbumId"), res.getString("Title")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Artist> getAllArtists(){
		final String sql = "SELECT * FROM Artist";
		List<Artist> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Artist(res.getInt("ArtistId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Playlist> getAllPlaylists(){
		final String sql = "SELECT * FROM Playlist";
		List<Playlist> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Playlist(res.getInt("PlaylistId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public void getAllTracks(Map<Integer, Track> idMap){
		final String sql = "SELECT * FROM Track";
		
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				if(!idMap.containsKey(res.getInt("TrackId"))) {
				Track track = new Track(res.getInt("TrackId"), res.getString("Name"), 
						res.getString("Composer"), res.getInt("Milliseconds"), 
						res.getInt("Bytes"),res.getDouble("UnitPrice"));
				idMap.put(res.getInt("TrackId"), track);
				}
			
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		
	}
	
	public List<Genre> getAllGenres(){
		final String sql = "SELECT * FROM Genre";
		List<Genre> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Genre(res.getInt("GenreId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<MediaType> getAllMediaTypes(){
		final String sql = "SELECT * FROM MediaType";
		List<MediaType> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new MediaType(res.getInt("MediaTypeId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}

	public List<String> getGeneri() {
		String sql = "SELECT DISTINCT g.* "
				+ "FROM genre g "
				+ "ORDER BY g.Name " ;
		List<String> result = new ArrayList<>() ;
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				result.add(res.getString("Name")) ;
			}
			conn.close();
			return result ;
		} catch (SQLException e) {
			throw new RuntimeException("Errore nel DB", e) ;
		}
	
	}

	public List<Track> getVertici(String genere, int min, int max, Map<Integer, Track> idMap) {
		String sql = "SELECT t.trackId as id "
				+ "FROM track t, genre g "
				+ "WHERE t.genreId = g.genreId "
				+ "AND g.Name = ? "
				+ "AND t.Milliseconds >= ? * 1000 "
				+ "AND t.Milliseconds <= ?*1000 ";

		List<Track> result = new LinkedList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			st.setInt(2, min);
			st.setInt(3, max);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				result.add(idMap.get(rs.getInt("id")));
			}
			
			rs.close();
			st.close();
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Adiacenza> getAdiacenze(String genere, int min, int max, Map<Integer, Track> idMap) {
		String sql = "WITH vertici AS( "
				+ "SELECT t.trackId, COUNT(*) AS peso "
				+ "FROM track t, genre g, playlisttrack p "
				+ "WHERE t.genreId = g.genreId "
				+ "AND g.Name = ? "
				+ "AND t.Milliseconds >= ? * 1000 "
				+ "AND t.Milliseconds <= ?*1000 "
				+ "AND t.trackId = p.trackId "
				+ "GROUP BY t.trackId "
				+ ") "
				+ "SELECT v1.trackId as id1, v2.trackId as id2 "
				+ "FROM vertici v1, vertici v2 "
				+ "WHERE v1.peso = v2.peso "
				+ "AND v1.trackId < v2.trackId ";
		List<Adiacenza> result = new LinkedList<Adiacenza>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			st.setInt(2, min);
			st.setInt(3, max);
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				Track sorgente = idMap.get(rs.getInt("id1"));
				Track destinazione = idMap.get(rs.getInt("id2"));
				if(sorgente != null && destinazione != null) {
					result.add(new Adiacenza(sorgente, 
							destinazione));
				} else {
					System.out.println("Errore in getAdiacenze");
				}
			}
			rs.close();
			st.close();
			conn.close();
			
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public int getPlaylist(Track c1) {
		String sql = "SELECT COUNT(t.trackId) as tot "
				+ "FROM track t, playlisttrack p "
				+ "WHERE t.trackId = p.trackID "
				+ "AND t.trackId = ? ";
				
		int tot = 0;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, c1.getTrackId());
			
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				tot = rs.getInt("tot");
			}
			rs.close();
			st.close();
			conn.close();
			
			return tot;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	
	
}
