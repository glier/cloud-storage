package com.gb.cloudclient;

import com.gb.cloudcore.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ClientHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LogManager.getLogger(ClientHandler.class);
    private Controller controller;


    public ClientHandler(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Client channel Active");
        Command cmd = new Command(CommandEvent.GET_USER_DIR_STRUCTURE, controller.getUser(), controller.getCurrentPath(), null);
        logger.info(cmd);
        ctx.writeAndFlush(cmd);
        logger.info("Sent command " + CommandEvent.GET_USER_DIR_STRUCTURE.toString());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
        logger.info("Read object from server");
        if (o instanceof UserStorageStructure) {
            controller.setUserStorageStructure((UserStorageStructure) o);
        }
        if (o instanceof ChunkedFile) {
            ChunkedFile uploadFile = (ChunkedFile) o;
            if (!uploadFile.isFromServer()) {
                uploadFile(ctx, uploadFile);
            } else {
                downloadFile(ctx, uploadFile);
            }
        }
    }

    private void downloadFile(ChannelHandlerContext ctx, ChunkedFile uploadFile) throws Exception {
        File file = new File(uploadFile.getClientPath());
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
        logger.info("The file is written: " + uploadFile.getClientPath() + ", " + uploadFile.getBytes().length);
    }

    private void uploadFile(ChannelHandlerContext ctx, ChunkedFile chunkedFile) {
        byte[] bytes = new byte[Constants.CHUNKED_FILE_LENGTH];
        int byteRead;

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(chunkedFile.getClientPath(), "r")) {
            randomAccessFile.seek(chunkedFile.getStarPos());

            if ((byteRead = randomAccessFile.read(bytes)) != -1) {
                chunkedFile.setEndPos(byteRead);
                chunkedFile.setBytes(bytes);
                ctx.writeAndFlush(chunkedFile);
            } else {
                controller.dirUpdateAction(null);
            }

            logger.info("Read " + byteRead);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
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
