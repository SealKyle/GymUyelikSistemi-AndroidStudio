package com.gymuyeligisistemi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Üye Kayıt ve Yönetim Activity
 * Üye ekleme, listeleme ve silme işlemlerinin yapıldığı ekran
 */
public class UyeKayitActivity extends AppCompatActivity {

    private EditText edtAd, edtSoyad, edtTelefon, edtEposta;
    private Spinner spnUyelikTipi;
    private Button btnKaydet, btnTemizle, btnCikis;
    private ListView lstUyeler;
    private TextView txtToplamUye;

    private List<Uye> uyeListesi;
    private UyeAdapter uyeAdapter;
    private int sonId = 0;

    private static final String PREF_NAME = "GymUyeVerileri";
    private static final String KEY_UYELER = "uyeler";
    private static final String KEY_SON_ID = "sonId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uye_kayit);

        // View bağlantıları
        edtAd = findViewById(R.id.edtAd);
        edtSoyad = findViewById(R.id.edtSoyad);
        edtTelefon = findViewById(R.id.edtTelefon);
        edtEposta = findViewById(R.id.edtEposta);
        spnUyelikTipi = findViewById(R.id.spnUyelikTipi);
        btnKaydet = findViewById(R.id.btnKaydet);
        btnTemizle = findViewById(R.id.btnTemizle);
        btnCikis = findViewById(R.id.btnCikis);
        lstUyeler = findViewById(R.id.lstUyeler);
        txtToplamUye = findViewById(R.id.txtToplamUye);

        // Spinner (Üyelik Tipi) ayarla
        String[] uyelikTipleri = {"Aylık", "3 Aylık", "6 Aylık", "Yıllık"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, uyelikTipleri);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnUyelikTipi.setAdapter(spinnerAdapter);

        // Üye listesini başlat
        uyeListesi = new ArrayList<>();
        verileriYukle();

        // Adapter
        uyeAdapter = new UyeAdapter(this, uyeListesi);
        lstUyeler.setAdapter(uyeAdapter);
        toplamUyeGuncelle();

        // ---- EVENT LISTENERS ----

        // Kaydet butonu
        btnKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uyeKaydet();
            }
        });

        // Temizle butonu
        btnTemizle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formuTemizle();
            }
        });

        // Çıkış butonu
        btnCikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UyeKayitActivity.this)
                        .setTitle("Çıkış")
                        .setMessage("Uygulamadan çıkmak istediğinize emin misiniz?")
                        .setPositiveButton("Evet", (dialog, which) -> {
                            verileriKaydet();
                            finishAffinity();
                        })
                        .setNegativeButton("Hayır", null)
                        .show();
            }
        });

        // Liste öğesine uzun basma → silme
        lstUyeler.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Uye secilenUye = uyeListesi.get(position);

                new AlertDialog.Builder(UyeKayitActivity.this)
                        .setTitle("Üye Sil")
                        .setMessage(secilenUye.getAd() + " " + secilenUye.getSoyad() +
                                " adlı üyeyi silmek istediğinize emin misiniz?")
                        .setPositiveButton("Sil", (dialog, which) -> {
                            uyeListesi.remove(position);
                            uyeAdapter.notifyDataSetChanged();
                            toplamUyeGuncelle();
                            verileriKaydet();
                            Toast.makeText(UyeKayitActivity.this,
                                    "Üye silindi.", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("İptal", null)
                        .show();

                return true;
            }
        });

        // Liste öğesine tıklama → detay gösterme
        lstUyeler.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uye secilenUye = uyeListesi.get(position);
                String detay = "Ad: " + secilenUye.getAd() + "\n" +
                        "Soyad: " + secilenUye.getSoyad() + "\n" +
                        "Telefon: " + secilenUye.getTelefon() + "\n" +
                        "E-posta: " + secilenUye.getEposta() + "\n" +
                        "Üyelik: " + secilenUye.getUyelikTipi();

                new AlertDialog.Builder(UyeKayitActivity.this)
                        .setTitle("Üye Detayı")
                        .setMessage(detay)
                        .setPositiveButton("Tamam", null)
                        .setNegativeButton("Sil", (dialog, which) -> {
                            uyeListesi.remove(position);
                            uyeAdapter.notifyDataSetChanged();
                            toplamUyeGuncelle();
                            verileriKaydet();
                            Toast.makeText(UyeKayitActivity.this,
                                    "Üye silindi.", Toast.LENGTH_SHORT).show();
                        })
                        .show();
            }
        });
    }

    private void uyeKaydet() {
        String ad = edtAd.getText().toString().trim();
        String soyad = edtSoyad.getText().toString().trim();
        String telefon = edtTelefon.getText().toString().trim();
        String eposta = edtEposta.getText().toString().trim();
        String uyelikTipi = spnUyelikTipi.getSelectedItem().toString();

        // Doğrulama
        if (TextUtils.isEmpty(ad)) {
            edtAd.setError("Ad giriniz");
            edtAd.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(soyad)) {
            edtSoyad.setError("Soyad giriniz");
            edtSoyad.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(telefon)) {
            edtTelefon.setError("Telefon giriniz");
            edtTelefon.requestFocus();
            return;
        }

        // Yeni üye oluştur
        sonId++;
        Uye yeniUye = new Uye(sonId, ad, soyad, telefon, eposta, uyelikTipi);
        uyeListesi.add(yeniUye);
        uyeAdapter.notifyDataSetChanged();
        toplamUyeGuncelle();
        verileriKaydet();

        Toast.makeText(this, "Üye başarıyla kaydedildi!", Toast.LENGTH_SHORT).show();
        formuTemizle();

        // Listenin en altına kaydır
        lstUyeler.smoothScrollToPosition(uyeListesi.size() - 1);
    }

    private void formuTemizle() {
        edtAd.setText("");
        edtSoyad.setText("");
        edtTelefon.setText("");
        edtEposta.setText("");
        spnUyelikTipi.setSelection(0);
        edtAd.requestFocus();
    }

    private void toplamUyeGuncelle() {
        txtToplamUye.setText("Toplam Üye: " + uyeListesi.size());
    }

    // SharedPreferences ile veri kaydetme
    private void verileriKaydet() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        JSONArray jsonArray = new JSONArray();
        for (Uye uye : uyeListesi) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("id", uye.getId());
                obj.put("ad", uye.getAd());
                obj.put("soyad", uye.getSoyad());
                obj.put("telefon", uye.getTelefon());
                obj.put("eposta", uye.getEposta());
                obj.put("uyelikTipi", uye.getUyelikTipi());
                jsonArray.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        editor.putString(KEY_UYELER, jsonArray.toString());
        editor.putInt(KEY_SON_ID, sonId);
        editor.apply();
    }

    // SharedPreferences'tan veri yükleme
    private void verileriYukle() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        sonId = prefs.getInt(KEY_SON_ID, 0);
        String jsonStr = prefs.getString(KEY_UYELER, null);

        if (jsonStr != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Uye uye = new Uye(
                            obj.getInt("id"),
                            obj.getString("ad"),
                            obj.getString("soyad"),
                            obj.getString("telefon"),
                            obj.getString("eposta"),
                            obj.getString("uyelikTipi")
                    );
                    uyeListesi.add(uye);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Eğer hiç veri yoksa örnek veri ekle
        if (uyeListesi.isEmpty()) {
            ornekVerilerEkle();
        }
    }

    private void ornekVerilerEkle() {
        sonId = 5;
        uyeListesi.add(new Uye(1, "Ahmet", "Yılmaz", "0532 111 2233", "ahmet@email.com", "Aylık"));
        uyeListesi.add(new Uye(2, "Fatma", "Demir", "0533 222 3344", "fatma@email.com", "3 Aylık"));
        uyeListesi.add(new Uye(3, "Mehmet", "Kaya", "0534 333 4455", "mehmet@email.com", "Yıllık"));
        uyeListesi.add(new Uye(4, "Ayşe", "Çelik", "0535 444 5566", "ayse@email.com", "6 Aylık"));
        uyeListesi.add(new Uye(5, "Ali", "Öztürk", "0536 555 6677", "ali@email.com", "Aylık"));
        verileriKaydet();
    }

    @Override
    protected void onPause() {
        super.onPause();
        verileriKaydet();
    }
}
