Dentro de la carpeta "metadata" funcionara como nuestro almacén de datos fisicos de nuestra BD
Contenido:
README.txt : Archivo de texto que explica el propósito de esta carpeta
*** El resto serán sub carpetas con el nombre de la base de datos que se creo, dentro contendran todas la    estructura de las tablas (attributos)

***Los datos de cada tabla se almacenaran dentro de una carpeta distinta
      llamada data, cada vez que se usa la sentencia USE DATABASE, el servidor extraera el metadatos de la DB
      de esta carpeta e ira a la carpeta data por el resto de información de las tablas, esto con la finalidad de           poder establecer cierta seguridad en la integridad de nuestros datos.
