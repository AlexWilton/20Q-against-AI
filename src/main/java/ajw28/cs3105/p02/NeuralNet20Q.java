package ajw28.cs3105.p02;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class NeuralNet20Q {
    public static final String DEFAULT_Q_FILE_PATH = "20q_data.csv";

    private ArrayList<Question> questions = new ArrayList<Question>();
    private ArrayList<Concept> concepts = new ArrayList<Concept>();

    public NeuralNet20Q(){
        this(DEFAULT_Q_FILE_PATH);
    }

    public NeuralNet20Q(String q_file_path){
        readInQuestionsAndConceptsFromCSV(q_file_path);
    }

    private void readInQuestionsAndConceptsFromCSV(String q_file_path) {
        String line;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(q_file_path));

            //parse header
            if ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                for(int conceptNum=0; conceptNum < columns.length -2; conceptNum++) {
                    concepts.add(new Concept(columns[conceptNum + 2]));
                }
            }

            //parse question data
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");

                //parse question
                int questionId = Integer.parseInt(columns[0]);
                Question newQuestion = new Question(questionId, columns[1]);
                questions.add(newQuestion);

                //parse question answers
                for(int conceptNum=0; conceptNum < columns.length -2; conceptNum++) {
                    int columnOfConceptAnswer = conceptNum + 2;
                    boolean answer = false;
                    if(columns[columnOfConceptAnswer].equals("1")) answer = true;
                    newQuestion.addResponseForConcept(concepts.get(conceptNum), answer);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error! Couldn't find file: " + q_file_path);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
