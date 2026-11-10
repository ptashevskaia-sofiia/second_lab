package storage;

import entity.Table;

import java.io.*;
import java.nio.file.*;

public class FileStorage {

    private final Path dataDir;

    public FileStorage(String dirPath) throws IOException{
        this.dataDir=Paths.get(dirPath);
        Files.createDirectories(dataDir);
    }

    public void save(Table table) throws IOException{
        Path target=dataDir.resolve(table.getName()+".dat");
        Path tmp=dataDir.resolve(table.getName()+".dat.tmp");

        try(ObjectOutputStream oos=new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(tmp)))){
            oos.writeObject(table);
        }

        try{
            Files.move(tmp,target,StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch(AtomicMoveNotSupportedException e){
            Files.move(tmp, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public Table load(String tableName) throws IOException, ClassNotFoundException{
        Path file=dataDir.resolve(tableName+".dat");
        if (!Files.exists(file)){
            return null;
        }

        try (ObjectInputStream ois=new ObjectInputStream(new BufferedInputStream(Files.newInputStream(file)))){
            return (Table) ois.readObject();       
        }
    }

    public Path getDataDir(){
        return dataDir;
    }
}
