package com.qerat.fstweek;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NotVerifiedEmailActivity extends AppCompatActivity {
    FirebaseAuth auth;
    private Button successSendButton, retryButton, sendConfirmEmailButton;
    private LinearLayout successfulSendingLayout, errorSendingLayout, sendingLayout;
    private LinearLayout confirmLayout;
    private TextView emailMsg1, emailMsg2,confirmTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_varified_email);



        successSendButton = findViewById(R.id.successSendButton);
        retryButton = findViewById(R.id.retryButton);
        successfulSendingLayout = findViewById(R.id.successfulSending);
        sendConfirmEmailButton = findViewById(R.id.confirmButton);
        errorSendingLayout = findViewById(R.id.errorSending);
        sendingLayout = findViewById(R.id.sending);
        confirmTextView=findViewById(R.id.confirmedittext);
        confirmLayout=findViewById(R.id.question);

        emailMsg1 = findViewById(R.id.optional_textView2);
        emailMsg2 = findViewById(R.id.optional_textView1);


        auth= FirebaseAuth.getInstance();



        String emailID = emailMsg1.getText() + "<b>" + auth.getCurrentUser().getEmail() + "</b>" + " through an email" + ". Please Follow the link to activate your account.";
        emailMsg1.setText(Html.fromHtml(emailID));

        emailID = emailMsg1.getText() + "<b>" + auth.getCurrentUser().getEmail() + "</b>" ;
        confirmTextView.setText(Html.fromHtml(emailID));

        emailID = emailMsg2.getText() + "<b>" + auth.getCurrentUser().getEmail() + "</b>";
        emailMsg2.setText(Html.fromHtml(emailID));

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationMsg();
            }
        });

        successSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                NotVerifiedEmailActivity.this.finish();
            }
        });

        sendConfirmEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationMsg();
            }
        });

      sendVerificationUI();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(auth.getCurrentUser().isEmailVerified()){
            finish();
        }
    }

    private void sendVerificationUI(){
        confirmLayout.setVisibility(View.VISIBLE);
        sendingLayout.setVisibility(View.GONE);
        errorSendingLayout.setVisibility(View.GONE);
        successfulSendingLayout.setVisibility(View.GONE);
    }

    private void verificationSent(){
        confirmLayout.setVisibility(View.GONE);
        sendingLayout.setVisibility(View.GONE);
        errorSendingLayout.setVisibility(View.GONE);
        successfulSendingLayout.setVisibility(View.VISIBLE);
    }

    private void errorVerificationSent(){
        confirmLayout.setVisibility(View.GONE);
        sendingLayout.setVisibility(View.GONE);
        errorSendingLayout.setVisibility(View.VISIBLE);
        successfulSendingLayout.setVisibility(View.GONE);
    }

    private void sendingVerification(){
        confirmLayout.setVisibility(View.GONE);
        sendingLayout.setVisibility(View.VISIBLE);
        errorSendingLayout.setVisibility(View.GONE);
        successfulSendingLayout.setVisibility(View.GONE);
    }
    private void sendVerificationMsg() {
        sendingVerification();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            verificationSent();


                        } else {

                            errorVerificationSent();
                        }
                    }
                });
    }
}
