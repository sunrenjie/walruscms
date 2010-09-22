package lt.walrus.model;

import java.io.Serializable;

/**
 * A specific box that contains a rubric. In template this can be utilized in
 * different ways, for example, rubric box might be used to display titles of
 * news from news rubric.
 */
public class RubricBox extends Box implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Rubric rubric;

	public Rubric getRubric() {
		return rubric;
	}

	public void setRubric(Rubric rubric) {
		this.rubric = rubric;
	}
	
	public RubricBox clone() {
		RubricBox b = new RubricBox();
		return b;
	}
}
