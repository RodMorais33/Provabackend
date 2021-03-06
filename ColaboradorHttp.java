package view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import controller.ColaboradorProcess;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Colaborador;

@WebServlet("/colaborador")
public class ColaboradorHttp extends HttpServlet {
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		PrintWriter pw = resp.getWriter();
		
		String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		
		try {
			
			JSONObject obj = new JSONObject(body);
			
			String nome_completo = obj.getString("nome_completo");
			int  data_desligamento = (int) obj.getInt("data_desligamento");
			float ultimo_salario = (float) obj.getDouble("ultimo_salario");
			float aliquoca = (float) obj.getDouble("aliquota");
			float irrf = (float) obj.getDouble("IRRF");
			
			Colaborador a = new Colaborador();
			a.setNome_completo(nome_completo);
			a.setData_desligamento(data_desligamento);
			a.setUltimo_salario(ultimo_salario);
			a.setAliquota(aliquoca);
			a.setIrrf(irrf);
			
			ColaboradorProcess ap = new ColaboradorProcess();
			
			if(ap.create(a) == true) {
				obj.put("matricula", a.getMatricula());
				pw.write(obj.toString());
			}else {
				resp.setStatus(401);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter pw = resp.getWriter();
		
		ColaboradorProcess ap = new ColaboradorProcess();
		
		JSONArray arr = ap.readAll();
		
		pw.write(arr.toString());
		
		
	}

	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter pw = resp.getWriter();
		
		String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

		JSONObject retJson = new JSONObject();
		
		try {
			JSONObject obj = new JSONObject(body);
			
			Colaborador a = new Colaborador();
			a.setMatricula(obj.getInt("matricula"));
			a.setNome_completo(obj.getString("nome_completo"));
			a.setData_desligamento((int) obj.getDouble("data_desligamento"));
			a.setUltimo_salario((float) obj.getDouble("ultimo_salario"));
			a.setAliquota((float) obj.getDouble("aliquota"));
			a.setIrrf((float) obj.getDouble("irrf"));
			
			ColaboradorProcess ap = new ColaboradorProcess();
			
			
			String ret = ap.update(a);
			
			if(ret.equals("sucesso")) {
				pw.write(obj.toString());
			}else {
				resp.setStatus(401);
				retJson.put("err", ret);
				pw.write(retJson.toString());
			}
		} catch (JSONException e) {
			resp.setStatus(401);
			pw.write("{err:'" + e.toString() + "'}");
		}
	}

	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter pw = resp.getWriter();
		
		String tempId = req.getParameter("matricula");
		
		int id = Integer.parseInt(tempId);
		
		ColaboradorProcess ap = new ColaboradorProcess();
		
		if(ap.delete(id) == true) {
			pw.write("{\"matricula\":" + id + "}");
		}else {
			resp.setStatus(401);
		}		
	}
	
}