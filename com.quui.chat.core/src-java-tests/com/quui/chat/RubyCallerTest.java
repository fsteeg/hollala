/** 
 Project "com.quui.chat.core" (C) 2006 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.quui.chat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.quui.chat.commands.RubyCaller;

public class RubyCallerTest {

    private RubyCaller caller;

    @Before
    public void init() {
        caller = new RubyCaller("src-ruby-tests/ruby-test-data");
    }

    @Test
    public void call() {
        String string = caller.exec("!tuennes", "testing1");
        Assert.assertEquals("Testing1", string);
        string = caller.exec("!kappes", "testing2");
        Assert.assertEquals("Testing2", string);
        string = caller.exec("kappesdudu", "testing2");
        Assert.assertEquals("", string);
    }

    @Test
    public void callAll() {
        for (String command : caller.getMap().keySet()) {
            String string = caller.exec(command, "testparam");
            Assert.assertEquals("Testparam", string);
        }
    }

}
