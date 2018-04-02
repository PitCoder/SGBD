package examen1;

public class Verifier {

    public Verifier() {
    }
    
    public boolean matchVal(String operator, int val1, int val2){
      boolean match = false;
        switch(operator){
            case "=" :
                if(val1 == val2)
                    match = true;
                break;
            case ">=":
                if(val1 >= val2)
                    match = true;
                break;
            case "<=":
                if(val1 <= val2)
                    match = true;
                break;
            case ">":
                if(val1 > val2)
                    match = true;
                break;
            case "<":
                if(val1 < val2)
                    match = true;
                break;
            default:
                match = false;
                break;
        }      
     return match;
  }
    
   public boolean matchVal(String operator, double  val1, double val2){
      boolean match = false;
        switch(operator){
            case "=" :
                if(val1 == val2)
                    match = true;
                break;
            case ">=":
                if(val1 >= val2)
                    match = true;
                break;
            case "<=":
                if(val1 <= val2)
                    match = true;
                break;
            case ">":
                if(val1 > val2)
                    match = true;
                break;
            case "<":
                if(val1 < val2)
                    match = true;
                break;
            default:
                match = false;
                break;
        }      
     return match;
  }
   
   public boolean matchVal(String operator, char val1, char val2){
      boolean match = false;
        switch(operator){
            case "=" :
                if(val1 == val2)
                    match = true;
                break;
            case ">=":
                if(val1 >= val2)
                    match = true;
                break;
            case "<=":
                if(val1 <= val2)
                    match = true;
                break;
            case ">":
                if(val1 > val2)
                    match = true;
                break;
            case "<":
                if(val1 < val2)
                    match = true;
                break;
            default:
                match = false;
                break;
        }      
     return match;
  }
   
   public boolean matchVal(String operator, String val1, String val2){
      int temp = val1.compareTo(val2) ;
      boolean match = false;
        switch(operator){
            case "=" :
                if(temp == 0)
                    match = true;
                break;
            case ">=":
                if(temp > 0 || temp == 0) 
                    match = true;
                break;
            case "<=":
                if(temp < 0 || temp == 0)
                    match = true;
                break;
            case ">":
                if(temp > 0)
                    match = true;
                break;
            case "<":
                if(temp < 0)
                    match = true;
                break;
            default:
                if(val1.contains(val2))
                    match = true;
                break;
        }      
     return match;
  }
   
   public boolean matchValBetween(int val1, int val2, int val3){
       boolean match = false;
       int mayor;
       int minor;
       
       if(val1 > val2){
           mayor = val1;
           minor = val2;
       }
       else{
           mayor = val2;
           minor = val1;
       }
       
       if(val3<= mayor && val3>=minor)
           match = true;
       
       return match;
   }
   
      public boolean matchValBetween(double val1, double val2, double val3){
       boolean match = false;
       double mayor;
       double minor;
       
       if(val1 > val2){
           mayor = val1;
           minor = val2;
       }
       else{
           mayor = val2;
           minor = val1;
       }
       
       if(val3<= mayor && val3>=minor)
           match = true;
       
       return match;
   }
   
    public boolean matchValBetween(char val1, char val2, char val3){
       boolean match = false;
       char mayor;
       char minor;
       
       if(val1 > val2){
           mayor = val1;
           minor = val2;
       }
       else{
           mayor = val2;
           minor = val1;
       }
       
       if(val3<= mayor && val3>=minor)
           match = true;
       
       return match;
   }
    
    public boolean matchValBetween(String val1, String val2, String val3){
       boolean match = false;
       String mayor;
       String minor;
       
       if(val1.compareTo(val2) > 0){
           mayor = val1;
           minor = val2;
       }
       else{
           mayor = val2;
           minor = val1;
       }
       int superior = val3.compareTo(mayor);
       int inferior = val3.compareTo(minor);
       
       if((superior < 0 || superior == 0) && (inferior > 0 || inferior == 0))
           match = true;
       
       return match;
   }
}