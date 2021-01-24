package com.gb.cloudserver;

import com.gb.cloudcore.Command;
import com.gb.cloudcore.CommandEvent;
import com.gb.cloudcore.User;
import com.gb.cloudcore.UserStorageStructure;
import com.gb.cloudserver.util.StorageUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

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
                UserStorageStructure uss = StorageUtil.getUserStorageStructure(command.getUser());
                logger.info("User storage structure:");
                logger.info(uss);
                ctx.writeAndFlush(uss);
                logger.info("User storage structure sent");
            }
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
