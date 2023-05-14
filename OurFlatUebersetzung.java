import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class OurFlatUebersetzung {
    Set<String> filter = new TreeSet<String>();
    Stack<String> stack = new Stack<String>();
    Stack<String> stackZumMonatFiltern = new Stack<String>();
    Set<String> tage = new TreeSet<String>();

    private String monat = "";
    private String jahr = "";
    public float betrag = 0;
    String dateiPfad = "";
    String ausgabeTextAreal = "";


    //Der Scanner wird nicht geschlossen, da er spaeter erneut verwendet wird, Verwendung mehrerer Scanner gleichzeitig fuehrt zu einer Exception
    //Scanner wird nur verwendet, wenn die Oberflaeche nicht verwendet wird -> liest die Eingabe des Dateipfads sowie monat/jahr etc aus der Konsole aus
    Scanner scanner = new Scanner(System.in);

    //definiert direkt die Filter filter und tage
    public OurFlatUebersetzung() {
        createWeekdaysFilter();
        createBaseFilters();
    }

    //                                                                                                                                                       *****Methoden fuer die Oberflaeche
    /**
    *Entfernt einen String aus dem Set
    *@param stringToDel der zu entfernende String
    *@return Meldung fuer Erfolg / Misserfolg
    */
    public String entferneStringAusSet(String stringToDel){
        if(filter.contains(stringToDel)){
            filter.remove(stringToDel);
            return stringToDel + " wurde erfolgreich entfernt.";
        }
        else{
            return "Zu entfernender String nicht im Filter enthalten.";
        }
    }

    /**
     * Fügt einen String zum Filter hinzu
     * @param stringToAdd der zu hinzufügende String
     */
    public void fuegeStringHinzu(String stringToAdd){
        filter.add(stringToAdd);
    }

    /**
     * Liefert den Text für den Filter
     * @return
     */
    public String getFilterText(){
        ausgabeTextAreal = "";
        for(String string: filter) {
			ausgabeTextAreal = ausgabeTextAreal + "\n" + string;
		}
        return ausgabeTextAreal;
    }

    /**
     * Liest den Dateipfad ein
     * @param pfad Der Dateinpfad
     */
    public void dateipfadLesen(String pfad){
        dateiPfad = pfad;
        System.out.println(dateiPfad);
    }

    /**
     * Liefert den Endbetrag
     * @return Der Enbetrag
     */
    public String getEndbetrag(){
        return Float.toString(betrag);
    }

    /**
     * Setz den Monat
     * @param monat Der Monat
     */
    public void setzeMonat(String monat){
        this.monat = monat;
    }

    /**
     * Setzt das Jahr
     * @param jahr Das Jahr
     */
    public void setzeJahr(String jahr){
        this.jahr = jahr;
    }

    /**
     * Überprüft ob Jahr, Monat und dateiPfad eingegeben wurden
     * @return boolean 
     */
    public boolean ueberpruefeVoraussetzungen(){
        if(!jahr.equals("") && !monat.equals("") && !dateiPfad.equals("")){
            return true;
        }
        else{
            return false;
        }
    }

    //                                                                                                                                          ****** Ende Methoden Oberflaeche */

    /**
     * Berechnet und liefert die Ausgaben
     *
     * @return Die Ausgaben
     */
    public float gebeAusgabenzuerueck(String dateiPfad){
        betrag = 0;
        String s =  dateiPfad;
        s = dateiAuslesen(s);
        s = entferneSonderzeichen(s);
        dateiZuListe(s);
        stackAufMonatUeberpreufen();
        stackAufBetraegePruefen();
        auslesenDerBetraege();
        return betrag;
    }

    /**
     *  ueber ein eingabe feld laesst sich der pfad angeben, dieser wird angesteuert und die vorhandene Datei dann eingelesen und als string gespeichert
     * gibt die eingelesene Datei als string zueruck, wird nur(!) verwendet, wenn die Oberflaeche nicht verwendet wird -> Testzwecke
     * @return Pfad zur datei
     */
    public String leseOurFlatDatei(){
		String eingabe = scanner.nextLine();
		String pfadOurFlatDatei = eingabe;
        return pfadOurFlatDatei;
    }

    /**
     * Liest die Datei aus
     *
     * @param pfadOurFlatDatei
     *
     * @return Die Datei
     */
    public String dateiAuslesen(String pfadOurFlatDatei){
        String gesamtString = "";
        try{
            BufferedReader b1 = new BufferedReader(
                new InputStreamReader(
                new FileInputStream(
                new File(pfadOurFlatDatei))));
            for(String s1 = b1.readLine(); s1 != null; s1 = b1.readLine()){
                //line prüfen auf Limiter (=) dann weitergeben an andere Funktionen, ==== nicht zu String 
                //dann reseten, und Reader geht automatisch auf nächste Zeile -> ToDo
                gesamtString = gesamtString + s1;
            }
            b1.close();
        }
        catch(IOException ex){
            System.out.println(ex.getStackTrace());
        }   
        return gesamtString;
    }
    
    /**
     * Initialisiert den Basisfilter (Basiskosten)
     */
    public void createBaseFilters() {
        for (String baseFilter: StandardFilters.BASE_FILTERS){
            filter.add(baseFilter);
        }
    }
    
    /**
     * Initialisiert den Wochentagsfilter
     */
    public void createWeekdaysFilter() {
        for(String weekday: StandardFilters.WEEKDAYS){
            tage.add(weekday);
        }
    }
    
    
    /**
     * entfernt gewoehnliche Sonderzeichen sowie redundante Formulierungen
     * ersetzt außerdem die Kommas in Bruchbetraegen durch Punkte damit diese spaeter als Float verarbeitet werden koennen
     * @param s der String
     * @return Der String mit entfernten Zeichen
     */
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

    /**
     * Fuegt die einzelnen Betraege einem Stack hinzu, in dem diese durch die Slashs getrennt und hinzugefuegt werden
     *
     * @param s Die datei
     * @return Der Stack
     */
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

    /**
     * Prueft die einzeln Eintraege des Stacks auf den gewuenschten Monat und fuegt diese dem Stack stackZumMonatFilter dann hinzu
     */
    public void stackAufMonatUeberpreufen(){
        boolean bereitsEntfernt = false;
        String aktuellerString = "";
        String stringZumPruefen = "";
        while(!stack.empty()){
            bereitsEntfernt = false;
            aktuellerString = stack.peek();
            for(int charIndex = 0; charIndex < aktuellerString.length(); charIndex++){
                if(aktuellerString.charAt(charIndex) == monat.charAt(0)){
                    int itemp = charIndex;
                    stringZumPruefen = "";
                    while(aktuellerString.charAt(itemp) != ' '){
                        stringZumPruefen = stringZumPruefen + Character.toString(aktuellerString.charAt(itemp));
                        itemp++;
                        if(stringZumPruefen.equals(monat)){
                            stackZumMonatFiltern.push(aktuellerString);
                            stack.pop();
                            bereitsEntfernt = true;
                            break;
                        }
                        if(aktuellerString.charAt(charIndex) == ' '){
                            break;
                        }
                    }
                    if(bereitsEntfernt){
                        break;
                    }
                }
            }
            if(!bereitsEntfernt){
                stack.pop();
            }
        }
    }

    /**
     * Die Inhalte des Stacks stackzumMonatFilter werden auf die ungewuenschten Betraege aus dem Set "filter" geprueft, Wiederverwendung des Stacks "stack" um die geprueften zu speichern
     * Die einzelnen Woerter zum Filtern werden ggf. durch Leerzeichen getrennt, deshalb wird auf das vorkommen der Wochentage nach einem Leerzeichen geprueft
     */
    public void stackAufBetraegePruefen(){
        String aktuellerString = "";
        String leerzeichenPruefen = "";
        String stringZumPruefen = "";
        boolean leerzeichenGefunden = false;
        //Schleife zum Durchlaufen des Stacks
        while(!stackZumMonatFiltern.empty()){
            aktuellerString = stackZumMonatFiltern.peek();
            //aktualisieren des Strings
            for(int charIndex = 1; charIndex < aktuellerString.length(); charIndex++){
                int itemp = 0;
                leerzeichenGefunden = false;
                leerzeichenPruefen = "";
                stringZumPruefen = stringZumPruefen + Character.toString(aktuellerString.charAt(charIndex));
                //zu pruefender String wurde definiert, sobald der naechste char ein " " ist beginnt die Ueberpruefueng auf die Wochentage
                if(Character.toString(aktuellerString.charAt(charIndex)).equals(" ")){
                    for(int i3 = charIndex + 1; itemp < 4; i3++){
                        leerzeichenPruefen = leerzeichenPruefen + Character.toString(aktuellerString.charAt(i3));
                        if(tage.contains(leerzeichenPruefen)){
                            leerzeichenGefunden = true;
                            stringZumPruefen = stringZumPruefen.substring(0, (charIndex - 1));
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

    /**
     * ungern gewaehlte zweiteilige Funktion, soll die einzelnen Element des Stacks auslesen und dann durch das Jahr den Betrag abziehen                         *****Teilen in zwei Funktionen
     * Das auslesen des Betrags sollte durch das erreichen des Jahres erreicht werden, da dieses vor dem Betrag steht                                            *****ToDo
     * Deshalb wird gleichzeitig nach dem jeweilig gewuenschten Jahr gefiltert - Aenderung in zwei Funktionen!
     * @return
     */
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

    /**
     * Methode, die dem Zwischenspeicher den Betrag aus dem JLabel anzeigeBetrag hinzufügt
     * @param betrag, der JButton zur Anzeige des Betrags der Oberfläche 
     */
    public void kopiereBetragInZwischenspeicher(JButton betrag){
        Clipboard systemClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        systemClip.setContents(new StringSelection(betrag.getText()), null);

    }
}
