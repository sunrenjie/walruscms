/**
 * 
 */
package lt.walrus.model;

import java.io.Serializable;

import lt.walrus.model.Rubric.Mode;
import lt.walrus.utils.EnumUserType;

/**
 * Helper class to assist saving of mode enumeration to Hibernate persistency
 * layer
 */
public class ModeHib extends EnumUserType<Mode> implements Serializable{ 
	private static final long serialVersionUID = -6819654353402720453L;

	public ModeHib() {
		super(Mode.class);
	}
}