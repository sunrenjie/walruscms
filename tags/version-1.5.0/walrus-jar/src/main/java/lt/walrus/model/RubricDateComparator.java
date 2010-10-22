package lt.walrus.model;

import java.util.Comparator;

public class RubricDateComparator implements Comparator<Rubric> {

	@Override
	public int compare(Rubric r1, Rubric r2) {
		if (null == r1 && null == r2) {
			return 0;
		} else
		if (null == r1 && null != r2) {
			return -1;
		} else
		if (null != r1 && null == r2) {
			return 1;
		} else 
		if (null == r1.getRubricDate() && null == r2.getRubricDate()) {
			return 0;
		} else
		if (null == r1.getRubricDate() && null != r2.getRubricDate()) {
			return -1;
		} else
		if (null != r1.getRubricDate() && null == r2.getRubricDate()) {
			return 1;
		} else 
		{
			return r1.getRubricDate().compareTo(r2.getRubricDate());
		}
	}
	
}