package lt.walrus.command;


public class FileListCommand {

	private String type;
	private boolean full = false;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public boolean isImage() {
		return "image".equals(type);
	}

	public void setFull(boolean full) {
		this.full = full;
	}

	public boolean isFull() {
		return full;
	}
	
}
