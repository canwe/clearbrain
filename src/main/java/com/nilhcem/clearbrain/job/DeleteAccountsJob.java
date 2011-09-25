package com.nilhcem.clearbrain.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.nilhcem.clearbrain.business.UserBo;

/**
 * Removes from database users which should be deleted.
 * <p>
 * Launched every day at midnight.
 * </p>
 *
 * @author Nilhcem
 * @since 1.0
 * @see UserBo#removeDeletableUsers
 */
@Service
public final class DeleteAccountsJob {
	private final Logger logger = LoggerFactory.getLogger(DeleteAccountsJob.class);

	@Autowired
	private UserBo userBo;

	/**
	 * Deletes all accounts which should be deleted.
	 * <p>
	 * An account which should be deleted is an account which have a delete date > 3 days.<br />
	 * This method is launched every day at midnight.
	 * </p>
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	public void deleteAccounts() {
		logger.debug("Start delete accounts job");
		userBo.removeDeletableUsers();
	}
}
