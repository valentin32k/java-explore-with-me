# Репозиторий для проекта Java-Explore-With-Me (Афиша)

### Функциональность ###
Приложение для обмена информацией об интересных событиях и нахождения компании для участия в них.

**Жизненный цикл события включает в себя следующие этапы:**  
***Создание.***  
***Ожидание публикации.*** В статус ожидания публикации событие переходит сразу после создания.  
***Публикация.*** В это состояние событие переводит администратор.  
***Отмена публикации.*** В это состояние событие переходит если администратор решил, что его нельзя публиковать, или если инициатор события решил отменить его на этапе ожидания публикации.

**Приложение состоит из двух сервисов:**  
***main-service*** - основной сервис, в котором содержится все необходимое для работы приложения;  
***stats-service*** - сервис статистики, который собирает информацию о количестве обращений пользователей к спискам событий и о количестве запросов к подробной информации о событии.

**API основного сервиса разделено на три части:**  
***публичная*** - доступна без регистрации любому пользователю сети;  
***закрытая*** - доступна только авторизованным пользователям;  
***административная*** - для администраторов сервиса.

### Стек технологий ### 
Java 11, Spring Boot, Spring Data, Hibernate, Docker, Maven, PostgreSQL и H2 (для тестирования). 

### Системные требования ###
Для запуска и работы приложения потребуется платформа контейнеризации Docker (https://www.docker.com/products/docker-desktop/)

### Инструкция по развёртыванию ### 
**Клонировать репозиторий.** *git clone https://github.com/valentin32k/java-explore-with-me.git*.  
**Соберать проект.** mvn clean package  
**Запустить контейнеры.** docker-compose up.  
Основной сервис будет доступен по адресу: http://localhost:8080, сервис статистики - http://localhost:9090.