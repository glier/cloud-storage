package HW2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class NioServer {

    private final ServerSocketChannel serverChannel = ServerSocketChannel.open();
    private final Selector selector = Selector.open();
    private final ByteBuffer buffer = ByteBuffer.allocate(5);
    private Path serverPath = Paths.get("./");

    public NioServer() throws IOException {
        serverChannel.bind(new InetSocketAddress(8189));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (serverChannel.isOpen()) {
            selector.select(); // block
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    handleAccept(key);
                }
                if (key.isReadable()) {
                    handleRead(key);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new NioServer();
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        int read = 0;
        StringBuilder msg = new StringBuilder();
        while ((read = channel.read(buffer)) > 0) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                msg.append((char) buffer.get());
            }
            buffer.clear();
        }
        String[] command = msg.toString().replaceAll("[\n|\r]", "").split(" ");

        switch (command[0]) {
            case "ls":
                commandLs(channel);
                break;
            case "cd":
                commandCd(channel, command[1]);
                break;
            case "cat":
                commandCat(channel, command[1]);
                break;
            case "touch":
                commandTouch(command[1]);
                break;
            case "mkdir":
                commandMkdir(command[1]);
                break;
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
    }

    private void commandLs(SocketChannel channel) throws IOException {
        String files = Files.list(serverPath)
                .map(path -> path.getFileName().toString())
                .collect(Collectors.joining(", "));
        files += "\n";
        channel.write(ByteBuffer.wrap(files.getBytes(StandardCharsets.UTF_8)));
    }

    private void commandCd(SocketChannel channel, String command) throws IOException {
        serverPath = Paths.get(String.valueOf(serverPath), command);
        channel.write(ByteBuffer.wrap((serverPath.toString()+"\n").getBytes(StandardCharsets.UTF_8)));
    }

    private void commandCat(SocketChannel channel, String command) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(serverPath+command));

        while (reader.ready()) {
            channel.write(ByteBuffer.wrap(((reader.readLine()+"\n").getBytes(StandardCharsets.UTF_8))));
        }
    }

    private void commandTouch(String command) throws IOException {
        Path newFile = Paths.get(String.valueOf(serverPath), command);
        Files.createFile(newFile);
    }

    private void commandMkdir(String command) throws IOException {
        Path newDir = Paths.get(String.valueOf(serverPath), command);
        Files.createDirectory(newDir);
    }
}
