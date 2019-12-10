package bootstrap;

import java.util.*;

public class DistanceTest {

    public double computeCosineDistance(String sentenceA, String sentenceB) {
        String cleanedSentenceA = cleanString(sentenceA);
        String cleanedSentenceB = cleanString(sentenceB);

        String[] uniqueWords = getUniqueWords(cleanedSentenceA, cleanedSentenceB);
        Map<String, Integer> wordVectorA = convertToVector(cleanedSentenceA);
        Map<String, Integer> wordVectorB = convertToVector(cleanedSentenceB);

        double magnitudeA = computeMagnitude(wordVectorA);
        double magnitudeB = computeMagnitude(wordVectorB);

        double dotProduct = computeDotProduct(wordVectorA, wordVectorB, uniqueWords);
        double cosineDist = dotProduct / (magnitudeA * magnitudeB);

        return cosineDist;
    }

    private double computeDotProduct(Map<String, Integer> vectorA, Map<String, Integer> vectorB, String[] coeficients) {
        double dotProduct = 0.0d;
        for (int i = 0; i < coeficients.length; i++) {
            double coefA = (vectorA.containsKey(coeficients[i])) ? vectorA.get(coeficients[i]) : 0.0d;
            double coefB = (vectorB.containsKey(coeficients[i])) ? vectorB.get(coeficients[i]) : 0.0d;
            dotProduct += coefA * coefB;
        }

        return dotProduct;
    }

    private double computeMagnitude(Map<String, Integer> wordVector) {
        double sum = 0.0d;
        for (int i = 0; i < wordVector.size(); i++) {
            sum += wordVector.get(i) * wordVector.get(i);
        }

        return Math.sqrt(sum);
    }

    private String cleanString(String sentence) {
        return sentence.toLowerCase().replaceAll(",", "").replaceAll(".", "");
    }

    private Map<String, Integer> convertToVector(String sentence) {
        Map<String, Integer> wordVector = new HashMap<>();
        String[] words = sentence.split(" ");
        for (String word : words) {
            if (wordVector.containsKey(word)) {
                int count = wordVector.get(word);
                wordVector.put(word, ++count);
            } else {
                wordVector.put(word, 1);
            }
        }
        return wordVector;
    }

    private String[] getUniqueWords(String a, String b) {
        Set<String> uniqueWords = new HashSet<>();
        uniqueWords.addAll(Arrays.asList(a.split(" ")));
        uniqueWords.addAll(Arrays.asList(b.split(" ")));

        String[] uniqueWordsArr = (String[]) uniqueWords.toArray();
        return uniqueWordsArr;
    }
}
