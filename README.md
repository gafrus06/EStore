## Замечание
У меня есть небольшое замечание. Возможно, я не до конца понял задание и допустил ошибку. При проверке может возникнуть вопрос, почему при добавлении данных из CSV в таблицу Purchase создается лишь одна запись. Согласно условию "Если товара нет в наличии, запись о покупке не должна формироваться, и на экран должна выводиться соответствующая информация во всплывающей подсказке", таблица ElectroShop содержит данные о наличии и количестве товаров в конкретном магазине. Если я правильно понял, в Purchase будет добавлена только одна запись, так как, например, в магазине с ID 1 есть только товары с ID 1 и ID 20. В таблице Purchase видно, что только товар с ID 1 доступен в магазине ID 1, а товар с ID 20 имеет shopId 2, что не проходит проверку и не добавляется в Purchase. Таким образом, из всех записей проходит лишь первая.
Кроме того, я не совсем понял, как использовать таблицу ElectroEmployee. Я думал, что она хранит информацию о том, какой сотрудник продал какой тип товара, но, похоже, это не так. Дополнительное задание "Вывод лучшего младшего-продавца консультанта" я выполнил, используя таблицы Purchase, ElectroItem и Employee.


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
