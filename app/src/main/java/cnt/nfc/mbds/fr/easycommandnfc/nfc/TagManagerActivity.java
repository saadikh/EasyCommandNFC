package cnt.nfc.mbds.fr.easycommandnfc.nfc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cnt.nfc.mbds.fr.easycommandnfc.R;

public class TagManagerActivity extends AppCompatActivity {


    private EditText editTextMessage;
    private TextView editTextUID;
    private TextView editTextNDEF;
    private Button btnShare;
    private Button btnClear;
    private Button btnRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_manager);

        btnShare = findViewById(R.id.buttonShare);
        btnClear = findViewById(R.id.buttonClear);
        btnRead = findViewById(R.id.buttonRead);

        editTextMessage = findViewById(R.id.textViewTagNFC);
        editTextNDEF = findViewById(R.id.textViewNDEF);
        editTextUID = findViewById(R.id.textViewUID);

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String doRead = "doRead";
                Intent intent = new Intent( TagManagerActivity.this, TagActivity.class );
                intent.putExtra(TagActivity.READ_TAG, doRead);
                startActivityForResult(intent, 0); //for starting tagActivity (child activity)
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String message = editTextMessage.getText().toString().trim();
                if (message != null & !message.trim().isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            TagManagerActivity.this);
                    builder.setMessage(
                            "Voulez-vous encoder ce message sur le tag NFC ?")
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            Intent intent = new Intent( TagManagerActivity.this, TagActivity.class );
                                            intent.putExtra(TagActivity.MESSAGE, message);
                                            startActivityForResult(intent, 0); //for starting tagActivity (child activity)
                                        }
                                    })
                            .setNegativeButton("Annuler",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface arg0, int id) {
                                        }

                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    editTextMessage.setError("Vous devez indiquer un message dans la zone de texte !");
                }

            }

        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        TagManagerActivity.this);
                builder.setMessage("Voulez-vous vraiment effacer ce message ?")
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        //EditText txt = findViewById(R.id.textViewTagNFC); ********** modification
                                        editTextMessage.setText("");
                                        editTextMessage.invalidate();
                                    }
                                })
                        .setNegativeButton("Annuler",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0,
                                                        int id) {
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            Bundle bundle = this.getIntent().getExtras();
            editTextUID.setText(bundle.getString(TagActivity.TAG_UID, ""));
            editTextNDEF.setText(bundle.getString(TagActivity.MESSAGE_NDEF, ""));
        } catch (Exception e) {
            // pas de message à écrire
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if( resultCode==1 ) {
            Toast.makeText(this, "Tag détecté !", Toast.LENGTH_SHORT).show();
        } else if( resultCode==2 ) {
            Toast.makeText(this, "L'écriture du message effectuée avec succès !", Toast.LENGTH_SHORT).show();
        } else if( resultCode==3 ) {
            Toast.makeText(this, "L'écriture du message a échouée !", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
