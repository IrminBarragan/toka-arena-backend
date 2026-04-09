# Toka Arena Backend
Este es el motor de nuestro proyecto Toka Arena o más bien proyecto de mi compañero Emmanuel(El tuvo la idea de este proyecto)
yo solo estoy ayudando con el backend.

## ¿Por qué un Monolito Modular?
* Una sola base de datos y un solo despliegue para no perder tiempo en configuraciones de red infinitas.
* `auth`, `battle`, `shop`, `payment` están separados. Si algo falla en las batallas, no tiene por qué romper la tienda.
* Futuros Microservicios: Si el día de mañana `battle` o `payment` necesitan su propio servidor, la extracción es casi "copy-paste" porque las reglas de negocio no están mezcladas.
* Paz mental: El equipo solo cuenta con dos programadores Emma en el frontend yo en el backend.



## Módulos del backend
* `auth`: Integración con la Super App TOKA y generación de JWT internos.
* `security`: Filtros de seguridad stateless.
* `user`: El corazón del jugador y su inventario.
* `tokagotchi`: Vida, obra y milagros de las criaturas.
* `battle`: Matchmaking y ataques.
* `inventory`: Gestión de accesorios, consumibles y paquetes de moneda TF.
* `shop`: El centro comercial donde se gastan los TF.
* `missions`: El sistema de progreso y recompensas diarias.
* `payment`: La pasarela real y el log de transacciones (Que no logramos implementar porque la API no respondi a las peticiones de REACT y eso que le rogamos para que respondiera).
* `config`: Donde vive la infraestructura y la carga de datos inicial.

## Stack Tecnológico
* **Java 21** 
* **Spring Boot 4.0.5**.
* **Spring Security + JWT**.
* **Spring Data JPA + MySQL**.
* **Maven**.

## Por si quieren probar el backend con Postman o alguna otra herramienta
1.  **Requisitos:** JDK 21 y MySQL con una DB llamada `toka`.
2.  **Levantar:** `./mvnw spring-boot:run`

> **Ojo:** Las variables de entorno críticas como `toka.jwt.secret` y las APIs de `TOKA` 
> están en el `application.properties`. Para producción, por favor, muévanlas a variables de entorno reales.

## Flujo de pagos TF

Para que no haya dudas de cómo funciona el dinero virtual:
1. Frontend pide paquetes (`GET /api/v1/shop/packages`).
2. Frontend inicia el pago (`POST /api/v1/payments/create`).
3. El usuario paga en la URL que devuelve el sistema.
4. Frontend confirma con el backend (`POST /api/v1/payments/inquiry`).
5. El sistema acredita los TF automáticamente si el estado es `SUCCESS`.

Este apartado no esta implementado porque la API de pagos no respondía a las peticiones de REACT, 
pero la lógica está implementada en el backend, solo falta que el frontend la pinte.
---
Este backend es el resultado de **6 horas de sueño en total** desde que empezó el hackaton. Si encuentran algún nombre de 
variable en spanglish, un comentario existencialista en el código o alguna ruta que parece escrita por un zombie, 
ya sabes por qué es. Mi cerebro ahora mismo funciona al 2% de capacidad.
---