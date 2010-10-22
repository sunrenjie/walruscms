package lt.walrus.service;

import lt.walrus.dao.IWalrusDao;
import lt.walrus.model.Comment;
import lt.walrus.model.Rubric;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("commentService")
public class CommentService implements CRUDService<Comment> {

	/**
	 * Walrus DAO service
	 */
	@Autowired
	private IWalrusDao dao;
	@Autowired
	private RubricService rubricService;

	@Override
	public void add(Comment comment) {
		add(comment, 0);
	}
	/**
	 * Add a comment
	 * 
	 * @param comment
	 * @param index
	 *            index at which comment is added
	 */
	public void add(Comment comment, int index) {
		Rubric rubric = rubricService.get(comment.getRubric().getId());
		comment.setRubric(rubric);
		rubric.getComments().add(index, comment);
		rubricService.save(rubric);
	}

	/**
	 * Delete comment
	 * 
	 * @param comment
	 */
	@Override
	public void delete(Comment comment) {
		if (null == comment) {
			return;
		}
		Rubric siteRubric = rubricService.get(comment.getRubric().getId());
		siteRubric.getComments().remove(comment);
		getDao().deleteComment(comment);
	}

	@Override
	public void save(Comment comment) {
		getDao().save(comment);
	}

	/**
	 * @param Id
	 * @return comment by id
	 */
	public Comment get(long Id) {
		return getDao().getComment(Id);
	}

	public void setDao(IWalrusDao dao) {
		this.dao = dao;
	}

	public IWalrusDao getDao() {
		return dao;
	}

	public void setRubricService(RubricService rubricService) {
		this.rubricService = rubricService;
	}
}
