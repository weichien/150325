package com.example.app.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.DialogPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by 030759 on 2015/3/11.
 */
public class Page1 extends Activity {

    private Button enter;
    private Button clear;
    private Button choose;
    private RadioGroup radioGroup1;
    private RadioGroup radioGroup2;
    private RadioGroup radioGroup3;
    private NumberPicker numberPicker;
    String[] values =  new String[1];
    int image = R.drawable.select;
    private int enable = 0;
    private int State = 0;
    private InetAddress serverAddr; //= null;
    private SocketAddress sc_add; //= null;
    private Socket socket; //= null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        serverAddr = null;
        sc_add = null;
        socket = null;

        enter = (Button)findViewById(R.id.enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Page1.this, MainActivity.class); //MainActivity為主要檔案名稱
                startActivity(i);
                try {
                    if (State == 0) {
                        serverAddr = InetAddress.getByName("192.168.2.1");
                        sc_add = new InetSocketAddress(serverAddr, 2000);
                        socket = new Socket();

                        //與Server連線，timeout時間2秒
                        socket.connect(sc_add, 2000);

                        if (socket.isConnected()) {

                            State = 1;
                            Intent page = new Intent(Page1.this, MainActivity.class); //MainActivity為主要檔案名稱
                            startActivity(page);

                        }
                    }
                } catch (UnknownHostException e) {
                    onCreateDialog();

                } catch (SocketException e) {
                    onCreateDialog();

                } catch (IOException e) {
                    onCreateDialog();
                }


            }
        });

        clear = (Button)findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                radioGroup1 = (RadioGroup)findViewById(R.id.rgroup1);
                radioGroup2 = (RadioGroup)findViewById(R.id.rgroup2);
                radioGroup3 = (RadioGroup)findViewById(R.id.rgroup3);
                radioGroup1.clearCheck();
                radioGroup2.clearCheck();
                radioGroup3.clearCheck();
            }
        });


    }

    public void createDialog(final View view){

        LayoutInflater factory = LayoutInflater.from(Page1.this);
        final View dialogView = factory.inflate(R.layout.dialog,null);
        TextView textView = (TextView)dialogView.findViewById(R.id.textview);
        String title = "";
        Button choose1 = (Button)findViewById(R.id.choose1);
        String type = choose1.getText().toString().trim();
        System.out.println(type);
        switch (view.getId()){
            case R.id.choose1:
                values = new String[]{"RAS(K)","RAS(N)","RAD(K)","RAD(N)","RA"};
                choose = (Button)findViewById(R.id.choose1);
                image = R.drawable.select2;
                textView.setText("機型");
                title = "室內機選擇";
                break;
            case R.id.choose2:
                switch (type){
                    case "RAS(K)":
                        values = new String[]{"22","28","36","40","50","60","71","80","90","110"};
                        break;
                    case "RAS(N)":
                        values = new String[]{"20","22","25","28","32","36","40","45","50","60","63","71"};
                        break;
                    case "RAD(K)":
                        values = new String[]{"22","28","36","40","50","60","63","71","80","90","110"};
                        break;
                    case "RAD(N)":
                        values = new String[]{"20","25","32","36","45","56","63","71","80"};
                        break;
                    case "RA":
                        values = new String[]{"28","36","40","50"};
                        break;
                }
                choose = (Button)findViewById(R.id.choose2);
                textView.setText("型號");
                title = "室內機選擇";
                break;
            case R.id.choose3:
                switch (type){
                    case "RAS(K)":
                        values = new String[]{"NB"};
                        break;
                    case "RAS(N)":
                        values = new String[]{"HQ","NJ","RL","FC","YS"};
                        break;
                    case "RAD(K)":
                        values = new String[]{"NJ"};
                        break;
                    case "RAD(N)":
                        values = new String[]{"NJ"};
                        break;
                    case "RA":
                        values = new String[]{"NA"};
                        break;
                }
                choose = (Button)findViewById(R.id.choose3);
                textView.setText("系列");
                title = "室外機選擇";
                break;
            case R.id.choose4:
                values = new String[]{"50","63","71","83","86","93","108","139"};
                choose = (Button)findViewById(R.id.choose4);
                textView.setText("型號");
                title = "室外機選擇";
                break;
            case R.id.choose5:
                values = new String[]{"NJ","RL","HQ"};
                choose = (Button)findViewById(R.id.choose5);
                textView.setText("系列");
                title = "室外機選擇";
                break;

        }

        numberPicker=(NumberPicker)dialogView.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(values.length-1);
        numberPicker.setMinValue(0);
        numberPicker.setDisplayedValues(values);

        AlertDialog.Builder dialog = new AlertDialog.Builder(Page1.this);
        dialog.setTitle(title);
        dialog.setView(dialogView);
        dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                choose.setBackgroundResource(image);
                choose.setText(values[numberPicker.getValue()]+"   ");
                if(view.getId()==R.id.choose1 && enable==0)
                {
                    enable = 1;
                    Button choose2 = (Button)findViewById(R.id.choose2);
                    Button choose3 = (Button)findViewById(R.id.choose3);
                    choose2.setEnabled(true);
                    choose3.setEnabled(true);

                }
            }
        });

        dialog.show();
    }


    public void onCreateDialog() {
        State = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(Page1.this);
        builder.setTitle("連線異常")
               .setMessage("請確認\n1.WIFI是否開啟\n2.Debug Port是否開啟")
               .setPositiveButton("確定",new DialogInterface.OnClickListener(){
                   public void onClick(DialogInterface dialog,int id){

                   }
               });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
