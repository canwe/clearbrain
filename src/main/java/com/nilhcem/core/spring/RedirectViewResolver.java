package com.nilhcem.core.spring;

import java.util.Locale;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Used for Spring MVC to redirect without sending existing model
 * <p>A view can therefore be called using "redirectWithoutModel:" prefix.</p>
 * @author Nilhcem
 * @since 1.0
 */
public class RedirectViewResolver implements ViewResolver, Ordered {
    // Have a highest priority by default
    private int order = Integer.MIN_VALUE; 

    // Uses this prefix to avoid interference with the default behaviour
    public static final String REDIRECT_URL_PREFIX = "redirectWithoutModel:";     

    /**
     * Check if view starts by "redirectWithoutModel:" prefix, if so, redirect without model.
     * @param viewName the view's name
     * @param arg1 Locale
     * @return a RedirectView with no model
     */
    public View resolveViewName(String viewName, Locale arg1) throws Exception {
        if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
            String redirectUrl = viewName.substring(REDIRECT_URL_PREFIX.length());
            return new RedirectView(redirectUrl, true, true, false);
        }
        return null;
    }

    /**
     * Return the highest priority, so that it will be the first view resolver.
     * @return order
     */
    public int getOrder() {
        return order;
    }

    /** Let Spring set the order.
     * 
     * @param order Order of the ViewResolver
     */
    public void setOrder(int order) {
        this.order = order;
    }
}
