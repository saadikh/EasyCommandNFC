package cnt.nfc.mbds.fr.easycommandnfc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignInActivity extends AppCompatActivity {
    Button mSignUpButton;
    private  View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.signup_button:
                    Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
                    startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mSignUpButton = findViewById(R.id.signup_button);
        mSignUpButton.setOnClickListener(mOnClickListener);
    }
}
