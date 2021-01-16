package HW1;

import java.io.*;
import java.net.Socket;

public class lesson1 {
    public static void main(String[] args) throws IOException {
        Socket clientSocket = new Socket("localhost",8181);
        System.out.println("Connect to server");
        readFile(clientSocket, new File("/home/glier/Документы/geekbrains/cloud-storage/cloud-client/src/main/resources/lesson1/test.jpg"));
        System.out.println("Transfer end");

    }

    public static void readFile(Socket clientSocket, File file) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
             BufferedOutputStream bos = new BufferedOutputStream(clientSocket.getOutputStream())) {

            int read;
            byte[] buffer = new byte[1024];

            while ((read = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, read);
            }
        }
    }
}
