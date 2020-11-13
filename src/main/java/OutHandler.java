import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class OutHandler extends ChannelOutboundHandlerAdapter {
    Path root = Path.of("server");
    FileWorker fw = new FileWorker(root);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf buf = ctx.alloc().directBuffer();
        System.out.println(buf);
        String message = (String) msg;
        System.out.println("OutBound: " + message);
        message = message.replaceAll("\r\n", "");
        String[] arrMessage = message.split(" ");
        if (arrMessage[0].equals("download")){
            if(!Files.exists(Path.of(fw.getCurrentPath().toString(), arrMessage[1]))){
                buf.writeBytes(("Такого файла в директории " + fw.getCurrentPath().toString() + "не существует").getBytes());
                ctx.writeAndFlush(buf);
            }
//            File file = new File(fw.getCurrentPath().toString(), arrMessage[1]);
            File file = new File("1.txt");
            RandomAccessFile rf = new RandomAccessFile(file, "rw");
            long length = rf.length();
            System.out.println("file size:" + length);
            FileChannel fileChannel = rf.getChannel();
            FileRegion fileRegion = new DefaultFileRegion(fileChannel, 0, length);
            fileRegion.transferTo(rf.getChannel(), 0);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read = fileChannel.read(buffer);
            System.out.println(read);
            System.out.println(buffer);
            buf.writeBytes(buffer);
            System.out.println(buf);
            ctx.writeAndFlush(buf);

        }
        if (arrMessage[0].equals("cd")) {
//            System.out.println(fw.changeDirectory(arrMessage[1]));
//            buf.writeBytes(("Текущая директория: " + fw.getCurrentPath()).getBytes());
            buf.writeBytes((fw.changeDirectory(arrMessage[1])).getBytes());
            ctx.writeAndFlush(buf);
        }
        if (arrMessage[0].equals("mkdir")) {
            fw.makedirectory(arrMessage[1]);
            System.out.println("mkdir отработал");
        }
        if (arrMessage[0].equals("touch")){
            buf.writeBytes((fw.touch(arrMessage[1])).getBytes());
            ctx.writeAndFlush(buf);
        }
        if (arrMessage[0].equals("ls")){
            buf.writeBytes((fw.getFilesList().getBytes()));
            ctx.writeAndFlush(buf);
        }
        if (arrMessage[0].equals("rm")){
            buf.writeBytes((fw.remove(arrMessage[1]).getBytes()));
            ctx.writeAndFlush(buf);
        }
        if (arrMessage[0].equals("cope")){
            buf.writeBytes((fw.cope(arrMessage[1], arrMessage[2]).getBytes()));
            ctx.writeAndFlush(buf);
        }
        if (arrMessage[0].equals("cat")){
            buf.writeBytes(fw.cat(arrMessage[1]).getBytes());
            ctx.writeAndFlush(buf);
        }
        message = (String) msg;
        buf.writeBytes(message.getBytes());
        ctx.writeAndFlush(buf);
    }
}



