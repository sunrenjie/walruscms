package lt.walrus.controller.users;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lt.walrus.dao.UserServiceDao;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class UserListController extends AbstractController {
	UserServiceDao userDao;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse arg1) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("list", userDao.findUser(request.getParameter("q")));
		return new ModelAndView("userListJson", map);
	}

	public UserServiceDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserServiceDao userDao) {
		this.userDao = userDao;
	}

}
