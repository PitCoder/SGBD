package examen1;

import java.io.*;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Servidor implements java.io.Serializable{
 
  public static void serializeDataOut(LinkedList table, String filepath) throws IOException{
    FileOutputStream fos = new FileOutputStream(filepath);
      try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
          oos.writeObject(table);
      }
  }

  public static LinkedList serializeDataIn(String filepath) throws IOException, ClassNotFoundException{
   FileInputStream fin = new FileInputStream(filepath);
   LinkedList table;
      try (ObjectInputStream ois = new ObjectInputStream(fin)) {
          table = (LinkedList) ois.readObject();
      }
   return table;
 }
      
  public static LinkedHashMap<String, LinkedHashMap<String, LinkedList>> loadFromStorage(){
      /* Cargamos a la carpeta build todas las clases que se generaron de compilaciones anteriores (metadatos de las bases) */
      String mdpath = "metadata/";
      String target = "build/classes/examen1/";
      File mdloader = new File(mdpath);
      for(File database: mdloader.listFiles()){
        if(database.isDirectory()){
        //Contiene al menos una tabla
            if(database.list().length > 0){   
              for(File table: database.listFiles()){
                  try{
                      File ftarget = new File(target + table.getName());
                      Files.copy(table.toPath(), ftarget.toPath(), StandardCopyOption.REPLACE_EXISTING);
                  }
                  catch(Exception e){
                      e.printStackTrace();
                  }
              }
            }
         }
      }
     
      /* Después regresamos toda la información de la carpeta data */
      String dpath = "data/";
      LinkedHashMap<String, LinkedHashMap<String, LinkedList>> bases = new LinkedHashMap<String, LinkedHashMap<String, LinkedList>>();      
      File dloader = new File(dpath);
      
      for(File database: dloader.listFiles()){
         if(database.isDirectory()){
            if(database.list().length > 0){   
              LinkedHashMap<String, LinkedList> tables = new LinkedHashMap<String, LinkedList>(); 
              for(File table: database.listFiles()){
                  try{
                    tables.put(table.getName(), serializeDataIn(dpath + database.getName() + "/" + table.getName()));
                  }
                  catch(Exception e){
                      e.printStackTrace();
                  }
              }
              bases.put(database.getName(), tables);
           }
            else{
                LinkedHashMap<String, LinkedList> tables = new LinkedHashMap<String, LinkedList>();             // Se crea una instancia de HM, que contentra nombre de tabla y Lista de Objetos
                bases.put(database.getName(), tables); //En caso de que la base de datos no tenga tabla alguna
            }
         }
      }
      return bases;
  }
  
  public static void deleteFromStorage(String path){
      File loader = new File(path);
      if(loader.isDirectory()){
          if(loader.list().length == 0){
              loader.delete();
          }
          else{
              String files[] = loader.list();
              for(String temp : files){
                  deleteFromStorage(path + "/" + temp);         
              }     
              if(loader.list().length == 0){
                  loader.delete();
              }
          }
      }
      else{
          loader.delete();
      }
  } 

  public static void saveDB(LinkedHashMap<String, LinkedHashMap<String, LinkedList>> bases){
      String datapath = "data/";
      String metadatapath = "metadata/";
      File loader = new File(datapath);     
      Collection<String> oldbases = new ArrayList(Arrays.asList(loader.list()));
      Collection<String> newbases = new ArrayList<>(bases.keySet());
      
      List<String> destinationList = new ArrayList<>(newbases);
      destinationList.removeAll(oldbases);
      
      for(int i=0; i < destinationList.size(); i++){
          File dbase = new File(datapath + destinationList.get(i));
          File mdbase = new File(metadatapath + destinationList.get(i));
          try{
              dbase.mkdir();
              mdbase.mkdir();
          }
          catch(Exception e){
              e.printStackTrace();
          }     
      }
  }
  
  public static void saveTable(LinkedList table, String dbname, String tablename) throws IOException{
      /* Primero almacenamos el linked list (información que contiene la tabla) */
      String dpath = "data/" + dbname + "/" + tablename;
      serializeDataOut(table, dpath);
      
      String mdpath = "metadata/" + dbname + "/" + tablename + ".class";
      File target = new File(mdpath);
      Path source = Paths.get("build/classes/examen1/"+ tablename + ".class");
      try{
          Files.copy(source, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
      }
      catch(Exception e){
          e.printStackTrace();
      }
  }
  
  private static String printDatabases(Set <String> dataBasesNames) {
    ArrayList<String> headers = new ArrayList<String>();
    ArrayList<ArrayList<String>>content = new ArrayList<ArrayList<String>>();
    headers.add(".:: Bases de Datos ::.");
    
    Iterator<String> iterator = dataBasesNames.iterator();
    while (iterator.hasNext()) {
      ArrayList<String> row = new  ArrayList<String>();
      row.add(iterator.next());
      content.add(row);
    }
    ConsoleTable ct = new ConsoleTable(headers, content);
    return ct.printTable();
  }
  
  private static String printTables(Set <String> tableNames) {
    ArrayList<String> headers = new ArrayList<String>();
    ArrayList<ArrayList<String>>content = new ArrayList<ArrayList<String>>();
    headers.add(".:: Tablas de la Base ::.");
    
    Iterator<String> iterator = tableNames.iterator();
    while (iterator.hasNext()) {
      ArrayList<String> row = new  ArrayList<String>();
      row.add(iterator.next());
      content.add(row);
    }
    ConsoleTable ct = new ConsoleTable(headers, content);
    return ct.printTable();
  }
    
  public static void main(String[] args) {
   LinkedHashMap <String, LinkedHashMap<String, LinkedList>> bases;
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
        /* Invocamos al metodo estático para cargar las bases de datos desde nuestro metadatos */
        bases = loadFromStorage();
        for (;;) {
          ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
          String oldquery  = (String) ois.readObject();
          String query  = oldquery.toUpperCase();
          
          /* Se checa que sentencia es la que se quiere */
          Sentencias se = new Sentencias();
          int a = se.getTipoSentencia(query);    
          String mensajeAEnviar = "";

          switch(a) {
            case 0:   /* Se crea la base de datos -> se pone en el hash map */
              if(se.verifySyntaxCreateDatabase(query)){
                  dbName = se.createDatabase(query);                                            // Se obtiene el nombre de la base con la que se hará la operación.
                  if(!se.verifyReservedWord(dbName)){
                    if(bases.containsKey(dbName)){
                        mensajeAEnviar = "Error de Creación:  La base de datos ya existe";
                    }
                    else{
                      LinkedHashMap <String, LinkedList> base = new LinkedHashMap<String, LinkedList>();             // Se crea una instancia de HM, que contentra nombre de tabla y Lista de Objetos
                      bases.put(dbName, base);                                                             // Se añade al LinkedHashMap bases los datos obtenidos anteriormente.
                      saveDB(bases);
                      mensajeAEnviar = "Base de datos creada satisfactoriamente";       // Se envía un mensaje de que se creo de manera correcta.
                    }                     
                  }
                  else{
                      mensajeAEnviar = "Error de Creación: No se puede usar una palabra reservada";
                  }
              }
              else{
                  mensajeAEnviar = "Error de Sintaxis: Create Database";
              }
              break; 
            
            case 1:  /* Se remueve la base de datos del hash map */
              if(se.verifySyntaxDropDatabase(query)){
                dbName = se.dropDatabase(query);    // Se obtiene el nombre de la base con la que se hará la operación.
                if(!bases.containsKey(dbName)){
                     mensajeAEnviar = "No existe una base de datos con ese nombre";
                }
                else{
                     LinkedHashMap<String, LinkedList> tablasActuales = bases.get(dbName);
                     if(!tablasActuales.isEmpty()){
                         Iterator<String> iterator = tablasActuales.keySet().iterator();
                         while (iterator.hasNext()) {
                             deleteFromStorage("build/classes/examen1/" + iterator.next() + ".class"); //Se eliminan las clases de la sesion pasada, si es que las hay
                         }
                     }
                     bases.remove(dbName);
                     deleteFromStorage("data/" + dbName); //Función generica que permite eliminar desde una tabla hasta una base de datos
                     deleteFromStorage("metadata/" + dbName); 
                     mensajeAEnviar = "Base eliminada satisfctoriamente!";
                     if(dbName.equals(baseActual)){
                         baseActual = null;
                     }
                }
              }
              else{
                  mensajeAEnviar = "Error de Sintaxis: Drop Database";
              }
              break;
            
            case 2:  /* Se pone en baseActual el nombre de la base */
              if(se.verifySyntaxUseDatabase(query)){
                dbName = se.useDatabase(query);                                   // Se obtiene el nombre de la base con la que se hará la operación.
                if (bases.containsKey(dbName)) {                                  // Se verifica que esté la base en el LinkedHashMap.
                  baseActual = dbName;                                            // Se pone el nombre actual de la base.
                  mensajeAEnviar = "Base de Datos seleccionada";                  // Se prepara mensaje de éxito.
                } 
                else {
                  baseActual = null;                                              // Se regresa a nulo si es que se equivocó el usuario.
                  mensajeAEnviar = "No existe la base de datos";                     // Se prepara mensaje de error.
                }
              }
              else{
                  mensajeAEnviar = "Error de Sintaxis: Use Database";
              }
              break;
            
            case 3: /* Se crea la tabla */
              if (baseActual == null) {                                         // Se verifica que ya se haya realizado 'USE DATABASE' con anterioridad.
                mensajeAEnviar = "No se ha seleccionado base de datos";
              } 
              else {
                if (se.verifySyntaxCreateTable(query)){                        // Se verifica la sintáxis.
                  String tableName = se.getTableName(query);                    // Se tiene el nombre de la tabla
                  System.out.println(baseActual);
                  System.out.println(tableName);
                  if(!se.verifyReservedWord(tableName)){
                    // Se obtienen atributos y se genera objeto dinámicamente.
                    if (!se.getTableAttributes(query, tableName)){                                                     // Nombres duplicados.
                      mensajeAEnviar = "Error: Nombres duplicados en atributos.";
                    } 
                    else {                  
                      /* Se obtiene el hashmap con las tablas ya creadas */
                      LinkedHashMap<String, LinkedList> tablasActuales = bases.get(baseActual);
                      if (tablasActuales.containsKey(tableName)) {                // Ya está esa tabla
                        mensajeAEnviar = "Error: Ya existe una tabla con el mismo nombre.";
                      } 
                      else {
                        /* Se añade la tabla al hashmap (En este paso ya debería estar la instancia de la clase */                      
                        //DynamicCompiler dc  = new DynamicCompiler();
                        //Object tabla  = dc.getInstance(tableName);
                        LinkedList<Object> linkedList = new LinkedList<>();
                        tablasActuales.put(tableName, linkedList);
                        saveTable(tablasActuales.get(tableName), baseActual, tableName);
                        System.out.println("TA: " + tablasActuales);
                        mensajeAEnviar = "Se creo la tabla " + tableName;
                      }                    
                    }
                  }
                  else{
                      mensajeAEnviar = "Error de Creación: No se puede usar una palabra reservada";
                  }
                }   
                else {
                  mensajeAEnviar = "Error de sintaxis. Create Table";
                }
              }
              break;
              
            case 4: /* Se elimina tabla */
              if (baseActual == null) {
                mensajeAEnviar = "No se ha seleccionado base de datos";
              } 
              else{
                if(se.verifySyntaxDropTable(query)){
                    String tableName = se.getTableNameDrop(query);                  // Se obtiene nombre.
                    LinkedHashMap tablasActuales = bases.get(baseActual);        // Se obtienen las tablas actuales
                    if(tablasActuales.containsKey(tableName)){
                        tablasActuales.remove(tableName); //Eliminamos dentro del hashmap
                        deleteFromStorage("build/classes/examen1/" + tableName + ".class");              //Eliminamos de lbuild
                        deleteFromStorage("data/" + baseActual + "/" + tableName);                           //Eliminamos de data
                        deleteFromStorage("metadata/" + baseActual + "/" + tableName + ".class");   //Eliminamos de metadata                        
                        System.out.println("TA: " + tablasActuales);
                        mensajeAEnviar = "Tabla eliminada exitosamente";
                    }
                    else{
                        mensajeAEnviar = "No existe tabla con ese nombre.";
                    }
                }
                else{
                    mensajeAEnviar = "Error de sintaxis. Drop Table";
                }
             }
             break;
             
            case 5: /* Realizamos una consulta a la tabla */
                if(baseActual == null){
                    mensajeAEnviar = "No se ha seleccioando base de datos";
                }
                else{
                    if(se.verifySyntaxSelect(query)){
                        String tableName = se.getSTableName(query);
                        System.out.println(tableName);
                        LinkedHashMap<String, LinkedList> tablasActuales = bases.get(baseActual);
                        if(tablasActuales.containsKey(tableName)){
                        LinkedList<Object> tuples = tablasActuales.get(tableName);
                            if(tuples.size()>0){
                                ArrayList<String> parameters = se.extractQueryAttributes(query);
                                ArrayList<String> elements = se.getSelectElements(query, oldquery);
                                if(elements.size() > 0){
                                    ArrayList<Integer> matchedTuples = se.getMatchTuples(tuples, elements, "SELECT");
                                    if(matchedTuples == null){
                                            mensajeAEnviar = "Los valores a buscar no coinciden con los definidos en la tabla";
                                    }
                                    else{
                                        LinkedList<Object> newTuples = se.getTuples(tuples, matchedTuples, elements);
                                        if(parameters.get(0).equals("ALL")){
                                            mensajeAEnviar = se.invokeAllGetters(newTuples, tableName);
                                        }
                                        else{
                                            mensajeAEnviar = se.invokeGetters(newTuples, parameters, tableName);
                                        }
                                    }
                                }
                                else{
                                    if(parameters.get(0).equals("ALL")){
                                        mensajeAEnviar = se.invokeAllGetters(tuples, tableName);
                                    }
                                    else{
                                        mensajeAEnviar = se.invokeGetters(tuples, parameters, tableName);
                                    }
                                }
                            }
                            else{
                                mensajeAEnviar = "Para actualizar, deben de haber por lo menos 1 registro en la tablas";
                            }                       
                        }
                        else{
                                mensajeAEnviar = "No existe tabla con ese nombre.";
                        }
                    }
                    else{
                        mensajeAEnviar = "Error de sintaxis. Select";
                    }
                }
                break;
                
            case 6 : /*Se insertan valores a la tabla */
                if(baseActual == null){
                    mensajeAEnviar =  "No se ha seleccionado base de datos";
                }
                else{
                    if(se.verifySyntaxInsertInto(query)){
                        String tableName = se.getITableName(query);
                        System.out.println(tableName);
                        LinkedHashMap<String, LinkedList> tablasActuales = bases.get(baseActual);
                        if(tablasActuales.containsKey(tableName)){
                            Object tupla = se.verifyInsertedValues(oldquery, tableName);
                            if(tupla!=null){
                                LinkedList<Object> tuplas = tablasActuales.get(tableName);
                                tuplas.add(tupla);
                                saveTable(tablasActuales.get(tableName), baseActual, tableName);
                                mensajeAEnviar = "Los valores se ha insertado correctamente";
                            }
                            else{
                                mensajeAEnviar = "Error: Los valores no coinciden con los definidos en la tabla.";
                            }
                        }
                        else{
                            mensajeAEnviar = "No existe tabla con ese nombre";
                        }
                        
                    }
                    else{
                        mensajeAEnviar = "Error de Sintaxis: Insert Into";
                    }
                }
                break;
                
            case 7:
                if(baseActual == null){
                    mensajeAEnviar =  "No se ha seleccionado base de datos";
                }
                else{
                    if(se.verifySyntaxUpdate(query)){
                        String tableName = se.getUTableName(query);
                        System.out.println(tableName);
                        LinkedHashMap<String, LinkedList> tablasActuales = bases.get(baseActual);
                        if(tablasActuales.containsKey(tableName)){
                            LinkedList<Object> tuples = tablasActuales.get(tableName);
                            if(tuples.size() > 0){
                                ArrayList<String> elements = se.getSetElements(query, oldquery);
                                ArrayList<Integer> matchedTuples = se.getMatchTuples(tuples, elements, "SET");
                                if(matchedTuples == null){
                                    mensajeAEnviar = "Los valores a buscar no coinciden con los definidos en la tabla";
                                }
                                else{
                                    if(matchedTuples.size() > 0){
                                        LinkedList<Object> newTuples = se.updateTuples(tuples, matchedTuples, elements);
                                        if(newTuples!=null){
                                            //tablasActuales.replace(query, tuples, newTuples);
                                            tablasActuales.replace(tableName, tuples, newTuples);
                                            saveTable(tablasActuales.get(tableName), baseActual, tableName);
                                            mensajeAEnviar = "Los valores se han actualizado exitosamente";
                                        }
                                        else{
                                            mensajeAEnviar = "No se pudieron modificar los valores";
                                        }
                                    }
                                    else{
                                        mensajeAEnviar = "No se encontraron registros que cumplan el criterio";
                                    }
                                }
                            }
                            else{
                                mensajeAEnviar = "Para actualizar, deben de haber por lo menos 1 registro en la tablas";
                            }
                        }
                        else{
                            mensajeAEnviar = "No existe tabla con ese nombre";
                        }
                    }
                    else{
                        mensajeAEnviar = "Error de Sintaxis: Update";
                    }
                }
                break;
                
            case 8: /* Se elimina base de datos */
                if(se.verifySyntaxShowDatabases(query)){
                    mensajeAEnviar = printDatabases(bases.keySet());    // Sintaxis correcta -> se imprimen las bases actuales.
                }
                else{
                    mensajeAEnviar = "Error de Sintaxis: Show Databases";
                }
              break;
              
            case 9:
              if (baseActual != null) {
                if(se.verifySyntaxShowTables(query)){
                    LinkedHashMap<String, LinkedList> tablasActuales = bases.get(baseActual);
                    if(tablasActuales.isEmpty()){
                        mensajeAEnviar = "No hay tablas almacenadas";    
                    }
                    else{
                        mensajeAEnviar = printTables(tablasActuales.keySet());        // Impresión de las tablas
                    }
                }
                else{
                     mensajeAEnviar = "Error de Sintaxis: Show Tables";
                }
              } 
              else {
                mensajeAEnviar = "No se ha seleccionado base de datos";
              }
              break;
           
            case 10:
                if(baseActual != null){
                    if(se.verifySyntaxDelete(query)){   
                        String tableName = se.getDTableName(query);
                        System.out.println(tableName);
                        LinkedHashMap<String, LinkedList> tablasActuales = bases.get(baseActual);
                        if(tablasActuales.containsKey(tableName)){
                         LinkedList<Object> tuples = tablasActuales.get(tableName);
                             if(tuples.size()>0){
                                 //ArrayList<String> parameters = se.extractQueryAttributes(query);
                                 ArrayList<String> elements = se.getDeleteElements(query, oldquery);
                                 if(elements.size() > 0){
                                     ArrayList<Integer> matchedTuples = se.getMatchTuples(tuples, elements, "SELECT");
                                     ArrayList<Integer> notmatchedTuples = new ArrayList<Integer>();
                                     
                                     for(int i=0;i<tuples.size();i++){
                                         if(!matchedTuples.contains(i))
                                             notmatchedTuples.add(i);
                                     }
                                     
                                     if(notmatchedTuples == null){
                                             mensajeAEnviar = "Los valores a eliminar no coinciden con los definidos en la tabla";
                                     }
                                     else{
                                        LinkedList<Object> newTuples = se.getTuples(tuples, notmatchedTuples, elements);
                                        tablasActuales.replace(tableName, tuples, newTuples);
                                        saveTable(tablasActuales.get(tableName), baseActual, tableName);
                                        mensajeAEnviar = "Los valores se han eliminado exitosamente";
                                     }
                                 }
                                 else{
                                        tablasActuales.replace(tableName, tuples, new LinkedList<Object>());
                                        saveTable(tablasActuales.get(tableName), baseActual, tableName);
                                        mensajeAEnviar = "Los valores se han eliminado exitosamente";
                                 }
                             }
                             else{
                                 mensajeAEnviar = "Para eliminar, deben de haber por lo menos 1 registro en la tablas";
                             }                       
                        }
                    }
                    else{
                        mensajeAEnviar = "Error de Sintaxis: Delete";
                    }
                
                }
                else{
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
    } 
    catch (IOException ex){  
        ex.printStackTrace();
    } 
    catch (ClassNotFoundException ex) {      
    }           
  }
}