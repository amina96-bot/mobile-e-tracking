package com.starbrands.stb_carburant;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

public class ResetActivity extends AppCompatActivity {

    TextView username;
    EditText pass, repass;
    Button btnconfirm;
    DbHandler db=new DbHandler(this);
    private static final String IMGUR_CLIENT_ID = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        username= (TextView) findViewById(R.id.username_reset_text) ;
        pass=(EditText) findViewById(R.id.password_reset);
        repass=(EditText) findViewById(R.id.re_password_reset);
        btnconfirm=(Button) findViewById((R.id.btn_confirm));
        Intent intent=getIntent();
        username.setText(intent.getStringExtra("username"));
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=username.getText().toString();
                String password=pass.getText().toString();
                String re_password=repass.getText().toString();
                if(re_password.equals(password)){
                    Boolean checkPasswordUpdate=db.updatepassword(user,password);
                    if(checkPasswordUpdate){
                        AsyncTask asyncTask=new AsyncTask() {
                            @Override
                            protected Object doInBackground(Object[] objects) {
                                try {
                                    OkHttpClient client = new OkHttpClient();
                                    @SuppressLint("StaticFieldLeak") RequestBody requestBody = new MultipartBuilder()
                                            .type(MultipartBuilder.FORM)
                                            .addFormDataPart("usr_id", String.valueOf(db.GetLastUser_id()))
                                            .addFormDataPart("usr_password", password)
                                            .build();
                                    Request request = new Request.Builder()
                                            .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                                            .url("http://41.111.138.18:7019/STB_CARBURANT/ChangePasswordServlet")
                                            .post(requestBody)
                                            .build();
                                    Response response = client.newCall(request).execute();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                        }.execute();
                        Intent intent = new Intent(ResetActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(ResetActivity.this, "Mot de passe modifié avec success", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ResetActivity.this, "Échec", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ResetActivity.this, "Les mots de passe introduits ne se correspont pas", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}