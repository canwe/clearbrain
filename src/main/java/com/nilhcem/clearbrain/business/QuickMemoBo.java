package com.nilhcem.clearbrain.business;

import java.util.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nilhcem.clearbrain.core.hibernate.TransactionalReadOnly;
import com.nilhcem.clearbrain.core.hibernate.TransactionalReadWrite;
import com.nilhcem.clearbrain.dao.QuickMemoDao;
import com.nilhcem.clearbrain.model.QuickMemo;
import com.nilhcem.clearbrain.model.User;

/**
 * Provides methods for dealing with {@code QuickMemo} objects.
 *
 * @author Nilhcem
 * @since 1.0
 * @see QuickMemo
 */
@Service
@TransactionalReadOnly
public class QuickMemoBo {
	@Autowired
	private QuickMemoDao dao;
	private final Logger logger = LoggerFactory.getLogger(QuickMemoBo.class);

	/**
	 * Returns the memo of the user specified in parameter.
	 * <p>
	 * Note: There is only one memo per user.<br />
	 * When a user signs up, a quick memo is automatically created.
	 * </p>
	 *
	 * @param user the owner of the memo.
	 * @return the quick memo of the user.
	 */
	public QuickMemo getByUser(User user) {
		return dao.getByUser(user);
	}

	/**
	 * Creates a quick memo for the user specified in parameter.
	 *
	 * @param user the owner of the memo.
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
	 * Updates the content of a quick memo.
	 *
	 * @param user the owner of the memo.
	 * @param content an HTML string containing the new memo's content.
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
