package server;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;


//new Thread(new SendingThread(in)).start();
public class SendingThread implements Runnable {

    private ArrayList<String> neighbors = new ArrayList<>(Arrays.asList("7500", "8000", "8500"));
    private ArrayList<String> httpText;
    private String postData;
    private double lazyness;
    private String getData;
    private String requestMethod;
    private String fileType;
    private String messageId;

    public SendingThread(ArrayList<String> httpText, String postData) {
        this.httpText = httpText;
        this.postData = postData;
    }

    public void run() {
        //System.out.println(httpText);
        //System.out.println(postData);
        for (String string : httpText) {
            if (string.contains("HTTP/1.1")) {
                requestMethod = string;
            }
        }
        if (requestMethod.contains("POST")) {
            try {
                sendBack();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestMethod.contains("GET")) {
            if (getMyRequest()) {
                try {
                    download();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    sendForward();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendForward() throws IOException {
        System.out.println("GET - forward");
        System.out.println("Lazyness: "+ lazyness);
        for (String string : httpText) {
            if (string.contains("veebiaadress")) {
                this.getData = string.substring(14);
            }
            if (string.contains("messageid")) {
                this.messageId = string.substring(11);
            }
        }
        System.out.println(getData);
        System.out.println(messageId);
        for (String neighbor : neighbors) {
            URL url = new URL("http://localhost:"+neighbor);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("veebiaadress", getData);
            conn.setRequestProperty("messageid",messageId);
            conn.setDoOutput(true);
        }


//
//        Reader inasd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
    }

    public void sendBack() throws IOException {
        System.out.println("POST - back");
        System.out.println(httpText);
        System.out.println(postData);

//        ObjectMapper mapper1 = new ObjectMapper();
//        PostPackage package1 = mapper1.readValue(postData, PostPackage.class);
//        System.out.println(package1.getContent());
//        for (String neighbor : neighbors) {
//            URL url = new URL("http://localhost:"+neighbor);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "text/plain"); // ALATI EI SAA OLLA TEXT !!!!!
//            //SIIA TULEB BODY LISAMINE !!!!!!!
//            conn.setDoOutput(true);
//        }

//
//        Reader inasdasd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
    }

    public void download() throws IOException {
        System.out.println("GET - download");
        System.out.println("Lazyness: "+ lazyness);
        for (String string : httpText) {
            if (string.contains("veebiaadress")) {
                this.getData = string.substring(14);
            }
            if (string.contains("messageid")) {
                this.messageId = string.substring(11);
            }
        }
        System.out.println(getData);
        System.out.println(messageId);
        if (getData.contains(".jpg")) {
            this.fileType = "jpg";
        } else if (getData.contains(".mp3")) {
            this.fileType = "mp3";
        } else if (getData.contains(".pdf")) {
            this.fileType = "pdf";
        } else if (getData.contains(".gif")) {
            this.fileType = "gif";
        } else if (getData.contains(".txt")) {
            this.fileType = "txt";
        } else if (getData.contains(".htm")) {
            this.fileType = "htm";
        } else if (getData.contains(".html")) {
            this.fileType = "html";
        } else if (getData.contains(".doc")) {
            this.fileType = "doc";
        } else if (getData.contains(".rtf")) {
            this.fileType = "rtf";
        } else if (getData.contains(".wav")) {
            this.fileType = "wav";
        } else if (getData.contains(".7z")) {
            this.fileType = "7z";
        } else if (getData.contains(".tar.gz")) {
            this.fileType = "tar.gz";
        } else if (getData.contains(".deb")) {
            this.fileType = "deb";
        } else if (getData.contains(".rar")) {
            this.fileType = "rar";
        } else if (getData.contains(".zip")) {
            this.fileType = "zip";
        } else if (getData.contains(".bin")) {
            this.fileType = "bin";
        } else if (getData.contains(".iso")) {
            this.fileType = "iso";
        } else if (getData.contains(".csv")) {
            this.fileType = "csv";
        } else if (getData.contains(".dat")) {
            this.fileType = "dat";
        } else if (getData.contains(".db")) {
            this.fileType = "db";
        } else if (getData.contains(".log")) {
            this.fileType = "log";
        } else if (getData.contains(".torrent")) {
            this.fileType = "torrent";
        } else if (getData.contains(".mdb")) {
            this.fileType = "mdb";
        } else if (getData.contains(".sql")) {
            this.fileType = "sql";
        } else if (getData.contains(".tar")) {
            this.fileType = "tar";
        } else if (getData.contains(".xml")) {
            this.fileType = "xml";
        } else if (getData.contains(".js")) {
            this.fileType = "js";
        } else if (getData.contains(".jar")) {
            this.fileType = "jar";
        } else if (getData.contains(".apk")) {
            this.fileType = "apk";
        } else if (getData.contains(".bat")) {
            this.fileType = "bat";
        } else if (getData.contains(".bin")) {
            this.fileType = "bin";
        } else if (getData.contains(".cgi")) {
            this.fileType = "cgi";
        } else if (getData.contains(".pl")) {
            this.fileType = "pl";
        } else if (getData.contains(".exe")) {
            this.fileType = "exe";
        } else if (getData.contains(".py")) {
            this.fileType = "py";
        } else if (getData.contains(".ttf")) {
            this.fileType = "ttf";
        } else if (getData.contains(".bmp")) {
            this.fileType = "bmp";
        } else if (getData.contains(".ico")) {
            this.fileType = "ico";
        } else if (getData.contains(".jpeg")) {
            this.fileType = "jpeg";
        } else if (getData.contains(".jpg")) {
            this.fileType = "jpg";
        } else if (getData.contains(".png")) {
            this.fileType = "png";
        } else if (getData.contains(".asp")) {
            this.fileType = "asp";
        } else if (getData.contains(".aspx")) {
            this.fileType = "aspx";
        } else if (getData.contains(".cer")) {
            this.fileType = "cer";
        } else if (getData.contains(".css")) {
            this.fileType = "css";
        } else if (getData.contains(".jsp")) {
            this.fileType = "jsp";
        } else if (getData.contains(".php")) {
            this.fileType = "php";
        } else if (getData.contains(".rss")) {
            this.fileType = "rss";
        } else if (getData.contains(".xhtml")) {
            this.fileType = "xhtml";
        } else if (getData.contains(".key")) {
            this.fileType = "key";
        } else if (getData.contains(".pps")) {
            this.fileType = "pps";
        } else if (getData.contains(".ppt")) {
            this.fileType = "ppt";
        } else if (getData.contains(".class")) {
            this.fileType = "class";
        } else if (getData.contains(".java")) {
            this.fileType = "java";
        } else if (getData.contains(".cpp")) {
            this.fileType = "cpp";
        } else if (getData.contains(".vb")) {
            this.fileType = "vb";
        } else if (getData.contains(".xls")) {
            this.fileType = "xls";
        } else if (getData.contains(".bak")) {
            this.fileType = "bak";
        } else if (getData.contains(".dll")) {
            this.fileType = "dll";
        } else if (getData.contains(".cfg")) {
            this.fileType = "cfg";
        } else if (getData.contains(".dmp")) {
            this.fileType = "dmp";
        } else if (getData.contains(".ini")) {
            this.fileType = "ini";
        } else if (getData.contains(".msi")) {
            this.fileType = "msi";
        } else if (getData.contains(".sys")) {
            this.fileType = "sys";
        } else if (getData.contains(".avi")) {
            this.fileType = "avi";
        } else if (getData.contains(".flv")) {
            this.fileType = "flv";
        } else if (getData.contains(".mp4")) {
            this.fileType = "mp4";
        } else if (getData.contains(".mpg")) {
            this.fileType = "mpg";
        } else if (getData.contains(".mpeg")) {
            this.fileType = "mpeg";
        } else if (getData.contains(".wmv")) {
            this.fileType = "wmv";
        } else {
            this.fileType = "html";
        }
        URL website = new URL(getData);
        ReadableByteChannel byteChannel = Channels.newChannel(website.openStream());
        FileOutputStream outputStream = new FileOutputStream("temporary."+fileType);
        outputStream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
        //VAJA TEHA TAGASI SAATMINE !!!!!!!!!
//        URL website = new URL("http://pm1.narvii.com/6311/3d4ff752b939276f48975c010a0e3de1ef116d99_00.jpg");
//        ReadableByteChannel byteChannel = Channels.newChannel(website.openStream());
//        FileOutputStream outputStream = new FileOutputStream("chika.jpg");
//        outputStream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
    }

    public boolean getMyRequest() {
        lazyness = Math.random();
        double chance = 0.5;
        return lazyness <= chance;
    }
}