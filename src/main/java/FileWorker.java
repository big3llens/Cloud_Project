import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

public class FileWorker {
    static Path rootPath = Path.of("Server");
    static Path currentPath = rootPath;

    public static String changeDirectory(String dir) throws IOException {
        Path newDir = Path.of(dir);
        if (!Files.exists(newDir)) {
            System.out.println("Такой директории не существует");
            return rootPath.toString();
        }
        StringBuilder sb = new StringBuilder();
        if (!Files.isDirectory(newDir)) {
            for (int i = 0; i < newDir.getNameCount() - 1; i++) {
                sb.append(newDir.getName(i) + "/");
            }
            currentPath = Path.of(sb.toString());
            return sb.toString();
        }
        currentPath = Path.of(newDir.toString());
        return newDir.toString();
    }

    public static void touch(String name) throws IOException {
        if (!Files.exists(Path.of(currentPath.toString(), name + ".txt"))) {
            Files.createFile(Path.of(currentPath.toString(), name + ".txt"));
        } else System.out.println("Такой файл уже существует");
    }

    public static void makedirectory(String name) throws IOException {
        if (!Files.exists(Path.of(currentPath.toString() + "/" + name))) {
            Files.createDirectory(Path.of(currentPath.toString() + "/" + name));
        } else System.out.println("Такая директория уже существует");
    }

    public static String remove(String name) throws IOException {
        List<String> filesList = Arrays.asList(currentPath.toFile().list());
        if (filesList == null) {
            return "В данной директории нет файлов";
        }
        for (String file : filesList) {
            if (file.equals(name)) {
                Files.delete(Path.of(currentPath.toString(), file));
                return String.format("Файл %s успешно удален", name);
            }
        }
        return "В данной директории нет такого файла";
    }

    public static void cope(String src, String target) throws IOException {
        Files.copy(Path.of(currentPath.toString(), src), Path.of(target), StandardCopyOption.REPLACE_EXISTING);
    }

    public static String cat(String name) throws IOException {
        if (Files.exists(Path.of(currentPath.toString(), name))) {
            if (Files.readString(Path.of(currentPath.toString(), name)) == null) return "Файл пустой";
            return Files.readString(Path.of(currentPath.toString(), name));
        }
        return "В данной директории нет такого файла";
    }
}