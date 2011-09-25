package com.nilhcem.clearbrain.enums;

/**
 * Distinguishes which part of the dashboard we are referring to.
 * <p>
 * Used in session and in {@code Controller} classes to know which part of the dashboard we are referring to.<br />
 * If this file is modified, please also check views/logged/dashboard.jsp.
 * </p>
 *
 * @see com.nilhcem.clearbrain.dao.NoteDao#getNbTaskTodoHeader
 * @see com.nilhcem.clearbrain.controller.dashboard.AbstractDashboardController
 * @author Nilhcem
 * @since 1.0
 */
public enum DashboardDateEnum {
	TODAY,
	TOMORROW,
	THIS_WEEK,
	GLOBAL
};
