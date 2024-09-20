FROM tomcat:10.0-jdk17
# Путь к папке с вашим приложением

LABEL authors="Monoramen"

# Настройка переменных окружения для отладки
ENV JPDA_ADDRESS="*:5005"
ENV JPDA_TRANSPORT="dt_socket"

# Запуск Tomcat в режиме отладки
CMD ["catalina.sh", "jpda", "run"]
