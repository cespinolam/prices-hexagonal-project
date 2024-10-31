# Pricing Service - Arquitectura Hexagonal en Spring Boot

Este proyecto implementa un servicio RESTful para gestionar precios, siguiendo una arquitectura hexagonal en Java 17 y Spring Boot.

## Descripción

La aplicación permite consultar el precio aplicable de un producto para una cadena (marca) en una fecha y hora determinada, considerando la prioridad de tarifas. Implementa una arquitectura hexagonal que organiza el código en tres capas principales: application, infrastructure y domain.

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3**
- **H2 (Base de Datos en Memoria)**
- **JUnit y Mockito** para pruebas unitarias e integración

## Ejecución del Proyecto

- Clona el repositorio:

   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd prices-hexagonal-project
