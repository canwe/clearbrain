package com.nilhcem.clearbrain.core.hibernate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Specifies that we want Spring to handle transactions which could modify the content of the database.
 * <p>
 * Automatically roll-back the transaction if an {@code Exception} object is caught.<br />
 * Should be used in every {@code Business} object accessing read/write data from DAO.
 * </p>
 *
 * @author Nilhcem
 * @since 1.0
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public @interface TransactionalReadWrite {
}
