package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.PartiteStagione;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {

	public List<Season> listSeasons() {
		String sql = "SELECT season, description FROM seasons";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Season(res.getInt("season"), res.getString("description")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Team> listTeams(Map<String, Team> idMap) {
		String sql = "SELECT team FROM teams";
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				if(!idMap.containsKey(res.getString("team"))) {
					Team team = new Team(res.getString("team"));
					result.add(team);
					idMap.put(res.getString("team"), team);
				}else {
					result.add(idMap.get(res.getString("team")));
				}
				
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<PartiteStagione> getPartiteStagione(Map<String, Team> idMap) {
		final String sql=	"SELECT m.HomeTeam AS homet, m.AwayTeam awayt, COUNT(*)*2 AS peso " + 
							"FROM matches AS m " + 
							"WHERE m.HomeTeam > m.AwayTeam " + 
							"GROUP BY homet, awayt";
		List<PartiteStagione> partite = new LinkedList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Team t1 = idMap.get(res.getString("homet"));
				Team t2 = idMap.get(res.getString("awayt"));
				if(t1!=null && t2!=null) {
					PartiteStagione partita = new PartiteStagione(t1, t2, res.getDouble("peso"));
					partite.add(partita);
				}
				
			}
			conn.close();
			return partite;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
