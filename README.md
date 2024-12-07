# Запуск приложения с использованием Docker и Docker Compose

## Шаги для запуска

1. Установите Docker и Docker Compose: Убедитесь, что на вашем компьютере установлены Docker и Docker Compose.

2. Создайте папку для проекта: Создайте новую папку и перейдите в нее.

3. Откройте терминал: В папке по пустому полю ПКМ выберите "Открыть в терминале".

4. Скачайте образ приложения: Введите следующую команду в терминале:

   
   docker pull gafrus06597/estore:v3
   

5. Создайте файл docker-compose.yml: В папке создайте новый файл с именем docker-compose.yml.

6. Скопируйте следующий код в docker-compose.yml:

   
   version: '3.8'

   services:
     app:
       image: 'gafrus06597/estore:v3'
       ports:
         - "8081:8081"  
       environment:
         DB_URL: jdbc:postgresql://db:5432/postgres
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
   

7. Сохраните и закройте файл.

8. Запустите приложение: Введите в терминале следующую команду:

   
   docker compose up
   

9. Откройте приложение в браузере: Перейдите по следующей ссылке:

   
   http://localhost:8081/
   

10. Готово: Ваше приложение успешно запущено и доступно по указанному адресу.
