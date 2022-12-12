package juken.android.com.juken_5;

import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TcpClient {
    public static final String Host = "www.google.com";
    public static final String SERVER_IP = "192.168.2.6";
    public static final int SERVER_PORT = 80;
    public static final String TAG = TcpClient.class.getSimpleName();
    private BufferedReader mBufferIn;
    /* access modifiers changed from: private */
    public PrintWriter mBufferOut;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    private String mServerMessage;

    public interface OnMessageReceived {
        void messageReceived(String str);
    }

    public TcpClient(OnMessageReceived listener) {
        this.mMessageListener = listener;
    }

    public void sendMessage(final String message) {
        new Thread(new Runnable() {
            public void run() {
                if (TcpClient.this.mBufferOut != null) {
                    Log.d(TcpClient.TAG, "Sending: " + message);
                    TcpClient.this.mBufferOut.println(message);
                    TcpClient.this.mBufferOut.flush();
                }
            }
        }).start();
    }

    public void stopClient() {
        this.mRun = false;
        if (this.mBufferOut != null) {
            this.mBufferOut.flush();
            this.mBufferOut.close();
            this.mBufferOut = null;
        }
        if (this.mMessageListener != null) {
            this.mMessageListener = null;
        }
        if (this.mBufferIn != null) {
            this.mBufferIn = null;
        }
        if (this.mServerMessage != null) {
            this.mServerMessage = null;
        }
    }

    public void run() {
        this.mRun = true;
        try {
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
            Log.d("TCP Client", "C: Connecting...");
            Socket socket = new Socket(serverAddr, 80);
            try {
                this.mBufferOut = new PrintWriter(socket.getOutputStream());
                this.mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (this.mRun) {
                    this.mServerMessage = this.mBufferIn.readLine();
                    if (this.mServerMessage != null) {
                        this.mMessageListener.messageReceived(this.mServerMessage);
                    }
                }
                Log.d("RESPONSE FROM SERVER", "S: Received Message: '" + this.mServerMessage + "'");
                socket.close();
            } catch (Exception e) {
                Log.e("TCP", "S: Error", e);
                socket.close();
            } catch (Throwable th) {
                socket.close();
                throw th;
            }
        } catch (Exception e2) {
            Log.e("TCP", "C: Error", e2);
        }
    }
}
