/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examen1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author cesar
 */
public class PruebasRapidas {
  public static void main(String[] args) {
    
  
  String regex = "(CREATE) (TABLE) [\\w]+[\\s]+[(]([A-Z0-9]+ (VARCHAR|INT|DOUBLE|CHAR)+[,]*[\\s]*)*([A-Z0-9]+ (VARCHAR|INT|DOUBLE|CHAR)+)+[);]";

  Pattern pat = Pattern.compile(regex);
  Matcher mat = pat.matcher("CREATE TABLE TABLA1 (NOMBRE VARCHAR, EDAD INT,)");
  
  if (mat.matches()) {
    System.out.println("SI");
  } else {
    System.out.println("NO");
  }  
}
}
