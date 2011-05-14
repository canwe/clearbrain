package com.nilhcem.business;

import static org.junit.Assert.*;
import java.util.Locale;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.nilhcem.model.Language;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
public class LanguageBoTest {
	@Autowired
	private LanguageBo languagesBo;

	private Language getLanguageByCode(String code) {
		return languagesBo.findByLocale(new Locale(code.split("_")[0], code.split("_")[1]));
	}

	@Test
	public void shouldGetFrLanguage() {
		String code = "fr_FR";
		assertEquals(code, getLanguageByCode(code).getCode());
	}

	@Test
	public void shouldGetUsLanguage() {
		String code = "en_US";
		assertEquals(code, getLanguageByCode(code).getCode());
	}

	@Test
	public void shouldReturnUsLanguageByDefault() {
		assertEquals("en_US", getLanguageByCode("omg_OMG").getCode());
	}
}
