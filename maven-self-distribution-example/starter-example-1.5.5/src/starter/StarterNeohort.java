package starter;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import neohort.universal.output.creator_iHort;

@WebServlet(
		name="creator_iHort",
		displayName="creator_iHort",
		urlPatterns = {"/report_creator"},
		loadOnStartup=1)

public class StarterNeohort extends creator_iHort{
	private static final long serialVersionUID = 1L;
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		super.doGet(req, res);
	}
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		super.doPost(req, res);
	}
}		      			
	