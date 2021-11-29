package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.Colaborador;

public class ColaboradorProcess {

	private Connection con;
	private ResultSet rs;
	
	public ColaboradorProcess() {
		this.con = new ConnectionDB().getConnection();
	}
	
	public JSONArray readAll() {
		JSONArray arr = new JSONArray();
		
		String query = "SELECT * FROM colaborador";
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				JSONObject obj = new JSONObject();
				obj.put("matricula", rs.getInt(1));
				obj.put("nome_completo", rs.getString(2));
				obj.put("data_desligamento", rs.getInt(3));
				obj.put("ultimo_salario", rs.getFloat(4));
				obj.put("aliquota", rs.getFloat(5));
				obj.put("IRRF", rs.getFloat(6));
				
				arr.put(obj);
			}
			
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return arr;		
	}
	
	public boolean create(Colaborador colaborador) {
		String query = "INSERT INTO colaborador VALUES (DEFAULT,?,?,?,?,?,?)";
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			
			ps.setInt(1, colaborador.getMatricula());
			ps.setString(2, colaborador.getNome_completo());
			ps.setInt(3, colaborador.getData_desligamento());
			ps.setFloat(4, colaborador.getUltimo_salario());
			ps.setFloat(4, colaborador.getAliquota());
			ps.setFloat(4, colaborador.getIrrf());
			
			if (ps.executeUpdate() > 0) {
				rs = ps.getGeneratedKeys();
				rs.next();
				int matricula = rs.getInt(1);
				colaborador.setMatricula(matricula);
				con.close();
				return true;
			} else {
				con.close();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	public String update(Colaborador colaborador) {
		
		String query = "UPDATE aluno SET nome = ?, altura = ?, peso = ?, nascimento = ? WHERE id = ?";
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			
			ps.setString(1, colaborador.getNome_completo());
			ps.setInt(2, colaborador.getData_desligamento());
			ps.setFloat(3, colaborador.getUltimo_salario());
			ps.setFloat(4, colaborador.getAliquota());
			ps.setFloat(5, colaborador.getIrrf());
			ps.setInt(5, colaborador.getMatricula());
			
			if(ps.executeUpdate() > 0) {
				con.close();
				return "sucesso";
			}

			con.close();
		} catch (SQLException e) {
			return e.toString();
			//e.printStackTrace();
		}
		
		return "falha ao atualizar cadastro";		
	}
	
	public boolean delete(int id) {
		
		String query = "DELETE FROM aluno WHERE id = ?";
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			
			ps.setInt(1, id);
			
			if(ps.executeUpdate() > 0) {
				con.close();
				return true;
			}

			con.close();	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
		
	}
	
}
