package tpgroup.persistence;
 
import java.util.List;
import tpgroup.model.exception.RecordNotFoundException;

public interface DAO<T> {

	public List<T> getAll();

	public T get(T obj) throws RecordNotFoundException;

	public void save(T obj);

	public boolean add(T obj);

	public void delete(T obj) throws RecordNotFoundException;
}

	// @Override
	// public boolean cascadeEvent(Event event, Map<String, ?> data){
	// 	switch(event){
	// 		case DELETE -> {
	// 			for(Room room: getAll()){
	// 				if(!data.containsKey("user"))
	// 					return false;

	// 				if(room.getMembers().contains(data.get("user"))){
	// 					room.getMembers().remove(data.get("user"));
	// 				}
	// 			}
	// 		}
	// 		case UPDATE -> {
	// 			for(Room room: getAll()){
	// 		}
	// 		default -> {}
	// 	}
	// 	return true;
	// }
	
