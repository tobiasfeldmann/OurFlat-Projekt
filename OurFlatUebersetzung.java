import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class OurFlatUebersetzung {
    Set<String> filter = new TreeSet<String>();
    Stack<String> stack = new Stack<String>();
    Stack<String> stackZumMonatFiltern = new Stack<String>();
    Set<String> tage = new TreeSet<String>();

    MainFrame mainframe1 = new MainFrame();

    private String monat = "";
    private String jahr = "";
    public float betrag = 0;
    String dateiPfad = "";
    String ausgabeTextAreal = "";


    //Der Scanner wird nicht geschlossen, da er spaeter erneut verwendet wird, Verwendung mehrerer Scanner gleichzeitig fuehrt zu einer Exception
    Scanner scanner = new Scanner(System.in);


    //Methoden fuer die Oberflache**********************************************************************
    //Methode um einen String aus dem Set zu entfernen
    public String entferneStringAusSet(String stringToDel){
        if(filter.contains(stringToDel)){
            filter.remove(stringToDel);
            return stringToDel + " wurde erfolgreich entfernt.";
        }
        else{
            return "Zu entfernender String nicht im Filter enthalten.";
        }
    }

    //Methode um String aus der Oberflache zum filter hinzuzufuegen
    public String fuegeStringHinzu(String stringToAdd){
        filter.add(stringToAdd);
        filter.add("Nebenkosten");
        filter.add("Vorzahlung Essen");
        filter.add("Vorzahlung");
        filter.add("Internet");
        filter.add("Ausgleich");
        filter.add("Abtrag");
        filter.add("Rundfunk");
        filter.add("Miete");
        return stringToAdd + " wurde erfolgreich hinzugefuegt";
    }

    //Methode um den text des Textareals zu aktualisieren
    public String textArealAktualisieren(){
        ausgabeTextAreal = "";
        for(String string: filter) {
			ausgabeTextAreal = ausgabeTextAreal + "\n" + string;
		}
        return ausgabeTextAreal;
    }

    //methode um den pfad einzugeben
    public void dateipfadLesen(String pfad){
        dateiPfad = pfad;
        gebeAusgabenzuerueck();
    }

    //Methode um den Endbetrag zur Ausgabe zu kriegen
    public String getEndbetrag(){
        return Float.toString(betrag);
    }

    //Methoden zum aufrufen um Monat und Jahr zu setzen
    public void setzeMonat(String monat){
        this.monat = monat;
    }
    public void setzeJahr(String jahr){
        this.jahr = jahr;
    }

    //*************************************************************************************************************************** Ende Methoden Oberflaeche */

    //methode die von ausserhalb aufgerufen wird und die noetigen Methoden aufruft um die einzelnen Schritte abzuarbeiten
    public float gebeAusgabenzuerueck(){
        betrag = 0;
        String s =  "";
        if(dateiPfad.equals("")){
            s = leseOurFlatDatei();
        }
        else{
            s = dateiPfad;
        }
        s = dateiAuslesen(s);
        zuFilterndeKosten();
        s = entferneSonderzeichen(s);
        dateiZuListe(s);
        stackAufMonatUeberpreufen();
        stackAufBetraegePruefen();
        auslesenDerBetraege();
        return betrag;
    }

    //ueber ein eingabe feld laesst sich der pfad angeben, dieser wird angesteuert und die vorhandene Datei dann eingelesen und als string gespeichert
    //gibt die eingelesene Datei als string zueruck, wird nur verwendet, wenn die Oberflaeche nicht verwendet wird
    public String leseOurFlatDatei(){
		String eingabe = scanner.nextLine();
		String pfadOurFlatDatei = eingabe;
        return pfadOurFlatDatei;
    }

    //Methode zum Auslesen der Textdatei von OurFlat
    public String dateiAuslesen(String pfadOurFlatDatei){
        String gesamtString = "";
        try{
            BufferedReader b1 = new BufferedReader(
                new InputStreamReader(
                new FileInputStream(
                new File(pfadOurFlatDatei))));
            for(String s1 = b1.readLine(); s1 != null; s1 = b1.readLine()){
                gesamtString = gesamtString + s1;
            }
            b1.close();
        }
        catch(IOException ex){
            System.out.println(ex.getStackTrace());
        }   
        return gesamtString;
    }
    
    //fuellt das Set mit standardkosten die rausgefiltert werden sollen, sowie das Set zum filtern der Tage
    public void zuFilterndeKosten(){ 
        filter.add("Nebenkosten");
        filter.add("Vorzahlung Essen");
        filter.add("Vorzahlung");
        filter.add("Internet");
        filter.add("Ausgleich");
        filter.add("Abtrag");
        filter.add("Rundfunk");
        filter.add("Miete");
        //Hinzuefuegen der Tage zum Set tage
        tage.add("Mo.");
        tage.add("Di.");
        tage.add("Mi.");
        tage.add("Do.");
        tage.add("Fr.");
        tage.add("Sa.");
        tage.add("So.");


        //Frueher einmal abfrage nach weiteren woertern die gefiltert werden sollen, vor der GUI
        /*boolean weiter = true;
        int counter = 0;
        System.out.println("Sollen weitere Woerter gefiltert werden?");
        String weiterStringFrage = scanner.nextLine();
        if(weiterStringFrage.equals("Nein")){
            weiter = false;
        }
        while(weiter == true){   
            if(counter == 0){
                System.out.println("Welche Woerter sollen gefiltert werden?");
                String zuFiltern = scanner.nextLine();
                filter.add(zuFiltern);
                counter = 1;
            }
            if(counter == 1){
                System.out.println("Weitere Woerter filtern?");
                String weitereFilter = scanner.nextLine();
                if(weitereFilter.equals("Nein")){
                    weiter = false;
                    break;
                }
                else{
                    counter = 0;
                }
            }
        }*/
    }
    
    //entfernt gewoehnliche Sonderzeichen sowie redundante Formulierungen
    //ersetzt außerdem die Kommas in Bruchbetraegen durch Punkte damit diese spaeter als Float verarbeitet werden koennen
    public String entferneSonderzeichen(String s){
        //einmaliges entfernen der == am beginn der Datei, die ein zeichen weniger enthalten
        s = s.replaceFirst("==========", "===========");
        s = s.replace("===========", "//");
        s = s.replace("Aktueller Stand (EUR):", "");
        s = s.replace("Ã¤", "ae");
        s = s.replace("â", " ");
        s = s.replace("¬"," ");
        s = s.replace("Betrag", "");
        s = s.replace("Bezahlt von: ", "");
        s = s.replace("Luisa", "");
        s = s.replace("Tobi", "");
        s = s.replace("Erstellt von: ", "");
        s = s.replace("Geteilt mit: ", "");
        s = s.replace("Wir verwenden 'OurFlat', um unsere WG zu organisieren! Schau es dir an unter https://ourflat-app.com", "");
        s = s.replace("Feb.", "Februar");
        s = s.replace("2023","2023 ");
        s = s.replace(",",".");
        s = s.replace("Datum:","");
        s = s.replace("Apr.","April");
        return s;
    }

    //Fuegt die einzelnen Betraege einem Stack hinzu, in dem diese durch die Slashs getrennt und hinzugefuegt werden
    public Stack<String> dateiZuListe(String s){
        int counterSlash = 0;
        int startIndex = 0;
        int laenge = s.length();
        String stringToList = "";
        String aktuellerChar;
        for(int i1 = 0; i1 < laenge; i1++){
            aktuellerChar = Character.toString(s.charAt(i1));
            if(aktuellerChar.equals("/") && counterSlash <= 1){
                counterSlash++;
                startIndex = i1;
            }
            if(counterSlash == 2){
                stringToList = stringToList + Character.toString(s.charAt(i1));
            }
            if(counterSlash == 2 && aktuellerChar.equals("/") && startIndex != i1){
                counterSlash = 1;
                stack.push(stringToList);
                stringToList = "";
            }
        }
        return stack;
    }

    //Prueft die einzeln Eintraege des Stacks auf den gewuenschten Monat und fuegt diese dem Stack stackZumMonatFilter dann hinzu
    public void stackAufMonatUeberpreufen(){
        boolean bereitsEntfernt = false;
        String aktuellerString = "";
        String stringZumPruefen = "";
        for(int i2 = 0; i2 <= stack.size(); i2++){
            bereitsEntfernt = false;
            aktuellerString = stack.peek();
            for(int i3 = 0; i3 < aktuellerString.length(); i3++){
                if(Character.toString(aktuellerString.charAt(i3)).equals(Character.toString(monat.charAt(0)))){
                    int itemp = i3;
                    stringZumPruefen = "";
                    while(!Character.toString(aktuellerString.charAt(itemp)).equals(" ")){
                        stringZumPruefen = stringZumPruefen + Character.toString(aktuellerString.charAt(itemp));
                        itemp++;
                        if(stringZumPruefen.equals(monat)){
                            stackZumMonatFiltern.push(aktuellerString);
                            stack.pop();
                            bereitsEntfernt = true;
                            break;
                        }
                        if(Character.toString(aktuellerString.charAt(i3)).equals(" ")){
                            break;
                        }
                    }
                    if(bereitsEntfernt == true){
                        break;
                    }
                }
            }
            if(stack.empty()){
                break;
            }
            else{
                i2 = 0;
            }
            if(bereitsEntfernt == false){
                stack.pop();
            }
        }
    }

    //Die Inhalte des Stacks stackzumMonatFilter werden auf die ungewuenschten Betraege aus dem Set "filter" geprueft, Wiederverwendung des Stacks "stack" um die geprueften zu speichern
    //Die einzelnen Woerter zum Filtern werden ggf. durch Leerzeichen getrennt, deshalb wird auf das vorkommen der Wochentage nach einem Leerzeichen geprueft
    public void stackAufBetraegePruefen(){
        String aktuellerString = "";
        String leerzeichenPruefen = "";
        String stringZumPruefen = "";
        boolean leerzeichenGefunden = false;
        //Schleife zum Durchlaufen des Stacks
        while(!stackZumMonatFiltern.empty()){
            aktuellerString = stackZumMonatFiltern.peek();
            //aktualisieren des Strings
            for(int i2 = 1; i2 < aktuellerString.length(); i2++){
                int itemp = 0;
                leerzeichenGefunden = false;
                leerzeichenPruefen = "";
                stringZumPruefen = stringZumPruefen + Character.toString(aktuellerString.charAt(i2));
                //zu pruefender String wurde definiert, sobald der naechste char ein " " ist beginnt die Ueberpruefueng auf die Wochentage
                if(Character.toString(aktuellerString.charAt(i2)).equals(" ")){
                    for(int i3 = i2 + 1; itemp < 4; i3++){
                        leerzeichenPruefen = leerzeichenPruefen + Character.toString(aktuellerString.charAt(i3));
                        if(tage.contains(leerzeichenPruefen)){
                            leerzeichenGefunden = true;
                            stringZumPruefen = stringZumPruefen.substring(0, (i2 - 1));
                            break;
                        }
                        if(i3 == aktuellerString.length() - 1){
                            break;
                        }
                        itemp++;
                    }
                }
                if(leerzeichenGefunden){
                    break;
                }
            }
            //StringPuffer um die Moeglichkeit eines doppelten Leerzeichens zu umgehen, OurFlat schreibt manchmal bspw "Netto" oder "Netto "
            String stringPuffer = stringZumPruefen.substring(0, stringZumPruefen.length() - 1);
            if(!filter.contains(stringZumPruefen) && !filter.contains((stringPuffer))){
                stringZumPruefen = "";
                leerzeichenPruefen = "";
                stack.push(stackZumMonatFiltern.pop());

            }
            else{
                stringZumPruefen = "";
                leerzeichenPruefen = "";
                stackZumMonatFiltern.pop();
            }
        }
    }

    //ungern gewaehlte zweiteilige Funktion, soll die einzelnen Element des Stacks auslesen und dann durch das Jahr den Betrag abziehen
    //Das auslesen des Betrags sollte durch das erreichen des Jahres erreicht werden, da dieses vor dem Betrag steht
    //Deshalb wird gleichzeitig nach dem jeweilig gewuenschten Jahr gefiltert - Aenderung in zwei Funktionen!
    public float auslesenDerBetraege(){
        String aktuellerString = "";
        String stringZumPruefen = "";
        String zuErgaenzenderBetrag = "";
        while(!stack.empty()){
            aktuellerString = stack.pop();
            for(int i2 = 0; i2 < aktuellerString.length(); i2++){
                stringZumPruefen = stringZumPruefen + Character.toString(aktuellerString.charAt(i2));
                if(Character.toString(aktuellerString.charAt(i2)).equals(" ")){
                    stringZumPruefen = "";
                }
                if(stringZumPruefen.equals(jahr)){
                    for(int i3 = i2 + 4; i3 < aktuellerString.length(); i3++){
                        if(Character.toString(aktuellerString.charAt(i3)).equals(" ")){
                            break;
                        }
                        zuErgaenzenderBetrag = zuErgaenzenderBetrag + Character.toString(aktuellerString.charAt(i3));
                    }
                    betrag = betrag + Float.parseFloat(zuErgaenzenderBetrag);
                    zuErgaenzenderBetrag = "";
                }
            }
        }
        return betrag;
    }
}
