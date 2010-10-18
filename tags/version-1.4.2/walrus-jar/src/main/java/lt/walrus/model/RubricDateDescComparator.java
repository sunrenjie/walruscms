package lt.walrus.model;

public class RubricDateDescComparator extends RubricDateComparator {

	@Override
	public int compare(Rubric r1, Rubric r2) {
		return -1 * super.compare(r1, r2);
	}
}
