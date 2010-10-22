package lt.walrus.dao;

import java.util.List;

import lt.walrus.model.Banner;
import lt.walrus.model.Box;
import lt.walrus.model.Comment;
import lt.walrus.model.Rubric;
import lt.walrus.model.Site;
import lt.walrus.model.Slide;

public interface IWalrusDao {
	public void save(Site site);

	public Site getSite(String host, String language);

	public Rubric getRubric(long id);

	public void addRubric(Rubric rubric);

	public void deleteRubric(Rubric r);

	public void save(Rubric rootRubric);

	public void save(Box box);

	public void deleteBanner(Banner banner);

	public List<Site> getAllSites();

	public void deleteSite(Site site);

	public void save(Comment comment);

	public Comment getComment(long commentId);

	public void deleteComment(Comment comment);

	public void delete(Slide slide);

	public void save(Slide slide);
}
