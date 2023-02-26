# Дипломный проект по профессии «Тестировщик»


## О проекте
В рамках данного проекта необходимо автоматизировать тестирование комплексного сервиса покупки тура, взаимодействующего с СУБД и API Банка.

База данных хранит информацию о заказах, платежах, статусах карт, способах оплаты.

Покупка тура возможна с помощью карты и в кредит. Данные по картам обрабатываются отдельными сервисами. 

Полные условия и исходные данные описанного кейса можно посмотреть [здесь](https://github.com/netology-code/qa-diploma)

## Документация по проекту

1. [План дипломного проекта](https://github.com/OlgaKusakina/qa-diploma/blob/main/Plan.md)
2. [Отчет по итогам тестирования] (https://github.com/OlgaKusakina/qa-diploma/blob/main/Report.md)

## Запуск приложения

* Для запуска приложения необходимо установать и запустить Docker Desktop. 

* склонировать репозиторий `https://github.com/OlgaKusakina/qa-diploma.git`

* Открыть проект в IntelliJ IDEA

* проверить есть ли образы MySql, PostgreSQL и Node.js командой docker image ls

* при необходимости (отсутствии вышеперечисленных образов), скачать нужный командой docker image pull <имя образа>


* запустить необходимые базы данных (MySQL и PostgreSQL), а также NodeJS. Параметры для запуска хранятся в файле `docker-compose.yml`. Для запуска необходимо ввести в терминале команду:
```
docker-compose up
```

* работать необходимо по адресу: http://localhost:8080/


* для запуска в терминале IntelliJ IDEA:

    - с использованием БД MySQL командой `java -jar aqa-shop.jar "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app"`

    - с использованием БД PostgreSQL командой `java -jar aqa-shop.jar "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app"`


* запустить автотесты командой:

    - для конфигурации БД MySql:
      `./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"`
    - для конфигурации БД PostgreSQL:
      `./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"`

## Перезапуск приложения и тестов
Если необходимо перезапустить приложение и/или тесты (например, для другой БД), необходимо выполнить остановку работы в запущенных ранее вкладках терминала, нажав в них Ctrl+С

## Запуск отчетов Allure

* Для запуска отчетов в терминале IntelliJ IDEA:

- ./gradlew allureReport (первоначальная команда)

- ./gradlew allureServe (запуск и открытие отчетов)

## Завершение работы

* остановить SUT комбдинацией клавиш CTRL+C

* остановить контейнеры командой docker-compose stop, удалить контейнеры командой docker-compose down
