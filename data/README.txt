Dentro de la carpeta "metadata" funcionara como nuestro almacén de datos fisicos de nuestra BD
Contenido:
README.txt : Archivo de texto que explica el propósito de esta carpeta
*** El resto serán sub carpetas con el nombre de la base de datos que se creo, dentro contendran todas la      estructura de las tablas (attributos)

***Los datos de cada base se almacenaran dentro de una carpeta distinta que se llamara igual que la base,
      cada vez que se usa la sentencia USE DATABASE o alguna que modifique la base (DELETE, INSERT o 
      UPDATE) el servidor extraera el metadatos de la DB de esta carpeta e ira a la carpeta  correspondiente por 
      el resto de información de las tablas.
