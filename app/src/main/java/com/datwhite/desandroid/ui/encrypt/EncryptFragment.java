package com.datwhite.desandroid.ui.encrypt;

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


public class EncryptFragment extends Fragment {



    private static final Charset ISO_CHARSET = Charset.forName("ISO_8859_1");

    private EncryptViewModel encryptViewModel;

    private EditText inputTextEncrypt;
    private EditText inputKeyEncrypt;
    private EditText outputTextEncrypt;
    private Button encryptBtn;
    private Button copyBtnEnc;;
    private RadioButton inputKeyBtn;
    private RadioButton generateKeyBtn;

    ClipboardManager clipboardManager;
    ClipData clipData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        encryptViewModel =
                new ViewModelProvider(this).get(EncryptViewModel.class);
        View root = inflater.inflate(R.layout.fragment_encrypt, container, false);

        encryptBtn = root.findViewById(R.id.encryptBtn);
        copyBtnEnc = root.findViewById(R.id.copyBtnEnc);

        inputKeyBtn = root.findViewById(R.id.inputKeyBtn);
        generateKeyBtn = root.findViewById(R.id.generateKeyBtn);

        inputTextEncrypt = root.findViewById(R.id.inputTextEncrypt);
        inputKeyEncrypt = root.findViewById(R.id.inputKeyEncrypt);
        outputTextEncrypt = root.findViewById(R.id.outputTextEncrypt);

        inputKeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputKeyEncrypt.setActivated(true);
                inputKeyEncrypt.setClickable(true);
                inputKeyEncrypt.setCursorVisible(true);
                inputKeyEncrypt.setFocusable(true);
                inputKeyEncrypt.setFocusableInTouchMode(true);
//                inputKeyEncrypt.setEnabled(true);

            }
        });
        generateKeyBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                inputKeyEncrypt.setClickable(false);
                inputKeyEncrypt.setCursorVisible(false);
                inputKeyEncrypt.setFocusable(false);
                inputKeyEncrypt.setFocusableInTouchMode(false);
//                inputKeyEncrypt.setEnabled(false);

                try {
                    String k = GenerateKey.generate(8);
                    inputKeyEncrypt.setText(k);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });

        encryptBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String text = inputTextEncrypt.getText().toString();
                String key = inputKeyEncrypt.getText().toString();

                byte[] enc = DES.encrypt(text.getBytes(), key.getBytes());
                String encodedString = Base64.getEncoder().encodeToString(enc);

                GenerateKey.setKey(key);
                outputTextEncrypt.setText(encodedString);
            }
        });

        android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) root.getContext()
                .getSystemService(Context.CLIPBOARD_SERVICE);
        copyBtnEnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipData = ClipData.newPlainText("text", outputTextEncrypt.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                Toast  toast = Toast.makeText(root.getContext(),"Скопировано ",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 200);
                toast.show();
            }
        });
        return root;
    }
}