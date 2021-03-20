package com.datwhite.desandroid.ui.decrypt;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.datwhite.desandroid.DES;
import com.datwhite.desandroid.GenerateKey;
import com.datwhite.desandroid.R;

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class DecryptFragment extends Fragment {

    private static final Charset ISO_CHARSET = Charset.forName("ISO_8859_1");

    private DecryptViewModel decryptViewModel;

    private EditText inputTextDecrypt;
    private EditText inputKeyDecrypt;
    private EditText outputTextDecrypt;
    private Button decryptBtn;
    private Button copyBtnDec;
    private RadioButton inputKeyBtnDec;
    private RadioButton copyKeyBtnDec;

    ClipboardManager clipboardManager;
    ClipData clipData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        decryptViewModel =
                new ViewModelProvider(this).get(DecryptViewModel.class);
        View root = inflater.inflate(R.layout.fragment_decrypt, container, false);

        decryptBtn = root.findViewById(R.id.decryptBtn);
        copyBtnDec = root.findViewById(R.id.copyBtnDec);

        inputKeyBtnDec = root.findViewById(R.id.inputKeyBtnDec);
        copyKeyBtnDec = root.findViewById(R.id.copyKeyBtnDec);

        inputTextDecrypt = root.findViewById(R.id.inputTextDecrypt);
        inputKeyDecrypt = root.findViewById(R.id.inputKeyDecrypt);
        outputTextDecrypt = root.findViewById(R.id.outputTextDecrypt);

        inputKeyBtnDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputKeyDecrypt.setActivated(true);
                inputKeyDecrypt.setClickable(true);
                inputKeyDecrypt.setCursorVisible(true);
                inputKeyDecrypt.setFocusable(true);
                inputKeyDecrypt.setFocusableInTouchMode(true);
//                inputKeyEncrypt.setEnabled(true);

            }
        });
        copyKeyBtnDec.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                inputKeyDecrypt.setClickable(false);
                inputKeyDecrypt.setCursorVisible(false);
                inputKeyDecrypt.setFocusable(false);
                inputKeyDecrypt.setFocusableInTouchMode(false);
//                inputKeyEncrypt.setEnabled(false);
                String k = GenerateKey.getKey();
                inputKeyDecrypt.setText(k);
            }
        });

        decryptBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String textDec = inputTextDecrypt.getText().toString();
                String keyDec = inputKeyDecrypt.getText().toString();

                byte[] decodedBytes = Base64.getDecoder().decode(textDec);
                byte[] dec = DES.decrypt(decodedBytes, keyDec.getBytes());
                String decryptedText = new String(dec);

                outputTextDecrypt.setText(decryptedText);
            }
        });

        android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) root.getContext()
                .getSystemService(Context.CLIPBOARD_SERVICE);
        copyBtnDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipData = ClipData.newPlainText("text", outputTextDecrypt.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                Toast toast = Toast.makeText(root.getContext(),"Скопировано ",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 200);
                toast.show();
            }
        });

        return root;
    }
}