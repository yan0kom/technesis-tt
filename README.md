# Тестовое задание
## Компания Технезис
### Вакансия Java-разработчик

#### Регулятор
Сборка: `mvn clean install -pl regulator`
#### Сервер
Сборка: `mvn clean package -pl server`  
Запуск: `java -jar server/target/server.jar`
#### Клиент
Сборка: `mvn clean package javafx:jlink -pl client`  
Запуск: `client/target/client/bin/client`
