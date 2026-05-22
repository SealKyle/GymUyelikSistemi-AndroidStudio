# Gym Üyelik Kayıt Sistemi - Android Studio

## Proje Açıklaması
Bu uygulama, bir spor salonunun üyelik kayıt işlemlerini yönetmek için geliştirilmiş Android uygulamasıdır.
Android Studio ile Java kullanılarak geliştirilmiştir.

## Özellikler
- **Giriş Ekranı (Activity 1):** Kullanıcı adı ve şifre ile sisteme giriş
- **Üye Kayıt Ekranı (Activity 2):** Üye ekleme, listeleme ve silme işlemleri
- Üye bilgileri: Ad, Soyad, Telefon, E-posta, Üyelik Tipi
- ListView ile üye listesi görüntüleme
- SharedPreferences ile basit veri saklama

## Varsayılan Giriş Bilgileri
- **Kullanıcı Adı:** admin
- **Şifre:** 1234

## Gereksinimler
- Android Studio (Arctic Fox veya üzeri)
- Android SDK API 24 (minimum) - API 34 (target)
- Java 8+

## Kurulum
1. Android Studio'yu açın
2. File → Open ile proje klasörünü açın
3. Gradle sync'in tamamlanmasını bekleyin
4. Emulator veya fiziksel cihaz üzerinde çalıştırın (Run)

## Proje Yapısı
```
app/src/main/
├── java/com/gymuyeligisistemi/
│   ├── GirisActivity.java       → Giriş Activity
│   ├── UyeKayitActivity.java    → Üye Kayıt & Yönetim Activity
│   ├── Uye.java                 → Üye Model Sınıfı
│   └── UyeAdapter.java          → ListView Adapter
├── res/
│   ├── layout/
│   │   ├── activity_giris.xml            → Giriş ekranı layout
│   │   ├── activity_uye_kayit.xml        → Üye kayıt ekranı layout
│   │   └── list_item_uye.xml             → Liste öğesi layout
│   ├── values/
│   │   ├── strings.xml
│   │   ├── colors.xml
│   │   └── themes.xml
│   └── drawable/
│       └── rounded_button.xml
└── AndroidManifest.xml
```
