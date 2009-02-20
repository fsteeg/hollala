package com.quui.chat.ui;

import com.quui.chat.Preprocessor;
import com.quui.chat.commands.ExternalSolution;

public class HollalaSolution extends ExternalSolution {

    private final Hollala hollala;
    private final String botName;
    private String userName;

    public HollalaSolution(String botName, String userName) {
        this.hollala = new Hollala("config/hollala.properties");
        this.botName = botName;
        this.userName = userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    @Override
    public String solve(String in) {
        in = in.toLowerCase().trim();
        in = Preprocessor.clean(in, botName.toLowerCase());
        return hollala.converse(null, userName, in, true);
    }
}
