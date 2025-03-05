package tpgroup.persistence.filesystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.exception.RecordNotFoundException;

public class PointOfInterestDAOFile extends FileDAO<PointOfInterest>{ 

    public PointOfInterestDAOFile(Path baseDir) {
        super(baseDir, "pois");
    }

    @Override
    protected Path getFilePath(String identifier) {
        // Use name and location as composite key
        String[] parts = identifier.split("_");
        return basePath.resolve(parts[0] + "_" + parts[1] + ".json");
    }

    @Override
    public PointOfInterest get(PointOfInterest poi) throws RecordNotFoundException {
        Path filePath = getFilePath(poi.getName() + "_" + poi.getLocation());
        if (!Files.exists(filePath)) {
            throw new RecordNotFoundException();
        }
        try {
            return gson.fromJson(Files.readString(filePath), PointOfInterest.class);
        } catch (IOException e) {
            throw new RuntimeException("Error reading PointOfInterest file", e);
        }
    }

    @Override
    public void save(PointOfInterest poi) {
        Path filePath = getFilePath(poi.getName() + "_" + poi.getLocation());
        try {
            atomicWrite(filePath, gson.toJson(poi));
        } catch (IOException e) {
            throw new RuntimeException("Error saving PointOfInterest", e);
        }
    }

    @Override
    public boolean add(PointOfInterest poi) {
        Path filePath = getFilePath(poi.getName() + "_" + poi.getLocation());
        if (Files.exists(filePath)) {
            return false;
        }
        save(poi);
        return true;
    }

    @Override
    public void delete(PointOfInterest poi) throws RecordNotFoundException {
        Path filePath = getFilePath(poi.getName() + "_" + poi.getLocation());
        try {
            if (!Files.deleteIfExists(filePath)) {
                throw new RecordNotFoundException();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error deleting POI", e);
        }
    }

	@Override
	public List<PointOfInterest> getAll() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getAll'");
	}

	@Override
	public List<PointOfInterest> getFiltered(Predicate<PointOfInterest> filter) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getFiltered'");
	}
}
