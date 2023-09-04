package QuizProcessor;

import java.io.InputStream;
import java.util.*;

class DifferentNumberOfAnswersException extends Exception {
    public DifferentNumberOfAnswersException() {
        super("A quiz must have same number of correct and selected answers");
    }
}

class Student {
    String id;
    String[] answers;
    String[] correctAnswers;

    public Student(String id, String[] answers, String[] correctAnswers) {
        this.id = id;
        this.answers = answers;
        this.correctAnswers = correctAnswers;
    }

    double result() throws DifferentNumberOfAnswersException {
        double result = 0;
        if (answers.length != correctAnswers.length)
            throw new DifferentNumberOfAnswersException();
        for (int i = 0; i < answers.length; i++) {
            if (answers[i].equals(correctAnswers[i])) {
                result += 1;
            } else {
                result -= 0.25;
            }
        }
        return result;
    }
}

class QuizProcessor {
    static Map<String, Double> processAnswers(InputStream is) {
        Scanner scanner = new Scanner(is);
        Map<String, Double> studentResults = new LinkedHashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String id = parts[0];

            String[] answers = parts[1].split(",");
            String[] correctAnswers = parts[2].split(",");
            Student student = new Student(id, answers, correctAnswers);
            double result;
            try {
                result = student.result();
                studentResults.put(id, result);
            } catch (DifferentNumberOfAnswersException e) {
                System.out.println(e.getMessage());
            }
        }
        return studentResults;

    }
}

public class QuizProcessorTest {
    public static void main(String[] args) {
        QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));
    }
}