package com.quui.chat.ui;

import com.quui.chat.Preprocessor;
import com.quui.chat.commands.ExternalSolution;

public class HollalaSolution extends ExternalSolution {

	private Hollala hollala;
	private String name;

	public HollalaSolution(String name) {
		this.hollala = new Hollala("config/hollala.properties");
		this.name = name;
	}

	@Override
	public String solve(String in) {
		in = in.toLowerCase().trim();
		in = Preprocessor.clean(in, name.toLowerCase());
		return hollala.converse(null, "susi", in, true);
	}
}
