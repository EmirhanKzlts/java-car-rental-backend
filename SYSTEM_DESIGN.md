Project Overview

Project Name: Car Rental System

Goal:
Kullanıcıların araçları belirli tarihler arasında arayıp müsait araçları görüntüleyebilmesi, rezervasyon yapabilmesi ve rezervasyonlarını yönetebilmesi.

Tech Stack:
    Backend:
    Java 21
    Spring Boot
    Spring Security
    JWT
    MySQL
    Maven

    Frontend:
    React
    Tailwind CSS
    Axios
    React Router



1. # Aktörler

Müşteri:
    Kayıt ol
    Giriş yap
    Çıkış yap
    Arabalara göz at
    Araba ara
    Araba filtrele
    Arabanın detaylarına bak
    Rezervasyonu tamamlanan araba puanla
    Rezervasyonu tamamlanan araba hakkında yorum bırak
    Rezervasyon oluştur
    Rezervasyon iptal et
    Rezervasyon ödemesi yap
    Rezervasyonlarını incele

Admin:
    Giriş yap
    Araba ekle-sil
    Araba özelliklerini değiştir
    Lokasyon ekle-sil
    Müşterileri yönet
    Müşteri sil
    Araba kategorisi ekle-sil
    Araba yorumlarını sil.
    Rezervasyonları görüntüle
    Rezervasyon iptal et




2. # Functional Requirements

Authentication:
    Müşteri kayıt ol.
    Müşteri giriş yap.
    Müşteri çıkış yap.
    Müşteri oturumunu JWT ile doğrula.
    Admin rolü ile admin girişi yap.

Araba:
    Müşteri arabaları görüntüleyebilir.
    Müşteri arabaları yer ve tarihe göre arayabilir.
    Müşteri arabları kategoriye göre filtreleyebilir.
    Müşteri araba detaylarını görebilir.
    Müşteri rezervasyonu tamamlanan arabaya puan verebilir.
    Müşteri rezervasyonu tamamlanan arabaya yorum bırakabilir.
    Admin araba ekle-sil-güncelle yapabilir.

Rezervasyon:
    Müşteri rezervasyonlarını görüntüleyebilir
    Müşteri rezervasyon yapabilir.
    Müşteri rezervasyonlarını iptal edebilir.
    Admin rezervasyonları görüntüleyebilir.
    Admin rezervasyon iptal edebilir.
    Admin araba yorumlarını silebilir.

Kategori(Araba modelleri(örneğin:sedan, suv):
    Admin kategori ekle-sil yapabilir.

Konum:
    Admin konum ekle-sil yapabilir.

Ödeme:
    Müşteri rezervasyon için ödeme yapabilir.
    Müşteri ödeme durumunu görüntüleyebilir.
    Admin rezervasyonun ödeme durumunu görüntüleyebilir.



3. # Core Entities

Kullanıcı
Araba
Araba Kategorisi
Konum
Rezervasyon
Ödeme
Yorum



4. # Entity Relationship

Müşteri 			1 ──── 0..N Rezervasyon
Müşteri			    1 ───── 0..N Yorum
Araba   			1 ──── 0..N Rezervasyon
Araba 			    1 ───── 0..N Yorum
Araba Kategorisi	1 ───── 0..N Araba
Rezervasyon 		1 ───── N Ödeme
Rezervasyon         1 ───── 0..1 Yorum
Konum 			    1 ───── N Rezervasyon
                        ↑
                        Alma Konumu

Konum 			    1 ───── N Rezervasyon
                        ↑
                        Bırakma Konumu



5. # Business Constraints

User Constraints:
    Email eşsiz olmalı
    Admin ya da müşteri rolü olmalı
    Şifre BCrypt ile şifrelenmeli

Car Constraints:
    STATUS: ACTIVE-MAINTENANCE-INACTIVE
    ACTIVE durumundaki arabalar rezerve edilebilir.
    MAINTENANCE ya da INACTIVE statüsündeki arabalar rezerve edilemez
    Bir kategorisi olmalı
    1900 ve günümüz yılları arasında bir yıla sahip olmalı
    0 dan büyük koltuk sayısı olmalı
    0 dan büyük kilometresi olmalı
    0 dan büyük günlük fiyatı olmalı
    

Reservation Constraints:
    STATUS: PENDING-CONFIRMED-ACTIVE-CANCELLED-COMPLETED
    Başlangıç tarihi bitiş tarihinden önce olmalı
    Günümüzden daha öncesinde bir başlangıç tarihi seçilmemeli
    Bir arabanın rezervasyon tarihleri çakışmamalı
    Aynı kişi bir arabayı bitiş tarihinde tekrar rezerve edebilir
    Müşteri sadece kendi rezervasyonlarını görebilir ve yönetebilir
    Toplam fiyat rezervasyon oluşturulduğunda hesaplanır
    Tamamlanan rezervasyonlar iptal edilemez
    İptal edilen rezervasyonlar geri döndürülemez
    Başlamasına 8 saat kalan rezervasyonlar iptal edilemez
    Admin pending ve confirmed rezervasyonları iptal edebilir
    Ödeme tamamlanmadan PENDING durumda olan rezervasyon için 15 dakika süre tanınır.
    15 dakika içerisinde ödeme tamamlanmazsa rezervasyon otomatik olarak CANCELLED durumuna geçirilir.

Payment Constraints:
    STATUS: PENDING-SUCCESS-FAILED
    Sadece PENDING durumundaki rezervasyonlar için ödeme yapılabilir.
    Ödeme miktarı rezervasyonun totalPrice değerine eşit olmalıdır.
    Başarılı ödeme sonucunda rezervasyon CONFIRMED durumuna geçirilir.
    Başarısız ödeme sonucunda rezervasyon PENDING durumunda kalır.
    CANCELLED veya CONFIRMED rezervasyonlar için ödeme yapılamaz.

    Payment Simulation
    Gerçek bir ödeme sağlayıcısı kullanılmayacaktır.
    Ödeme işlemi Mock Payment Gateway kullanılarak simüle edilecektir.
    Payment request içerisinde gönderilen result değeri ödeme sonucunu simüle eder.
    SUCCESS → başarılı ödeme
    FAILED → başarısız ödeme

Review Constraints:
    Bir COMPLETED rezervasyon için yalnızca 1 review oluşturulabilir.
    Müşteri yalnızca kendisine ait COMPLETED rezervasyonlardaki araçlara yorum verebilir.
    Puanlama 1-5 arası
    Admin yorumu silebilir



# 6. Reservation Lifecycle

   PENDING
   |
   | payment successful
   ▼
   CONFIRMED
   |
   | pick up time reached
   ▼
   ACTIVE
   |
   | return time reached
   ▼
   COMPLETED

    PENDING ─────→ CANCELLED
    │
    │ payment timeout (15 min)
    ▼
    CANCELLED
    
    CONFIRMED ───→ CANCELLED



7. # API Design

Authentication:
    POST	/api/auth/register

        RegisterRequest:
            {
                "firstName":"Alex",
                "lastName":"Felix",
                "email":"alex_felix@example.com",
                "password":"alexfelix123",
            }
            
        RegisterResponse:
            {
                "id": 1,
                "firstName": "Alex",
                "lastName": "Felix",
                "email": "alex_felix@example.com",
                "role": "CUSTOMER",
            }
    
    POST    /api/auth/login

        LoginRequest:
            {
                "email": "alex_felix@example.com",
                "password": "alexfelix123",
            }

        LoginResponse:
            {
                "token": "...",
                "tokenType": "Bearer",
                "user": {
                            "id": 1,
                            "firstname": "Alex",
                            "lastName": "Felix",
                            "email": "alex_felix@example.com",
                            "role": "CUSTOMER",
                        }
                    
            }

-------------------------------------------------------------------------

Users:
Admin:
    GET    /api/admin/users
            
        List<AdminUserResponse>
    
    GET   /api/admin/users/{id}
        
        AdminUserResponse:
            {
                "id": 5,
                "firstName": "Alex",
                "lastName": "Felix",
                "email": "alex@example.com",
                "role": "CUSTOMER",
                "createdAt": "2026-08-20T12:00:00"
            }

    DELETE /api/admin/users/{id}
        
        204 No Content
    

-------------------------------------------------------------------------

Cars:
    GET     /api/cars
        
        Query Parameters:
            categoryId
            pickupLocationId
            startDate
            endDate
            minPrice
            maxPrice
            sort

        CarListResponse:
            [
                {
                    "id": 1,
                    "brand": "BMW",
                    "model": "320i",
                    "year": 2024,
                    "category":{
                                    "id": 1,
                                    "name": "Sedan",
                                },
                    "dailyPrice": 120.00,
                    "status": "ACTIVE"
                },
                {
                    "id": 2,
                    "brand": "Mercedes",
                    "model": "C200",
                    "year": 2023,
                    "category":{
                                    "id": 1,
                                    "name": "Sedan",
                                },
                    "dailyPrice": 140.00,
                    "status": "ACTIVE"
                }
            ]


    GET     /api/cars/{id}

        CarDetailsResponse:
            {
                "id": 1,
                "brand": "BMW",
                "model": "320i",
                "year": 2024,
                "category":{
                                "id": 1,
                                "name": "Sedan",
                            },
                "fuelType": "Diesel",
                "transmission": "Manual",
                "mileage": 15000,
                "seatCount": 5,
                "dailyPrice": 120.00,
                "status": "ACTIVE",
                "averageRating": 4.6,
                "reviewCount": 23
            }

Admin:
    POST    /api/admin/cars
        
        CreateCarRequest:
            {
                "brand": "BMW",
                "model": "320i",
                "year": 2024,
                "fuelType": "Diesel",
                "transmission": "Manual",
                "mileage": 15000,
                "seatCount": 5,
                "dailyPrice": 120.00,
                "categoryId": 2,
            }

    PUT     /api/admin/cars/{id}

        UpdateCarRequest:
            {
                "brand": "BMW",
                "model": "320i",
                "year": 2024,
                "fuelType": "Diesel",
                "transmission": "Manual",
                "mileage": 15000,
                "seatCount": 5,
                "dailyPrice": 120.00,
                "categoryId": 2,
            }

    DELETE  /api/admin/cars/{id}

            204 No Content

-------------------------------------------------------------------------

Categories:
    GET     /api/categories

        List<CategoryResponse>

    Admin:
    POST    /api/admin/categories
        
        CreateCategoryRequest:
            {
                "name": "Sedan"
            }
        
        CategoryResponse:
            {
                "id": 1,
                "name": "Sedan",
            }

    DELETE  /api/admin/categories/{id}

            204 No Content

-------------------------------------------------------------------------

Locations:
    GET     /api/locations

        List<LocationResponse>
    
    Admin:
    POST    /api/admin/locations

        CreateLocationRequest:
            {
                "name": "Istanbul Airport",
                "address": "Istanbul Airport, Terminal 1",
            }

        LocationResponse:
            {   
                "id": 1,
                "name": "Istanbul Airport",
                "address": "Istanbul Airport, Terminal 1",
            }

    DELETE  /api/admin/locations/{id}
            
            204 No Content

-------------------------------------------------------------------------

Reservations:
    POST    /api/reservations
        
        CreateReservationRequest:
            {
                "carId": 1,
                "startDate": "2026-08-27T10:00:00",
                "endDate": "2026-08-29T10:00:00",
                "pickupLocationId": 1,
                "dropoffLocationId": 1,
            }

        ReservationResponse:
            {
                "id": 1,
                "car": {
                            "id": 1,
                            "brand:"BMW",
                            "model":320i
                        }
                "startDate": "2026-08-27T10:00:00",
                "endDate": "2026-08-29T10:00:00",
                "pickupLocation": {
                                    "id": 1,
                                    "name": "Istanbul Airport",
                                  }
                "dropoffLocation": {
                                    "id": 1,
                                    "name": "Istanbul Airport",
                                  }
                "totalPrice": 500.00,
                "status": "PENDING"
            }

    GET     /api/reservations

        ReservationListResponse:
            [
                {
                    "id": 1,
                    "car": {
                                "id": 1,
                                "brand": "BMW",
                                "model": "320i"
                            },
                    "startDate": "2026-08-27T10:00:00",
                    "endDate": "2026-08-29T10:00:00",
                    "totalPrice": 500.00,
                    "status": "PENDING"
                }
            ]

    GET     /api/reservations/{id}

        ReservationResponse
            

    PATCH   /api/reservations/{id}/cancel
        
        ReservationResponse

    
    Admin:
    GET     /api/admin/reservations
            
            List<AdminReservationResponse>

    PATCH   /api/admin/reservations/{id}/cancel

            AdminReservationResponse:
                {
                    "id": 1,
                    "user": {
                        "id": 5,
                        "firstName": "Alex",
                        "lastName": "Felix",
                        "email": "alex@example.com"
                    },
                    "car": {
                        "id": 1,
                        "brand": "BMW",
                        "model": "320i"
                    },
                    "startDate": "2026-08-27T10:00:00",
                    "endDate": "2026-08-29T10:00:00",
                    "totalPrice": 500.00,
                    "status": "CANCELLED"
                },

-------------------------------------------------------------------------

Payments:
    GET     /api/reservations/{reservationID}/payment
            
            PaymentResponse:
                {
                    "id": 1,
                    "status": "SUCCESS",
                    "amount": 140.00,
                }

    POST    /api/reservations/{reservationID}/payment

            PaymentRequest:
                {
                    "result": "SUCCESS"
                }

            PaymentResponse
    
    Admin:
    GET     /api/admin/reservations/{reservationID}/payment
            
            PaymentResponse

-------------------------------------------------------------------------

Reviews:
    POST    /api/cars/{carId}/reviews
            
            CreateReviewRequest:
                {
                    "comment": "Car was good.",
                    "rating": 4
                }

            ReviewResponse:
                {
                    "id": 1,
                    "user": {
                                "id": 5,
                                "firstName": "Alex",
                                "lastName": "Felix"
                            },
                    "comment": "Car was good.",
                    "rating": 4
                    "createdAt": "2026-08-29T15:30:00"
                }

    GET     /api/cars/{carId}/reviews
            
            List<ReviewResponse>

    DELETE  /api/reviews/{id}

            204 No Content



8. # Backend Architecture

    8.1 Architecture Pattern
        Layered Architecture
        
            Controller
                ↓
            Service
                ↓
            Repository
                ↓
            Database

   8.2 Package Structure
       com.carrental.carrental
       │
       ├── config
       │
       ├── controller
       │   ├── AuthController
       │   ├── CarController
       │   ├── CategoryController
       │   ├── LocationController
       │   ├── ReservationController
       │   ├── PaymentController
       │   └── ReviewController
       │
       ├── controller.admin
       │   ├── AdminUserController
       │   ├── AdminCarController
       │   ├── AdminCategoryController
       │   ├── AdminLocationController
       │   ├── AdminReservationController
       │   └── AdminReviewController
       │
       ├── service
       │   ├── AuthService
       │   ├── CarService
       │   ├── CategoryService
       │   ├── LocationService
       │   ├── ReservationService
       │   ├── PaymentService
       │   └── ReviewService
       │
       ├── repository
       │   ├── UserRepository
       │   ├── CarRepository
       │   ├── CarCategoryRepository
       │   ├── LocationRepository
       │   ├── ReservationRepository
       │   ├── PaymentRepository
       │   └── ReviewRepository
       │
       ├── entity
       │   ├── User
       │   ├── Car
       │   ├── CarCategory
       │   ├── Location
       │   ├── Reservation
       │   ├── Payment
       │   └── Review
       │
       ├── dto
       │   ├── auth
       │   ├── car
       │   ├── category
       │   ├── location
       │   ├── reservation
       │   ├── payment
       │   └── review
       │
       ├── security
       │   ├── SecurityConfig
       │   ├── JwtService
       │   ├── JwtAuthenticationFilter
       │   └── CustomUserDetailsService
       │
       ├── exception
       │   ├── GlobalExceptionHandler
       │   ├── ResourceNotFoundException
       │   ├── BadRequestException
       │   └── ...
       │
       └── mapper
       ├── CarMapper
       ├── ReservationMapper
       └── ...

   8.3 Controller Layer
        Controller'ların görevi HTTP request'lerini karşılamak ve ilgili service metodunu çağırmaktır.
        
        Controller içerisinde business logic bulunmayacaktır.
        
        Örnek:
        
        POST /api/reservations
        ↓
        ReservationController
        ↓
        ReservationService
        
        Controller:
        
        Request'i alır.
        Request DTO'yu service'e gönderir.
        Service'den gelen sonucu Response DTO'ya dönüştürür.
        HTTP response döner.

   8.4 Service Layer
        Business logic Service katmanında uygulanacaktır.
        
        Örneğin rezervasyon oluşturulurken:
        
        ReservationService
        │
        ├── Car mevcut mu?
        ├── Car ACTIVE mi?
        ├── Tarihler geçerli mi?
        ├── Araç müsait mi?
        ├── Total price hesapla
        ├── Reservation oluştur
        └── PENDING olarak kaydet
        
        Business Constraints bölümünde tanımlanan kuralların uygulanmasından Service katmanı sorumludur.

   8.5 Repository Layer
        Repository katmanı database işlemlerinden sorumludur.
        
        Örneğin:
        
        CarRepository
        ├── findById()
        ├── findAll()
        ├── findByCategory()
        └── findAvailableCars()
        
        Repository database'den veriyi getirir veya kaydeder.

   8.6 DTO Structure
        API request ve response'ları Entity sınıflarından ayrı tutulacaktır.
        
        dto
        │
        ├── auth
        │   ├── RegisterRequest
        │   ├── RegisterResponse
        │   ├── LoginRequest
        │   └── LoginResponse
        │
        ├── car
        │   ├── CreateCarRequest
        │   ├── UpdateCarRequest
        │   ├── CarListResponse
        │   └── CarDetailsResponse
        │
        ├── category
        │   ├── CreateCategoryRequest
        │   └── CategoryResponse
        │
        ├── location
        │   ├── CreateLocationRequest
        │   └── LocationResponse
        │
        ├── reservation
        │   ├── CreateReservationRequest
        │   ├── ReservationResponse
        │   └── AdminReservationResponse
        │
        ├── payment
        │   ├── PaymentRequest
        │   └── PaymentResponse
        │
        └── review
        ├── CreateReviewRequest
        └── ReviewResponse

   8.7 Security Architecture
        Authentication ve authorization için Spring Security ve JWT kullanılacaktır.
        
        Login akışı:
        POST /api/auth/login
        ↓
        AuthController
        ↓
        AuthService
        ↓
        Kullanıcı doğrulama
        ↓
        JwtService
        ↓
        JWT oluştur
        ↓
        LoginResponse
        
        Authenticated request akışı:
        React
        │
        │ Authorization: Bearer <JWT>
        ▼
        JwtAuthenticationFilter
        │
        ├── JWT geçerli mi?
        ├── Kullanıcı kim?
        └── Kullanıcının rolü ne?
        │
        ▼
        Controller
        │
        ▼
        Service
        
        Admin endpoint'leri yalnızca ADMIN rolüne sahip kullanıcılar tarafından erişilebilir.
        
        Örneğin:
        
        GET  /api/cars
        → CUSTOMER / ADMIN
        
        POST /api/admin/cars
        → ADMIN

   8.8 Exception Handling
        Uygulamadaki exception'lar merkezi olarak yönetilecektir.
        
        exception
        │
        ├── GlobalExceptionHandler
        ├── ResourceNotFoundException
        ├── BadRequestException
        └── ...
        
        Örneğin bir araba bulunamadığında:
        
        CarService
        ↓
        ResourceNotFoundException
        ↓
        GlobalExceptionHandler
        ↓
        404 NOT FOUND
        
        Standart hata response'u:
        
        {
        "status": 404,
        "message": "Car not found",
        "timestamp": "2026-08-29T15:30:00"
        }

   8.9 Request Flow
        Genel backend request akışı:
        
        React Frontend
        │
        │ HTTP Request
        ▼
        Security Filter
        │
        ▼
        Controller
        │
        ▼
        Service
        │
        ▼
        Repository
        │
        ▼
        MySQL
        │
        ▼
        Repository
        │
        ▼
        Service
        │
        ▼
        Controller
        │
        ▼
        Response DTO
        │
        ▼
        React Frontend

9. # Entity Design

   User
   ├── id (long, pk, auto increment)
   ├── firstName (string, notnull)
   ├── lastName (string, notnull)
   ├── email (string, notnull, unique)
   ├── password (string, notnull)
   ├── role (enum, notnull)
   └── createdAt (timestamp, notnull)
   User 1 ───── 0..N Reservation
   User 1 ───── 0..N Review
    
    Car
    ├── id (long, pk, auto increment)
    ├── brand (string, notnull)
    ├── model (string, notnull)
    ├── year (int, notnull)
    ├── fuelType (enum, notnull)  (GASOLINE, DIESEL, ELECTRIC, HYBRID)
    ├── transmission (enum, notnull)   (MANUAL, AUTOMATIC)
    ├── mileage (int, notnull)
    ├── seatCount (int, notnull)
    ├── dailyPrice (decimal, notnull)
    ├── status (enum, notnull)  (ACTIVE, MAINTENANCE, INACTIVE)
    ├── categoryId (long, fk, notnull)
    ├── createdAt (timestamps, notnull)
    Car N ───── 1 CarCategory
    Car 1 ───── 0..N Reservation
    Car 1 ───── 0..N Review

   CarCategory
   ├── id (long, pk, auto increment)
   ├── name (string, notnull, unique)
   └── createdAt (timestamp, notnull)
    CarCategory 1 ───── 0..N Car

   Location
   ├── id (long, pk, auto increment)
   ├── name (string, notnull)
   ├── address (string, notnull)
   └── createdAt (timestamp, notnull)
    Location 1 ───── 0..N Reservation (pickup)
    Location 1 ───── 0..N Reservation (dropoff)

   Reservation
   ├── id (long, pk, auto increment)
   ├── startDate (timestamp, notnull)
   ├── endDate (timestamp, notnull)
   ├── totalPrice (decimal, notnull)
   ├── status (enum, notnull)
   ├── userId (long, fk, notnull)
   ├── carId (long, fk, notnull)
   ├── pickupLocationId (long, fk, notnull)
   ├── dropoffLocationId (long, fk, notnull)
   └── createdAt (timestamp, notnull)
    Reservation N ───── 1 User
    Reservation N ───── 1 Car
    Reservation N ───── 1 Location (pickup)
    Reservation N ───── 1 Location (dropoff)
    Reservation 1 ───── 0..N Payment
    Reservation 1 ───── 0..1 Review

   Payment
   ├── id (long, pk, auto increment)
   ├── amount (decimal, notnull)
   ├── status (enum, notnull)  (PENDING, SUCCESS, FAILED)
   ├── createdAt (timestamp, notnull)
   └── reservation_id (long, fk, notnull)
   Payment N ───── 1 Reservation

    Review
    ├── id (long, pk, auto increment)
    ├── rating (int, notnull)
    ├── comment (string, notnull)
    ├── userId (long, fk, notnull)
    ├── carId (long, fk, notnull)
    ├── reservationId (long, fk, notnull)
    └── createdAt (timestamp, notnull)
    Review N ───── 1 User
    Review N ───── 1 Car
    Review 1 ───── 1 Reservation




