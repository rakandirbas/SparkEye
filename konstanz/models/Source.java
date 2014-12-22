package de.uni.konstanz.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Source {
	String name;
	String url;

	public static Source getSource(String s) {
		Source source = new Source();
		String regex = "<a.*href=\"(.*)\" .*>(.*)</a>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(s);
		if ( matcher.find() ) {
			source.url = matcher.group(1);
			source.name = matcher.group(2);
		}
		else {
			source.name = "web";
		}
		return source;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


}