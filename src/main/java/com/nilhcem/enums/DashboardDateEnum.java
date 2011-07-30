package com.nilhcem.enums;

/**
 * Used in session and in Controller classes to know which part of the dashboard we are referring to.
 * If this file is modified, please check views/logged/dashboard.jsp
 *
 * @see NoteDao.getNbTaskTodoHeader
 * @see AbstractDashboardController
 * @author nilhcem
 * @since 1.0
 */
public enum DashboardDateEnum {
	TODAY,
	TOMORROW,
	THIS_WEEK,
	GLOBAL
};
