
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    final private Font mainFont = new Font("Arial", Font.PLAIN, 18);
    JTextField textFieldlabelWoerterHinzufuegen, textFieldlabelWoerterEntfernen, textFieldLabelMonatJahr, textFeldLabelBetrag;
    JLabel labelBetrag, anzeigeFilterLabel;
    JTextArea anzeigeFilter = new JTextArea();
    JButton startButton;
    Dimension buttonSize = new Dimension(100, 100);


  public void initialize(OurFlatUebersetzung objekt) {
    
    //Erstellung des Welcome Labels
    //Ausgabefeld als Button erstellt um späteres rauskopieren des Betrags zu ermöglichen -> noch nicht implementiert
    labelBetrag = new JLabel("Anzeige für den Betrag",0);
    labelBetrag.setFont(mainFont);
    JButton textFeldLabelBetrag = new JButton();
    textFeldLabelBetrag.setText(Float.toString(objekt.betrag));


    //Erstellung des FormPanels für die spaetere Anzeige                                                                                        *****First / Second / Monat / Jahr Labels
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
    anzeigeFilter.setText(objekt.getFilterText());
    //setOpaque true verhindert die Durchsichtigkeit des Elements
    anzeigeFilter.setOpaque(true);
    anzeigeFilter.setFont(mainFont);

    anzeigeFilterLabel = new JLabel("Zu filternde Kosten:", 0);
    anzeigeFilterLabel.setFont(mainFont);

    JPanel anzeigeFilterPanel = new JPanel();
    anzeigeFilterPanel.setLayout(new GridLayout(1,1,50,10));
    anzeigeFilterPanel.setOpaque(false);
    anzeigeFilterPanel.add(anzeigeFilter);
    

    //Erstellen eines Feldes zum aufrufen einer datei und start knopf fuer das programm                                                         *****File Chooser
    JFileChooser chooser = new JFileChooser();
    JLabel temp = new JLabel();
    chooser.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
            String dateiPfad = chooser.getSelectedFile().getPath().toString();
            temp.setText(dateiPfad);
            objekt.dateipfadLesen(temp.getText());
            enableStartButton(objekt.ueberpruefeVoraussetzungen());
        }
    });

    //Startbutton, der den dateipfad uebergibt und dann das Programm startet                                                                    *****Start Button
    startButton = new JButton("Start"); 
    startButton.setEnabled(false);                                                                                        //ToDo: Anpassen der Groeße der Buttons
    startButton.setFont(mainFont);
    startButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            textFeldLabelBetrag.setText("0.0");
            textFeldLabelBetrag.setText(Float.toString(objekt.betrag));
            anzeigeFilter.setText(objekt.getFilterText());
        }
    });

    //Button zum setzen des Monats                                                                                                              ***** Monat Button
    JButton monatButton = new JButton("Monat setzen");
    monatButton.setFont(mainFont);
    monatButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            String monatButtonText = textFieldLabelMonatJahr.getText();
            objekt.setzeMonat(monatButtonText);
            enableStartButton(objekt.ueberpruefeVoraussetzungen());
            textFieldLabelMonatJahr.setText("");
        }
    });

    //Button zum setzen des Jahres                                                                                                             ***** Jahr Button
    JButton jahrButton = new JButton("Jahr setzen");
    jahrButton.setFont(mainFont);
    jahrButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            String jahrButtonText = textFieldLabelMonatJahr.getText();
            objekt.setzeJahr(jahrButtonText);
            enableStartButton(objekt.ueberpruefeVoraussetzungen());
            textFieldLabelMonatJahr.setText("");
        }
    });


    //Panel für die Wahl der Datei
    JPanel fileChooserPanel = new JPanel();
    fileChooserPanel.setSize(900,600);
    fileChooserPanel.setLayout(new GridLayout(1,1,5,5));
    fileChooserPanel.setOpaque(false);
    fileChooserPanel.add(chooser);


    //Erstellen des Panels zur Anzeige der oberen Textfields und Labels                                                                         *****Form Panel
    JPanel formPanel = new JPanel();
    //Tabelle mit 4 Zeilen, einer Reihe und danach folgen die border zwischen den Komponenten
    formPanel.setLayout(new GridLayout(9,1,20,20));
    //transparent machen des FormPanels
    formPanel.setOpaque(false);
    //Hinzufuegen der einzelnen Bestandteile
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
    JButton buttonHinzufuegen = new JButton("Wort hinzufügen");
    buttonHinzufuegen.setFont(mainFont);
    buttonHinzufuegen.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            //throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
            String firstTextFieldText = textFieldlabelWoerterHinzufuegen.getText();
            objekt.fuegeStringHinzu(firstTextFieldText);
            textFieldlabelWoerterHinzufuegen.setText("");
            String tempString = objekt.getFilterText();
            anzeigeFilter.setText(tempString);
            
        }
    });

    //Abbruch button                                                                                                                                *****Lösch Button
    JButton buttonEntfernen = new JButton("Wort entfernen");
    buttonEntfernen.setFont(mainFont);
    buttonEntfernen.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            String secondTextFieldText = textFieldlabelWoerterEntfernen.getText();
            objekt.entferneStringAusSet(secondTextFieldText);
            textFieldlabelWoerterEntfernen.setText("");
            anzeigeFilter.setText(objekt.getFilterText());
        }
    });

    JPanel buttonsPanel = new JPanel();
    buttonsPanel.setLayout(new GridLayout(1,2,5,5));
    //transparenz einschalten
    buttonsPanel.setOpaque(false);
    buttonsPanel.add(buttonHinzufuegen);
    buttonsPanel.add(buttonEntfernen);
    buttonsPanel.add(monatButton);
    buttonsPanel.add(jahrButton);
    buttonsPanel.add(startButton);

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
 
    //Hinzufuegen des File Panel (File chooser und monatButton / jahrButton)
    fileChooserPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    mainPanel.add(fileChooserPanel, BorderLayout.EAST);

    //Hinzufuegen des Mainpanels zu JFrame, nötig fuer anzeige
    add(mainPanel);

    //Eigenschaften des Fensters, nicht verkleinerbar, da sonst die Anordnung zerstoert wird
    setTitle("OurFlat Übersetzung");
    setSize(1400,790);
    setMinimumSize(new Dimension(1400, 790));
    setLocationRelativeTo(null);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setVisible(true);
  }


  public void enableStartButton(boolean sindErfuellt){
    if(sindErfuellt){
        startButton.setEnabled(true);
    }
  }
}

