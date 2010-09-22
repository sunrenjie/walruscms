package lt.walrus.service.user;

import java.io.Serializable;
import java.util.regex.Pattern;

public class EmailAddress implements Serializable {

	private static final long serialVersionUID = -2657128002545698788L;

	/**
     * This constant states that domain literals are allowed in the email address, e.g.:
     *
     * &lt;p&gt;&lt;tt&gt;someone@[192.168.1.100]&lt;/tt&gt; or &lt;br/&gt;

     * &lt;tt&gt;john.doe@[23:33:A2:22:16:1F]&lt;/tt&gt; or &lt;br/&gt;
     * &lt;tt&gt;me@[my computer]&lt;/tt&gt;&lt;/p&gt;
     *
     * &lt;p&gt;The RFC says these are valid email addresses, but most people don't like allowing them.
     * If you don't want to allow them, and only want to allow valid domain names
     * (&lt;a href="http://www.ietf.org/rfc/rfc1035.txt"&gt;RFC 1035&lt;/a&gt;, x.y.z.com, etc),
     * change this constant to &lt;tt&gt;false&lt;/tt&gt;.
     *
     * &lt;p&gt;Its default value is &lt;tt&gt;true&lt;/tt&gt; to remain RFC 2822 compliant, but
     * you should set it depending on what you need for your application.
     */
    private static final boolean ALLOW_DOMAIN_LITERALS = false;

    /**
     * This contstant states that quoted identifiers are allowed
     * (using quotes and angle brackets around the raw address) are allowed, e.g.:
     *
     * &lt;p&gt;&lt;tt&gt;"John Smith" &lt;john.smith@somewhere.com&gt;&lt;/tt&gt;

     *
     * &lt;p&gt;The RFC says this is a valid mailbox.  If you don't want to
     * allow this, because for example, you only want users to enter in
     * a raw address (&lt;tt&gt;john.smith@somewhere.com&lt;/tt&gt; - no quotes or angle
     * brackets), then change this constant to &lt;tt&gt;false&lt;/tt&gt;.
     *
     * &lt;p&gt;Its default value is &lt;tt&gt;true&lt;/tt&gt; to remain RFC 2822 compliant, but
     * you should set it depending on what you need for your application.
     */
    private static final boolean ALLOW_QUOTED_IDENTIFIERS = true;

    // RFC 2822 2.2.2 Structured Header Field Bodies
    private static final String wsp = "[ \\t]"; //space or tab
    private static final String fwsp = wsp + "*";

    //RFC 2822 3.2.1 Primitive tokens
    private static final String dquote = "\\\"";
    //ASCII Control characters excluding white space:
    private static final String noWsCtl = "\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F";
    //all ASCII characters except CR and LF:
    private static final String asciiText = "[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F]";

    // RFC 2822 3.2.2 Quoted characters:
    //single backslash followed by a text char
    private static final String quotedPair = "(\\\\" + asciiText + ")";

    //RFC 2822 3.2.4 Atom:
    private static final String atext = "[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]";
    private static final String atom = fwsp + atext + "+" + fwsp;
    private static final String dotAtomText = atext + "+" + "(" + "\\." + atext + "+)*";
    private static final String dotAtom = fwsp + "(" + dotAtomText + ")" + fwsp;

    //RFC 2822 3.2.5 Quoted strings:
    //noWsCtl and the rest of ASCII except the doublequote and backslash characters:
    private static final String qtext = "[" + noWsCtl + "\\x21\\x23-\\x5B\\x5D-\\x7E]";
    private static final String qcontent = "(" + qtext + "|" + quotedPair + ")";
    private static final String quotedString = dquote + "(" + fwsp + qcontent + ")*" + fwsp + dquote;

    //RFC 2822 3.2.6 Miscellaneous tokens
    private static final String word = "((" + atom + ")|(" + quotedString + "))";
    private static final String phrase = word + "+"; //one or more words.

    //RFC 1035 tokens for domain names:
    private static final String letter = "[a-zA-Z]";
    private static final String letDig = "[a-zA-Z0-9]";
    private static final String letDigHyp = "[a-zA-Z0-9-]";
    private static final String rfcLabel = letDig + "(" + letDigHyp + "{0,61}" + letDig + ")?";
    private static final String rfc1035DomainName = rfcLabel + "(\\." + rfcLabel + ")*\\." + letter + "{2,6}";

    //RFC 2822 3.4 Address specification
    //domain text - non white space controls and the rest of ASCII chars not including [, ], or \:
    private static final String dtext = "[" + noWsCtl + "\\x21-\\x5A\\x5E-\\x7E]";
    private static final String dcontent = dtext + "|" + quotedPair;
    private static final String domainLiteral = "\\[" + "(" + fwsp + dcontent + "+)*" + fwsp + "\\]";
    private static final String rfc2822Domain = "(" + dotAtom + "|" + domainLiteral + ")";

    private static final String domain = ALLOW_DOMAIN_LITERALS ? rfc2822Domain : rfc1035DomainName;

    private static final String localPart = "((" + dotAtom + ")|(" + quotedString + "))";
    private static final String addrSpec = localPart + "@" + domain;
    private static final String angleAddr = "&lt;" + addrSpec + "&gt;";
    private static final String nameAddr = "(" + phrase + ")?" + fwsp + angleAddr;
    private static final String mailbox = nameAddr + "|" + addrSpec;

    //now compile a pattern for efficient re-use:
    //if we're allowing quoted identifiers or not:
    private static final String patternString = ALLOW_QUOTED_IDENTIFIERS ? mailbox : addrSpec;
    public static final Pattern VALID_PATTERN = Pattern.compile(patternString);

    //class attributes
    private String text;
    private boolean bouncing = true;
    private boolean verified = false;
    private String label;

    public EmailAddress() {
        super();
    }

    public EmailAddress(String text) {
        super();
        setText(text);
    }

    /**
     * Returns the actual email address string, e.g. &lt;tt&gt;someone@somewhere.com&lt;/tt&gt;

     *
     * @return the actual email address string.
     */
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns whether or not any emails sent to this email address come back as bounced
     * (undeliverable).
     *
     * &lt;p&gt;Default is &lt;tt&gt;false&lt;/tt&gt; for convenience's sake - if a bounced message is ever received for this
     * address, this value should be set to &lt;tt&gt;true&lt;/tt&gt; until verification can made.
     *
     * @return whether or not any emails sent to this email address come back as bounced
     *         (undeliverable).
     */
    public boolean isBouncing() {
        return bouncing;
    }

    public void setBouncing(boolean bouncing) {
        this.bouncing = bouncing;
    }

    /**
     * Returns whether or not the party associated with this email has verified that it is
     * their email address.
     *
     * &lt;p&gt;Verification is usually done by sending an email to this
     * address and waiting for the party to respond or click a specific link in the email.
     *
     * &lt;p&gt;Default is &lt;tt&gt;false&lt;/tt&gt;.
     *
     * @return whether or not the party associated with this email has verified that it is
     *         their email address.
     */
    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    /**
     * Party label associated with this address, for example, 'Home', 'Work', etc.
     *
     * @return a label associated with this address, for example 'Home', 'Work', etc.
     */
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns whether or not the text represented by this object instance is valid
     * according to the &lt;tt&gt;RFC 2822&lt;/tt&gt; rules.
     *
     * @return true if the text represented by this instance is valid according
     *         to RFC 2822, false otherwise.
     */
    public boolean isValid() {
        return isValidText(getText());
    }

    /**
     * Utility method that checks to see if the specified string is a valid
     * email address according to the * RFC 2822 specification.
     *
     * @param email the email address string to test for validity.
     * @return true if the given text valid according to RFC 2822, false otherwise.
     */
    public static boolean isValidText(String email) {
        return (email != null) && VALID_PATTERN.matcher(email).matches();
    }

    public boolean equals(Object o) {
        if (o instanceof EmailAddress) {
            EmailAddress ea = (EmailAddress) o;
            return getText().equals(ea.getText());
        }
        return false;
    }

    public int hashCode() {
        return getText().hashCode();
    }

    public String toString() {
        return getText();
    }

    public static void main(String[] args) {
        String addy = "\"John Smith\" &lt;john.smith@u.washington.edu&gt;";
        if (isValidText(addy)) {
            System.out.println("Valid email address.");
        } else {
            System.out.println("Invalid email address!");
        }
    }
}