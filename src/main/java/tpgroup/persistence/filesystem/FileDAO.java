package tpgroup.persistence.filesystem;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import tpgroup.model.exception.InvalidPathException;
import tpgroup.persistence.DAO;

public abstract class FileDAO<T> implements DAO<T> {
	protected final File file;
	protected final Gson gson;
	protected final Type collectionType;

	protected FileDAO(String filePath, GsonBuilder config) {
		this.file = new File(filePath);
		this.gson = config.setPrettyPrinting().create();
		this.collectionType = new TypeToken<List<T>>() {}.getType();
		if(!file.exists()){
			try(Writer wr = new FileWriter(file)){
				wr.write("");
			}catch(IOException e){
				throw new InvalidPathException();
			}

		}
	}

	protected List<T> readAll() {
		try (Reader reader = new FileReader(file)) {
			List<T> list = gson.fromJson(reader, collectionType);
			return list != null ? list : new ArrayList<>();
		} catch (IOException e) {
			throw new InvalidPathException();
		}
	}

	protected void writeAll(List<T> list) {
		try (Writer writer = new FileWriter(file)) {
			gson.toJson(list, collectionType, writer);
		} catch (IOException e) {
			throw new InvalidPathException();
		}
	}
}
