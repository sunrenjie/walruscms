package lt.walrus.service.user;

public class EmailConfig {

	private String emailFrom = "info@baip.lt";
	private String emailFromName = "BAIP.lt administratorius";

	private String inviteEmailSubject = "Pakvietimas į baip.lt";
	private String inviteText = "\n\nNorėdami prisijungti prie baip.lt ypatingųjų vartotojų, spauskite šią nuorodą ir užpildykite pateiktą formą:\n";

	private String welcomeEmailSubject = "Sveiki atvykę į baip.lt!";
	private String welcomeTemplate = "Sveiki atvykę į baip.lt!\n\n\nJūsų prisijungimo vardas: %1\n\nJūsų slaptažodis: %2\n\n";

	private String defaultFooter = "\n\nŠis pranešimas automatiškai sugeneruotas svetainės www.baip.lt\n" + 
	"\n" + 
	"Prašome į šį pranešimą neatsakinėti.\n" + 
	"\n" + 
	" \n" + 
	"\n" + 
	"Jei Jums iškilo klausimų, kreipkitės el. paštu:  registracija@baip.lt arba tel.: + 370 5 278 04 06";

	public String getEmailFrom() {
		return emailFrom;
	}

	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

	public String getWelcomeEmailSubject() {
		return welcomeEmailSubject;
	}

	public void setWelcomeEmailSubject(String welcomeEmailSubject) {
		this.welcomeEmailSubject = welcomeEmailSubject;
	}

	public String getInviteEmailSubject() {
		return inviteEmailSubject;
	}

	public void setInviteEmailSubject(String inviteEmailSubject) {
		this.inviteEmailSubject = inviteEmailSubject;
	}

	public String getDefaultFooter() {
		return defaultFooter;
	}

	public void setDefaultFooter(String defaultFooter) {
		this.defaultFooter = defaultFooter;
	}

	public void setEmailFromName(String emailFromName) {
		this.emailFromName = emailFromName;
	}

	public String getEmailFromName() {
		return emailFromName;
	}

	public String getInviteText() {
		return inviteText;
	}

	public void setInviteText(String inviteText) {
		this.inviteText = inviteText;
	}

	public String getWelcomeTemplate() {
		return welcomeTemplate ;
	}

	public void setWelcomeTemplate(String welcomeTemplate) {
		this.welcomeTemplate = welcomeTemplate;
	}


}
