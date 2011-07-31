package com.nilhcem.business;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.nilhcem.core.test.AbstractDbTest;

public class LanguageBoTest extends AbstractDbTest {
	@Autowired
	private LanguageBo languageBo;
	private static final Logger logger = LoggerFactory.getLogger(LanguageBoTest.class);

	@Test
	public void testGetFrLanguage() {
		logger.debug("Get FR language");
		String code = "fr_FR";
		assertEquals(code, languageBo.findByLocale(languageBo.getLocaleFromCode(code)).getCode());
	}

	@Test
	public void testGetUsLanguage() {
		logger.debug("Get US language");
		String code = "en_US";
		assertEquals(code, languageBo.findByLocale(languageBo.getLocaleFromCode(code)).getCode());
	}

	@Test
	public void testReturnUsLanguageByDefault() {
		logger.debug("Get US language by default");
		assertEquals("en_US", languageBo.findByLocale(languageBo.getLocaleFromCode("omg_OMG")).getCode());
	}

	//UNIT TEST
	@Test
	public void testGetLocaleFromCode() {
		logger.debug("Get locale from Code");
		Locale localeUs =  new Locale("en", "US");
		Locale localeFr =  new Locale("fr", "FR");

		assertNull(languageBo.getLocaleFromCode(null));
		assertEquals(localeUs, languageBo.getLocaleFromCode("en_US"));
		assertEquals(localeFr, languageBo.getLocaleFromCode("fr_FR"));
	}
}
