package com.gymuyeligisistemi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Giriş Ekranı Activity
 * Kullanıcı adı ve şifre ile sisteme giriş yapılan ekran
 * Varsayılan: admin / 1234
 */
public class GirisActivity extends AppCompatActivity {

    private EditText edtKullaniciAdi;
    private EditText edtSifre;
    private Button btnGiris;
    private TextView txtBilgi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        // View bağlantıları
        edtKullaniciAdi = findViewById(R.id.edtKullaniciAdi);
        edtSifre = findViewById(R.id.edtSifre);
        btnGiris = findViewById(R.id.btnGiris);
        txtBilgi = findViewById(R.id.txtBilgi);

        // Giriş butonuna tıklama
        btnGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                girisYap();
            }
        });
    }

    private void girisYap() {
        String kullaniciAdi = edtKullaniciAdi.getText().toString().trim();
        String sifre = edtSifre.getText().toString().trim();

        // Boş alan kontrolü
        if (TextUtils.isEmpty(kullaniciAdi)) {
            edtKullaniciAdi.setError("Kullanıcı adı giriniz");
            edtKullaniciAdi.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(sifre)) {
            edtSifre.setError("Şifre giriniz");
            edtSifre.requestFocus();
            return;
        }

        // Basit doğrulama (admin / 1234)
        if (kullaniciAdi.equals("admin") && sifre.equals("1234")) {
            Toast.makeText(this, "Giriş başarılı! Hoş geldiniz.", Toast.LENGTH_SHORT).show();

            // Üye Kayıt ekranına geç
            Intent intent = new Intent(GirisActivity.this, UyeKayitActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Kullanıcı adı veya şifre hatalı!", Toast.LENGTH_LONG).show();
            edtSifre.setText("");
            edtSifre.requestFocus();
        }
    }
}
