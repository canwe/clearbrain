package com.nilhcem.core.hibernate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Custom annotation, used in every Business object, to automatically rollback if an Exception object is caught.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public @interface WithTransaction {
}
