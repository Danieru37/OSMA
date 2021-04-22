## Project Title ##
Online Shopping with Marketing Analysis

## Project Description ##
This software is an online shopping software for both the customer and supplier. For customers, this program aids them in the transaction process. As for the suppliers, this program aids them in editing their sold product’s information. Also, this program views their marketing analysis within the platform in order for them to be acknowledged about their business and competition status.

## Requirements and/or Dependencies used ##
Android Studio 4.1.3. IDE, Javamail API (activation.jar, additional.jar and mail.jar), Firestore account and database platform itself, Android Emulator with virtual device named Android Studio Pixel 3a API 30 x86.

## Environmental setup to run the project ##

#### Setting up an Android Studio project ####
- Click File > New > New Project
- In Choose your Project, select the type of project you want to create from categories of device form factors and click Next
- Specify the Name of your project.
- Specify the Package name. By default, this package name also becomes your application ID, which you can change later.
- Specify the Save location where you want to locally store your project.
- Select the Language you want Android Studio to use when creating sample code for your new project.
- Select the Minimum API level you want your app to support. When you select a lower API level, your app can rely on fewer modern Android APIs. However, a larger percentage of Android devices are able to run your app. The opposite is true when selecting a higher API level. If you want to see more data to help you decide, click Help me choose.
- When you're ready to create your project, click Finish.

#### Connection with Cloud Firestore database ####
- For instrumentation of the app to talk to the emulators (Android, iOS, and Web SDKs). Set up your in-app configuration or test classes to interact with Cloud Firestore as follows:<br/>

// For Web<br/>
// Initialize your Web app as described in the Get started for Web<br/>
// Firebase previously initialized using firebase.initializeApp().<br/>
var db = firebase.firestore();<br/>
if (location.hostname === "localhost") {<br/>
  db.useEmulator("localhost", 8080);<br/>
}<br/>
//End of Code<br/>

- For instrumentation of the app to talk to the emulators (Admin SDKs). Set up your in-app configuration or test classes to interact with Cloud Firestore as follows: The Firebase Admin SDKs automatically connect to the Cloud Firestore emulator when the FIRESTORE_EMULATOR_HOST environment variable is set:
export FIRESTORE_EMULATOR_HOST="localhost:8080"

- If your code is running inside the Cloud Functions emulator your project ID and other configuration will be automatically set when calling initalizeApp. When connecting to the Cloud Firestore emulator from any other environment, you will need to specify a project ID. You can pass a project ID to initializeApp directly or set the GCLOUD_PROJECT environment variable. Note that you do not need to use your real Firebase project ID; the Cloud Firestore emulator will accept any project ID, as long as it has a valid format:
export GCLOUD_PROJECT="your-project-id" (for environmental variable)

- For clearing the database between tests. Production Firestore provides no platform SDK method for flushing the database, but the Firestore emulator gives you a REST endpoint specifically for this purpose, which can be called from a test framework setup/tearDown step, from a test class, or from the shell (e.g., with curl) before a test is kicked off. You can use this approach as an alternative to simply shutting down the emulator process. In an appropriate method, perform an HTTP DELETE operation, supplying your Firebase projectID, for example firestore-emulator-example, to the following endpoint: "http://localhost:8080/emulator/v1/projects/firestore-emulator-example/databases/(default)/documents"

- Naturally, your code should await REST confirmation that the flush finished or failed. You can perform this operation from the shell:
$ curl -v -X DELETE "http://localhost:8080/emulator/v1/projects/firestore-emulator-example/databases/(default)/documents"

- Having implemented a step like this, you can sequence your tests and trigger your functions with confidence that old data will be purged between runs and you're using a fresh baseline test configuration.
- For importing and exporting of data, The database emulators allow you to export data from a running emulator instance. Define a baseline set of data to use in your unit tests or continuous integration workflows, then export it to be shared among the team:
firebase emulators:export ./dir

- In tests, on emulator startup, import the baseline data:
firebase emulators:start --import=./dir

- You can instruct the emulator to export data on shutdown, either specifying an export path or simply using the path passed to the --import flag.
firebase emulators:start --import=./dir --export-on-exit

- These data import and export options work with the firebase emulators:exec command as well
- For visualize rules evaluation, As you add Security Rules to your prototype you can debug them with Local Emulator Suite debug tools. After running a suite of tests, you can access test coverage reports that show how each of your security rules was evaluated. To get the reports, query an exposed endpoint on the emulator while it's running. For a browser-friendly version, use the following URL: http://localhost:8080/emulator/v1/projects/<database_name>:ruleCoverage.html

- This breaks your rules into expressions and subexpressions that you can mouseover for more information, including number of evaluations and values returned. For the raw JSON version of this data, include the following URL in your query: http://localhost:8080/emulator/v1/projects/<database_name>:ruleCoverage

- Here, the HTML version of the report highlights evaluations that throw undefined and null-value errors:
![image](https://user-images.githubusercontent.com/82215248/115719335-925ae700-a3ae-11eb-958a-5067ecd03433.png)

#### Setting up an Android Studio project: ####
- Download Javamail API for android in this site: http://code.google.com/p/javamail-android/

- First step is to define a JSSE (Java Security Socket Extension) Provider. To make that, we can get JSSE Provider of the Harmony project:<br/>
//Start of Code<br/>
public final class JSSEProvider extends Provider {<br/>
 public JSSEProvider() {<br/>
   super("HarmonyJSSE", 1.0, "Harmony JSSE Provider");<br/>
   AccessController.doPrivileged(new java.security.PrivilegedAction<Void>() {<br/>
     public Void run() {<br/>
       put("SSLContext.TLS", "org.apache.harmony.xnet.provider.jsse.SSLContextImpl");<br/>
       put("Alg.Alias.SSLContext.TLSv1", "TLS");<br/>
       put("KeyManagerFactory.X509", "org.apache.harmony.xnet.provider.jsse.KeyManagerFactoryImpl");<br/>
       put("TrustManagerFactory.X509", "org.apache.harmony.xnet.provider.jsse.TrustManagerFactoryImpl");<br/>
       return null;<br/>
     }<br/>
   });<br/>
 }<br/>
}<br/>
//End of Code<br/>
  
- To transport data during sending email, you must implement javax.activation.DataSource .Here, we choose to create a byte array implementation:<br/>
//Start of Code<br/>
public class ByteArrayDataSource implements DataSource {<br/>
 private byte[] data;<br/>
 private String type;<br/>
 public ByteArrayDataSource(byte[] data, String type) {<br/>
   super();<br/>
   this.data = data;<br/>
   this.type = type;<br/>
 }<br/>
 public ByteArrayDataSource(byte[] data) {<br/>
   super();<br/>
   this.data = data;<br/>
 }<br/>
 public void setType(String type) {<br/>
   this.type = type;<br/>
 }<br/>
 public String getContentType() {<br/>
   if (type == null)<br/>
     return "application/octet-stream";<br/>
   else<br/>
     return type;<br/>
 }<br/>
 public InputStream getInputStream() throws IOException {<br/>
   return new ByteArrayInputStream(data);<br/>
 }<br/>
 public String getName() {<br/>
   return "ByteArrayDataSource";<br/>
 }<br/>
 public OutputStream getOutputStream() throws IOException {<br/>
   throw new IOException("Not Supported");<br/>
 }<br/>
}<br/>
//End of Code<br/>

- Third step is to create email sender object that will contain all the logic to send email. Here, we’re going to use GMail as SMTP server. So, class will be named GMailSender:<br/>
//Start of Code<br/>
public class GMailSender extends javax.mail.Authenticator {<br/>
 private String mailhost = "smtp.gmail.com";<br/>
 private String user;<br/>
 private String password;<br/>
 private Session session;<br/>
 static {<br/>
   Security.addProvider(new JSSEProvider());<br/>
 }<br/>
 public GMailSender(String user, String password) {<br/>
   this.user = user;<br/>
   this.password = password;<br/>
   Properties props = new Properties();<br/>
   props.setProperty("mail.transport.protocol", "smtp");<br/>
   props.setProperty("mail.host", mailhost);<br/>
   props.put("mail.smtp.auth", "true");<br/>
   props.put("mail.smtp.port", "465");<br/>
   props.put("mail.smtp.socketFactory.port", "465");<br/>
   props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");<br/>
   props.put("mail.smtp.socketFactory.fallback", "false");<br/>
   props.setProperty("mail.smtp.quitwait", "false");<br/>
   session = Session.getDefaultInstance(props, this);<br/>
 }<br/>
 protected PasswordAuthentication getPasswordAuthentication() {<br/>
   return new PasswordAuthentication(user, password);<br/>
 }<br/>
 public synchronized void sendMail(String subject, String body,<br/>
   String sender, String recipients) throws Exception {<br/>
   MimeMessage message = new MimeMessage(session);<br/>
   DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));<br/>
   message.setSender(new InternetAddress(sender));<br/>
   message.setSubject(subject);<br/>
   message.setDataHandler(handler);<br/>
   if (recipients.indexOf(',') > 0)<br/>
     message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));<br/>
   else<br/>
     message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));<br/>
   Transport.send(message);<br/>
 }<br/>
}<br/>
//End of Code<br/>

- In a static block, we add our JSSE Provider to Security class. Like our email sender is specialized for GMail, mailhost is hard coded. So, constructor takes only user and password to authenticate to SMTP server. In constructor, we define all the Properties used during the Session which we get a default instance. You need also to override getPasswordAuthentication method and you must return a PasswordAuthentication instance that use user / password entered in constructor.
- Last step is the sendMail method that takes as arguments subject, body, sender and recipients. Here, we use MimeMessage associated to current Session. Email body is transported inside our ByteArrayDataSource previously created. To send email, we finish by call static method Transport.send with message in parameter.
To send an email, don’t forget to add Internet permission to your AndroidManifest:
<uses-permission android:name=”android.permission.INTERNET” />:

- It’s also important to send email in a separate Thread to avoid NetworkOnMainException. So, code is here:<br/>
//Start of Code<br/>
new Thread(new Runnable() {<br/>
 @Override<br/>
 public void run() {<br/>
   try {<br/>
     GMailSender sender = new GMailSender("sylvain.saurel@gmail.com",<br/>
        "your_password");<br/>
     sender.sendMail("Hello from JavaMail", "Body from JavaMail",<br/>
        "sylvain.saurel@gmail.com", "sylvain.saurel@gmail.com");<br/>
   } catch (Exception e) {<br/>
     Log.e("SendMail", e.getMessage(), e);<br/>
 }<br/>
}<br/>
}).start();<br/>
//End of Code<br/>

- To test the application, you must authorize external access to your GMail account by going to this page : https://www.google.com/settings/security/lesssecureapps. Then, you must enable less secure apps:
![image](https://user-images.githubusercontent.com/82215248/115721142-588ae000-a3b0-11eb-9c1d-36c6d9567a0b.png)

- Now, you can launch your Android application and check that GMailSender works pretty well:
![image](https://user-images.githubusercontent.com/82215248/115721224-6c364680-a3b0-11eb-85a2-6bcb206b0d5f.png)

## Revision Log ##<br/>
- Version 4.0
- Description: This is the fourth revision, where there are quite many revisions speaking of content of requirements and GUI design.
- Date Completed: 04/22/2021

## Information about Contributors ##<br/>
- Beltran, Paul Joshua C.
  BSCpE Student in De La Salle University
  paul_beltran@dlsu.edu.ph
- De Leon, Daniel Gabriel B.
  BSCpE Student in De La Salle University
  daniel_deleon@dlsu.edu.ph
