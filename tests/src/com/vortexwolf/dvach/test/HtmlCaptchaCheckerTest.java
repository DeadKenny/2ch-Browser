package com.vortexwolf.dvach.test;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import com.vortexwolf.dvach.interfaces.IHtmlCaptchaChecker;
import com.vortexwolf.dvach.interfaces.IHttpStringReader;
import com.vortexwolf.dvach.services.domain.HtmlCaptchaChecker;
import com.vortexwolf.dvach.services.presentation.DvachUriBuilder;
import com.vortexwolf.dvach.test.R;

import android.net.Uri;
import android.test.InstrumentationTestCase;

public class HtmlCaptchaCheckerTest extends InstrumentationTestCase {

	private final DvachUriBuilder mDvachUriBuilder = new DvachUriBuilder(Uri.parse("http://2ch.hk"));
	
	public void testCanSkip(){
		String responseText = "OK";
		
		IHtmlCaptchaChecker checker = new HtmlCaptchaChecker(new FakeHttpStringReader(responseText), mDvachUriBuilder);
		HtmlCaptchaChecker.CaptchaResult result = checker.canSkipCaptcha(null);
		
		assertTrue(result.canSkip);
	}
	
	public void testMustEnter(){
		String responseText = "CHECK\nSomeKey";
		
		IHtmlCaptchaChecker checker = new HtmlCaptchaChecker(new FakeHttpStringReader(responseText), mDvachUriBuilder);
		HtmlCaptchaChecker.CaptchaResult result = checker.canSkipCaptcha(null);
		
		assertFalse(result.canSkip);
		assertEquals("SomeKey", result.captchaKey);
	}
	
	private class FakeHttpStringReader implements IHttpStringReader{

		private final String mResponse;
		public FakeHttpStringReader(String response){
			this.mResponse = response;
		}
		
		@Override
		public String fromUri(String uri) {
			return mResponse;
		}

		@Override
		public String fromResponse(HttpResponse response) {
			return mResponse;
		}

		public String fromUri(String uri, Header[] customHeaders) {
			return null;
		}
	}
}
