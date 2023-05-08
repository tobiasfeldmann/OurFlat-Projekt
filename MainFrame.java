
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    final private Font mainFont = new Font("Arial", Font.PLAIN, 18);
    JTextField tfFirstLabel, tfSecondLabel, tfThirdLabel, tfWelcomeLabel;
    JLabel welcomeLabel, textAreaLabel;
    JTextArea textArea = new JTextArea();
    

  public void initialize(OurFlatUebersetzung objekt) {

    //Erstellung des Welcome Labels ***************************
    //Ausgabefeld als Button erstellt um späteres rauskopieren des Betrags zu ermöglichen
    welcomeLabel = new JLabel("Anzeige für den Betrag",0);
    welcomeLabel.setFont(mainFont);
    JButton tfWelcomeLabel = new JButton();
    tfWelcomeLabel.setText(Float.toString(objekt.betrag));


    //erstellung des FormPanels für die spaetere Anzeige ****************************************************************************************************************FirstSecondThirdLabel
    JLabel firstLabel = new JLabel("Dieses Wort zum Filter hinzufuegen: ",0);
    firstLabel.setFont(mainFont);

    tfFirstLabel = new JTextField();
    tfFirstLabel.setFont(mainFont);

    JLabel secondLabel = new JLabel("Dieses Wort aus dem Filter entfernen: ",0);
    secondLabel.setFont(mainFont);

    tfSecondLabel = new JTextField();
    tfSecondLabel.setFont(mainFont);

    JLabel thirdLabel = new JLabel("zu filternder Monat / Jahr",0);
    thirdLabel.setFont(mainFont);

    tfThirdLabel = new JTextField();
    tfThirdLabel.setFont(mainFont);



    //Erstellen eines TextAreas für die Anzeige der Inhalte des Filters**************************************************************************************************** TextArea
    textArea.setText("Hier wird der Filter angezeigt");
    textArea.setOpaque(true);
    textArea.setFont(mainFont);

    textAreaLabel = new JLabel("Ausgabe des Sets Filter", 0);
    textAreaLabel.setFont(mainFont);

    JPanel textAreaPanel = new JPanel();
    textAreaPanel.setLayout(new GridLayout(2,1,50,10));
    textAreaPanel.setOpaque(false);
    textArea.setSize(250,800);
    //textAreaPanel.add(textAreaLabel);
    textAreaPanel.add(textArea);
    

    //Erstellen eines Feldes zum aufrufen einer datei und start knopf fuer das programm*****************************************************************************File Chooser
    JFileChooser chooser = new JFileChooser();
    JLabel temp = new JLabel();
    chooser.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
            String dateiPfad = chooser.getSelectedFile().getPath().toString();
            temp.setText(dateiPfad);
        }
    });

    //Startbutton, der den dateipfad uebergibt und dann das Programm startet********************************************************************************Start Button
    JButton startButton = new JButton("Start");
    startButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            tfWelcomeLabel.setText("0.0");
            objekt.dateipfadLesen(temp.getText());
            tfWelcomeLabel.setText(Float.toString(objekt.betrag));
        }
    });

    textAreaPanel.add(startButton);
    //Button zum setzen des Monats                            *************************************************************************** Monat Button
    JButton monatButton = new JButton("Monat setzen");
    monatButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            String monatButtonText = tfThirdLabel.getText();
            objekt.setzeMonat(monatButtonText);
            tfThirdLabel.setText("");
        }
    });

    //Button zum setzten des Jahres     *************************************************************************************************** Jahr Button
    JButton jahrButton = new JButton("Jahr setzen");
    jahrButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            String jahrButtonText = tfThirdLabel.getText();
            objekt.setzeJahr(jahrButtonText);
            tfThirdLabel.setText("");
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


    //Erstellen des Panels zur Anzeige der oberen Textfields und Labels*******************************************************************************Form Panel
    JPanel formPanel = new JPanel();
    //Tabelle mit 4 Zeilen, einer Reihe und danach folgen die border zwischen den Komponenten
    formPanel.setLayout(new GridLayout(9,1,20,20));
    //transparent machen des FormPanels
    formPanel.setOpaque(false);
    formPanel.add(welcomeLabel);
    formPanel.add(tfWelcomeLabel);
    formPanel.add(firstLabel);
    formPanel.add(tfFirstLabel);
    formPanel.add(secondLabel);
    formPanel.add(tfSecondLabel);
    formPanel.add(thirdLabel);
    formPanel.add(tfThirdLabel);



    //Erstellung eines Button Panels für die Einrichtung in dem Main Panel ******************************************************************Hinzufuegen Button
    //OK button
    JButton buttonOK = new JButton("Hinzufügen");
    buttonOK.setFont(mainFont);
    buttonOK.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            //throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
            String firstTextFieldText = tfFirstLabel.getText();
            objekt.fuegeStringHinzu(firstTextFieldText);
            tfFirstLabel.setText("");
            String tempString = passeTextAn(objekt);
            textArea.setText(tempString);
            
        }
    });

    //Abbruch button*********************************************************************************************************************Lösch Button
    JButton buttonABBRUCH = new JButton("Entfernen");
    buttonABBRUCH.setFont(mainFont);
    buttonABBRUCH.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            String secondTextFieldText = tfSecondLabel.getText();
            objekt.entferneStringAusSet(secondTextFieldText);
            tfSecondLabel.setText("");
            textArea.setText(passeTextAn(objekt));
        }
    });

    JPanel buttonsPanel = new JPanel();
    buttonsPanel.setLayout(new GridLayout(1,2,5,5));
    //transparenz einschalten
    buttonsPanel.setOpaque(false);
    buttonsPanel.add(buttonOK);
    buttonsPanel.add(buttonABBRUCH);



    //Main anzeige *******************************************************************************************************************************Main Anzeige
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

    //Hinzufuegen des Panels TextArea
    textAreaPanel.setBorder(BorderFactory.createEmptyBorder(5,50,5,50));
    mainPanel.add(textAreaPanel, BorderLayout.WEST);
 
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


  //Methode um den Text des TextArea anzupassen
public String passeTextAn(OurFlatUebersetzung objekt){
    String temp = objekt.textArealAktualisieren();
    return temp;
    }
}

