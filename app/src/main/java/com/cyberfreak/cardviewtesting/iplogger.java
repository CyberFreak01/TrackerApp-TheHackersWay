package com.cyberfreak.cardviewtesting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class iplogger extends AppCompatActivity {
private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iplogger);
        button=findViewById(R.id.button);
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openAct1();
        }
    });


    }
    public void openAct1(){
        Intent intent1 = new Intent(this, iploggerMain.class);
        startActivity(intent1);
    }

}