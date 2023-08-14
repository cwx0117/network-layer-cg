package com.poffingames.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

public class MessagePackDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int length = in.readableBytes();
        byte[] bytes = new byte[length];
        in.readBytes(bytes); // Read the bytes from the ByteBuf

        MessagePack messagePack = new MessagePack();
        Object decodedObject = messagePack.read(bytes); // Deserialize the bytes using MessagePack

        out.add(decodedObject); // Add the decoded object to the output list
    }
}

