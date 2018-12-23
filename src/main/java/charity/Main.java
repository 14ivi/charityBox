package charity;

import io.restassured.response.Response;

import java.io.*;
import java.util.Properties;

import static io.restassured.RestAssured.get;

public class Main {

    private final static String PROPS_FILE = "C:/Projects/charityBox/target/email.properties";

    public static void main(String[] args) throws InterruptedException {
//        int i = 0;
        Response response = get("https://charitybox.ru/gotovye-korobochki/");
        while (response.getStatusCode() == 404) {
            System.out.println(response.getStatusCode());
            Thread.sleep(20000);
        }
        sendEmail("Коробочки появились", "Срочно проверить коробочки");

    }

    private static void sendEmail(String title, String text) {
        try {
            InputStream is = new FileInputStream(PROPS_FILE);
            if (is != null) {
                Reader reader = new InputStreamReader(is, "UTF-8");
                Properties pr = new Properties();
                pr.load(reader);
                SendEmail.SMTP_SERVER = pr.getProperty("server");
                SendEmail.SMTP_Port = pr.getProperty("port");
                SendEmail.EMAIL_FROM = pr.getProperty("from");
                SendEmail.SMTP_AUTH_USER = pr.getProperty("user");
                SendEmail.SMTP_AUTH_PWD = pr.getProperty("pass");
                SendEmail.REPLY_TO = pr.getProperty("replyto");
                SendEmail.FILE_PATH = PROPS_FILE;

                String emailTo = pr.getProperty("to");
//                String thema   = pr.getProperty ("thema");
//                String text    = pr.getProperty ("text" );

                is.close();

                SendEmail se = new SendEmail(emailTo, title);
                se.sendMessage(text);
                System.out.println("Сообщение отправлено");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
