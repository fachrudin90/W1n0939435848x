package com.tamboraagungmakmur.winwin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by innan on 11/14/2017.
 */

public class EmailTempLinkActivity extends FragmentActivity {

    private EditText link, alias;
    private Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailtemplink);

        link = (EditText) findViewById(R.id.link);
        alias = (EditText) findViewById(R.id.alias);
        ok = (Button) findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("link");
                intent.putExtra("link", link.getText().toString());
                intent.putExtra("alias", alias.getText().toString());
                LocalBroadcastManager.getInstance(EmailTempLinkActivity.this).sendBroadcast(intent);
                onBackPressed();
            }
        });
    }

}
