package lt.walrus.service;

import lt.walrus.dao.IWalrusDao;
import lt.walrus.model.Slide;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SlideService implements SaveService<Slide> {

	@Autowired
	private IWalrusDao dao;

	@Override
	public void save(Slide slide) {
		dao.save(slide);
	}

	public void setDao(IWalrusDao dao) {
		this.dao = dao;
	}

	public IWalrusDao getDao() {
		return dao;
	}

}
