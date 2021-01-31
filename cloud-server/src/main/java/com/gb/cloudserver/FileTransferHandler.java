package com.gb.cloudserver;

import com.gb.cloudcore.*;
import com.gb.cloudserver.util.StorageUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileTransferHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger = LogManager.getLogger(FileTransferHandler.class);

//    private static final ConcurrentLinkedDeque<ChannelHandlerContext> clients = new ConcurrentLinkedDeque<>();
//    private static final ConcurrentHashMap<User, ChannelHandlerContext> clients = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("NEW USER CONNECTED");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
        logger.info("Read object from channel");
        if (o instanceof Command) {
            logger.info("Object instance of Command");
            Command command = (Command) o;

            if (command.getCommand() == CommandEvent.GET_USER_DIR_STRUCTURE) {
                logger.info("Command is " + CommandEvent.GET_USER_DIR_STRUCTURE.toString());
                UserStorageStructure uss = StorageUtil.getUserStorageStructure(command.getUser(), command.getServerPath());
                logger.info("User storage structure:");
                logger.info(uss);
                ctx.writeAndFlush(uss);
                logger.info("User storage structure sent");
            }

            if (command.getCommand() == CommandEvent.FILE_DOWNLOAD) {

                if (command.getUser() != null &&  command.getServerPath() != null) {
                    String file = StorageUtil.getCurrentPath(command.getUser(), command.getServerPath());
                    logger.info("Choosed file " + file);
                    ChunkedFile chunkedFile = new ChunkedFile();
                    chunkedFile.setUser(command.getUser());
                    chunkedFile.setClientPath(command.getClientPath());
                    chunkedFile.setStoragePath(file);
                    chunkedFile.setStarPos(0);
                    chunkedFile.setFromServer(true);

                    fileDownload(ctx, chunkedFile);
                }
            }

            if (command.getCommand() == CommandEvent.FILE_DELETE) {
                boolean result = Files.deleteIfExists(Paths.get(StorageUtil.getCurrentPath(command.getUser(), command.getServerPath())));
                if (result) {
                    Path path = Paths.get(command.getServerPath());
                    String serverDir = path.getParent().toString();

                    UserStorageStructure uss = StorageUtil.getUserStorageStructure(command.getUser(), serverDir);
                    ctx.writeAndFlush(uss);
                }
            }
        }
        if (o instanceof ChunkedFile) {
            ChunkedFile uploadFile = (ChunkedFile) o;
            if (!uploadFile.isFromServer()) {
                fileUpload(ctx, uploadFile);
            } else {
                fileDownload(ctx, uploadFile);
            }
        }
    }

    private void fileUpload(ChannelHandlerContext ctx, ChunkedFile uploadFile) throws Exception {
        String path = StorageUtil.getCurrentPath(uploadFile.getUser(), uploadFile.getStoragePath());
        File file = new File(path);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        randomAccessFile.seek(uploadFile.getStarPos());
        randomAccessFile.write(uploadFile.getBytes());
        if (uploadFile.getEndPos() > 0) {
            uploadFile.setStarPos(uploadFile.getStarPos() + uploadFile.getEndPos());
            ctx.writeAndFlush(uploadFile);
            randomAccessFile.close();
            if (uploadFile.getEndPos() != Constants.CHUNKED_FILE_LENGTH) {
                Thread.sleep(1000);
                channelInactive(ctx);
            }
        } else {
            ctx.close();
        }
        logger.info("The file is written: " + path + ", " + uploadFile.getBytes().length);
    }

    private void fileDownload(ChannelHandlerContext ctx, ChunkedFile chunkedFile) {
        byte[] bytes = new byte[Constants.CHUNKED_FILE_LENGTH];
        int byteRead;
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(chunkedFile.getStoragePath(), "r")) {
            randomAccessFile.seek(chunkedFile.getStarPos());

            if ((byteRead = randomAccessFile.read(bytes)) != -1) {
                chunkedFile.setEndPos(byteRead);
                chunkedFile.setBytes(bytes);
                ctx.writeAndFlush(chunkedFile);
            } else {
            }
            logger.info("Read " + byteRead);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error(cause);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("THIS");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("THIS");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("THIS");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("THIS");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        logger.info("THIS");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        logger.info("THIS");
    }
}
