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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import cnt.nfc.mbds.fr.easycommandnfc.R;

public class WriteTextActivity extends NfcBaseActivity {


    private Button buttonW;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_text);

        progress = new ProgressDialog(this);

        buttonW = (Button) findViewById(R.id.buttonW);
        buttonW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view.getId() == R.id.buttonW) {
                    progress.setMessage("Touch and hold tag against phone to write. \n Wait please ");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setIndeterminate(true);
                    progress.show();
                    enableWriteMode();
                    handleIntent(getIntent());
                }
            }
        });
    }

    private void handleIntent(Intent intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag tag;
            tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (tag == null)
                displayMessage("TAG NULL");
            else
                writeTag(this, tag);
        }
    }

    /**
     * Format a tag and write our NDEF message
     */
    private boolean writeTag(Context context, Tag tag) {

        EditText tonEdit = (EditText) findViewById(R.id.edit_idResto);
        EditText idTable = (EditText)findViewById(R.id.edit_idTable);

        String NFCMessage = tonEdit.getText().toString();
        String NFCIdTable = idTable.getText().toString();

        NdefRecord textRecord = NdefRecord.createTextRecord("", NFCMessage);
        NdefRecord textRecord2 = NdefRecord.createTextRecord("", NFCIdTable);

        NdefMessage message = new NdefMessage(new NdefRecord[]{textRecord, textRecord2});

        try {
            // see if tag is already NDEF formatted
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    displayMessage("Read-only tag.");
                    return false;
                }

                // work out how much space we need for the data
                int size = message.toByteArray().length;
                if (ndef.getMaxSize() < size) {
                    displayMessage("Tag doesn't have enough free space.");
                    return false;
                }

                NdefMessage old_message = ndef.getNdefMessage();
                NdefRecord[] records = old_message.getRecords();
                for (NdefRecord ndefRecord : records) {
                    if (ndefRecord.getTnf() == 0) {
                        ndef.writeNdefMessage(message);
                        progress.dismiss();
                        displayMessage("Tag written successfully.");
                        return true;
                    } else {
                        progress.dismiss();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                        alertDialogBuilder.setMessage("Tag is already written. Do you want to erase?")
                                .setCancelable(false)
                                .setPositiveButton("YES",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent erase_intent = new Intent(getApplicationContext(), EraseActivity.class);
                                                startActivity(erase_intent);
                                            }
                                        });
                        alertDialogBuilder.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = alertDialogBuilder.create();
                        alert.show();
                        return false;
                    }
                }
            } else {
                // attempt to format tag
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        displayMessage("Tag written successfully!\nClose this app and scan tag.");
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
            displayMessage("Failed to write tag");
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
