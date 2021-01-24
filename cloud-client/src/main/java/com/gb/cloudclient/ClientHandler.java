package com.gb.cloudclient;

import com.gb.cloudcore.Command;
import com.gb.cloudcore.CommandEvent;
import com.gb.cloudcore.User;
import com.gb.cloudcore.UserStorageStructure;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LogManager.getLogger(ClientHandler.class);
    private Controller controller;
    private static int userNumber = 0;


    public ClientHandler(Controller controller) {
        this.controller = controller;
        userNumber++;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Client channel Active");
        Command cmd = new Command(CommandEvent.GET_USER_DIR_STRUCTURE, new User("Alex"));
        logger.info(cmd);
        ctx.writeAndFlush(cmd);
        logger.info("Send command " + CommandEvent.GET_USER_DIR_STRUCTURE.toString());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
        logger.info("Read object from server");
        if (o instanceof UserStorageStructure) {
            controller.setUserStorageStructure((UserStorageStructure) o);
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
