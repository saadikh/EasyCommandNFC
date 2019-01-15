package cnt.nfc.mbds.fr.easycommandnfc.NFCtestfinal;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import cnt.nfc.mbds.fr.easycommandnfc.R;

public class NfcBaseActivity extends AppCompatActivity {

    private final static String TAG = NfcBaseActivity.class.getSimpleName();
    protected PendingIntent mPendingIntent;
    protected static IntentFilter[] mIntentFiltersArray;
    protected static String[][] mTechListsArray;
    protected NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_base);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        else if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC is disabled.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        }

        // Android system will populate it with the details of the tag when it is scanned
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        //Launched by tag scan ?
        Tag tag = (Tag) getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null && (getIntent().getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED)
                || getIntent().getAction().equals(NfcAdapter.ACTION_TECH_DISCOVERED)
                || getIntent().getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED))) {
            Log.i(TAG, "onCreate, tag found, calling onNewTag");
            getNewTag(tag, getIntent());
        }
    }

    //Static initialization
    static {

        // add intent filter
        IntentFilter mndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter mtech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter mtag = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        try {
            //Handles all MIME based dispatches !!! specify only the ones that you need.
            mndef.addDataType("*/*");

        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }

        mIntentFiltersArray = new IntentFilter[]{mndef, mtech/*, mtag*/};
        //array of TAG TECHNOLOGIES that your application wants to handle
        mTechListsArray = new String[][]{};
    }

    private void getNewTag(Tag tag, Intent intent) {
        if (tag == null) return;
        //Indicate to childs that a new tag has been detected
        onNewTag(tag);
    }

    protected void enableWriteMode() {
        IntentFilter mndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter mtech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter mtag = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mIntentFiltersArray = new IntentFilter[]{mndef,mtech,mtag};

        mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFiltersArray, null);
    }

    protected void disableWriteMode() {
        mNfcAdapter.disableForegroundDispatch(this);
    }

    protected void displayMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        // get the tag object for the discovered tag
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        getNewTag(tag, intent);
    }

    //This function is called in child activities when a new tag is scanned.
    protected void onNewTag(Tag tag) {
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
