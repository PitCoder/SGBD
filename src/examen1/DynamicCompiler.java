package examen1;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Locale;
 
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
 
public class DynamicCompiler {    
    private static String classOutputFolder = "build/classes/";
    
    public static class MyDiagnosticListener implements DiagnosticListener<JavaFileObject> {
        public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
            System.out.println("Line Number->" + diagnostic.getLineNumber());
            System.out.println("code->" + diagnostic.getCode());
            System.out.println("Message->"
                               + diagnostic.getMessage(Locale.ENGLISH));
            System.out.println("Source->" + diagnostic.getSource());
            System.out.println(" ");
        }
    }
     
    public static class InMemoryJavaFileObject extends SimpleJavaFileObject {
        private String contents = null;
 
        public InMemoryJavaFileObject(String className, String contents) throws Exception {
            super(URI.create("string:///" + className.replace('.', '/')
                             + Kind.SOURCE.extension), Kind.SOURCE);
            this.contents = contents;
        }
 
        public CharSequence getCharContent(boolean ignoreEncodingErrors)
                throws IOException {
            return contents;
        }
    }
     
    private static JavaFileObject getJavaFileObject(String className, StringBuilder code) {
        StringBuilder contents = code;
        JavaFileObject jfo = null;
        
        try {
            jfo = new InMemoryJavaFileObject(className, contents.toString());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        
        return jfo;
    }
     
    public static void compile(Iterable<? extends JavaFileObject> files) {
        //get system compiler:
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
 
        // for compilation diagnostic message processing on compilation WARNING/ERROR
        MyDiagnosticListener c = new MyDiagnosticListener();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(c,
                                                                              Locale.ENGLISH,
                                                                              null);
        //specify classes output folder
        Iterable options = Arrays.asList("-d", classOutputFolder);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager,
                                                             c, options, null,
                                                             files);
        Boolean result = task.call();
        if (result == true) {
            System.out.println("Succeeded");
        }
    }
     
    public Object getInstance(String className) {                
        Object tabla = null;        
        
        try {                                        
          Class<?> tClass = Class.forName("examen1." + className); // convert string classname to class          
          tabla = tClass.newInstance(); // invoke empty constructor          
        } catch (ClassNotFoundException e) { e.printStackTrace();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
        
        return tabla;
    }
 
    public void compileClass(String className, StringBuilder code) {
      JavaFileObject file = getJavaFileObject(className, code);
      Iterable<? extends JavaFileObject> files = Arrays.asList(file);   
      compile(files);
    }    
}
