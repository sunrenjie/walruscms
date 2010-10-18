package lt.walrus.service.user;

import java.io.Serializable;

public class PasswordChangeCommand implements Serializable {
	private static final long serialVersionUID = 8464270639097459259L;
	public String password;
	public String repeatPassword;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}
}
