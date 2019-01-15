package cnt.nfc.mbds.fr.easycommandnfc.NFCtestfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cnt.nfc.mbds.fr.easycommandnfc.R;

public class ChoiceWriteActivity extends AppCompatActivity {

    private Button mWtriteTextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_write);

        mWtriteTextButton = (Button)findViewById(R.id.writeTextButton);
        mWtriteTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.writeTextButton) {
                    Intent intentWriteText = new Intent(getApplicationContext(), WriteTextActivity.class);
                    startActivity(intentWriteText);
                }
            }
        });


    }
}
