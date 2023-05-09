
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    final private Font mainFont = new Font("Arial", Font.PLAIN, 18);
    JTextField textFieldlabelWoerterHinzufuegen, textFieldlabelWoerterEntfernen, textFieldLabelMonatJahr, textFeldLabelBetrag;
    JLabel labelBetrag, anzeigeFilterLabel;
    JTextArea anzeigeFilter = new JTextArea();
    

  public void initialize(OurFlatUebersetzung objekt) {

    //Erstellung des Welcome Labels
    //Ausgabefeld als Button erstellt um späteres rauskopieren des Betrags zu ermöglichen
    labelBetrag = new JLabel("Anzeige für den Betrag",0);
    labelBetrag.setFont(mainFont);
    JButton textFeldLabelBetrag = new JButton();
    textFeldLabelBetrag.setText(Float.toString(objekt.betrag));


    //erstellung des FormPanels für die spaetere Anzeige                                                                                        *****FirstSecondlabelMonatJahr
    JLabel labelWoerterHinzufuegen = new JLabel("Dieses Wort zum Filter hinzufuegen: ",0);
    labelWoerterHinzufuegen.setFont(mainFont);

    textFieldlabelWoerterHinzufuegen = new JTextField();
    textFieldlabelWoerterHinzufuegen.setFont(mainFont);

    JLabel labelWoerterEntfernen = new JLabel("Dieses Wort aus dem Filter entfernen: ",0);
    labelWoerterEntfernen.setFont(mainFont);

    textFieldlabelWoerterEntfernen = new JTextField();
    textFieldlabelWoerterEntfernen.setFont(mainFont);

    JLabel labelMonatJahr = new JLabel("zu filternder Monat / Jahr",0);
    labelMonatJahr.setFont(mainFont);

    textFieldLabelMonatJahr = new JTextField();
    textFieldLabelMonatJahr.setFont(mainFont);



    //Erstellen eines anzeigeFilters für die Anzeige der Inhalte des Filters                                                                    ****** anzeigeFilter
    anzeigeFilter.setText("Hier wird der Filter angezeigt");
    anzeigeFilter.setOpaque(true);
    anzeigeFilter.setFont(mainFont);

    anzeigeFilterLabel = new JLabel("", 0);
    anzeigeFilterLabel.setFont(mainFont);

    JPanel anzeigeFilterPanel = new JPanel();
    anzeigeFilterPanel.setLayout(new GridLayout(2,1,50,10));
    anzeigeFilterPanel.setOpaque(false);
    anzeigeFilter.setSize(250,800);
    anzeigeFilterPanel.add(anzeigeFilter);
    

    //Erstellen eines Feldes zum aufrufen einer datei und start knopf fuer das programm                                                         *****File Chooser
    JFileChooser chooser = new JFileChooser();
    JLabel temp = new JLabel();
    chooser.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
            String dateiPfad = chooser.getSelectedFile().getPath().toString();
            temp.setText(dateiPfad);
        }
    });

    //Startbutton, der den dateipfad uebergibt und dann das Programm startet                                                                    *****Start Button
    JButton startButton = new JButton("Start");                                                                                            //ToDo: Anpassen der Groeße der Buttons
    startButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            textFeldLabelBetrag.setText("0.0");
            objekt.dateipfadLesen(temp.getText());
            textFeldLabelBetrag.setText(Float.toString(objekt.betrag));
            anzeigeFilter.setText(objekt.anzeigeFilterAktualisieren());
        }
    });

    anzeigeFilterPanel.add(startButton);
    //Button zum setzen des Monats                                                                                                              ***** Monat Button
    JButton monatButton = new JButton("Monat setzen");
    monatButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            String monatButtonText = textFieldLabelMonatJahr.getText();
            objekt.setzeMonat(monatButtonText);
            textFieldLabelMonatJahr.setText("");
        }
    });

    //Button zum setzten des Jahres                                                                                                             ***** Jahr Button
    JButton jahrButton = new JButton("Jahr setzen");
    jahrButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            String jahrButtonText = textFieldLabelMonatJahr.getText();
            objekt.setzeJahr(jahrButtonText);
            textFieldLabelMonatJahr.setText("");
        }
    });


    //Panel für die file aussuche und den start button
    JPanel fileChooserPanel = new JPanel();
    fileChooserPanel.setLayout(new GridLayout(3,1,5,5));
    fileChooserPanel.setOpaque(false);
    fileChooserPanel.add(chooser);
    //fileChooserPanel.add(startButton);
    fileChooserPanel.add(monatButton);
    fileChooserPanel.add(jahrButton);


    //Erstellen des Panels zur Anzeige der oberen Textfields und Labels                                                                         *****Form Panel
    JPanel formPanel = new JPanel();
    //Tabelle mit 4 Zeilen, einer Reihe und danach folgen die border zwischen den Komponenten
    formPanel.setLayout(new GridLayout(9,1,20,20));
    //transparent machen des FormPanels
    formPanel.setOpaque(false);
    formPanel.add(labelBetrag);
    formPanel.add(textFeldLabelBetrag);
    formPanel.add(labelWoerterHinzufuegen);
    formPanel.add(textFieldlabelWoerterHinzufuegen);
    formPanel.add(labelWoerterEntfernen);
    formPanel.add(textFieldlabelWoerterEntfernen);
    formPanel.add(labelMonatJahr);
    formPanel.add(textFieldLabelMonatJahr);



    //Erstellung eines Button Panels für die Einrichtung in dem Main Panel                                                                      *****Hinzufuegen Button
    //OK button
    JButton buttonHinzufuegen = new JButton("Hinzufügen");
    buttonHinzufuegen.setFont(mainFont);
    buttonHinzufuegen.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            //throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
            String firstTextFieldText = textFieldlabelWoerterHinzufuegen.getText();
            objekt.fuegeStringHinzu(firstTextFieldText);
            textFieldlabelWoerterHinzufuegen.setText("");
            String tempString = passeTextAn(objekt);
            anzeigeFilter.setText(tempString);
            
        }
    });

    //Abbruch button                                                                                                                                *****Lösch Button
    JButton buttonEntfernen = new JButton("Entfernen");
    buttonEntfernen.setFont(mainFont);
    buttonEntfernen.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            String secondTextFieldText = textFieldlabelWoerterEntfernen.getText();
            objekt.entferneStringAusSet(secondTextFieldText);
            textFieldlabelWoerterEntfernen.setText("");
            anzeigeFilter.setText(passeTextAn(objekt));
        }
    });

    JPanel buttonsPanel = new JPanel();
    buttonsPanel.setLayout(new GridLayout(1,2,5,5));
    //transparenz einschalten
    buttonsPanel.setOpaque(false);
    buttonsPanel.add(buttonHinzufuegen);
    buttonsPanel.add(buttonEntfernen);



    //Main anzeige                                                                                                                                  *****Main Anzeige
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainPanel.setBackground(new Color(128, 128, 255));
    //erzeugung eines Margin zu allein seiten
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    //Hinzufuegen des formPanels zum MainPanel im Norden des Fensters (unterteilt in Norden, osten, süden, westen und Mitte/center)
    formPanel.setBorder(BorderFactory.createEmptyBorder(5,50,5,50));
    mainPanel.add(formPanel, BorderLayout.CENTER);

    //Hinzufuegen des ButtonPanels
    buttonsPanel.setBorder(BorderFactory.createEmptyBorder(5,50,5,50));
    mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

    //Hinzufuegen des Panels anzeigeFilter
    anzeigeFilterPanel.setBorder(BorderFactory.createEmptyBorder(5,50,5,50));
    mainPanel.add(anzeigeFilterPanel, BorderLayout.WEST);
 
    //Hinzufuegen des File Panel
    fileChooserPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    mainPanel.add(fileChooserPanel, BorderLayout.EAST);

    //Hinzufuegen des Mainpanels zu JFrame, nötig fuer anzeige
    add(mainPanel);


    setTitle("OurFlat Übersetzung");
    setSize(1400,790);
    setMinimumSize(new Dimension(650, 400));
    setLocationRelativeTo(null);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setVisible(true);
  }


  //Methode um den Text des anzeigeFilter anzupassen
public String passeTextAn(OurFlatUebersetzung objekt){
    String temp = objekt.anzeigeFilterAktualisieren();
    return temp;
    }
}

