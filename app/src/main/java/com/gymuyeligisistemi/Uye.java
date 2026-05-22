package com.gymuyeligisistemi;

import java.io.Serializable;

/**
 * Üye Model Sınıfı
 * Spor salonu üyelerinin bilgilerini tutan sınıf
 */
public class Uye implements Serializable {

    private int id;
    private String ad;
    private String soyad;
    private String telefon;
    private String eposta;
    private String uyelikTipi;

    // Parametresiz constructor
    public Uye() {
    }

    // Parametreli constructor
    public Uye(int id, String ad, String soyad, String telefon, String eposta, String uyelikTipi) {
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
        this.telefon = telefon;
        this.eposta = eposta;
        this.uyelikTipi = uyelikTipi;
    }

    // Getter ve Setter metotları
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEposta() {
        return eposta;
    }

    public void setEposta(String eposta) {
        this.eposta = eposta;
    }

    public String getUyelikTipi() {
        return uyelikTipi;
    }

    public void setUyelikTipi(String uyelikTipi) {
        this.uyelikTipi = uyelikTipi;
    }

    @Override
    public String toString() {
        return ad + " " + soyad + " - " + uyelikTipi;
    }
}
