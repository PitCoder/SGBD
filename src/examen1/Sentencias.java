package examen1;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.LinkedList;
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
    validPrefixes.add("SHOW TABLES");    // 9
    validPrefixes.add("DELETE"); //10
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
      String regex = "[\\s]*(SELECT)[\\s]+((ALL)|(([0-9_A-Z$]+[_A-Z$])([\\s]*[,][\\s]*[0-9_A-Z$]+[_A-Z$])*))[\\s]+(FROM)[\\s]+[0-9_A-Z$]+[_A-Z$]([\\s]+(WHERE)[\\s]+([0-9_A-Z$]+[_A-Z$])[\\s]+((([=]|(>=)|(<=)|[>]|[<]|(LIKE))[\\s]+([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"])))|((BETWEEN)[\\s]+([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"]))[\\s]+(AND)[\\s]+([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"])))))*[\\s]*[;][\\s]*";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(query);
      return matcher.matches();
  }
  
  public boolean verifySyntaxInsertInto(String query){
      String regex = "[\\s]*(INSERT)[\\s]+(INTO)[\\s]+([0-9_A-Z$]+[_A-Z$])[\\s]+((VALUES)|(VALUE))[\\s]*([(]([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"]))([\\s]*[,][\\s]*([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"])))*[)])[\\s]*[;][\\s]*";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(query);
      return matcher.matches();
  }
  
  public boolean verifySyntaxUpdate(String query){
      String regex = "[\\s]*(UPDATE)[\\s]+([0-9_A-Z$]+[_A-Z$])[\\s]+(SET)[\\s]+([0-9_A-Z$]+[_A-Z$])[\\s]+[=][\\s]+([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"]))([\\s]+(WHERE)[\\s]+([0-9_A-Z$]+[_A-Z$])[\\s]+((([=]|(>=)|(<=)|[>]|[<]|(LIKE))[\\s]+([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"])))|((BETWEEN)[\\s]+([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"]))[\\s]+(AND)[\\s]+([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"])))))*[\\s]*[;][\\s]*";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(query);
      return matcher.matches();
  }
  
  public boolean verifySyntaxDelete(String query){
      String regex = "[\\s]*(DELETE)[\\s]+(FROM)[\\s]+([0-9_A-Z$]+[_A-Z$])([\\s]+(WHERE)[\\s]+([0-9_A-Z$]+[_A-Z$])[\\s]+((([=]|(>=)|(<=)|[>]|[<]|(LIKE))[\\s]+([.0-9]+|([\\\"][.0-9_a-z A-Z$]+[\\\"])))|((BETWEEN)[\\s]+([.0-9]+|([\\\"][.0-9_a-z A-Z$]+[\\\"]))[\\s]+(AND)[\\s]+([.0-9]+|([\\\"][.0-9_a-z A-Z$]+[\\\"])))))*[\\s]*[;][\\s]*";
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
  
  public boolean verifyReservedWord(String query){
      String regex = "((CREATE)|(DATABASE)|(DROP)|(USE)|(TABLE)|(SELECT)|(INSERT)|(INTO)|(FROM)|(UPDATE)|(DESC)|(SHOW)|(DATABASES)|(TABLES)|(WHERE)|(ALL)|(DELETE))";
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
      else if (query.contains("FROM")){
          tableName = tableName.substring(tableName.indexOf(" FROM ") + 5, tableName.length());
          System.out.println(tableName);
          if(tableName.contains("WHERE")){
              tableName = tableName.trim();
              System.out.println(tableName);
              tableName = tableName.substring(0, tableName.indexOf(" "));
          }
          else{
              tableName = tableName.substring(0, tableName.indexOf(";"));
              tableName = tableName.trim();
          }
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
      code.append("public ").append(realType).append(" get_").append(key).append(" () {\n return this.").append(key).append(";}\n");
    }
    
    /* Getter especializado en regresar un array de strings con el nombre de los tipos de los atributos */
    code.append("public ").append("ArrayList<String> ").append(" extractTypes () {\n ArrayList<String> types = new ArrayList<>();\n");
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
  
  /* Método de la sentencia DROP TABLE  */
  public String getTableNameDrop(String query) {
    return getNombreBase(query.replaceAll("[\\s]+", " "), "DROP TABLE");
  }
  /* Fin del método de la sentencia DROP TABLE */
  
  /* Métodos de la sentencia SELECT */
  public String getSTableName(String query){
      return getNombreTabla(query.replaceAll("[\\s]+", " "), "SELECT");
  }
  
  public ArrayList<String> extractQueryAttributes(String query){
      ArrayList<String> attributes = new ArrayList<>();
      String newquery = query.replaceAll("[\\s]+"," ").trim();
      newquery = newquery.replace("SELECT ", "");
      newquery = newquery.substring(0,newquery.indexOf(" FROM "));
      if(newquery.startsWith("ALL")){
          attributes.add("ALL");
      }
      else{
          String atts[] = newquery.split(",");
          for(String att : atts){
              att = att.trim();
              attributes.add(att);
          }
      }
      return attributes;
  }
  
  public String invokeAllGetters(LinkedList<Object> tuples, String tableName){
      DynamicCompiler dc = new DynamicCompiler();
      Object tupla = dc.getInstance(tableName);
      ArrayList<String> headers = new ArrayList<String>();
      ArrayList<ArrayList<String>> content = new ArrayList<ArrayList<String>>();
      String resultSet = "";
      try{
          //System.out.println(tupla.getClass().getName());
          Method[] methods = tupla.getClass().getMethods();
          ArrayList<String> getters = new ArrayList<>();
          for (Method method : methods){
              String methodName = method.getName();
              if(methodName.contains("get_")){
                  getters.add(methodName);
                  headers.add(methodName.replace("get_", ""));
                  //resultSet = resultSet + methodName.replace("get_", "");
                  //resultSet = resultSet + "\t";
              }
          }
          //resultSet = resultSet + "\n";
          
          for(int i=0;i<tuples.size();i++){
            Object tuple = tuples.get(i);
            Method method;
            ArrayList<String> row = new ArrayList<String>();
            for(int j=0;j<getters.size();j++){
               method = tuple.getClass().getMethod(getters.get(j),null);
               if(method.getReturnType().equals(int.class)){
                   //resultSet = resultSet + Integer.toString((int)method.invoke(tuple, null));
                   row.add(Integer.toString((int)method.invoke(tuple, null)));
               }
               else if(method.getReturnType().equals(double.class)){
                   //resultSet = resultSet + Double.toString((double)method.invoke(tuple, null));
                   row.add(Double.toString((double)method.invoke(tuple, null)));
               }
               else if(method.getReturnType().equals(char.class)){
                   //resultSet = resultSet  + String.valueOf((char)method.invoke(tuple, null));
                   row.add(String.valueOf((char)method.invoke(tuple, null)));
               }
               else{
                   //resultSet = resultSet + (String)method.invoke(tuple, null);
                   row.add((String)method.invoke(tuple, null));
               }
               //resultSet =  resultSet + ",";
            }
            //resultSet = resultSet + "\n";
            content.add(row);
          }
          ConsoleTable ct = new ConsoleTable(headers, content);
          resultSet = ct.printTable();
      }
      catch(Exception e){
          e.printStackTrace();
      }
      return resultSet;
  }
  
  public String invokeGetters(LinkedList<Object> tuples, ArrayList<String> attributes, String tableName){
      DynamicCompiler dc = new DynamicCompiler();
      Object tupla = dc.getInstance(tableName);
      ArrayList<String> headers = new ArrayList<String>();
      ArrayList<ArrayList<String>> content = new ArrayList<ArrayList<String>>();
      String resultSet = "";
      
      try{
          //System.out.println(tupla.getClass().getName());
          Method[] methods = tupla.getClass().getMethods();
          ArrayList<String> getters = new ArrayList<>();
          
          for(int i=0;i<attributes.size();i++){
              getters.add("get_" + attributes.get(i));
              headers.add(attributes.get(i));
          }
          
          for(int i=0;i<tuples.size();i++){
            Object tuple = tuples.get(i);
            Method method;
            ArrayList<String> row = new ArrayList<String>();
            for(int j=0;j<getters.size();j++){
               method = tuple.getClass().getMethod(getters.get(j),null);
               if(method.getReturnType().equals(int.class)){
                   //resultSet = resultSet + Integer.toString((int)method.invoke(tuple, null));
                   row.add(Integer.toString((int)method.invoke(tuple, null)));
               }
               else if(method.getReturnType().equals(double.class)){
                   //resultSet = resultSet + Double.toString((double)method.invoke(tuple, null));
                   row.add(Double.toString((double)method.invoke(tuple, null)));
               }
               else if(method.getReturnType().equals(char.class)){
                   //resultSet = resultSet  + String.valueOf((char)method.invoke(tuple, null));
                   row.add(String.valueOf((char)method.invoke(tuple, null)));
               }
               else{
                   //resultSet = resultSet + (String)method.invoke(tuple, null);
                   row.add((String)method.invoke(tuple, null));
               }
               //resultSet =  resultSet + ",";
            }
            //resultSet = resultSet + "\n";
            content.add(row);
          }
          ConsoleTable ct = new ConsoleTable(headers, content);
          resultSet = ct.printTable();
      }
      catch(Exception e){
          e.printStackTrace();
      }
      return resultSet;
  }
  
  public ArrayList<String> getSelectElements(String query, String oldquery){
      ArrayList<String> elements = new ArrayList<>(); 
      Pattern pattern = Pattern.compile("[\\s]*(SELECT)[\\s]+((ALL)|(([0-9_A-Z$]+[_A-Z$])([\\s]*[,][\\s]*[0-9_A-Z$]+[_A-Z$])*))[\\s]+(FROM)[\\s]+[0-9_A-Z$]+[_A-Z$][\\s]*");
      Matcher matcher = pattern.matcher(query);
      try{
              int beginIndex = 0;
              int endIndex = 0;
              if(matcher.find()){
                  beginIndex = matcher.start();
                  endIndex = matcher.end();
                  System.out.println("Match found at index = " +   beginIndex + "-" + endIndex);
              }
              
              String outter = "(.*)([\\s]+(WHERE)[\\s]+([0-9_A-Z$]+[_A-Z$])[\\s]+((([=]|(>=)|(<=)|[>]|[<]|(LIKE))[\\s]+([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"])))|((BETWEEN)[\\s]+([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"]))[\\s]+(AND)[\\s]+([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"])))))(.*)";
              if(query.matches(outter)){
                    System.out.println("Where sentence detected");
                    elements.addAll(getWhereElements(query.substring(endIndex),oldquery.substring(endIndex)));
              }
       }
       catch(Exception e){
           System.out.println("No region match");
       }        
      return elements;
  }
  
  public LinkedList<Object> getTuples(LinkedList<Object> tuples, ArrayList<Integer> matchedTuples, ArrayList<String> elements){
      LinkedList<Object> newTuples = new LinkedList<Object>();
      Object tuple = tuples.get(0);
      Method[] methods = tuple.getClass().getMethods();
      Method method = null;
      String attribute = elements.get(0);
      String value = elements.get(1);
      System.out.println(attribute);
      System.out.println(value);
      
      if(value.startsWith("\"")){
          value = value.substring(1,value.length()-1);
      }
      
      for(int i=0;i<matchedTuples.size();i++){
          newTuples.add(tuples.get(matchedTuples.get(i)));
      }
      return newTuples;
  }
  /* Fin de los métodos de la sentencia SELECT */
  
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
//          System.out.println(tupla.getClass().getName());
//          Method[] methods = tupla.getClass().getMethods();
//          for (Method method : methods) {
//              System.out.println(method.getName());
//          }
          System.out.println(tableName);
          Method method  = tupla.getClass().getMethod("extractTypes", null);
          System.out.println("method = " + method.toString());
          ArrayList<String> pairs = (ArrayList<String>) method.invoke(tupla, null);
          System.out.println("Success");
          
          String rvals = query.substring(query.indexOf("(") + 1, query.lastIndexOf(")"));
          String[] vals = rvals.split(",");
          System.out.println(vals.length);
          System.out.println(pairs.size());
          if(pairs.size() == vals.length){
              String[] pair;
              String val, valtype;
              Method setter;
              for(int i=0;i < pairs.size(); i++){
                  pair = pairs.get(i).split(",");
                  val = vals[i].trim();
                  System.out.println(val);
                  if(val.startsWith("\"")){
                      val = val.substring(1,val.length()-1);
                      System.out.println(val);
                  }
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
  
  /* Métodos de la sentencia UPDATE */
  public String getUTableName(String query){
      return getNombreTabla(query.replaceAll("[\\s]+"," "), "UPDATE");
  }

  private ArrayList<String> getWhereElements(String query, String oldquery){
      ArrayList<String> elements = new ArrayList<>();
      String inner1 = "(([\\s]+(BETWEEN)[\\s]+([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"]))[\\s]+(AND)[\\s]+([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"])))[\\s]*[;][\\s]*)";
      String inner2 = "(([\\s]+([=]|(>=)|(<=)|[>]|[<]|(LIKE))[\\s]+([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"])))[\\s]*[;][\\s]*)";
      
       if(query.matches("(.*)"+ inner2 + "(.*)")){
                System.out.println("Matched second pattern");
                Pattern pattern = Pattern.compile(inner2);
                Matcher matcher = pattern.matcher(query);
                try{
                    int indexOf = 0;
                    while(matcher.find()){
                        indexOf = matcher.start();
                        System.out.println("Match found at index = " + indexOf);
                    }
                    
                    String attribute = (query.substring(0, indexOf).replaceFirst("(WHERE)[\\s]+","")).trim();
                    elements.add(attribute);
                    System.out.println(query);
                    
                    Pattern innerPattern = Pattern.compile("[\\s]*([=]|(>=)|(<=)|[>]|[<]|(LIKE))[\\s]*");
                    Matcher innerMatcher = innerPattern.matcher(query);
                    try{
                        int indexOfB = 0;
                        indexOf = 0;
                        if(innerMatcher.find()){
                            indexOfB = innerMatcher.start();
                            indexOf = innerMatcher.end();
                            System.out.println("Match end at index = " +  indexOfB + " - "+ indexOf);
                        }
                        String operator = query.substring(indexOfB,indexOf).trim();
                        String value = oldquery.substring(indexOf).trim();
                        value = value.substring(0,value.length()-1).trim();
                        elements.add(operator);
                        elements.add(value);
                    }
                    catch(Exception e){
                        System.out.println("No region match");
                    }
                }
                catch(Exception e){
                    System.out.println("No region match");
                }
        }
        else{
                System.out.println("Matched first pattern");
                Pattern pattern = Pattern.compile(inner1);
                Matcher matcher = pattern.matcher(query);
                try{
                    int indexOf = 0;
                    while(matcher.find()){
                        indexOf = matcher.start();
                        System.out.println("Match found at index = " + indexOf);
                    }
                    
                    String attribute = query;
                    attribute = (attribute.substring(0, indexOf).replaceFirst("(WHERE)[\\s]+","")).trim();
                    elements.add(attribute);
                    elements.add("BETWEEN");
                    query = query.substring(indexOf);
                    oldquery = oldquery.substring(indexOf);                  
                    
                    Pattern innerPattern = Pattern.compile("[\\s]+(BETWEEN)[\\s]+([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"]))[\\s]+(AND)[\\s]+");
                    Matcher innerMatcher = innerPattern.matcher(query);
                    try{
                        indexOf = 0;
                        if(innerMatcher.find()){
                            indexOf = innerMatcher.end();
                            System.out.println("Match end at index = " + indexOf);
                        }
                        String value1 = oldquery.substring(0, indexOf);
                        String[] temp = value1.split("([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"]))");
                        value1 = value1.substring(temp[0].length(),value1.length()-temp[1].length());
                        
                        String value2 = oldquery.substring(indexOf);
                        value2 = value2.trim();
                        value2 = value2.substring(0, value2.length()-1).trim();
                        
                        elements.add(value1);
                        elements.add(value2);
                    }
                    catch(Exception e){
                        System.out.println("No region match");
                    }
                }
                catch(Exception e){
                    System.out.println("No region match");
                }
        }
      return elements;
  }
  
  public ArrayList getSetElements(String query, String oldquery){
      ArrayList<String> elements = new ArrayList<>();
      Pattern pattern = Pattern.compile("[\\s]+([0-9_A-Z$]+[_A-Z$])[\\s]+[=][\\s]+([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"]))[\\s]*");
      Matcher matcher = pattern.matcher(query);
          try{
              int beginIndex = 0;
              int endIndex = 0;
              if(matcher.find()){
                  beginIndex = matcher.start();
                  endIndex = matcher.end();
                  System.out.println("Match found at index = " +   beginIndex + "-" + endIndex);
              }
              String temquery = query.substring(beginIndex, endIndex);
              String tempoldquery = oldquery.substring(beginIndex, endIndex);
              elements.add(temquery.substring(0, temquery.indexOf("=")).trim());
              elements.add(tempoldquery.substring(tempoldquery.indexOf("=")+1).trim());
              
              String outter = "(.*)([\\s]+(WHERE)[\\s]+([0-9_A-Z$]+[_A-Z$])[\\s]+((([=]|(>=)|(<=)|[>]|[<]|(LIKE))[\\s]+([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"])))|((BETWEEN)[\\s]+([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"]))[\\s]+(AND)[\\s]+([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"])))))(.*)";
              if(query.matches(outter)){
                System.out.println("Where sentence detected");
                elements.addAll(getWhereElements(query.substring(endIndex),oldquery.substring(endIndex)));
              }
          }
          catch(Exception e){
              System.out.println("No region match");
          }
      return elements;
  }
  
  public ArrayList<Integer> getMatchTuples(LinkedList<Object> tuples, ArrayList<String> elements, String query){
      ArrayList<Integer> matchedTuples =  new ArrayList<Integer>();
      Object tuple;
      Method method;
      Verifier verifier = new Verifier();
      if(query.equals("SET")){
          /* Forzozamente contiene una sentencia que involucra WHERE */
          if(elements.size() > 2){
              if(elements.size() > 5){
                  String attribute = elements.get(2);
                  String operator = elements.get(3);
                  String value1 = elements.get(4);
                  String value2 = elements.get(5);
                  tuple = tuples.get(0);
                  try{
                      if(value1.startsWith("\"")){
                          value1 = value1.substring(1,value1.length()-1);
                      }
                      if(value2.startsWith("\"")){
                          value2 = value2.substring(1,value2.length()-1);
                      }
                    String returnType;
                    method = tuple.getClass().getMethod("get_"+ attribute, null);
                    if(method.getReturnType().equals(int.class)){
                        returnType = "int";
                        if(returnType.equals(getValType(value1)) && returnType.equals(getValType(value2))){
                            int val1 = Integer.parseInt(value1);
                            int val2 = Integer.parseInt(value2);
                            int getval;
                            for(int i=0; i<tuples.size(); i++){
                                tuple = tuples.get(i);
                                getval = (int)method.invoke(tuple, null);
                                if(verifier.matchValBetween(val1,val2,getval)){
                                    System.out.println(getval);
                                    matchedTuples.add(i);
                                }
                            }
                        }
                        else{
                            return null;
                        }
                    }
                    else if(method.getReturnType().equals(double.class)){
                        returnType = "double";
                        if(returnType.equals(getValType(value1)) && returnType.equals(getValType(value2))){
                            double val1 = Double.parseDouble(value1);
                            double val2 = Double.parseDouble(value2);
                            double getval;
                            for(int i=0; i<tuples.size(); i++){
                                tuple = tuples.get(i);
                                getval = (double)method.invoke(tuple, null);
                                if(verifier.matchValBetween(val1,val2,getval)){
                                    System.out.println(getval);
                                    matchedTuples.add(i);
                                }
                            }
                        }
                        else{
                            return null;
                        }
                    }
                    else if(method.getReturnType().equals(char.class)){
                        returnType = "char";
                        if(returnType.equals(getValType(value1)) && returnType.equals(getValType(value2))){
                            char val1 = value1.charAt(0);
                            char val2 = value2.charAt(0);
                            char getval;
                            for(int i=0; i<tuples.size(); i++){
                                tuple = tuples.get(i);
                                getval = (char)method.invoke(tuple, null);
                                if(verifier.matchValBetween(val1,val2,getval)){
                                    System.out.println(getval);
                                    matchedTuples.add(i);
                                }
                            }
                        }
                        else{
                            return null;
                        }
                    }
                    else{
                        returnType = "String";
                        if(returnType.equals(getValType(value1)) && returnType.equals(getValType(value2))){
                            String getval;
                            for(int i=0; i<tuples.size(); i++){
                                tuple = tuples.get(i);
                                getval = (String)method.invoke(tuple, null);
                                if(verifier.matchValBetween(value1,value2,getval)){
                                    System.out.println(getval);
                                    matchedTuples.add(i);
                                }  
                            }
                        }
                        else{
                            return null;
                        }
                    }                       
                  }
                  catch(Exception e){
                      e.printStackTrace();
                  }            
              }
              else{
                  String attribute = elements.get(2);
                  String operator = elements.get(3);
                  String value = elements.get(4);
//                  System.out.println(attribute);
//                  System.out.println(operator);
//                  System.out.println(value);
                  tuple = tuples.get(0);
                  try{
                    if(value.startsWith("\"")){
                        value = value.substring(1,value.length()-1);
                    }
                    String returnType;
                    method = tuple.getClass().getMethod("get_"+ attribute, null);
                    if(method.getReturnType().equals(int.class)){
                        returnType = "int";
                        if(returnType.equals(getValType(value))){
                            int val = Integer.parseInt(value);
                            int getval;
                            for(int i=0; i<tuples.size(); i++){
                                tuple = tuples.get(i);
                                getval = (int)method.invoke(tuple, null);
                                if(verifier.matchVal(operator,getval,val)){
                                    System.out.println(getval);
                                    matchedTuples.add(i);
                                }
                            }
                        }
                        else{
                            return null;
                        }
                    }
                    else if(method.getReturnType().equals(double.class)){
                        returnType = "double";
                        if(returnType.equals(getValType(value))){
                            double val = Double.parseDouble(value);
                            double getval;
                            for(int i=0; i<tuples.size(); i++){
                                tuple = tuples.get(i);
                                getval = (double)method.invoke(tuple, null);
                                if(verifier.matchVal(operator,getval, val)){
                                    System.out.println(getval);
                                    matchedTuples.add(i);
                                }
                            }
                        }
                        else{
                            return null;
                        }
                    }
                    else if(method.getReturnType().equals(char.class)){
                        returnType = "char";
                        if(returnType.equals(getValType(value))){
                            char val = value.charAt(0);
                            char getval;
                            for(int i=0; i<tuples.size(); i++){
                                tuple = tuples.get(i);
                                getval = (char)method.invoke(tuple, null);
                                if(verifier.matchVal(operator,getval, val)){
                                    System.out.println(getval);
                                    matchedTuples.add(i);
                                }
                            }
                        }
                        else{
                            return null;
                        }
                    }
                    else{
                        returnType = "String";
                        if(returnType.equals(getValType(value))){
                            String getval;
                            for(int i=0; i<tuples.size(); i++){
                                tuple = tuples.get(i);
                                getval = (String)method.invoke(tuple, null);
                                if(verifier.matchVal(operator,getval, value)){
                                    System.out.println(getval);
                                    matchedTuples.add(i);
                                }  
                            }
                        }
                        else{
                            return null;
                        }
                    }
                  }
                  catch(Exception e){
                      e.printStackTrace();
                  }
              }
          }
          else{
              for(int i=0;i<tuples.size();i++)
                  matchedTuples.add(i);
          }
      }
      else if(query.equals("SELECT")){
          if(elements.size() > 3){
                  String attribute = elements.get(0);
                  String operator = elements.get(1);
                  String value1 = elements.get(2);
                  String value2 = elements.get(3);
                  
                                    tuple = tuples.get(0);
                  try{
                      if(value1.startsWith("\"")){
                          value1 = value1.substring(1,value1.length()-1);
                      }
                      if(value2.startsWith("\"")){
                          value2 = value2.substring(1,value2.length()-1);
                      }
                    String returnType;
                    method = tuple.getClass().getMethod("get_"+ attribute, null);
                    if(method.getReturnType().equals(int.class)){
                        returnType = "int";
                        if(returnType.equals(getValType(value1)) && returnType.equals(getValType(value2))){
                            int val1 = Integer.parseInt(value1);
                            int val2 = Integer.parseInt(value2);
                            int getval;
                            for(int i=0; i<tuples.size(); i++){
                                tuple = tuples.get(i);
                                getval = (int)method.invoke(tuple, null);
                                if(verifier.matchValBetween(val1,val2,getval)){
                                    System.out.println(getval);
                                    matchedTuples.add(i);
                                }
                            }
                        }
                        else{
                            return null;
                        }
                    }
                    else if(method.getReturnType().equals(double.class)){
                        returnType = "double";
                        if(returnType.equals(getValType(value1)) && returnType.equals(getValType(value2))){
                            double val1 = Double.parseDouble(value1);
                            double val2 = Double.parseDouble(value2);
                            double getval;
                            for(int i=0; i<tuples.size(); i++){
                                tuple = tuples.get(i);
                                getval = (double)method.invoke(tuple, null);
                                if(verifier.matchValBetween(val1,val2,getval)){
                                    System.out.println(getval);
                                    matchedTuples.add(i);
                                }
                            }
                        }
                        else{
                            return null;
                        }
                    }
                    else if(method.getReturnType().equals(char.class)){
                        returnType = "char";
                        if(returnType.equals(getValType(value1)) && returnType.equals(getValType(value2))){
                            char val1 = value1.charAt(0);
                            char val2 = value2.charAt(0);
                            char getval;
                            for(int i=0; i<tuples.size(); i++){
                                tuple = tuples.get(i);
                                getval = (char)method.invoke(tuple, null);
                                if(verifier.matchValBetween(val1,val2,getval)){
                                    System.out.println(getval);
                                    matchedTuples.add(i);
                                }
                            }
                        }
                        else{
                            return null;
                        }
                    }
                    else{
                        returnType = "String";
                        if(returnType.equals(getValType(value1)) && returnType.equals(getValType(value2))){
                            String getval;
                            for(int i=0; i<tuples.size(); i++){
                                tuple = tuples.get(i);
                                getval = (String)method.invoke(tuple, null);
                                if(verifier.matchValBetween(value1,value2,getval)){
                                    System.out.println(getval);
                                    matchedTuples.add(i);
                                }  
                            }
                        }
                        else{
                            return null;
                        }
                    }                       
                  }
                  catch(Exception e){
                      e.printStackTrace();
                  }
          }
          else{
              String attribute = elements.get(0);
              String operator = elements.get(1);
              String value = elements.get(2);
                  tuple = tuples.get(0);
                  try{
                    if(value.startsWith("\"")){
                        value = value.substring(1,value.length()-1);
                    }
                    String returnType;
                    method = tuple.getClass().getMethod("get_"+ attribute, null);
                    if(method.getReturnType().equals(int.class)){
                        returnType = "int";
                        if(returnType.equals(getValType(value))){
                            int val = Integer.parseInt(value);
                            int getval;
                            for(int i=0; i<tuples.size(); i++){
                                tuple = tuples.get(i);
                                getval = (int)method.invoke(tuple, null);
                                if(verifier.matchVal(operator,getval,val)){
                                    System.out.println(getval);
                                    matchedTuples.add(i);
                                }
                            }
                        }
                        else{
                            return null;
                        }
                    }
                    else if(method.getReturnType().equals(double.class)){
                        returnType = "double";
                        if(returnType.equals(getValType(value))){
                            double val = Double.parseDouble(value);
                            double getval;
                            for(int i=0; i<tuples.size(); i++){
                                tuple = tuples.get(i);
                                getval = (double)method.invoke(tuple, null);
                                if(verifier.matchVal(operator,getval, val)){
                                    System.out.println(getval);
                                    matchedTuples.add(i);
                                }
                            }
                        }
                        else{
                            return null;
                        }
                    }
                    else if(method.getReturnType().equals(char.class)){
                        returnType = "char";
                        if(returnType.equals(getValType(value))){
                            char val = value.charAt(0);
                            char getval;
                            for(int i=0; i<tuples.size(); i++){
                                tuple = tuples.get(i);
                                getval = (char)method.invoke(tuple, null);
                                if(verifier.matchVal(operator,getval, val)){
                                    System.out.println(getval);
                                    matchedTuples.add(i);
                                }
                            }
                        }
                        else{
                            return null;
                        }
                    }
                    else{
                        returnType = "String";
                        if(returnType.equals(getValType(value))){
                            String getval;
                            for(int i=0; i<tuples.size(); i++){
                                tuple = tuples.get(i);
                                getval = (String)method.invoke(tuple, null);
                                if(verifier.matchVal(operator,getval, value)){
                                    System.out.println(getval);
                                    matchedTuples.add(i);
                                }  
                            }
                        }
                        else{
                            return null;
                        }
                    }
                  }
                  catch(Exception e){
                      e.printStackTrace();
                  }          
          }     
      }
      return matchedTuples;
  }
  
  public LinkedList<Object> updateTuples(LinkedList<Object> tuples, ArrayList<Integer> matchedTuples, ArrayList<String> elements){
      //LinkedList<Object> newTuples = new LinkedList<Object>();
      Object tuple = tuples.get(0);
      Method[] methods = tuple.getClass().getMethods();
      Method method = null;
      String attribute = elements.get(0);
      String value = elements.get(1);
      System.out.println(attribute);
      System.out.println(value);
      
      if(value.startsWith("\"")){
          value = value.substring(1,value.length()-1);
      }
      
      try{
          for (Method method1 : methods) {
              if (method1.getName().equals("set"+attribute)) {
                  method = method1;
                  break;
              }
          }
         switch (getValType(value)) {
             case "String":
                 String val = value;
                 for(int i=0;i<matchedTuples.size();i++){
                    tuple = tuples.get(matchedTuples.get(i));
                    Class[] typeParameters= method.getParameterTypes();
                    if(typeParameters[0].equals(String.class)){
                        method.invoke(tuple, val);
                        tuples.set(matchedTuples.get(i), tuple);
                    }
                    else{
                        return null;
                    }
                }
                 break;
             case "int":
                 int intval = Integer.parseInt(value);
                 for(int i=0;i<matchedTuples.size();i++){
                    tuple = tuples.get(matchedTuples.get(i));
                    Class[] typeParameters= method.getParameterTypes();
                    if(typeParameters[0].equals(int.class)){
                        method.invoke(tuple, intval);
                        tuples.set(matchedTuples.get(i), tuple);
                    }
                    else{
                        return null;
                    }
                }
                 break;
             case "double":
                 double doubleval = Double.parseDouble(value);
                 for(int i=0;i<matchedTuples.size();i++){
                    tuple = tuples.get(matchedTuples.get(i));
                    Class[] typeParameters= method.getParameterTypes();
                    if(typeParameters[0].equals(double.class)){
                        method.invoke(tuple, doubleval);
                        tuples.set(matchedTuples.get(i), tuple);
                    }
                    else{
                        return null;
                    }
                }
                 break;
             default:
                 char charval = value.charAt(0);
                 for(int i=0;i<matchedTuples.size();i++){
                    tuple = tuples.get(matchedTuples.get(i));
                    Class[] typeParameters= method.getParameterTypes();
                    if(typeParameters[0].equals(char.class)){
                        method.invoke(tuple, charval);
                        tuples.set(matchedTuples.get(i), tuple);
                    }
                    else{
                        return null;
                    }
                }
                 break;
          }
       }
      catch(Exception e){
          return null;
      }
      return tuples;
  }
  /*Fin de los métodos de la sentencia UPDATE */
  
  /* Métodos de la sentencia DELETE */
  public String getDTableName(String query){
      return getNombreTabla(query.replaceAll("[\\s]+", " "), "DELETE");
  }
  
    public ArrayList<String> getDeleteElements(String query, String oldquery){
      ArrayList<String> elements = new ArrayList<>(); 
      Pattern pattern = Pattern.compile("[\\s]*(DELETE)[\\s]+(FROM)[\\s]+([0-9_A-Z$]+[_A-Z$])[\\s]*");
      Matcher matcher = pattern.matcher(query);
      try{
              int beginIndex = 0;
              int endIndex = 0;
              if(matcher.find()){
                  beginIndex = matcher.start();
                  endIndex = matcher.end();
                  System.out.println("Match found at index = " +   beginIndex + "-" + endIndex);
              }
              
              String outter = "(.*)([\\s]+(WHERE)[\\s]+([0-9_A-Z$]+[_A-Z$])[\\s]+((([=]|(>=)|(<=)|[>]|[<]|(LIKE))[\\s]+([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"])))|((BETWEEN)[\\s]+([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"]))[\\s]+(AND)[\\s]+([.0-9]+|([\"][.0-9_a-z A-Z$]+[\"])))))(.*)";
              if(query.matches(outter)){
                    System.out.println("Where sentence detected");
                    elements.addAll(getWhereElements(query.substring(endIndex),oldquery.substring(endIndex)));
              }
       }
       catch(Exception e){
           System.out.println("No region match");
       }        
      return elements;
  }
  /*Fin de los métodos de la sentencia DELETE */
}