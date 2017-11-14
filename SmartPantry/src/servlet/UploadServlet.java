package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import bean.BaseBean;
import bean.RegisterBean;
import db.DBUtils;
import utils.Base64Utils;
import utils.TimeUtils;
import utils.TokenUtils;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setHeader("Content-Type", "text/html;charset=UTF-8");

		String col = request.getParameter("column");
		String row = request.getParameter("row");
		String image_code = request.getParameter("image_code");

		// 判空 / see if it contains null data
//		if (col == null || col.equals("") || row == null || row.equals("") || image_code == null
//				|| image_code.equals("")) {
        if (col == null || col.equals("") || row == null || row.equals("")){
            System.out.println("");
			return;
		}

		// request from database
		DBUtils dbUtils = new DBUtils();
		dbUtils.openConnect();
		BaseBean bean = new BaseBean();
		RegisterBean data = new RegisterBean();

		String imageName = row + "_" + col + "_" + TimeUtils.getNowTime() + ".jpg"; // temporally store image with time as its name for a test
		System.out.println(getServletContext().getRealPath("/images"));
		String path = getServletContext().getRealPath("/images/" + imageName); // absolute path for photo which would be stored under apache server

		if (!Base64Utils.GenerateImage(image_code, path)) { // see if it has been scuessfully stored
			bean.setCode(-2);
			bean.setData(data);
			bean.setMsg("A problem occurred with storing image...");
		} else if (dbUtils.isExistInDB(col, row)) {
            dbUtils.updateDataToDB(col, row, path);
			bean.setCode(1);
            data.setPantryColumn(col);
            data.setPantryRow(row);
			bean.setData(data);
			bean.setMsg("Photo updated");
		} else if (!dbUtils.insertDataToDB(col, row, imageName)) {
			bean.setCode(0);
			bean.setMsg("Successfully uploaded photos");
			data.setPantryColumn(col);
			data.setPantryRow(row);
			data.setToken(TokenUtils.getToken(col, row));
			bean.setData(data);
		} else {
			bean.setCode(500);
			bean.setData(data);
			bean.setMsg("A mistake occurred in server!");
		}
		Gson gson = new Gson();
		String json = gson.toJson(bean);
		try {
			response.getWriter().println(json);// send json back to pi
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			response.getWriter().close(); // close steam
		}
		dbUtils.closeConnect(); // close database connection
	}
}
