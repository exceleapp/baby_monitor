package com.example.mybabymonitor;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CardView prnButton, bbyButton;

    ImageButton bt_help;

    Button dialogButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prnButton = (CardView) findViewById(R.id.parentBtn);
        bbyButton = (CardView) findViewById(R.id.babyBtn);

        bt_help   = (ImageButton) findViewById(R.id.btn_help);

        prnButton.setOnClickListener(this);
        bbyButton.setOnClickListener(this);

        bt_help.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.parentBtn :
                Intent parent = new Intent(MainActivity.this, ParentActivity.class);
                this.startActivity(parent);

                break;

            case R.id.babyBtn :
                Intent baby = new Intent(MainActivity.this, BabyActivity.class);
                this.startActivity(baby);

                break;

            case R.id.btn_help :
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.popup_window);
                dialog.setTitle("Bantuan");

                /**
                 * Mengeset komponen dari custom dialog
                 */
                TextView text = (TextView) dialog.findViewById(R.id.tv_desc);
                text.setText("1. Siapkan dua perangkat smartphone. \n " +
                        "2. Sambungkan ke jaringan wifi yang sama.\n " +
                        "3. Tentukan salah satu perangkat sebagai bayi dan orangtua \n" +
                        "4. Kemudian tap tombol sambung untuk menyambungkan perangkat. ");

                dialog.dismiss();

                dialog.show();

                break;
        }
    }
}
