package com.nilhcem.business;

import java.util.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nilhcem.core.hibernate.TransactionalReadOnly;
import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.dao.QuickMemoDao;
import com.nilhcem.model.QuickMemo;
import com.nilhcem.model.User;

/**
 * Business class for accessing {@code QuickMemo} data.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Service
@TransactionalReadOnly
public class QuickMemoBo {
	@Autowired
	private QuickMemoDao dao;
	private final Logger logger = LoggerFactory.getLogger(QuickMemoBo.class);

	/**
	 * Find a memo from its {@code user}.
	 *
	 * @param user User of the memo.
	 * @return QuickMemo.
	 */
	public QuickMemo getByUser(User user) {
		return dao.getByUser(user);
	}

	/**
	 * Create a QuickMemo.
	 *
	 * @param user User of the memo.
	 */
	@TransactionalReadWrite
	public void createQuickMemo(User user) {
		QuickMemo memo = new QuickMemo();
		memo.setContent("");
		memo.setSaveDate(null);
		memo.setUser(user);
		dao.save(memo);
	}

	/**
	 * Update the content of a quick memo.
	 *
	 * @param user User of the memo.
	 * @param content New quick memo content.
	 */
	@TransactionalReadWrite
	public void updateMemo(User user, String content) {
		logger.debug("Memo #{} updated: {}", user.getId(), content);
		QuickMemo memo = getByUser(user);
		memo.setContent(content);
		memo.setSaveDate(Calendar.getInstance().getTime());
		dao.update(memo);
	}
}
