# Acceso a PostgreSQL desde Kotlin haciendo uso del conector JDBC para Postgresql (CRUD BASICO)

Descubre cómo conectar de manera sencilla tu aplicación Android a una base de datos PostgreSql mediante el uso del connector JDBC. En éste tutorial detallado, te guiaré paso a paso para que puedas implementar un CRUD completo, desde la creación hasta la eliminación de datos. ¡Aprende a integrar tu app con una base de datos de forma sencilla y efectiva!

## Requisitos

- Android Studio Jellyfish | 2023.3.1 Patch 1 o superior.
- Android Gradle Plugin Version 8.4.1
- Gradle Version 8.6
- Kotlin 1.9.22 o superior.

## Base de datos DemoDB en PostgreSql

```sql
CREATE DATABASE DemoDB;

CREATE TABLE Producto (
    id SERIAL PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    codigobarra VARCHAR(50) UNIQUE NOT NULL,
    precio DECIMAL(10, 2) NOT NULL
);
```

## Pantallazos de la app

![Image text](https://github.com/programadorescs/PostgresqlBasicoYT/blob/master/app/src/main/assets/PostgresqlBasicoYT_001.png)
![Image text](https://github.com/programadorescs/PostgresqlBasicoYT/blob/master/app/src/main/assets/PostgresqlBasicoYT_002.png)

## Video en YouTube
[![Alt text](https://img.youtube.com/vi/5IMrEkIg1J0/0.jpg)](https://www.youtube.com/watch?v=5IMrEkIg1J0)