package com.nilhcem.clearbrain.core.hibernate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Specifies that we want Spring to handle read-only transactions.
 * <p>
 * Should be used in every {@code Business} object accessing read-only data from DAO.
 * </p>
 *
 * @author Nilhcem
 * @since 1.0
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public @interface TransactionalReadOnly {
}
