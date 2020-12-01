package socialnetwork.repository.file;

import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.memory.InMemoryRepository;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


///Aceasta clasa implementeaza sablonul de proiectare Template Method; puteti inlucui solutia propusa cu un Factori (vezi mai jos)
public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    String fileName;
    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName=fileName;
        loadData();
    }

    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String linie;
            while((linie=br.readLine())!=null){
                if(linie.isEmpty())
                    continue;
                List<String> attr=Arrays.asList(linie.split(";"));
                E e=extractEntity(attr);
                try{super.save(e);}
                catch (ValidationException ignored){

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //sau cu lambda - curs 4, sem 4 si 5
//        Path path = Paths.get(fileName);
//        try {
//            List<String> lines = Files.readAllLines(path);
//            lines.forEach(linie -> {
//                E entity=extractEntity(Arrays.asList(linie.split(";")));
//                super.save(entity);
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    /**
     *  extract entity  - template method design pattern
     *  creates an entity of type E having a specified list of @code attributes
     * @param attributes
     * @return an entity of type E
     */
    public abstract E extractEntity(List<String> attributes);
    ///Observatie-Sugestie: in locul metodei template extractEntity, puteti avea un factory pr crearea instantelor entity

    protected abstract String createEntityAsString(E entity);

    @Override
    public E save(E entity){
        E e=super.save(entity);
        if (e==null)
        {
            writeToFile(entity);
        }
        return e;
    }

    @Override
    public E delete(ID id) {
        E e = super.delete(id);
        if(e != null) {//In case something was deleted.
            writeAllToFile();
            return null;
        }
        return e;
    }

    @Override
    public E update(E entity) {
        E e = super.update(entity);
        if(e == null){
            writeAllToFile();
            return null;
        }
        return e;
    }

    protected void writeToFile(E entity){
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName,true))) {
            bW.newLine();
            bW.write(createEntityAsString(entity));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected void writeAllToFile(){
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName,false))) {
            for(E e : this.findAll()) {
                bW.write(createEntityAsString(e));
                bW.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

