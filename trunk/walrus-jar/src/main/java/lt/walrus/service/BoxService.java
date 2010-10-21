package lt.walrus.service;

import lt.walrus.dao.IWalrusDao;
import lt.walrus.model.Banner;
import lt.walrus.model.Box;
import lt.walrus.model.Slide;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("boxService")
public class BoxService implements CRUDService<Box> {

	/**
	 * Walrus DAO service
	 */
	@Autowired
	private IWalrusDao dao;

	public void save(Box box) {
		dao.save(box);
	}

	public void deleteBanner(Banner banner) {
		dao.deleteBanner(banner);
	}

	public void deleteSlide(Slide slide) {
		dao.delete(slide);
	}

	public void setDao(IWalrusDao dao) {
		this.dao = dao;
	}

	public IWalrusDao getDao() {
		return dao;
	}

	@Override
	public void add(Box o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Box o) {
		// TODO Auto-generated method stub

	}

	@Override
	public Box get(long id) {
		// TODO Auto-generated method stub
		return null;
	}
}
