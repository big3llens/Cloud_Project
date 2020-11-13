import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;

/**
 * Client command: upload fileName | download fileName
 *
 * @author user
 */
public class Client extends JFrame {
    private JTextField text;

    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private final BufferedInputStream bin;
    private final BufferedOutputStream bout;

    public Client() throws HeadlessException, IOException {
        socket = new Socket("localhost", 9000);
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
        bin = new BufferedInputStream(in);
        bout = new BufferedOutputStream(out);
        setSize(300, 300);
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JButton send = new JButton("SEND");
        text = new JTextField();
        send.addActionListener(a -> {
            String[] cmd = text.getText().split(" ");
            if (cmd[0].equals("upload")) {
                sendFile(cmd[1]);
            }
            if (cmd[0].equals("download")) {
                getFile(cmd[1]);
            }
            else sendMessage(text.getText());
        });
        panel.add(text);
        panel.add(send);
        add(panel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosed(e);
                sendMessage("exit");
            }
        });
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        new Thread(new Run()).start();

    }

    private void getFile(String fileName) {
        try {
//            out.writeUTF("download");
//            out.writeUTF(fileName);
            File file = new File("client/" + fileName);
            if (!file.exists()) {file.createNewFile();}
//            long size = in.readLong();
//            FileOutputStream fos = new FileOutputStream(file);
//            for (int i = 0; i < size; i++){
//                fos.write(in.read());
//            }
//            fos.close();
//            String status = in.readUTF();
//            System.out.println(status);
            byte[] buf = new byte[2048];
            FileOutputStream fos = new FileOutputStream(file);
            int length = in.read(buf);
            for (int i = 0; i < length; i++) {
                in.read(buf, 0, length);
                fos.write(buf, 0, length);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getMessage () {
        while (true) {
            try {
                int length = bin.read();
                System.out.println(length);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i <length ; i++) {
//                    sb.append((char)bin.rea)
                }

//                text.setText("server: " + s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void sendFile(String filename) {
        try {
//            out.writeUTF("upload");
//            out.writeUTF(filename);
//            File file = new File("client/" + filename);
//            long length = file.length();
//            out.writeLong(length);
//            FileInputStream fileBytes = new FileInputStream(file);
//            int read = 0;
//            byte[] buffer = new byte[256];
//            while ((read = fileBytes.read(buffer)) != -1) {
//                out.write(buffer, 0, read);
//            }
//            out.flush();
//            String status = in.readUTF();
//            System.out.println(status);
            File file = new File("client/" + filename);
            FileInputStream fileBytes = new FileInputStream(file);
            long length = file.length();
            byte[] buf = new byte[256];
            int read = 0;
            while ((read = fileBytes.read(buf)) != -1){
                bout.write(buf, 0, read);
            }
            bout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        try {
//            out.writeUTF(text);
//            System.out.println(in.readUTF());
            bout.write(message.getBytes());
            bout.flush();
            text.setText("");
            text.grabFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class Run implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println("что-то пришло");
                    byte[] buf = new byte[32];
                    int size = bin.read(buf);
                    System.out.println(size);
                    System.out.println(new String(buf, 0, size));
                    text.setText("server: " + new String(buf, 0, size));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Client();
    }
}