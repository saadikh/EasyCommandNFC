package cnt.nfc.mbds.fr.easycommandnfc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import cnt.nfc.mbds.fr.easycommandnfc.api.RetrofitInstance;
import cnt.nfc.mbds.fr.easycommandnfc.api.UserClient;
import cnt.nfc.mbds.fr.easycommandnfc.api.model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private UserClient userClient = RetrofitInstance.getRetrofitInstance().create(UserClient.class);

    EditText editTextUsername;
    EditText editTextEmail;
    EditText editTextPassword;
    Button btnSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editTextUsername = findViewById(R.id.username);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        btnSignUp = findViewById(R.id.signup_button);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser(editTextUsername.getText().toString().trim(), editTextPassword.getText().toString().trim(),editTextEmail.getText().toString().trim());
            }
        });
    }

    public void createUser(String username, String email, String password) {
        Call<ResponseBody> call = userClient.createUser(new User(username, email, password));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Toast.makeText(SignUpActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                        /*Intent intent = new Intent(SignUpActivity.this, TagManagerActivity.class);
                        startActivity(intent);*/
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "CreateUser error :/\n"+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "CreateUser error :/\n" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
