import java.util.*;
import java.io.*;
import java.util.stream.*;

public class EnercoopTest {

    /**
     * Les 5 IDs les plus fréquents.
     * @param dir Le répertoire contenant les fichiers logs.
     * @return List<Integer> Les 5 IDs les plus fréquents.
     */
    public static List<Integer> fiveMostFrequentIds(String dir) throws IOException {
        // Dictionnaire pour compter les occurrences des IDs
        Map<Integer, Integer> idCountMap = new HashMap<>();

        // Parcours de tous les fichiers du répertoire
        for (String absolutePath : listAbsolutePath(dir)) {
            try (BufferedReader br = new BufferedReader(new FileReader(absolutePath))) {
                String ligne;
                while ((ligne = br.readLine()) != null) {
                    // Extraction de l'ID à partir de la ligne
                    String[] parts = ligne.split("id=");
                    if (parts.length > 1) {
                        String idPart = parts[1].split("&")[0];  // On prend la première partie avant "&"
                        try {
                            int id = Integer.parseInt(idPart);
                            idCountMap.put(id, idCountMap.getOrDefault(id, 0) + 1);
                        } catch (NumberFormatException e) {
                            // Si l'ID n'est pas valide (non entier), on l'ignore.
                        }
                    }
                }
            }
        }

        // Trier les IDs par fréquence décroissante et prendre les 5 premiers
        return idCountMap.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Obtenir la liste des chemins absolus de tous les fichiers dans un répertoire.
     * @param dir Le répertoire contenant les fichiers logs.
     * @return Set<String> Liste des chemins absolus des fichiers.
     */
    private static Set<String> listAbsolutePath(String dir) {
        File folder = new File(dir);
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("Le répertoire spécifié n'existe pas ou n'est pas valide.");
        }
        return Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getAbsolutePath)
                .collect(Collectors.toSet());
    }

    // Classe main pour tester notre programme
    public static void main(String[] args) {
        try {
            List<Integer> frequentIds = fiveMostFrequentIds("logs/");
            System.out.println("Les 5 IDs les plus fréquents : " + frequentIds);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

