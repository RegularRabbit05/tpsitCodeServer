package remote;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket a;
    private final String th;

    public ClientHandler(Socket accept, String th) {
        this.a = accept;
        this.th = th;
    }

    @Override
    public void run() {
        InputStream inputStream;
        try {
            inputStream = a.getInputStream();
        } catch (IOException e) {
            try {
                a.close();
            } catch (IOException ignored) {}
            throw new RuntimeException(e);
        }

        try {
            byte[] bytes = new byte[4096000];
            int read = inputStream.read(bytes);
            Class<?> aClass = new ClassLoader() {
                @Override
                protected Class<?> findClass(String name) {
                    return defineClass(name, bytes, 0, read);
                }
            }.loadClass("client"+".Run");
            System.out.println("Executing class for client: " + a.getInetAddress().toString());
            aClass.getMethod("main", String[].class).invoke(null, (Object) new String[]{th});
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                a.close();
            } catch (IOException ignored) {}
        }
    }
}
