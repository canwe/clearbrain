package com.nilhcem.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.nilhcem.business.UserBo;

/**
 * Job scheduler launched every day at midnight to remove users which should be deleted.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Service
public class DeleteAccountsJob {
	private static final Logger logger = LoggerFactory.getLogger(DeleteAccountsJob.class);

	@Autowired
	UserBo userBo;

	/**
	 * Delete accounts which should be deleted every day at midnight
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	public void deleteAccounts() {
		logger.debug("Start delete accounts job");
		userBo.removeDeletableUsers();
	}
}
