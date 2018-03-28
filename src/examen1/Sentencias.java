package examen1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
      String regex = "(CREATE) (TABLE) [\\w]+[\\s]+[(]([A-Z0-9]+ (VARCHAR|INT|DOUBLE|CHAR)+[,]*[\\s]*)*([A-Z0-9]+ (VARCHAR|INT|DOUBLE|CHAR)+)+[)][;]";
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
      String regex = "[\\s]*(INSERT)[\\s]+(INTO)[\\s]+([0-9_A-Z$]+[_A-Z$])[\\s]+((VALUES)|(VALUE))[\\s]*([(]([0-9_A-Z$]+[0-9_A-Z$])([\\s]*[,][\\s]*[0-9_A-Z$]+[_A-Z$])*[)])[\\s]*[;][\\s]*";
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
  
  public String useDatabase(String query) {
    return getNombreBase(query, "USE");
  }
  
  public String getTableName(String query) {
    String p1 = getNombreBase(query, "CREATE TABLE");
    return p1.substring(0, p1.indexOf("("));
  }
  
  public String getTableNameDrop(String query) {
    return getNombreBase(query, "DROP TABLE");
  }
  
  public boolean getTableAttributes(String query, String tableName) {
    String attr = query.substring(query.indexOf("(") + 1, query.indexOf(")"));  // Se obtienen los atributos
    attr = attr.trim();                                                         // Se eliminan posibles espacios en blanco al inicio del substring
    String[] attrs = attr.split(",");                                           // Se obtienen todos los atributos con su tipo de variable.
    
    /* Se obtienen los campos ingresados */
    HashMap<String, String> atts = new HashMap<>();
    for(String at: attrs) {
      at = at.trim();
      String parts[] = at.split(" ");
      parts[0] = parts[0].trim(); parts[1] = parts[1].trim();
      if (atts.containsKey(parts[0])) {                                         // valor duplicado
        return false;
      } else {
        atts.put(parts[0], parts[1]);                                           // Se ponen los campos en el HashMap
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
  
  private StringBuilder codeGeneration(String className, HashMap<String, String> attr) {
    StringBuilder code = new StringBuilder();    
    Set<String> attrNames = attr.keySet();
    Iterator<String> iterator = attrNames.iterator();
    
    /* Generación del código */
    code.append("package examen1;\n");
    code.append("public class " + className + " {\n");   
    
    /* Declaración de variables */
    while (iterator.hasNext()) {
      String key = iterator.next();
      String virtualType = attr.get(key);
      String realType = getRealType(virtualType);
      code.append("private " + realType + " " + key + ";\n");
    }
    
    /* Getters */
    iterator = attrNames.iterator();
    while (iterator.hasNext()) {
      String key = iterator.next();
      String virtualType = attr.get(key);
      String realType = getRealType(virtualType);
      code.append("public " + realType + " get" + key + " () {\n return this." + key + ";}\n");
    }
    
    /* Setters */
    iterator = attrNames.iterator();
    while (iterator.hasNext()) {
      String key = iterator.next();
      String virtualType = attr.get(key);
      String realType = getRealType(virtualType);
      code.append("public void set" + key + " (" + realType + " " + key + ") {\n this." + key + " = " + key + ";}\n");
    }
    
    code.append("}");
    // System.out.println("Generated Code: " + code);
    
    return code;
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
  
  public int showDatabases(String query) {
    query = query.replace(";", "");
    if (query.equals("SHOW DATABASES")) {
      return 1;
    } else {
      return 0;
    }
  }
  
  public boolean showTables(String query) {
    query = query.replace(";", "");
    return query.equals("SHOW TABLES");
  }   
  
}