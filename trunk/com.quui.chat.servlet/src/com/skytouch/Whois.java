package com.skytouch;
/*

 Package:    GeekTools Whois Java Client 1.0.1
 File:       Whois.java (Java source file)
 Author:     Erik C. Thauvin <erik@skytouch.com>
 Comments:   Part of the GeekTools Whois Java Client package.
 See the README.TXT file for more information.

 Copyright (C) 2000-2001 SkyTouch Communications. All Rights Reserved.

 This program is distributed under the terms of the GNU General
 Public License as published by the Free Software Foundation.
 See the COPYING.TXT file for more information.

 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Properties;

/**
 * Class Whois
 * 
 * 
 * @author Erik C. Thauvin (erik@skytouch.com)
 * @version 1.0.1
 */
public class Whois {
	private static boolean isFromCologne = false;
	public Whois() {

	}
	private static boolean isFromTurkishTelecom = false;

	public boolean isFromTurkishTelecom() {
		return isFromTurkishTelecom;
	}

	/**
	 * Method main
	 * 
	 * The Truth is Out There!
	 * 
	 * @param args
	 *            Command line arguments
	 */
	public static void main(String[] args) {
		System.out.println(doWhois(args));
	}

	public static String doWhois(String[] args) {
		isFromCologne = false; isFromTurkishTelecom = false;
		StringBuffer buf = new StringBuffer();
		// Display usage if there are no command line arguments
		if (args.length < 1) {
			buf = new StringBuffer("Usage: java Whois query[@<whois.server>]");

			return buf.toString();
		}

		// Default server is whois.geektools.com
		String server = "whois.geektools.com";
		// Default server port is 43
		int port = 43;

		// Load the properties file.
		try {
			FileInputStream in = new FileInputStream("Whois.properties");
			Properties app = new Properties();

			app.load(in);

			// Get the server property
			server = (app.getProperty("server", server));

			// Get the port property
			try {
				port = Integer.parseInt(app.getProperty("port"));
			} catch (NumberFormatException e) {
				// Do nothing!
			}

			in.close();
		} catch (FileNotFoundException e) {
			// Do nothing!
		} catch (IOException e) {
			System.err
					.println("Whois: an error occurred while loading the properties file: "
							+ e);
		}

		// Build the whois query using command line arguments
		StringBuffer buff = new StringBuffer(args[0]);

		for (int i = 1; i < args.length; i++) {
			buff.append(" " + args[i]);
		}

		// Convert string buffer to string
		String query = buff.toString();

		// The whois server can be specified after "@"
		// e.g.: query@whois.networksolutions.com
		int at = query.lastIndexOf("@");
		int len = query.length();

		if ((at != -1)) {
			// Remove the @ if last character in query
			// e.g.: john@doe.com@
			if (at == (len - 1)) {
				query = query.substring(0, len - 1);
			} else {
				// The whois server is specified after "@"
				server = query.substring(at + 1, len);
				// The whois query is specified before "@"
				query = query.substring(0, at);
			}
		}

		try {
			// Establish connection to whois server & port
			Socket connection = new Socket(server, port);
			PrintStream out = new PrintStream(connection.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line = "";

			// Send the whois query
			out.println(query);
			// Display the whois server's address & port
			System.out.println("[" + server + ":" + port + "]");

			// Read/display the query's result
			while ((line = in.readLine()) != null) {
				buf.append(line + "\n");
				if (line.matches("^descr:.*$")
						&& line.toLowerCase().indexOf("turktelecom") != -1) {
					isFromTurkishTelecom = true;
				}
				if (line.matches("^descr:.*$")
						&& line.toLowerCase().indexOf("cologne") != -1) {
					isFromCologne = true;
				}
			}
		} catch (java.net.UnknownHostException e) {
			// Unknown whois server
			System.err.println("Whois: unknown host: " + server);

			return buf.toString();
		} catch (IOException e) {
			// Could not connect to whois server
			System.err.println("Whois: " + e);

			return buf.toString();
		}
		return buf.toString();
	}

	public boolean isFromCologne() {
		// TODO Auto-generated method stub
		return isFromCologne;
	}
}
