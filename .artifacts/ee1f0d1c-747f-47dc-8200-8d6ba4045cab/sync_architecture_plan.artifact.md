# Rencana & Arsitektur Sinkronisasi Master Data POS PIK ke PostgreSQL Server (24/7)

Rencana ini dirancang agar **mudah diimplementasikan, efisien, aman, dan 100% GRATIS** tanpa biaya langganan cloud.

---

## 🏗️ 1. Pilihan Arsitektur Terbaik: REST API + Cloudflare Tunnel

Karena komputer Windows Anda menyala 24 jam dan sudah terinstal PostgreSQL, Anda tidak memerlukan server cloud berbayar. Cukup buat **Backend API sederhana** di PC Windows tersebut.

```
[ Android POS App ]
       │
       │ (HTTP POST / GET via Internet / HTTPS)
       ▼
 [ Cloudflare Tunnel ] (Gratis, mengubah localhost menjadi URL publik aman HTTPS)
       │
       │
       ▼
[ PC Windows 24/7 ]
  ├── Node.js / Python FastAPI (Backend API)
  ├── Folder /uploads (Menyimpan file gambar produk .jpg/.png)
  └── PostgreSQL Database (Menyimpan data master produk & path gambar)
```

---

## 🛠️ 2. Komponen & Teknologi yang Digunakan (100% Gratis)

1. **Backend Server**: **Node.js (Express)** atau **Python (FastAPI)** yang berjalan di PC Windows.
2. **Database**: **PostgreSQL** (yang sudah Anda siapkan di PC).
3. **Penyimpanan Gambar**: Disimpan di folder lokal Windows (`C:/pos_pik_images/`), sedangkan database PostgreSQL hanya menyimpan **nama file / URL path-nya**. Ini jauh lebih efisien daripada menyimpan gambar mentah (BLOB) di dalam kolom database.
4. **Public Tunnel**: **Cloudflare Tunnel (`cloudflared`)** atau **Ngrok (Free Tier)**.
   - *Mengapa Cloudflare Tunnel?* 100% gratis, memberikan domain HTTPS permanen (`https://pos-sync.namadomain.com` atau `trycloudflare.com`), dan tidak memerlukan setting Port Forwarding di modem/router WiFi rumah Anda.

---

## 📡 3. Desain Endpoint API yang Dibutuhkan

Di server PC Windows Anda, cukup buat 2 endpoint utama:

### A. Upload / Sync Data Master & Gambar (`POST /api/products/sync`)
* **Method**: `POST` (Multipart Form-Data)
* **Payload dari Android**:
  - `sku` (Text)
  - `name` (Text)
  - `category_id` (Int)
  - `cost_price` (Decimal)
  - `selling_price` (Decimal)
  - `image` (File / Multipart Image binary)
* **Proses di Server**:
  1. Menyimpan file gambar ke folder `C:/pos_pik_images/` dengan nama unik (misal: `PRD_12345.jpg`).
  2. Menyimpan/Update data produk ke tabel PostgreSQL (`UPSERT` berdasar `sku`).

### B. Download / Restore Master Data (`GET /api/products`)
* **Method**: `GET`
* **Proses di Server**:
  - Mengambil seluruh daftar produk dari PostgreSQL dan mengembalikan dalam format JSON.
  - Saat aplikasi Android di-install ulang, Anda cukup menekan tombol **"Restore dari Server"**, dan aplikasi akan otomatis mendownload semua produk beserta gambar primadonanya ke database lokal Room!

---

## 📝 4. Langkah Implementasi Praktis untuk Anda

1. **Di PC Windows (Server)**:
   - Buat database PostgreSQL dengan tabel `products` (`sku`, `name`, `category_id`, `cost_price`, `selling_price`, `image_path`).
   - Buat script backend sederhana (misal menggunakan Python FastAPI atau Node.js Express).
   - Jalankan Cloudflare Tunnel (`cloudflared tunnel --url http://localhost:3000`) untuk mendapatkan URL publik HTTPS.

2. **Di Aplikasi Android (POS PIK)**:
   - Tambahkan tombol **"Kirim Master ke Server"** di menu Pengaturan/Produk.
   - Gunakan pustaka **Retrofit / Ktor** di Kotlin untuk mengirim data produk dan file gambar menggunakan `MultipartBody`.
   - Tambahkan tombol **"Download Master dari Server"** untuk fitur *Restore* saat ganti/install ulang HP.
