/* Koden är tagen från:
 * https://github.com/karlstad-business-school/isgb33-examples/blob/main/isgb33-forelasning-6-exempel/isgb33-eclipse-lab-template/src/Stub.java
*/

import javax.swing.*;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;

public class Gui {
	// databas modellen
    private Mongodb model;
    
    //Konstruktor
    public Gui() throws Exception {
    	model = new Mongodb();
        buildGui();
    }
    // Skapar Gui
    private void buildGui() {
    	// Skapar fönster
        JFrame frame = new JFrame("Sök Film");
        frame.setSize(400, 500);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Område som visar resultatet
        JTextArea area = new JTextArea();
        area.setLineWrap(true);
        area.setEditable(false);
        
        //Scroll för textområdet
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBounds(10, 10, 365, 400);
        
        // textruta för användar input
        JTextField genreField = new JTextField();
        genreField.setBounds(10, 415, 260, 40);
        
        //Sök knapp
        JButton searchButton = new JButton("SÖK");
        searchButton.setBounds(275, 415, 100, 40);

        // ActionListener 
        searchButton.addActionListener(e -> {
        	//hämtar text från genreField och tar bort mellanslag
            String genre = genreField.getText().trim();
            //rensar textrutan
            area.setText("");
            
            //om användare söker utan att fylla i genre
            if (genre.isEmpty()) {
                area.setText("Ange en genre");
                return;
            }
            // Hämtar resultat från databasen
            List<Document> movies = model.findMoviesByGenre(genre);

            // Meddelande om inget resultat finns
            if (movies.isEmpty()) {
                area.setText("Ingen film matchade kategorin");
                return;
            }
            // // Skapar iterator 
            Iterator<Document> iterator = movies.iterator();

            // loopar igenom alla 
            while (iterator.hasNext()) {
                Document myDoc = iterator.next();

                // Skriver ut titel
                area.append("Title: " + myDoc.getString("title") + "\n");
                // Skriver ut år
                area.append("År: " + myDoc.getInteger("year") + "\n");

                // Skriv ut genre som lista
                List<String> genres = myDoc.getList("genres", String.class);
                area.append("Genres: " + genres + "\n");

                // Extra rad mellan filmer
                area.append("\n");
                
            }
        });
        //Lägger till kompontenter i fönstret
        frame.add(scroll);
        frame.add(genreField);
        frame.add(searchButton);
        frame.setVisible(true);
    }
}