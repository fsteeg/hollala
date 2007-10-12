package com.quui.chat.ui;

import com.quui.chat.commands.ExternalSolution;

public class HollalaSolution extends ExternalSolution {

	private Hollala hollala;

	public HollalaSolution() {
		this.hollala = new Hollala("config/hollala.properties");
	}

	@Override
	public String solve(String in) {
		return hollala.converse(null, "susi", in.toLowerCase().trim(), true);
		// return super.solve(command);
	}

}
