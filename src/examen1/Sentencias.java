package examen1;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sentencias {
  private String query;
  private final ArrayList<String> validPrefixes;
  
  public Sentencias() {
    validPrefixes = new ArrayList<>();
    validPrefixes.add("CREATE DATABASE");   // 0
    validPrefixes.add("DROP DATABASE");     // 1
    validPrefixes.add("USE");               // 2
    validPrefixes.add("CREATE TABLE");      // 3    /* CREATE TABLE tabla1 (nombre varchar(50), edad int); */
    validPrefixes.add("DROP TABLE");        // 4
    validPrefixes.add("SELECT");            // 5
    validPrefixes.add("INSERT INTO");       // 6
    validPrefixes.add("UPDATE");            // 7
    validPrefixes.add("SHOW DATABASES");    // 8
    validPrefixes.add("SHOW TABLES");    // 8
  }
  
  
  public int getTipoSentencia(String query) {
    int tipo = -1;
    //this.query = query.toUpperCase();      Esta Linea es inecesaria, ya que se ha convertido a mayúsculas previamente
    for (int i = 0; i < validPrefixes.size(); i++) {
      if (startsWith(query.trim().replaceFirst("[\\s]+"," "), validPrefixes.get(i))) {
        return i;
      }
    }
    return tipo;
  }
  
  private boolean startsWith(String query, String prefix) {
      System.out.println(query);
    return query.startsWith(prefix);
  }
  
  /* Verificación de sintaxis para todas las sentencias SQL que se implementaran */
  public boolean verifySyntaxCreateDatabase(String query){
      String regex = "[\\s]*(CREATE)[\\s]+(DATABASE)[\\s]+[_0-9A-Z$]+[_A-Z$][\\s]*[;][\\s]*";   
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(query);
      return matcher.matches();
  }
  
  public boolean verifySyntaxDropDatabase(String query){
      String regex = "[\\s]*(DROP)[\\s]+(DATABASE)[\\s]+[_0-9A-Z$]+[_A-Z$][\\s]*[;][\\s]*";   
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(query);
      return matcher.matches();
  }
  
  public boolean verifySyntaxUseDatabase(String query){
      String regex = "[\\s]*(USE)[\\s]+[_0-9A-Z$]+[_A-Z$][\\s]*[;][\\s]*";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(query);
      return matcher.matches();
  }
  
  public boolean verifySyntaxCreateTable(String query) { 
      String regex = "\\s*(CREATE)\\s+(TABLE)\\s+([0-9_A-Z$]+[_A-Z$])\\s*[(](\\s*[0-9_A-Z$]+[_A-Z$]\\s+(VARCHAR|INT|DOUBLE|CHAR))((\\s*[,]\\s*[0-9_A-Z$]+[_A-Z$])\\s*([0-9_A-Z$]+[_A-Z$]\\s+(VARCHAR|INT|DOUBLE|CHAR)))*\\s*[)]\\s*[;]\\s*";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(query);
      return matcher.matches();
  }
  
  public boolean verifySyntaxDropTable(String query){
      String regex = "[\\s]*(DROP)[\\s]+(TABLE)[\\s]+[0-9_A-Z$]+[_A-Z$][\\s]*[;][\\s]*";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(query);
      return matcher.matches();
  }
  
  public boolean verifySyntaxSelect(String query){
      String regex = "[\\s]*(SELECT)[\\s]+((ALL)|(([0-9_A-Z$]+[_A-Z$])([\\s]*[,][\\s]*[0-9_A-Z$]+[_A-Z$])*))[\\s]+(FROM)[\\s]+[0-9_A-Z$]+[_A-Z$][\\s]*[;][\\s]*";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(query);
      return matcher.matches();
  }
  
  public boolean verifySyntaxInsertInto(String query){
      String regex = "[\\s]*(INSERT)[\\s]+(INTO)[\\s]+([0-9_A-Z$]+[_A-Z$])[\\s]+((VALUES)|(VALUE))[\\s]*([(]([.0-9_A-Z$]+)([\\s]*[,][\\s]*[.0-9_A-Z$]+)*[)])[\\s]*[;][\\s]*";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(query);
      return matcher.matches();
  }
  
  public boolean verifySyntaxUpdate(String query){
      String regex = "[\\s]*(INSERT)[\\s]+(INTO)[\\s]+([0-9_A-Z$]+[_A-Z$])[\\s]+((VALUES)|(VALUE))[\\s]*([(]([0-9_A-Z$]+[0-9_A-Z$])([\\s]*[,][\\s]*[0-9_A-Z$]+[_A-Z$])*[)])[\\s]*[;][\\s]*";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(query);
      return matcher.matches();
  }
  
  public boolean verifySyntaxShowDatabases(String query){
      String regex = "[\\s]*(SHOW)[\\s]+(DATABASES)[\\s]*[;][\\s]*";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(query);
      boolean result = matcher.matches();
      return matcher.matches();
  }
  
  public boolean verifySyntaxShowTables(String query){
      String regex = "[\\s]*(SHOW)[\\s]+(TABLES)[\\s]*[;][\\s]*";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(query);
      boolean result = matcher.matches();
      return matcher.matches();
  }
  /* Fin de los métodos de verificación */
  
  /* Devuelve nombre base de sentencias simples */
  private String getNombreBase(String query, String sentencia) {
    System.out.println(query); //Mensaje de Prueba para verificar si elimina correctamente los espacios de la sentencia
    String dataBaseName = query.replace(sentencia, "");      // Se elimina el inicio de la sentencia
    dataBaseName        = dataBaseName.replace(";", "");      // Se quita el ;
    dataBaseName        = dataBaseName.replace(" ", "");      // Se quitan posibles espacios.
    dataBaseName        = dataBaseName.trim();                   // Se quitan espacios de inicio.
    
    return dataBaseName;
  }
  
  /* Devuelve nombre de una tabla */
  private String getNombreTabla(String query, String sentencia){
      System.out.println(query);
      String tableName = query.replace(sentencia, "");
      tableName = tableName.replaceFirst(" ", "");
      if(query.contains("VALUES") || query.contains("VALUE")){
          tableName =  tableName.substring(0,tableName.indexOf(" "));
      }
      else if(query.contains("SET")){
          tableName = tableName.substring(0,tableName.indexOf(" "));
      }
      else{
          tableName = tableName.replaceFirst(" ", "");
          tableName =  tableName.substring(0,tableName.indexOf("("));
      }
      return tableName;
  }
  
  /* Generador del codigo del objecto tabla */
  private StringBuilder codeGeneration(String className, LinkedHashMap<String, String> attr) {
    StringBuilder code = new StringBuilder();    
    Set<String> attrNames = attr.keySet();
    Iterator<String> iterator = attrNames.iterator();
    
    /* Generación del código */
    code.append("package examen1;\n");
    code.append("import java.util.ArrayList;\n");
    code.append("public class ").append(className).append(" implements java.io.Serializable{\n");   
    
    /* Declaración de variables */
    while (iterator.hasNext()) {
      String key = iterator.next();
      String virtualType = attr.get(key);
      String realType = getRealType(virtualType);
      code.append("private  ").append(realType).append(" ").append(key).append(";\n");
    }
    
    /* Getters */
    iterator = attrNames.iterator();
    while (iterator.hasNext()) {
      String key = iterator.next();
      String virtualType = attr.get(key);
      String realType = getRealType(virtualType);
      code.append("public ").append(realType).append(" get").append(key).append(" () {\n return this.").append(key).append(";}\n");
    }
    
    /* Getter especializado en regresar un array de strings con el nombre de los tipos de los atributos */
    code.append("public ").append("ArrayList<String> ").append(" getTypes () {\n ArrayList<String> types = new ArrayList<>();\n");
    iterator = attrNames.iterator();
    ArrayList<String> types = new ArrayList<>();
    while (iterator.hasNext()){
        String key = iterator.next();
        String virtualType = attr.get(key);
        String realType = getRealType(virtualType);
        code.append("types.add(\"").append(key).append(",").append(realType).append("\");\n");
    }
    code.append("return types;}\n");
    
    /* Setters */
    iterator = attrNames.iterator();
    while (iterator.hasNext()) {
      String key = iterator.next();
      String virtualType = attr.get(key);
      String realType = getRealType(virtualType);
      code.append("public void set").append(key).append(" (").append(realType).append(" ").append(key).append(") {\n this.").append(key).append("  =  ").append(key).append(";}\n");
    }
    
    code.append("\n}");
    System.out.println("Generated Code: " + code); //Pruebas vemos como genero el codigo
    return code;
  }
  
  /* Método de la sentencia CREATE DATABASE */
  public String createDatabase(String query) {
    return getNombreBase(query.replaceAll("[\\s]+"," "), "CREATE DATABASE");
  }
  /* Fin del método de la sentencia CREATE DATABASE */
  
  /* Método de la sentencia DROP DATABASE */
  public String dropDatabase(String query) {
    return getNombreBase(query.replaceAll("[\\s]+"," "), "DROP DATABASE");
  }
  /* Fin del método de la sentencia DROP DATABASE */
  
  /* Método de la sentencia USE */
  public String useDatabase(String query) {
    return getNombreBase(query.replaceAll("[\\s+]"," "), "USE");
  }
  /* Fin del método de la sentencia USE */
  
  /* Métodos de la sentencia CREATE TABLE */
  public String getTableName(String query) {
    return getNombreTabla(query.replaceAll("[\\s+]", " "), "CREATE TABLE");
  }
  
  public boolean getTableAttributes(String query, String tableName){
    String attr = query.substring(query.indexOf("(") + 1, query.indexOf(")"));  // Se obtienen los atributos
    attr = attr.replaceAll("[\\s]+"," ");                                                // Se eliminan todos los espacios en blanco
    String[] attrs = attr.split(",");                                           // Se obtienen todos los atributos con su tipo de variable.
    
    /* Se obtienen los campos ingresados */
    LinkedHashMap<String, String> atts = new LinkedHashMap<>();
    for(String at: attrs) {
      at = at.trim();   //Se eliminan los espacios al principio y al final de cada atributo, dejando unicamente un espacio entre el tipo y el valor
      String parts[] = at.split(" ");
//      parts[0] = parts[0].trim(); 
//      parts[1] = parts[1].trim();
      if (atts.containsKey(parts[0])) {  // valor duplicado
        return false;
      } 
      else {
        atts.put(parts[0], parts[1]);  // Se ponen los campos en el LinkedHashMap
      }
    }
    
    /* Se crea la clase dinamicamente */
    // 1. Se genera el código a crear.
    StringBuilder code = codeGeneration(tableName, atts);
    
    // 2. Se compila.
    DynamicCompiler dc = new DynamicCompiler();
    dc.compileClass(tableName, code);
    return true;
  }
  
  private String getRealType(String virtualType) {
    if (virtualType.compareTo("VARCHAR") == 0) {
      return "String";
    } else if (virtualType.compareTo("INT") == 0) {
      return "int";
    } else if (virtualType.compareTo("DOUBLE") == 0) {
      return "double";
    } else {
      return "char";
    }
  }
  /* Fin de los métodos de la sentencia CREATE TABLE */
  
  /* Método de la sentecnia DROP TABLE  */
  public String getTableNameDrop(String query) {
    return getNombreBase(query.replaceAll("[\\s]+", " "), "DROP TABLE");
  }
  /* Fin del método de la sentencia DROP TABLE */
  
  /* Métodos de la sentencia INSERT INTO */
  public String getITableName(String query){
      return getNombreTabla(query.replaceAll("[\\s+]", " "), "INSERT INTO");
  }
  
  private String getValType(String value){
      String type = "String";
      try{
          Integer.parseInt(value);
          type = "int";
      }
      catch(NumberFormatException e){
          try{
              Double.parseDouble(value);
              type = "double";
          }
          catch(NumberFormatException ex){
              if(value.length() == 1){
                type = "char";
              } 
          }
      }
      return type;
  }
  
  private Class getClass(String value){
      Class c;
      switch (value) {
          case "String":
              c = String.class;
              break;
          case "int":
              c = int.class;
              break;
          case "double":
              c = double.class;
              break;
          default:
              c = char.class;
              break;
      }
      return c;    
  }
  
  public Object verifyInsertedValues(String query, String tableName){
      DynamicCompiler dc = new DynamicCompiler();
      Object tupla = dc.getInstance(tableName);
      try{
          System.out.println(tupla.getClass().getName());
          Method[] methods = tupla.getClass().getMethods();
          for (Method method : methods) {
              System.out.println(method.getName());
          }
          Method method  = tupla.getClass().getMethod("getTypes", null);
          System.out.println("method = " + method.toString());
          ArrayList<String> pairs = (ArrayList<String>) method.invoke(tupla, null);
          System.out.println("Success");
          
          String rvals = query.substring(query.indexOf("(") + 1, query.indexOf(")"));
          String[] vals = rvals.split(",");
          if(pairs.size() == vals.length){
              String[] pair;
              String val, valtype;
              Method setter;
              for(int i=0;i < pairs.size(); i++){
                  pair = pairs.get(i).split(",");
                  val = vals[i].trim();
                  valtype = getValType(val);
                  if(!valtype.equals(pair[1])){
                      return  null;
                  }
                setter = tupla.getClass().getMethod("set" + pair[0], getClass(pair[1]));
                
                switch (valtype) {
                      case "String":
                          setter.invoke(tupla, val);
                          break;
                      case "int":
                          setter.invoke(tupla, Integer.parseInt(val));
                          break;
                      case "double":
                          setter.invoke(tupla, Double.parseDouble(val));
                          break;
                      default:
                          setter.invoke(tupla, val.charAt(0));
                          break;
                  }
              }
          }            
      }
      catch(Exception e){
          e.printStackTrace();
          tupla = null;
      }
      return tupla;
  }
  /*Fin de los métodos de la sentencia INSERT INTO */
}