package FileSystem;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

interface IFile extends Comparable<IFile>{
    String getFileName();
    long getFileSize();
    String getFileInfo(int indent);
    void sortBySize();
    long findLargestFile ();
}
class FileNameExistsException extends Exception{
    public FileNameExistsException(String fileName, String folderName) {
        super((String.format("There is already a file named %s in the folder %s",
                fileName,
                folderName)));
    }
}
class Indentation{
    public static String indentation(int indent){
        return IntStream.range(0, indent)
                .mapToObj(i -> "\t")
                .collect(Collectors.joining());
    }
}
class File implements IFile{
    String name;
    long size;

    public File(String name, long size) {
        this.name = name;
        this.size = size;
    }

    public File(String name){
        this.name = name;
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        return size;
    }

    @Override
    public String getFileInfo(int indent) {
        //Folder name:       test Folder size:      13070
        return String.format("%sFile name: %10s File size: %10d\n",
                Indentation.indentation(indent), getFileName(), getFileSize());
    }

    @Override
    public void sortBySize() {

    }

    @Override
    public long findLargestFile() {
        return this.size;
    }

    @Override
    public int compareTo(IFile iFile) {
        return Long.compare(getFileSize(), iFile.getFileSize());
        //return Long.compare(this.getFileSize(), iFile.getFileSize());
    }
}
class Folder extends File implements IFile{
    List<IFile> files;
    public Folder(String name) {
        super(name);
        files = new ArrayList<>(0);
    }

    public boolean fileExists(String fileName){
        return files.stream().map(IFile::getFileName)
                .anyMatch(name -> name.equals(fileName));
    }
    public void addFile (IFile file) throws FileNameExistsException {

        if(fileExists(file.getFileName())){
            throw new FileNameExistsException(file.getFileName(), this.getFileName());
        }
        files.add(file);
    }
    @Override
    public String getFileName() {
        return this.name;
    }

    @Override
    public long getFileSize() {
        return files.stream().mapToLong(IFile::getFileSize)
                .sum();
    }

    @Override
    public String getFileInfo(int indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%sFolder name: %10s Folder size: %10d\n",
                Indentation.indentation(indent), getFileName(), getFileSize()));

        files.forEach(file -> sb.append(file.getFileInfo(indent+1)));
        return sb.toString();

    }

    @Override
    public void sortBySize() {
        Comparator<IFile> iFileComparator = Comparator.comparingLong(IFile::getFileSize);
        files.sort(iFileComparator);

        files.forEach(IFile::sortBySize);
    }

    @Override
    public long findLargestFile() {
        OptionalLong largestFile = files.stream().mapToLong(IFile::findLargestFile).max();
        if(largestFile.isPresent())
            return largestFile.getAsLong();
        return 0L;
    }
}
class FileSystem{

    Folder root;

    public FileSystem() {
        root = new Folder("root");
    }

    public void addFile(IFile file) throws FileNameExistsException {
        root.addFile(file);
    }

    public void sortBySize() {
        root.sortBySize();
    }

    public long findLargestFile() {
        return root.findLargestFile();
    }

    @Override
    public String toString() {
        return this.root.getFileInfo(0);
    }
}
public class FileSystemTest {

    public static Folder readFolder(Scanner sc) {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < totalFiles; i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String[] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args) {

        //file reading from input

        Scanner sc = new Scanner(System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());

    }
}