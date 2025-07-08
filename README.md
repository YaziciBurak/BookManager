# 📚 Book Management API

Bu proje, Java ve Spring Boot kullanılarak geliştirilmiş bir kitap yönetim sistemidir.  
RESTful API yapısı sayesinde kitap ekleme, listeleme, güncelleme ve silme işlemleri yapılabilir.

---

## 🚀 Kullanılan Teknolojiler

- Java 17  
- Spring Boot 3.5.3  
- Spring Web (REST API)  
- Spring Data JPA  
- H2 In-Memory Database  
- Lombok  
- Jakarta Bean Validation  
- Maven

---

## 📂 API Özellikleri

- ✅ Kitap oluşturma (POST `/api/books`)
- ✅ Tüm kitapları listeleme (GET `/api/books`)
- ✅ ID ile kitap getirme (GET `/api/books/{id}`)
- ✅ Kitap güncelleme (PUT `/api/books/{id}`)
- ✅ Kitap silme (DELETE `/api/books/{id}`)
- ✅ DTO yapısıyla veri transferi
- ✅ Validasyon ve global hata yönetimi

---

## ▶️ Uygulamayı Çalıştırma

```bash
mvn spring-boot:run veya IDE üzerinden BookapiApplication.java dosyasını çalıştırarak başlatabilirsin.

Örnek POST
json
Copy
Edit
POST /api/books
Content-Type: application/json

{
  "title": "Sefiller",
  "author": "Victor Hugo",
  "publicationYear": 1862
}

---

## 🙏 Not

Kendi gelişimim için oluşturduğum basit bir Spring Boot projesidir.


