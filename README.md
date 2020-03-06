# SATAPP
Aplicación destinada a facilitar la gestión de los equipos informáticos del colegio y las incidencias que tengan.

## 1. LOGIN Y REGISTRO
--------------------
Por razones técnicas no nos ha dado tiempo subir tanto el login como el registro a master. No obstante toda la información referente a estas dos opciones se podrá encontrar en la rama "loginAndRegister".

La última actualización de esta rama se encuentra en este [commit](https://github.com/LucasAmado/SATAPP-repository/commit/252ea994a65b0e419bcd1151359ae9aa3c09bb3f).

### 1.1 Registro
Para poder registrarse en la aplicación se deberá incluir su nombre, correo y contraseña. Una vez registrado se redireccionará automáticamente al login.

### 1.2 Login
A la hora de iniciar sesión se debe incluir el correo y la contraseña asociada. Una vez logeado se accederá a la gestión de dispositivos.
Para tener una mejor experiencia probando la plicación se recomienda hacer uso de los 3 tipos de roles en SATApp: administrador, técnico y usuario. Para probar la app, existen los siguientes usuarios:

```
Administrador:
admin@administrador.com
12345678

Usuario:
luismi.lopez@salesianos.edu
12345678

Técnico:
amadolucas99@gmail.com
123456
```


## 2. GESTIÓN DE USUARIOS
-------
Esta opción tan solo se podrá ver si se tiene rol de administrador.
Podrá ver una lista de usuarios, que se encuentra dividida por aquellos que están validados y los que están pendientes de validación.

### 2.1 DETALLE DEL USUARIO
El administrador al seleccionar un usuario podrá ver sus datos, así como podrá modificar el rol de los usuarios a técnicos.
Desde dentro de este detalle también se podrá validar al usuario,borrarlo o editarlo.

### 2.2 PERFIL DEL USUARIO
Esta opción a diferencia de la anterior podrá ver sus datos, así como modificarlos.

## 3. GESTIÓN DEL INVENTARIO
------------------
La página principal de la aplicación nos mostrará una lista con todos los dispositivos, que pueden ser filtrados por su ubicación.
Además existe la posibilidad de buscar un dispositivo en concreto por su nombre, gracias a la barra de búsqueda de la parte superior.

### 3.1 CREAR INVENTARIO
En la parte inferior de la lista de dispositivos se encontrará la opción de crear un nuevo dispositivo, rellenando los campos del formulario con los datos.
Una vez creado con éxito se volverá a la lista de dispositivos.

### 3.2 DETALLE INVENTARIO
Al seleccionar un dispositivo de la lista podremos acceder a sus datos. Desde este detalle tendremos la opción de modificar el dispositivo o borrarlo.

### 3.4 CÓDIGO QR
Al seleccionar el icono del código Qr situado en el menú superior podremos escanear el código código qr de cualquier dispositivo y acceder a su detalle.


## 4. GESTIÓN DE TICKETS
-------
Esta opción cambiará en función del rol del usuario logeado.
Los administradores podrán ver todos los tickets mientras que los técnicos podrán ver aquellos tickets que hayan creado o le hayan sido asignados. Por otra parte los usuarios únicamente podrán ver los que hayan creado.

La lista de tickets podrá ser filtrada por los administradores y técnicos en función de su estado.

### 4.1 CREAR TICKET
Desde la lista de tickets al darle al botón de crear se desplegará un formulario con los campos a rellenar para crear el nuevo ticket.

### 4.2 DETALLE TICKET
Al seleccionar un ticket de la lista se podrá acceder a un detalle con sus datos. Dentro de este detalle se podrá editar o borrar el ticket.
En caso de que ese ticket tenga anotaciones hechas se encontrará al final del detalle.


## 5. GESTIÓN DE ANOTACIONES
-------
Dentro de un ticket concreto al final del detalle del vimos encontraremos el apartado de anotaciones en caso de que las tuviera.


### 5.1 CREAR ANOTACIÓN
Dentro del detalle del ticket existe la posibilidad de crear una nueva anotación.

### 5.2 EDITAR ANOTACIÓN
Al seleccionar una anotación en concreto saldrá un formulario con el cuerpo de la anotación, la cual podremos modificar.



__AUTORES:__
- [Esperanza Macarena Escacena](https://github.com/EsperanzaMacarena)
- [José Luís Díez Cortés](https://github.com/joseluis10cortes)
- [Gonzalo Punta Pérez](https://github.com/gonzalopunta)
- [Lucas Amado Santos](https://github.com/LucasAmado)

