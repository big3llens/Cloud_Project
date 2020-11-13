import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class InHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключился");
        String firstMessage = "Добро пожаловать в хранилище!\n\rВам доступны следующие команды:\n\rcd (name direcory)- перейти в указанную директорию\n\rls- показать список файлов в текущей директории\n\r" +
                "touch (name.txt)- создать текстовый файл\n\rmkdir (name)- создать директорию в текущей директории\n\rrm(name)- удалить файл\n\rcopy(src, target)- скопировать файл из одного пути в другой\n\r" +
                "cat(name)- вывести в консоль содержимое файла";
        byte[] b = firstMessage.getBytes();
        String newMassege = new String(b, StandardCharsets.UTF_8);
        ByteBuf buf = ctx.alloc().directBuffer();
        buf.writeBytes(newMassege.getBytes());
        ctx.write(buf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        System.out.println(ch.toString());
        ByteBuf buf = (ByteBuf) msg;
        StringBuilder sb = new StringBuilder();
        while (buf.isReadable()){
            sb.append((char)buf.readByte());
        }
        String string = sb.toString();
        byte[] b = string.getBytes();
        String encodeMessage = new String(b, StandardCharsets.UTF_8);
        System.out.println("InBound: " + encodeMessage);
        ctx.write(encodeMessage);
//        buf.writeBytes(sb.toString().getBytes());
//        ctx.writeAndFlush(buf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
