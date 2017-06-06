
package dsdv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aistė Mackevičiūtė
 */
public class Main {

    /**
     *
     * @param args
     */
    public static void main(String[] args){
        try {
            DSDV dsdv = new DSDV();
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String s, s1;
            int i = 0;
            dsdv.addNode("A1");
            dsdv.addNode("B1");
            dsdv.addNode("C1");
            dsdv.addNode("D1");
            dsdv.addNode("E1");
            dsdv.addNode("F1");
            dsdv.addNode("F2");
            dsdv.addNode("D2");
            dsdv.addNode("A2");
            dsdv.addNode("A3");
            dsdv.addNeighbours("A1", "B1", 3);
            dsdv.addNeighbours("A1", "C1", 1);
            dsdv.addNeighbours("A1", "E1", 1);
            dsdv.addNeighbours("B1", "D1", 2);
            dsdv.addNeighbours("C1", "E1", 2);
            dsdv.addNeighbours("E1", "B1", 5);
            dsdv.addNeighbours("A3", "A2", 1);
            dsdv.addNeighbours("A3", "A1", 3);
            dsdv.addNeighbours("A1", "A2", 3);
            dsdv.addNeighbours("B1", "A2", 4);
            dsdv.addNeighbours("D1", "D2", 3);
            dsdv.addNeighbours("D1", "E1", 5);
            dsdv.addNeighbours("E1", "F1", 2);
            dsdv.addNeighbours("F1", "F2", 3);
            dsdv.addNeighbours("D1", "F1", 1);
            dsdv.print();
            while(true){
                
                System.out.println("_________________________");
                System.out.println("Pasirinkite: ");
                System.out.println("1. Pridėti mazgą. ");
                System.out.println("2. Sujungti mazgus. ");
                System.out.println("3. Siusti paketą. ");
                System.out.println("4. Pašalinti jungtį. ");
                System.out.println("5. Pašalinti mazgą. ");
                System.out.println("6. Atspausdinti mazgų lenteles. ");
                System.out.println("7. Baigti programą. ");
                System.out.println("__________________________");
                
                
                try {
                    // s = br.readLine();
                    i = Integer.parseInt(br.readLine());
                    
                    
                    switch(i){
                        case 1 :
                            System.out.println("Įveskite mazgo pavadinimą");
                            s = br.readLine();
                            checkStirng(s);
                            if(dsdv.addNode(s)){
                                System.out.println("Mazgas sekmingai pridetas");
                            }else{
                                System.out.println("Toks mazgas jau egzistuoja.");
                            }
                            break;
                        case 2 :
                            System.out.println("Įveskite pirmo mazgo pavadinimą");
                            s = br.readLine();
                            checkStirng(s);
                            System.out.println("Įveskite antro mazgo pavadinimą");
                            s1 = br.readLine();
                            checkStirng(s1);
                            System.out.println("Veskite kelio kainą");
                            i = Integer.parseInt(br.readLine());
                            if(dsdv.addNeighbours(s1, s, i)){
                                System.out.println("Mazgai sekmingai sujungti");
                            }else{
                                System.out.println("Tokio mazgo nera.");
                            }
                            break;
                        case 3 :
                            System.out.println("Įveskite pradzios mazgo pavadinimą");
                            s = br.readLine();
                            checkStirng(s);
                            System.out.println("Įveskite pabaigos mazgo pavadinimą");
                            s1 = br.readLine();
                            checkStirng(s1);
                            if(dsdv.sendpackets(s, s1)){
                                System.out.println("Paketas nusiustas");
                            }else{
                                System.out.println("Kelio nera.");
                            }
                            break;
                        case 4 :
                            System.out.println("Įveskite pirmo mazgo pavadinimą");
                            s = br.readLine();
                            checkStirng(s);
                            System.out.println("Įveskite antro mazgo pavadinimą");
                            s1 = br.readLine();
                            checkStirng(s1);
                            if(dsdv.removeLink(s, s1)){
                                System.out.println("Jungtis sekmingai pasalintas");
                            }else{
                                System.out.println("Sie mazgai nera kaimynai.");
                            }
                            break;
                        case 5 :
                            System.out.println("Įveskite mazgo pavadinimą");
                            s = br.readLine();
                            checkStirng(s);
                            if(dsdv.removeLinks(s)){
                                System.out.println("Mazgas sekmingai pasalintas");
                            }else{
                                System.out.println("Tokio mazgo nera.");
                            }
                            break;
                        case 6 :
                            dsdv.print();
                            break;
                        case 7 :
                            System.exit(0);
                    }
                    
            
                } catch (IOException ex) {
                    // Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Klaidindi duomenys");
                } catch (InterruptedException ex) {
                    // Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NumberFormatException ex) {
                    System.out.println("Klaidingi duomenys");
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                
                
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }

   private static void checkStirng(String s) throws Exception{
       String pattern= "^[a-zA-Z0-9]*$";
   
       if(!s.matches(pattern)){
          throw new Exception("Klaidingi duomenys");
        } 
   }
}
