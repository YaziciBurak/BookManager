# 📚 Book Management API

Bu proje, Java ve Spring Boot kullanılarak geliştirilmiş bir kitap yönetim sistemidir.  
RESTful API yapısı sayesinde kitap ekleme, listeleme, güncelleme ve silme işlemleri yapılabilir.

---

## 🚀 Kullanılan Teknolojiler

- Java 17  
- Spring Boot 3.1.5  
- Spring Web (REST API)  
- Spring Data JPA  
- H2 In-Memory Database  
- Lombok  
- Jakarta Bean Validation  
- Maven
- Swagger

---

## 🔐 API Özellikleri

- Kullanıcı kayıt & giriş – JWT & Refresh Token
- Access token kısa ömürlü, Refresh token ile yenilenir
- Tüm kitapları listeleme (GET `/api/books`)
- Tüm kitap CRUD işlemleri yalnızca **doğrulanmış kullanıcılar** tarafından yapılabilir  
- Kitap CRUD işlemleri  
  - `POST /api/books` – kitap ekleme  
  - `GET /api/books` – tüm kitapları listeleme  
  - `GET /api/books/{id}` – ID ile kitap getirme  
  - `PUT /api/books/{id}` – kitap güncelleme  
  - `DELETE /api/books/{id}` – kitap silme  
- DTO & Validasyon & Global hata yönetimi  
- Swagger UI ile test edilebilir

---

## ▶️ Uygulamayı Çalıştırma

### 1. Terminal Üzerinden

```bash
git clone https://github.com/YaziciBurak/BookManager.git
cd BookManager
mvn spring-boot:run
```
2. IDE Üzerinden
BookapiApplication.java dosyasını çalıştırarak uygulamayı başlatabilirsiniz.

🔍 Swagger UI
API'yi görsel olarak test etmek için:
http://localhost:8080/swagger-ui/index.html


## 🧪 API Testi Örnekleri

### 🔐 Auth

```http
POST /api/auth/register
{
  "username": "test",
  "password": "123"
}

POST /api/auth/login
{
  "username": "test",
  "password": "123"
}

POST /api/auth/refresh-token
{
  "refreshToken": "<refresh token burada>"
}
```

📚 Kitap Ekleme (JWT gereklidir)

```
POST /api/books
Authorization: Bearer <access_token>

{
  "title": "The Witcher",
  "author": "Andrzej Sapkowski",
  "publicationYear": 1948
}
```

➡️ Diğer CRUD işlemleri için Swagger arayüzünü kullanabilirsiniz.

---

##  Not

Kendi gelişimim için oluşturduğum basit bir Spring Boot projesidir.


