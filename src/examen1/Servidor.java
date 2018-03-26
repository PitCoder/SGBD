package examen1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {
  public static void main(String[] args) {
    HashMap <String, HashMap> bases = new HashMap<>();
    String baseActual = null;
    String dbName;
    
    try {
      /* Envio los datos */
      ServerSocket s = new ServerSocket(9000);
      System.out.println(baseActual);
      System.out.println("Esperando clientes...");
      for (;;) {              
        Socket cl = s.accept();
        System.out.println("Cliente conectado desde: " + cl.getInetAddress());
        for (;;) {
          ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
          String query          = (String) ois.readObject();
          query                 = query.toUpperCase();
          
          /* Se checa que sentencia es la que se quiere */
          Sentencias se = new Sentencias();
          int a = se.getTipoSentencia(query);    
          String mensajeAEnviar = "";
          
          switch(a) {
            case 0:   /* Se crea la base de datos -> se pone en el hash map */
              if(se.verifySyntaxCreateDatabase(query)){
                  dbName = se.createDatabase(query);                         // Se obtiene el nombre de la base con la que se hará la operación. 
                  HashMap <String, LinkedList> base = new HashMap<>();                  // Se crea una instancia de HM, que contentra nombre de tabla y Lista de Objetos
                  bases.put(dbName, base);                                          // Se añade al HashMap bases los datos obtenidos anteriormente.
                  mensajeAEnviar = "Base de datos creada satisfactoriamente";       // Se envía un mensaje de que se creo de manera correcta.
              }
              else{
                  System.out.println("Error de Sintaxis: Create Database");
              }
              break; 
            
            case 1:  /* Se remueve la base de datos del hash map */
              dbName = se.dropDatabase(query);                                  // Se obtiene el nombre de la base con la que se hará la operación.
              if (bases.remove(dbName) == null) {                               // Si es nulo quiere decir que no hay esa llave; por ende no existe esa base.
                mensajeAEnviar = "No existe una base de datos con ese nombre";
              } else {                                                          // Se eliminó bien !.
                mensajeAEnviar = "Base eliminada satisfctoriamente!";
              }
              break;
            
            case 2:  /* Se pone en baseActual el nombre de la base */
              dbName = se.useDatabase(query);                                   // Se obtiene el nombre de la base con la que se hará la operación.
              if (bases.containsKey(dbName)) {                                  // Se verifica que esté la base en el HashMap.
                baseActual = dbName;                                            // Se pone el nombre actual de la base.
                mensajeAEnviar = "Base de Datos seleccionada";                  // Se prepara mensaje de éxito.
              } else {
                baseActual = null;                                              // Se regresa a nulo si es que se equivocó el usuario.
                mensajeAEnviar = "No existe base de datos";                     // Se prepara mensaje de error.
              }
              break;
            
            case 3: /* Se crea la tabla */
              if (baseActual == null) {                                         // Se verifica que ya se haya realizado 'USE DATABASE' con anterioridad.
                mensajeAEnviar = "No se ha seleccionado base de datos";
              } else {
                if (se.verifySyntaxCreateTable(query)) {                        // Se verifica la sintáxis.
                  String tableName = se.getTableName(query);                    // Se tiene el nombre de la tabla
                  boolean f = se.getTableAttributes(query, tableName);          // Se obtienen atributos y se genera objeto dinámicamente.
                 
                  if (!f) {                                                     // Nombres duplicados.
                    mensajeAEnviar = "Error: Nombres duplicados en atributos.";
                  } else {                  
                    /* Se obtiene el hashmap con las tablas ya creadas */
                    HashMap tablasActuales = bases.get(baseActual);
                      
                    if (tablasActuales.containsKey(tableName)) {                // Ya está esa tabla
                      mensajeAEnviar = "Error: Ya existe una tabla con el mismo nombre.";
                    } else {
                      /* Se añade la tabla al hashmap (En este paso ya debería estar la instancia de la clase */                      
                      DynamicCompiler dc  = new DynamicCompiler();
                      Object tabla        = dc.getInstance(tableName);                      
                      LinkedList<Object> linkedList = new LinkedList<>();                      
                      
                      tablasActuales.put(tableName, linkedList);                     // Se pone 
                      bases.put(baseActual, tablasActuales);
                      
                      System.out.println("TA: " + tablasActuales);
                      mensajeAEnviar = "Se creo la tabla!";
                    }                    
                  }
                } else {
                  mensajeAEnviar = "Error de sintaxis. Verificar";
                }
              }
              break;
              
            case 4: /* Se elimina tabla */
              if (baseActual == null) {
                mensajeAEnviar = "No se ha seleccionado base de datos";
              } else {
                String tableName = se.getTableNameDrop(query);                  // Se obtiene nombre.
                HashMap tablasActuales = bases.get(baseActual);                 // Se obtienen las tablas actuales
                
                if(tablasActuales.remove(tableName) == null) {                  // No existe tabla con ese nombre.
                  mensajeAEnviar = "No existe tabla con ese nombre.";
                } else {
                  mensajeAEnviar = "Tabla eliminada con éxito!";
                }
                
                System.out.println("TA: " + tablasActuales);
              }
              break;
            
            case 8: /* Se elimina base de datos */
              int res = se.showDatabases(query);                                // Se verifica sintaxis.
              if (res == 0) {                                                   // La sintaxis no es la correcta.
                mensajeAEnviar = "Sintaxis no correcta. Favor de verificar.";
              } else {
                mensajeAEnviar = printDatabases(bases.keySet());                // Sintaxis correcta -> se imprimen las bases actuales.
              }
              break;
              
            case 9:
              if (baseActual != null) {
                if (se.showTables(query)) {                                       // Sintaxis correcta
                  HashMap tablasActuales = bases.get(baseActual);               // Se obtienen las tablas actuales.
                  mensajeAEnviar = printTables(tablasActuales.keySet());        // Impresión de las tablas
                } else {
                  mensajeAEnviar = "Sintaxis no correcta. Favor de verificar.";
                }
              } else {
                mensajeAEnviar = "No se ha seleccionado base de datos";
              }
              break;
              
            default: /* Sentencia no reconocida */
              mensajeAEnviar = "Sentencia no reconocida.";
          }
          
          /* Se envia respuesta */
          ObjectOutputStream oos  = new ObjectOutputStream(cl.getOutputStream());
          oos.writeObject(mensajeAEnviar);          
        }

      }
    } catch (IOException ex) {  ex.printStackTrace();
    } catch (ClassNotFoundException ex) {      
    }           
  }
  
  private static String printDatabases(Set <String> dataBasesNames) {
    String text = ".:: Bases de Datos ::. \n";
    Iterator<String> iterator = dataBasesNames.iterator();
    
    while (iterator.hasNext()) {
      text += "- " + iterator.next() + "\n";
    }
    
    return text;
  }
  
  private static String printTables(Set <String> tableNames) {
    String text = ".:: Tablas de la Base ::. \n";
    Iterator<String> iterator = tableNames.iterator();
    
    while (iterator.hasNext()) {
      text += "- " + iterator.next() + "\n";
    }
    
    return text;    
  }
}
