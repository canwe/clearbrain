package com.nilhcem.business;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nilhcem.core.test.abstr.AbstractDbTest;

public class LanguageBoTest extends AbstractDbTest {
	@Autowired
	private LanguageBo languageBo;

	@Test
	public void testGetFrLanguage() {
		String code = "fr_FR";
		assertEquals(code, languageBo.findByLocale(languageBo.getLocaleFromCode(code)).getCode());
	}

	@Test
	public void testGetUsLanguage() {
		String code = "en_US";
		assertEquals(code, languageBo.findByLocale(languageBo.getLocaleFromCode(code)).getCode());
	}

	@Test
	public void testReturnUsLanguageByDefault() {
		assertEquals("en_US", languageBo.findByLocale(languageBo.getLocaleFromCode("omg_OMG")).getCode());
	}

	@Test
	public void testGetLocaleFromCode() {
		Locale localeUs =  new Locale("en", "US");
		Locale localeFr =  new Locale("fr", "FR");

		assertNull(languageBo.getLocaleFromCode(null));
		assertEquals(localeUs, languageBo.getLocaleFromCode("en_US"));
		assertEquals(localeFr, languageBo.getLocaleFromCode("fr_FR"));
	}
}
