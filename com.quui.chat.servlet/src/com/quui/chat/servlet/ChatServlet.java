//package com.quui.chat.servlet;
//
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Date;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.jdom.Attribute;
//import org.jdom.Document;
//import org.jdom.Element;
//import org.jdom.output.Format;
//import org.jdom.output.XMLOutputter;
//
//import com.quui.chat.Log;
//import com.quui.chat.StaticResources;
//import com.quui.chat.mind.Mind;
//import com.skytouch.Whois;
//
///**
// * Servlet implementation class for Servlet: ChatServlet
// *
// */
// public class ChatServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
//     static String HOME = "Users";
//	 static final String TOPICS = "/" + HOME + "/fsteeg/bin/dyirbal/config/";
//
//		static final String WN = "/" + HOME + "/fsteeg/bin/dyirbal/wn/file_properties_unix.xml";
//
//		static final String LOGS = "/" + HOME + "/fsteeg/logs";
//
//		Document dom;
//
//		Element conversation;
//
//		String infoText = "welcome! when you talk about something i think i know about i'll be :) and try to talk to you on that topic. "
//				+ "also i will try to learn what you say about that topic and related topics. if you talk about something thats new to me "
//				+ "i'll be :o and try to learn it. you can also talk to me on IRC at irc.freenode.net in channel #hollala. have fun!";
//
//		String lastChatAnswer = "hi there!";
//
//		String lastTopicName = "";
//
//		Mind mind;
//
//		String fileName;
//
//		String status = ":|";
//
//		public String getServletInfo() {
//			return "Chat";
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see javax.servlet.GenericServlet#init()
//		 */
//		public void init() throws ServletException {
//			super.init();
//		}
//
//		protected void doGet(HttpServletRequest request,
//				HttpServletResponse response) throws IOException, ServletException {
//			String nextURL = request.getParameter("nextURL");
//			if (nextURL == null) {
//				nextURL = "/";
//			}
//			System.out.println("remote: " + request.getRemoteAddr());
//			System.out.println(Whois
//					.doWhois(new String[] { request.getRemoteAddr() }));
//			// always load topics from file when starting to chat:
//			initChat();
//			lastChatAnswer = StaticResources.getMindInstance(TOPICS, WN, LOGS)
//					.processConversation("hello");
//			lastTopicName = StaticResources.getMindInstance(TOPICS, WN, LOGS)
//					.getLastTopicName().split(";")[0];
//			conversation = new Element("ul");
//			Element chatli = new Element("li").addContent("Hollala: "
//					+ lastChatAnswer);
//			conversation.addContent(chatli);
//
//			response.setContentType("text/html");
//			PrintWriter pw = response.getWriter();
//
//			dom = buildDOM();
//			XMLOutputter outp = new XMLOutputter();
//			try {
//				outp.setFormat(Format.getPrettyFormat());
//				outp.output(dom, System.out);
//				outp.output(dom, pw);
////				Document infoDom = buildInfoDom(request);
////				outp.setFormat(Format.getPrettyFormat());
//				
//				String loc = null;
//				if (System.getProperty("os.name").indexOf("Windows") != -1) {
//					loc = "E:\\\\tomcat\\Tomcat 5.0\\webapps\\ROOT\\chatlogs\\";
//				} else {
//					loc = "/usr/local/jakarta-tomcat-5.0.28/webapps/ROOT/chatlogs/";
//				}
//				
//				BufferedWriter wInfo = new BufferedWriter(new FileWriter(loc
//						+ fileName + "-info.txt"));
//				wInfo.write(Whois.doWhois(new String[] { request.getRemoteAddr() }));
//				wInfo.close();
////				outp.output(infoDom, wInfo);
//			} catch (IOException x) {
//				x.printStackTrace();
//			}
//		}
//
//		/**
//		 * @return
//		 */
//		private Document buildDOM() {
//			Document dom;
//
//			Element root = new Element("html");
//			dom = new Document(root);
//			Element body = new Element("body");
//			Element headerTable = new Element("table");
//			headerTable.setAttribute("width", "100%");
//			Element headerRow = new Element("tr");
//			Element welcome = new Element("font");
//			welcome.setAttribute("size", "1");
//			welcome.setText("Welcome to Hollala!");
//			Element welcomeData = new Element("td");
//			welcomeData.addContent(welcome);
//			welcomeData.setAttribute("align", "left");
//			Element dates = new Element("font");
//			Element homeLink = new Element("a");
//			homeLink.setAttribute("href", "http://www.quui.com/hollala");
//			homeLink.addContent(dates);
//			dates.setAttribute("size", "1");
//			dates.setText("home");
//			Element datesData = new Element("td");
//			datesData.addContent(homeLink);
//			datesData.setAttribute("align", "right");
//			headerRow.addContent(welcomeData);
//			headerRow.addContent(datesData);
//			headerTable.addContent(headerRow);
//
//			Element hr = new Element("hr");
//			hr.setAttribute("size", "1");
//
//			body.addContent(headerTable);
//			body.addContent(hr);
//
//			Element hr2 = new Element("hr");
//			hr2.setAttribute("size", "1");
//
//			Element table = new Element("table");
//			table.setAttribute("width", "100%");
//			body.addContent(table);
//			Element tr = new Element("tr");
//			table.addContent(tr);
//			Element tdPic = new Element("td");
//			tdPic.setAttribute("align", "right");
//			Element tdChat = new Element("td");
//			tdChat.setAttribute("align", "left");
//
//			tr.addContent(tdChat);
//			tr.addContent(tdPic);
//			body.setAttribute(new Attribute("onload",
//					"document.form.answer.focus();"));
//			Element br = new Element("br");
//			// Element p = new Element("p");
//			// p.setAttribute(new Attribute("align", "center"));
//			Element chatAnswer = new Element("h2");
//			chatAnswer.setText(lastChatAnswer);
//			Element form = new Element("form");
//			form.setAttribute(new Attribute("name", "form"));
//			form.setAttribute(new Attribute("method", "post"));
//			Element input = new Element("input");
//			input.setAttribute(new Attribute("name", "answer"));
//			input.setAttribute(new Attribute("size", "60"));
//			Element history = new Element("font");
//			Element pHist = new Element("p");
//			// pHist.setAttribute("align", "right");
//			pHist.addContent(history);
//			history.setAttribute(new Attribute("size", "1"));
//			history.addContent(conversation.cloneContent());
//			Element info = new Element("p");
//			Element infoFont = new Element("font");
//			infoFont.setAttribute(new Attribute("size", "1"));
//			infoFont.setText(infoText);
//			info.addContent(infoFont);
//			// Element save = new Element("input");
//			// input.setAttribute(new Attribute("type", "submit"));
//			// input.setAttribute(new Attribute("name", "save"));
//			// input.setAttribute(new Attribute("value", "save map"));
//			// form.addContent(save);
//			Element img = new Element("img");
//			img.setAttribute("src", "/hollala.jpg");
//			// img.setAttribute("border", "2");
//			tdPic.addContent(img);
//			form.addContent(input);
//			// body.addContent(br);
//			tdChat.setAttribute("width", "70%");
//			tdChat.addContent(info);
//			tdChat.addContent(chatAnswer);
//			tdChat.addContent(form);
//			// body.addContent(save);
//			Element topic = new Element("font");
//			topic.setAttribute("size", "1");
//
//			topic.setText(" | Topic: " + lastTopicName + " | Status: " + status);
//			welcomeData.addContent(topic);
//			body.addContent(hr2);
//
//			Element footerTable = new Element("table");
//			footerTable.setAttribute("width", "100%");
//			Element footerRow = new Element("tr");
//
//			Element convUL = new Element("li");
//			// Element conv = new Element("font");
//			// conv.setAttribute("size", "1");
//			//        
//			// conv.setText("some selected conversation");
//			//
//			// Element histData = new Element("td");
//			//
//			// Element linkToConv = new Element("a");
//			// linkToConv.setAttribute("href", "/conv");
//			// linkToConv.addContent(conv);
//			// convUL.addContent(linkToConv);
//			//
//			// Element techUL = new Element("li");
//			// Element tech = new Element("font");
//			// tech.setAttribute("size", "1");
//			// tech.setText("some more info");
//			//
//			// Element linkToTech = new Element("a");
//			// linkToTech.setAttribute("href", "/tech");
//			// linkToTech.addContent(tech);
//			// techUL.addContent(linkToTech);
//			//
//			// histData.addContent(techUL);
//			// // histData.addContent(new Element("br"));
//			// histData.addContent(convUL);
//
//			// histData.setAttribute("align", "left");
//			Element ircAdress = new Element("font");
//			ircAdress.setAttribute("size", "1");
//			ircAdress.setText("current conversation log");
//			Element bottomRight = new Element("td");
//			bottomRight.addContent(ircAdress);
//			bottomRight.setAttribute("align", "right");
//			// footerRow.addContent(histData);
//			footerRow.addContent(bottomRight);
//			footerTable.addContent(footerRow);
//			body.addContent(footerTable);
//			// body.addContent(new Element("br"));
//			body.addContent(new Element("table").setAttribute("width", "100%")
//					.addContent(
//							new Element("tr").addContent(new Element("td")
//									.setAttribute("align", "right").addContent(
//											pHist))));
//
//			root.addContent(body);
//			return dom;
//		}
//
//		protected void doPost(HttpServletRequest request,
//				HttpServletResponse response) throws IOException, ServletException {
//			// System.out.println("para-save: " + request.getParameter("save"));
//			// if(request.getParameter("save")!=null){
//			// try {
//			// talk.saveMap();
//			// } catch (ParserConfigurationException e) {
//			// // TODO Auto-generated catch block
//			// e.printStackTrace();
//			// }
//			// }
//			// else{
//			PrintWriter pw = response.getWriter();
//
//			response.setContentType("text/html");
//			XMLOutputter outp = new XMLOutputter();
//			response.setStatus(response.SC_ACCEPTED);
//			String answer = request.getParameter("answer");
//			// String conversation = request.getParameter("conversation");
//
//			// conversation = conversation + "\n" + " you: " + answer;
//
//			String chatAnswer = "***";
//			chatAnswer = StaticResources.getMindInstance(TOPICS, WN, LOGS)
//					.processConversation(answer);
//			lastTopicName = StaticResources.getMindInstance(TOPICS, WN, LOGS)
//					.getLastTopicName().split(";")[0];
//
//			status = StaticResources.getMindInstance(TOPICS, WN, LOGS).getEmotion();
//
//			Element userli = new Element("li").setText("User: " + answer);
//			Element chatli = new Element("li").addContent("Hollala: " + chatAnswer);
//
//			conversation.addContent(userli);
//			conversation.addContent(chatli);
//
//			// conversation = conversation + "\n" + chatAnswer;
//			lastChatAnswer = chatAnswer;
//
//			response.setContentType("text/html");
//			// initChat();
//			dom = buildDOM();
//			outp = new XMLOutputter();
//			try {
//				outp.setFormat(Format.getPrettyFormat());
//				// outp.output(dom, System.out);
//				outp.output(dom, pw);
//				// while user already sees new page, save the map to disk
//				StaticResources.getMindInstance(TOPICS, WN, LOGS).saveMap(
//						"topics-hollala.xml");
//				String loc = null;
//				if (System.getProperty("os.name").indexOf("Windows") != -1) {
//					loc = "E:\\\\tomcat\\Tomcat 5.0\\webapps\\ROOT\\chatlogs\\";
//				} else {
//					loc = "/usr/local/jakarta-tomcat-5.0.28/webapps/ROOT/chatlogs/";
//				}
//				Log.logger.fine("writing conversation to " + loc);
//
//				BufferedWriter w = new BufferedWriter(new FileWriter(loc + fileName
//						+ ".html"));
//				outp.output(dom, w);
//			} catch (Exception x) {
//				x.printStackTrace();
//			}
//
//			// System.out.println("conversation in post: " + conversation);
//			// System.out.println("answer in post: " + answer);
//			// }
//		}
//
////		private Document buildInfoDom(HttpServletRequest request) {
////			Document dom;
//	//
////			Element root = new Element("html");
////			dom = new Document(root);
////			Element body = new Element("body");
//	//
////			Element p = new Element("p");
//	//
////			p.setText(Whois.doWhois(new String[] { request.getRemoteAddr() }));
//	//
////			body.addContent(p);
//	//
////			root.addContent(body);
////			return dom;
////		}
//
//		private void initChat() {
//			Date date = new Date();
//
//			fileName = date.toString().replace(' ', '-').replace(':', '-');
//			StaticResources.init(TOPICS, WN, LOGS);
//		}
//
//	}