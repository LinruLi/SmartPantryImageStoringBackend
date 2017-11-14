package bean;

//class for storing col, row and image path info for BaseBean
public class RegisterBean {

	private String token;
	private String pantryColumn;
	private String pantryRow;
	private String head;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getPantryColumn() {
		return pantryColumn;
	}
	public void setPantryColumn(String pantryColumn) {
		this.pantryColumn = pantryColumn;
	}
	public String getPantryRow() {
		return pantryRow;
	}
	public void setPantryRow(String pantryRow) {
		this.pantryRow = pantryRow;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}

}