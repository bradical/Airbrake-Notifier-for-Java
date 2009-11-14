// Modified or written by Luca Marrocco for inclusion with hoptoad.
// Copyright (c) 2009 Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package code.lucamarrocco.hoptoad;

public class NoticeApi2 {

	private final StringBuilder stringBuilder = new StringBuilder();

	public NoticeApi2(HoptoadNotice notice) {
		notice("2.0.0");
		{
			apikey(notice);

			notifier();
			{
				name("hoptoad");
				version("1.7");
				url("http://hoptoad.googlecode.com");
			}
			end("notifier");

			error();
			{
				tag("class", notice.errorClass());
				tag("message", notice.errorMessage());

				backtrace();
				{
					for (final String backtrace : notice.backtrace()) {
						line(backtrace);
					}
				}
				end("backtrace");
			}
			end("error");
		}
		end("notice");
	}

	private void apikey(HoptoadNotice notice) {
		tag("api-key");
		{
			append(notice.apiKey());
		}
		end("api-key");
	}

	private void append(String str) {
		stringBuilder.append(str);
	}

	private void backtrace() {
		tag("backtrace");
	}

	private void end(String string) {
		append("</" + string + ">");
	}

	private void error() {
		tag("error");
	}

	private void line(String backtrace) {
		append(new BacktraceLine(backtrace).toXml());
	}

	private void name(String name) {
		tag("name", name);
	}

	private void notice(String string) {
		append("<?xml version=\"1.0\"?>");
		append("<notice version=\"" + string + "\">");
	}

	private void notifier() {
		tag("notifier");
	}

	private void notifier(String name, String version, String url) {
		notifier();
		{
			name(name);
			version(version);
			url(url);
		}
		end("notifier");
	}

	private NoticeApi2 tag(String string) {
		append("<" + string + ">");
		return this;
	}

	private void tag(String string, String name2) {
		tag(string).text(name2).end(string);
	}

	private NoticeApi2 text(String string) {
		append(string);
		return this;
	}

	public String toString() {
		return stringBuilder.toString();
	}

	private void url(String url) {
		tag("url", url);
	}

	private void version(String version) {
		tag("version", version);
	}
}
