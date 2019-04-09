package tester;

public class UnusedCode {
            /*line = "";
            // looks for post data
            int postDataI = -1;
            while ((line = in.readLine()) != null && (line.length() != 0)) {
                System.out.println(line);
                if (line.contains("Content-Length:")) {
                    postDataI = new Integer(
                            line.substring(
                                    line.indexOf("Content-Length:") + 16,
                                    line.length()));
                }
            }
            String postData = null;

            // read the post data
            if (postDataI > 0) {
                char[] charArray = new char[postDataI];
                in.read(charArray, 0, postDataI);
                postData = new String(charArray);
            }

            //On false entry sends back a message and closes thread
            if (postData == null) {
                String message = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "\r\n" +
                        "No data was sent";
                output.write(message.getBytes());
                output.close();
                input.close();
                clientSocket.close();
                return;
            }

            JSONParser parser = new JSONParser();
            JSONObject test = null;
            try {
                test = (JSONObject)parser.parse(postData);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String actionNeeded = (String) test.get("action");
            String ip = (String) test.get("ip");
            String message;

            */
    //Compiles a message to send back based on information gotten from the body of message received.
    //if (actionNeeded.equals("Enter") && ip != null) {
    //message = actionEnter(ip);
    //} else {
    //message = "HTTP/1.1 200 OK\r\n" +
    //"Content-Type: text/html\r\n" +
    //"\r\n" +
    //"???";
    //}
}
