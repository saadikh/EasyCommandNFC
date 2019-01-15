package cnt.nfc.mbds.fr.easycommandnfc.NFCtestfinal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;

import java.io.IOException;

import cnt.nfc.mbds.fr.easycommandnfc.R;

public class EraseActivity extends NfcBaseActivity {

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erase);


        progress = new ProgressDialog(this);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("How you sure you want to erase the tag?")
                .setCancelable(false)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                progress.setMessage("Touch and hold tag against phone to erase. \n Wait please ");
                                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progress.setIndeterminate(true);
                                progress.show();
                                enableWriteMode();
                                handleIntent(getIntent());
                                setContentView(R.layout.activity_erase);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent back = new Intent(getApplicationContext(), Main2Activity.class);
                        startActivity(back);
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    private void handleIntent(Intent intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag tag;
            tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (tag == null)
                displayMessage("TAG NULL");
            else
                eraseTag(this, tag);
        }
    }

    /**
     * Format a tag and write our NDEF message
     */
    private boolean eraseTag(Context context, Tag tag) {
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    displayMessage("Read-only tag.");
                    return false;
                }
                ndef.writeNdefMessage(new NdefMessage(new NdefRecord(NdefRecord.TNF_EMPTY, null, null, null)));
                progress.dismiss();
                displayMessage("Tag erased successfully.");
                return true;
            } else {
                // attempt to format tag
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        displayMessage("Tag erased successfully!");
                        return true;
                    } catch (IOException e) {
                        displayMessage("Unable to format tag to NDEF.");
                        return false;
                    }
                } else {
                    displayMessage("Tag doesn't appear to support NDEF format.");
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            displayMessage("Failed to erase tag");
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableWriteMode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableWriteMode();
    }

    @Override
    public void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

}
