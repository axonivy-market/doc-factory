package ch.ivyteam.ivy.addons.filemanager.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DateUtilTest {
	

	@Test
	public void formatTime_returns_formatedTime() {
		assertTrue(DateUtil.formatTime("18").equals("18:00:00"));
		assertTrue(DateUtil.formatTime("18:35").equals("18:35:00"));
		assertTrue(DateUtil.formatTime("48:35").equals("00:35:00"));
		assertTrue(DateUtil.formatTime("Hello:35:you").equals("00:35:00"));
		assertTrue(DateUtil.formatTime("45:35:76").equals("00:35:00"));
		assertTrue(DateUtil.formatTime("pibrivbq").equals("00:00:00"));
		assertTrue(DateUtil.formatTime("9:6:5").equals("09:06:05"));
		assertTrue(DateUtil.formatTime("9:6:5 567").equals("09:06:00"));
		assertTrue(DateUtil.formatTime("9:6:5:567").equals("09:06:05"));
	}

}
