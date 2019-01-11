package cnt.nfc.mbds.fr.easycommandnfc.nfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;

import cnt.nfc.mbds.fr.easycommandnfc.R;

public class TagActivity extends AppCompatActivity {

    public final static String MESSAGE_NDEF = "cnt.nfc.mbds.fr.easycommandnfc.nfc.MESSAGE_NDEF";
    public final static String MESSAGE = "cnt.nfc.mbds.fr.easycommandnfc.nfc.MESSAGE";
    public final static String TAG_UID = "cnt.nfc.mbds.fr.easycommandnfc.nfc.UID";
    public final static String READ_TAG = "cnt.nfc.mbds.fr.easycommandnfc.nfc.READ_TAG";

    private static String message;
    private static String doRead;
    private String messageNDEF; //data in nfc tag
    private String tagUID;
    private Tag tag;
    int result;

    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    NfcAdapter nfcAdapter = null;
    PendingIntent pendingIntent;
    Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_nfc);

        // message à écrire ?
        myIntent = getIntent();
        try {
            message = myIntent.getStringExtra(MESSAGE);
            doRead = myIntent.getStringExtra(READ_TAG);
        } catch (Exception e) {
            // pas de message à écrire
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        nfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).
                addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT), 0);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        onNewIntent(myIntent);
    }

    @Override
    public void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);//for offering the must priority at this activity
    }

    //M�thode invoqu�e lors de la d�tection d'un tag/p�riph�rique NFC
    @Override
    public void onNewIntent(Intent intent) {
        //D�tection d'un p�riph�rique NFC (tag ou autre)
        if ((intent.getAction() != null) &&
                ((NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction()) ||
                        NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()) ||
                        NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())))) {

            tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG); //for geting tag
            boolean isWritable = false;
            int content = -1;
            String[] technologies = null;
            //Infos sur le tag
//            technologies = tag.getTechList();
//            content = tag.describeContents();
//            isWritable = ndef.isWritable();
//            boolean canMakeReadOnly = ndef.canMakeReadOnly();
            byte[] id = tag.getId();
            tagUID = byteArrayToHex(id).toUpperCase();
            //messageNDEF = tag.toString();
            if (message!=null && !message.equals("")) {
                if (writeNdef(tag)) {
                    result = 2;
                } else {
                    result = 3;
                }

                TagActivity.this.setResult(result, new Intent()); //to redirect mainactivity with data
            }

            //for reading data nfc tag
            if(doRead!=null && !doRead.equals("")){
                readNdef(tag);
            }

            // retour à l'activité main pour affichage du résultat
            Bundle bundle = new Bundle();
            bundle.putString(TagActivity.MESSAGE_NDEF,
                    messageNDEF);
            bundle.putString(TagActivity.TAG_UID,
                    tagUID);//ok

            Intent mainActivity = new Intent(
                    getBaseContext(),
                    TagManagerActivity.class);
            mainActivity.putExtras(bundle);
            startActivity(mainActivity);

            finish();
        }
    }

    //method for writing data on tag
    private boolean writeNdef(Tag tag) {
        NdefMessage msg = new NdefMessage(
                NdefRecord.createMime("application/cnt.nfc.mbds.fr.easycommandnfc.nfc", message.getBytes())
        );
        try {
            int size = msg.toByteArray().length;
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    return false;
                }
                ndef.writeNdefMessage(msg);
                ndef.close();
                message = "";
                //messageNDEF = message;
                return true;
            } else {
                //Tags qui n�cessitent un formatage :
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    //Inspir� de la source � cette URL :
                    //http://www.jessechen.net/blog/how-tonfc-on-the-android-platform
                    try {
                        format.connect();
                        //Formatage et �criture du message:
                        format.format(msg);
                        //ou en verrouillant le tag en �criture :
                        //formatable.formatReadOnly(message);
                        format.close();
                    } catch (IOException e) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Read the tag
     */
    public boolean readNdef(Tag tag) {
        Ndef ndef = Ndef.get(tag);
        if (ndef != null) {
            try {
                ndef.connect();
                NdefRecord[] records = ndef.getNdefMessage().getRecords();
                messageNDEF = "";
                if (records != null) {
                    for (int i = 0; i < records.length; i++) {
                        NdefRecord record = records[i];
                        //Infos sur le tag...
                        //byte[] idRec = "".getBytes();
                        //short tnf = 0;
     /*                   byte[] type = "".getBytes();
                             //idRec = record.getId();
                            //tnf = record.getTnf();
                            type = record.getType();
                        } catch (Exception e) {
                            // Les infos du tag sont incompl�tes
                        }*/
                        //Message contenu sur le tag sous forme d'URI
                        /*if (Arrays.equals(type, NdefRecord.RTD_SMART_POSTER) ||
                                Arrays.equals(type, NdefRecord.RTD_URI) ||
                                Arrays.equals(type, NdefRecord.RTD_TEXT)) {
                            Uri uri = record.toUri();
                        }*/
                        messageNDEF += new String(record.getPayload(), UTF8_CHARSET) + " ";
                    }
                }
                ndef.close();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

}
