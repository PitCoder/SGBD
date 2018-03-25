package examen1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
  public static void main(String[] args) throws ClassNotFoundException {
    Scanner entrada = new Scanner(System.in);
    System.out.println("Escriba la direccion del servidor: ");
    String dir = entrada.nextLine();    
    
    try {
      Socket cl = new Socket(dir, 9000);
      System.out.println("Me conecté");
      for (;;) {
        System.out.print("> ");
        String query = entrada.nextLine();
        ObjectOutputStream oos  = new ObjectOutputStream(cl.getOutputStream());
        oos.writeObject(query); 
        
        /* Se recibe resultado */
        ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
        String res            = (String) ois.readObject();
        System.out.println(res);
      }
      
    } catch (IOException ex) { ex.printStackTrace();
    }
  } 
}

