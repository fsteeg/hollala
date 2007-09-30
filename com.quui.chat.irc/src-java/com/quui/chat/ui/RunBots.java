/** 
 Project "com.quui.chat.core" (C) 2006 Fabian Steeg

 This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.quui.chat.ui;

/**
 * Runs Hollaka Hollala :D
 * 
 * @author Fabian Steeg (fsteeg)
 */
public class RunBots {

    /**
     * @param args
     *            Nothing or the two props files
     */
    public static void main(final String[] args) {
        boolean b = args != null && args.length == 2;
//        new Hollala(b ? args[0] : "config/hollala.properties");
//        //TODO: does this solve the problem on logging in on the other network?
//        //if not, please remove it
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //end section to be removed if it didn't help
        new Hollaka(b ? args[1] : "config/hollaka.properties");
    }

}
