package com.nptel.courses.online.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nptel.courses.online.R;
import com.nptel.courses.online.WebViewActivity;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("About");
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, WebViewActivity.class);
        switch (view.getId()) {
            case R.id.privacy_policy:
                intent.putExtra("url", "https://sites.google.com/view/nptel-coursevideos/privacy-policy");
                startActivity(intent);
                break;
            case R.id.terms_condition:
                intent.putExtra("url", "https://sites.google.com/view/nptel-coursevideos/terms-and-conditions");
                startActivity(intent);
                break;
            case R.id.contact_us:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("Contact us at: contact.iamyourfriend@gmail.com");
                alert.setCancelable(true);
                alert.show();
                break;
        }
    }
}
