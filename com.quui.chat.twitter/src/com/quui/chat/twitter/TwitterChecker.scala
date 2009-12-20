package com.quui.chat.twitter
import scala.xml._
import java.net._
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.methods.{PostMethod}
import org.apache.commons.httpclient.params.{HttpMethodParams}
import org.apache.commons.httpclient.cookie.CookiePolicy
import org.apache.commons.httpclient.Credentials
import org.apache.commons.httpclient.auth.AuthScope
import org.apache.commons.httpclient.UsernamePasswordCredentials
import com.quui.chat.Talk
import java.math._
import java.io._


/* Some prototypical playing with the Twitter REST API from Scala. Checks for mentions since last response, and replies. */

// For easy calling from Java:
class TwitterChecker {
    TwitterCheckerApp.main(Array())
}

object TwitterCheckerApp {
    
  //TODO put these values in a config file
  val deploy = false
  
  val Core = if (deploy) "" else "../com.quui.chat.engine/"
  val Account = if (deploy) "hollaka" else "hllhll"
  val Latest = Core + "config/latest.txt"
  
  def main(args : Array[String]) : Unit = { 
      println("----------------------------------")
      println("Checking Mentions")
      println("----------------------------------")
     
      val talk = new Talk(Core + "config", "topics-hollala.xml", Core + "config/file_properties.xml", Core + "log")
      oncePerSeconds(60 * 5, checkAndReply(Account, "dyirbal", talk))
      //TODO actors for second action: say random stuff rarely
  }
  
  def checkAndReply(name:String, pass:String, talk: Talk) = {
      val mentions :Seq[Status] = userMentions(name, pass, readLatest().toString)
      println("Replying to " + mentions.size + " mentions")
      var max:BigInteger = new BigInteger(readLatest().toString)
      var ok = false
      for(mention <- mentions) {
         val plainReply = replyFromMind(mention.text,talk).replaceAll("@"+name, "")
         val reply = formattedReplyTo(mention.userName, plainReply)
         println(reply)
         ok = sendStatusUpdate(name, pass, reply)
         if (ok) {  max = max max new BigInteger(mention.id);  writeLatest(max) }
         Thread sleep 1000 * 60 * 5
         sendStatusUpdate(name, pass, replyFromMind(plainReply, talk).replaceAll("@"+name, ""))
      }
      println("----------------------------------")
  }
  
  def formattedReplyTo(sender: String, message: String) = String.format("@%s %s", sender, message)
  
  def writeLatest(value:BigInteger) = {
      val file = new File(Latest)
      file.delete()
      val writer  = new FileWriter(file)
      writer.write(value.toString)
      writer.close
  }
  
  def readLatest():BigInteger={
      new BigInteger(scala.io.Source.fromFile(new File(Latest)).getLines("\n").mkString.trim)
  }

  def userMentions(login:String, password:String, since:String) = {
      // Simple way for GET: just a URL and some core Java auth stuff:
      Authenticator.setDefault(new BasicAuthentication(login, password))
      println("Getting mentions since: " + since)
      statusesFrom("http://twitter.com/statuses/mentions.xml?since_id="+since) 
  }
  
  def statusesFrom(loc:String) : Seq[Status] = {
      try {
          val timeline = XML.load(new URL(loc).openStream())
          for(status <- timeline \\ "status") 
              yield new Status((status \ "user" \ "screen_name").text, (status \ "text").text, (status \ "id").text)
      } catch {
          case e => e.printStackTrace(); List()
      }
  }
  
  class Status(val userName: String, val text: String, val id: String)
  
  private class BasicAuthentication(login:String, password:String) extends Authenticator {
      override def getPasswordAuthentication() = new PasswordAuthentication(login, password.toCharArray())
  }
  
  def sendStatusUpdate(login:String, password:String, message:String) : Boolean = {
      // The more complex way for POST: Apache HttpClient
      val client = createHttpClient(login, password)
      val post = postMethod(client, "http://twitter.com/statuses/update.xml", "status" -> message.substring(0,140 min message.length))
      println(post)
      Thread sleep 1000 * 60
      return post.toString == "200"
  }
  
  def createHttpClient(name:String, pass:String) = {
      val client = new HttpClient()
      client.getParams().setParameter(HttpMethodParams.USER_AGENT, "Hollaka Hollala")
      val creds : Credentials = new UsernamePasswordCredentials(name, pass)
      client.getState().setCredentials(AuthScope.ANY, creds)
      client.getParams().setAuthenticationPreemptive(true)
      client
  }
  
  def postMethod(client: HttpClient, url:String, param : (String,String)) : Int = {
      val post : PostMethod = new PostMethod("http://twitter.com/statuses/update.xml")
      post.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY)
      post.getParams().setContentCharset("utf-8")
      post.addParameter(param._1, param._2)
      try {
          val response = client.executeMethod(post)
          post.releaseConnection()
          return response
      } catch {
          case e => e.printStackTrace(); -1
      }
  }
  
  def replyFromMind(s:String, talk : Talk) : String = {
      talk.process(s, true)
  }
  
  def oncePerSeconds(s:Int, callback: => Unit/*, num: Int*/) {
      /*var count = num; */while(/*count > 0*/true) { callback; Thread sleep s * 1000; /* count = count - 1  */ }
  }
  
  // experimental stuff
  def printPublicStatuses() = 
      println(statusesFrom("http://twitter.com/statuses/public_timeline.xml") mkString "\n")
          
  def printUserStatuses(u:String) = 
      println(statusesFrom("http://twitter.com/statuses/user_timeline/"+u+".xml?count=200") mkString "\n")
  
}