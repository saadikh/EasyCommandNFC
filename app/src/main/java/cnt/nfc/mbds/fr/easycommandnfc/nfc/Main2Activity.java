package cnt.nfc.mbds.fr.easycommandnfc.NFCtestfinal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import cnt.nfc.mbds.fr.easycommandnfc.R;

public class Main2Activity extends NfcBaseActivity {
    public static final String PREFS_NAME = "MyPrefsFile";

    SharedPreferences preferences;
    private Button mReadTagButton;
    private Button mWriteTagButton;
    private Button mEraseTagButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mWriteTagButton = (Button) findViewById(R.id.writeButton);
        mWriteTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.writeButton) {
                    Intent intentWrite = new Intent(getApplicationContext(), ChoiceWriteActivity.class);
                    startActivity(intentWrite);
                }
            }
        });

        mReadTagButton = (Button) findViewById(R.id.readButton);
        mReadTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.readButton) {
                    Intent intentRead = new Intent(getApplicationContext(), ReadActivity.class);
                    startActivity(intentRead);
                }
            }
        });

        mEraseTagButton = (Button) findViewById(R.id.eraseButton);
        mEraseTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.eraseButton) {
                    Intent intentErase = new Intent(getApplicationContext(), EraseActivity.class);
                    startActivity(intentErase);
                }
            }
        });
    }

    /**
     * Create the options menu that is shown on the action bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_about:
                displayAboutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayAboutDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.about_title));
        builder.setMessage(getString(R.string.about_desc));

        builder.setNegativeButton(R.string.about_close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
