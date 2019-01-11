package cnt.nfc.mbds.fr.easycommandnfc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import cnt.nfc.mbds.fr.easycommandnfc.api.AuthClient;
import cnt.nfc.mbds.fr.easycommandnfc.api.RetrofitInstance;
import cnt.nfc.mbds.fr.easycommandnfc.api.model.Auth;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private AuthClient authClient = RetrofitInstance.getRetrofitInstance().create(AuthClient.class);
    EditText mUsername;
    EditText mPassword;
    Button mSignUpButton;
    Button mSignInButton;
    private  View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.signup_button:
                    Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
                    startActivity(intent);
                    return;
                case R.id.signin_button:
                    signIn(mUsername.getText().toString().trim(),mPassword.getText().toString().trim());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mSignInButton = findViewById(R.id.signin_button);
        mSignInButton.setOnClickListener(mOnClickListener);

        mSignUpButton = findViewById(R.id.signup_button);
        mSignUpButton.setOnClickListener(mOnClickListener);
    }

    public void signIn(String username, String password){
        Call<ResponseBody> call = authClient.signIn(new Auth(username,password));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Toast.makeText(SignInActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SignInActivity.this, "CreateUser error :/\n"+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SignInActivity.this, "CreateUser error :/\n" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
