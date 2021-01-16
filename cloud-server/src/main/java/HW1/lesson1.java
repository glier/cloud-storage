package HW1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class lesson1 {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8181);
        System.out.println("Server started");
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected");
        writeFile(clientSocket, new File("/home/glier/Документы/geekbrains/cloud-storage/cloud-server/src/main/resources/lesson1/test.jpg"));
        System.out.println("Transfer end");

    }


    public static void writeFile(Socket clientSocket, File file) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
             BufferedInputStream bis = new BufferedInputStream(clientSocket.getInputStream())) {

            int read;
            byte[] buffer = new byte[1024];

            while ((read = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, read);
            }
        }
    }
}
