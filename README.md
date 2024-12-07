Как запустить?
1. Установить Docker/Docker Compose
2. Создать папку и перейдите в нее
3. В папке по пустому полю ПКМ -> Открыть в терминале
4. Ввести docker pull gafrus06597/estore:v2
5. Создать в папке файл docker-compose.yml
6. Скопировуйте туда 
services:
  app:
    image: 'd84a0c97f833'
    ports:
      - "8081:8081"  
    environment:
      DB_URL: jdbc:postgresql://localhost:5432/postgres
      DB_USERNAME: postgres
      DB_PASSWORD: root
    depends_on:
      - db

  db:
    image: postgres:17  
    container_name: db  
    environment:
      POSTGRES_DB: postgres  
      POSTGRES_USER: postgres 
      POSTGRES_PASSWORD: root 
    ports:
      - "5432:5432"
7.Сохраните и закройте файл
8.Введите в терминале docker compose up
