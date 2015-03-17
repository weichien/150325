package com.example.app.myapplication;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.Message;
import android.util.DisplayMetrics;

import java.net.Socket;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.app.Application;




public class MainActivity extends ActionBarActivity {

    private Button btnConnect;
    private TextView textResponse;
    private int State = 0;
    private Handler mUI_Handler = new Handler();
    private Handler mThreadHandler;
    private HandlerThread mThread;
    public String receive;
    private InetAddress serverAddr; //= null;
    private SocketAddress sc_add; //= null;
    private Socket socket; //= null;
    DataOutputStream out;
    DataInputStream in;
    public byte[]buffer1=new byte[16];
    public int length;
    public String unit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        serverAddr = null;
        sc_add = null;
        socket = null;


        // 文字方塊
        textResponse = (TextView) findViewById(R.id.Response);
        btnConnect = (Button) findViewById(R.id.btnConnect);

        textResponse.setText("狀態  : 尚未連線");

        btnConnect.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                try {
                    if (State == 0) {
                        serverAddr = InetAddress.getByName("192.168.2.1");
                        sc_add = new InetSocketAddress(serverAddr, 2000);
                        socket = new Socket();

                        //與Server連線，timeout時間2秒
                        socket.connect(sc_add, 2000);

                        if (socket.isConnected()) {
                            textResponse.setText("狀態  : 連線成功");
                            out = new DataOutputStream(socket.getOutputStream());
                            in = new DataInputStream(socket.getInputStream());
                            State = 1;
                        }
                    }
                } catch (UnknownHostException e) {
                    State = 0;
                    textResponse.setText("狀態  : InetAddress物件建立失敗");
                } catch (SocketException e) {
                    State = 0;
                    textResponse.setText("狀態  : socket建立失敗");
                } catch (IOException e) {
                    State = 0;
                    textResponse.setText("狀態  : 傳送失敗");
                }
            }
        });

        /*

        button01.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String Comment = getCommend("FF58");

                try {
                    //傳送資料
                    if (State == 1) {
                        int temp = 0;
                        byte[] buffer = new byte[6];
                        //向輸出流中寫要發送的數據
                        int j = 0;


                        for (int i = 0; i < Comment.length(); i += 2) {
                            buffer[j] = (byte) (Integer.parseInt(Comment.substring(i, i + 2), 16) & 0xff);
                            j++;
                        }


                        out.write(buffer, 0, 6);
                        mThread = new HandlerThread("name");
                        mThread.start();
                        mThreadHandler = new Handler(mThread.getLooper());
                        mThreadHandler.post(r1);
                        TextView01.setText("狀態  : 已傳送");

                    } else {
                        TextView01.setText("狀態  : 尚未連線status=0");
                    }


                } catch (UnknownHostException e) {
                    TextView01.setText("狀態  : UnknownHostException");
                } catch (SocketException e) {
                    TextView01.setText("狀態  : SocketException");
                } catch (IOException e) {
                    TextView01.setText("狀態  : IOException");
                }

            }
        });
        */
    }

    public void Send(View view){
        String Comment ="" ;
        unit ="";
        switch (view.getId()){
            case R.id.button1:/*室內溫*/
                Comment = getCommend("FF58");
                unit = "溫度";
                break;
            case R.id.button2:/*設定溫*/
                Comment = getCommend("FF3B");
                unit = "溫度";
                break;
            case R.id.button3:/*設定風速*/
                Comment = getCommend("FF30");
                unit = "風速";
                break;
            case R.id.button4:/*運轉模式*/
                Comment = getCommend("FF11");
                unit = "模式";
                break;
            case R.id.button5:/*室內風扇轉速*/
                Comment = getCommend("FE44");
                unit = "轉速";
                break;
            case R.id.button6:/*防感溫器凍*/
                Comment = getCommend("FF59");
                unit = "溫度";
                break;
            case R.id.button7:/*故障履歷(即時)*/
                Comment = getCommend("FDEA");
                unit = "履歷";
                break;
            case R.id.button8:/*異常代碼*/
                Comment = getCommend("FF51");
                unit = "代碼";
                break;
        }

        try {
            //傳送資料
            if (State == 1) {
                byte[] buffer = new byte[6];

                //向輸出流中寫要發送的數據
                int j = 0;

                for (int i = 0; i < Comment.length(); i += 2) {
                    buffer[j] = (byte) (Integer.parseInt(Comment.substring(i, i + 2), 16) & 0xff);
                    System.out.println(buffer[j]);
                    j++;
                }

                out.write(buffer, 0, 6);
                mThread = new HandlerThread("name");
                mThread.start();
                mThreadHandler = new Handler(mThread.getLooper());
                mThreadHandler.post(r1);
                textResponse.setText("狀態  : 已傳送");

            } else {
                textResponse.setText("狀態  : 尚未連線status=0");
            }


        } catch (UnknownHostException e) {
            textResponse.setText("狀態  : UnknownHostException");
        } catch (SocketException e) {
            textResponse.setText("狀態  : SocketException");
        } catch (IOException e) {
            textResponse.setText("狀態  : IOException");
        }
    }


    public String getCommend(String Ram){

        //位址反序
        String addrReverse = new StringBuilder(Ram).reverse().toString();
        int addrSum =0;

        for(int i=0;i<addrReverse.length();i++){
            addrSum = addrSum + Integer.parseInt(addrReverse.substring(i,i+1),16);
        }
        System.out.println("addrSum:"+addrSum);

        //XOR
        byte CheckSum = (byte)(0xff & 255 ^ addrSum);
        Log.v("Address Hex",Integer.toHexString(CheckSum));
        String Commend = addrReverse + Integer.toHexString(CheckSum).substring(6,8).toUpperCase();
        Log.v("Address Reverse",Commend);

        //ASCII轉換成Hex
        Commend = asciiToHex(Commend);
        Log.v("Commend",Commend);

        return Commend;

    }
    public static String asciiToHex(String ascii){
        StringBuilder hex = new StringBuilder();

        for (int i=0; i < ascii.length(); i++) {
            hex.append(Integer.toHexString(ascii.charAt(i)));
        }
        return hex.toString();
    }
    public String intToascii(byte[] buffer1){
        receive = "";
        for(int i=0;i<length;i++)
        {
            int x = buffer1[i];
            char ch = (char) x;

            receive += Character.toString(ch);
        }
        System.out.println(receive);
        return receive;
    }

    private Runnable r1 = new Runnable(){
        public void run(){
            try{
                length = in.read(buffer1);
                receive = intToascii(buffer1);
                String data = receive.substring(0,2);
                switch (unit){
                    case "溫度":
                        receive += ","+Integer.parseInt(data,16)/3+"度";
                        break;
                    case "風速":
                        switch (data){
                            case "01":
                                receive += ",靜";
                                break;
                            case "02":
                                receive += ",微";
                                break;
                            case "03":
                                receive += ",弱";
                                break;
                            case "04":
                                receive += ",強";
                                break;
                            case "05":
                                receive += ",自動";
                                break;
                        }
                        break;
                    case "模式":
                        switch (data){
                            case "01":
                                receive += ",暖氣";
                                break;
                            case "02":
                                receive += ",冷氣";
                                break;
                            case "04":
                                receive += ",除濕";
                                break;
                            case "08":
                                receive += ",送風";
                                break;
                        }
                        break;
                    case "轉速":
                        receive += ","+Integer.parseInt(data,16)*10+"rpm";
                        break;
                    case "履歷":
                        receive += ","+(Integer.parseInt(data,16)+50);
                        break;
                    case "代碼":

                        break;
                }
                mUI_Handler.post(r2);
            }catch (IOException e)
            {

            }

        }
    };

    private Runnable r2 = new Runnable(){
        public void run(){
            textResponse.setText(receive);
            Log.v("Receive",receive);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}