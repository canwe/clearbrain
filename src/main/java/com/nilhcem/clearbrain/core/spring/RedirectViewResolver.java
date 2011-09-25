package com.nilhcem.clearbrain.core.spring;

import java.util.Locale;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Redirects without sending existing model, using the {@code redirectWithoutModel:} prefix.
 * <p>
 * Used in Spring MVC controllers to redirect without sending existing model.
 * </p>
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class RedirectViewResolver implements ViewResolver, Ordered {
    // Have a highest priority by default
    private int order = Integer.MIN_VALUE;

    // Uses this prefix to avoid interference with the default behavior
    public static final String REDIRECT_URL_PREFIX = "redirectWithoutModel:";

    /**
     * Checks if the view starts by "redirectWithoutModel:" prefix, if so, redirect without model.
     *
     * @param viewName the view's name.
     * @param locale the user's locale.
     * @return a {@code RedirectView} with no model.
     */
    public View resolveViewName(String viewName, Locale locale) {
        if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
            String redirectUrl = viewName.substring(REDIRECT_URL_PREFIX.length());
            return new RedirectView(redirectUrl, true, true, false);
        }
        return null;
    }

    /**
     * Always returns the highest priority, so that it will be the first view resolver.
     *
     * @return the lowest value (the highest priority).
     */
    public int getOrder() {
        return order;
    }

    /**
     * Lets Spring sets the order.
     *
     * @param order the order of the {@code ViewResolver}.
     */
    public void setOrder(int order) {
        this.order = order;
    }
}
