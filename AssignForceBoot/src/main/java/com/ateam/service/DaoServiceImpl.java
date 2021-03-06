package com.ateam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ateam.beans.B_Status;
import com.ateam.beans.Batch;
import com.ateam.beans.Curriculum;
import com.ateam.beans.Location;
import com.ateam.beans.R_Status;
import com.ateam.beans.Room;
import com.ateam.beans.Skill;
import com.ateam.beans.Topic;
import com.ateam.beans.Trainer;
import com.ateam.beans.Unavailable;
import com.ateam.dao.BStatusDao;
import com.ateam.dao.BatchDao;
import com.ateam.dao.CurriculumDao;
import com.ateam.dao.LocationDao;
import com.ateam.dao.RStatusDao;
import com.ateam.dao.RoomDao;
import com.ateam.dao.SkillDao;
import com.ateam.dao.TopicDao;
import com.ateam.dao.TrainerDao;
import com.ateam.dao.UnavailableDao;

@Transactional
@Service
public class DaoServiceImpl implements DaoService {
	//Add a few service methods using needed DAO
	@Autowired
	BatchDao BatchDao;
	@Autowired
	BStatusDao BStatusDao;
	@Autowired
	CurriculumDao CurriculumDao;
	@Autowired
	LocationDao LocationDao;
	@Autowired
	RoomDao RoomDao;
	@Autowired
	RStatusDao RStatusDao;
	@Autowired
	SkillDao SkillDao;
	@Autowired
	TopicDao TopicDao;
	@Autowired
	TrainerDao TrainerDao;
	@Autowired
	UnavailableDao UnavailableDao;

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getAllItem(T sample) {

		
		if ((Class<T>) sample.getClass() == Batch.class) {
			return (List<T>) BatchDao.findAll();
		} else if ((Class<T>) sample.getClass() == B_Status.class) {
			return (List<T>) BStatusDao.findAll();
		} else if ((Class<T>) sample.getClass() == Curriculum.class) {
			return (List<T>) CurriculumDao.findAll();
		} else if ((Class<T>) sample.getClass() == Location.class) {
			return (List<T>) LocationDao.findAll();
		} else if ((Class<T>) sample.getClass() == Room.class) {
			return (List<T>) RoomDao.findAll();
		} else if ((Class<T>) sample.getClass() == R_Status.class) {
			return (List<T>) RStatusDao.findAll();
		} else if ((Class<T>) sample.getClass() == Skill.class) {
			return (List<T>) SkillDao.findAll();
		} else if ((Class<T>) sample.getClass() == Topic.class) {
			return (List<T>) TopicDao.findAll();
		} else if ((Class<T>) sample.getClass() == Trainer.class) {
			System.out.println("trainer if");
			return (List<T>) TrainerDao.findAll();
		} else if ((Class<T>) sample.getClass() == Unavailable.class) {
			return (List<T>) UnavailableDao.findAll();
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T saveItem(T sample) {
		if ((Class<T>) sample.getClass() == Batch.class) {
			return (T) BatchDao.save((Batch) sample);
		} else if ((Class<T>) sample.getClass() == B_Status.class) {
			return (T) BStatusDao.save((B_Status) sample);
		} else if ((Class<T>) sample.getClass() == Curriculum.class) {
			return (T) CurriculumDao.save((Curriculum) sample);
		} else if ((Class<T>) sample.getClass() == Location.class) {
			return (T) checksLocationBeforeSave((Location) sample);
		} else if ((Class<T>) sample.getClass() == Room.class) {
			return (T) RoomDao.save((Room) sample);
		} else if ((Class<T>) sample.getClass() == R_Status.class) {
			return (T) RStatusDao.save((R_Status) sample);
		} else if ((Class<T>) sample.getClass() == Skill.class) {
			// return (T) SkillDao.save((Skill) sample);
			return (T) checksSkillBeforeSave((Skill) sample);
		} else if ((Class<T>) sample.getClass() == Topic.class) {
			return (T) TopicDao.save((Topic) sample);
		} else if ((Class<T>) sample.getClass() == Trainer.class) {
			System.out.println("trainer if: save");
			// return (T) TrainerDao.save((Trainer) sample);
			return (T) checksTrainerBeforeSave((Trainer) sample);
		} else if ((Class<T>) sample.getClass() == Unavailable.class) {
//			return (T) UnavailableDao.save((Unavailable) sample);
			return (T) checksUnavailableBeforeSave((Unavailable) sample);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void delete(T sample) {
		if ((Class<T>) sample.getClass() == Batch.class) {
			BatchDao.delete((Batch) sample);
		} else if ((Class<T>) sample.getClass() == B_Status.class) {
			BStatusDao.delete((B_Status) sample);
		} else if ((Class<T>) sample.getClass() == Curriculum.class) {
			CurriculumDao.delete((Curriculum) sample);
		} else if ((Class<T>) sample.getClass() == Location.class) {
			LocationDao.delete((Location) sample);
		} else if ((Class<T>) sample.getClass() == Room.class) {
			RoomDao.delete((Room) sample);
		} else if ((Class<T>) sample.getClass() == R_Status.class) {
			RStatusDao.delete((R_Status) sample);
		} else if ((Class<T>) sample.getClass() == Skill.class) {
			SkillDao.delete((Skill) sample);
		} else if ((Class<T>) sample.getClass() == Topic.class) {
			TopicDao.delete((Topic) sample);
		} else if ((Class<T>) sample.getClass() == Trainer.class) {
			TrainerDao.delete((Trainer) sample);
		} else if ((Class<T>) sample.getClass() == Unavailable.class) {
			UnavailableDao.delete((Unavailable) sample);
		}

	}

	private Trainer checksTrainerBeforeSave(Trainer trainer) {
		List<Trainer> elems = TrainerDao.findAll();
		for (Trainer elem : elems) {
			if (elem.getTrainerFirstName().equals(trainer.getTrainerFirstName())
					&& elem.getTrainerLastName().equals(trainer.getTrainerLastName())) {
				trainer.setTrainerID(elem.getTrainerID());
				trainer.setTrainerLocationID(checkLocation(trainer.getTrainerLocationID()));
				for (Skill sk : trainer.getSkill()) {
					trainer.getSkill().set(trainer.getSkill().indexOf(sk), checkSkill(sk));
				} // updates skills accordingly

				for (Unavailable away : trainer.getUnavailable()) {
					trainer.getUnavailable().set(trainer.getUnavailable().indexOf(away), checkUnavailable(away));
				} // updates unavailable accordingly

				// TODO
			} // if the name is the same, change the value
		}
		return TrainerDao.save((Trainer) trainer);
	}


	//Check loc data to the data within the database before
	//entering the data into the database
	private Location checksLocationBeforeSave(Location loc) {
		loc = checkLocation(loc);
		return LocationDao.save((Location) loc);
	}

	//If the location data matches the data in the Database
	//Just set the id to the original data
	private Location checkLocation(Location loc) {
		List<Location> locs = LocationDao.findAll();
		for (Location location : locs) {
			if (location.getLocationName().equals(loc.getLocationName())) {
				loc.setLocationID(location.getLocationID());
			} // if the name is the same, change the value
		}
		return loc;
	}

	
	//Same concept as location
	private Skill checksSkillBeforeSave(Skill skill) {
		skill = checkSkill(skill);
		return SkillDao.save((Skill) skill);
	}

	private Skill checkSkill(Skill skill) {
		List<Skill> skills = SkillDao.findAll();
		for (Skill sk : skills) {
			if (sk.getSkillName().equals(skill.getSkillName())) {
				skill.setSkillID(sk.getSkillID());
			} // if the name is the same, change the value
		}
		return skill;
	}

	//Same concept as location
	private Unavailable checksUnavailableBeforeSave(Unavailable away) {
		away = checkUnavailable(away);
		return UnavailableDao.save(away);
	}

	private Unavailable checkUnavailable(Unavailable away) {
		List<Unavailable> uv = UnavailableDao.findAll();
		for (Unavailable u : uv) {
			if (u.getUnavailableStartDate().equals(away.getUnavailableStartDate())
					&& u.getUnavailableEndDate().equals(away.getUnavailableEndDate())) {
				away.setUnavailableID(u.getUnavailableID());
			} // if the name is the same, change the value
		}
		return away;
	}

	//Gets a trainer object by ID
	@Override
	public Trainer findTrainerById(int id){
		Trainer trainer = TrainerDao.findByTrainerID(id);
	
		return trainer;
	}
	
	//Retrieves all trainers
	@Override
	public List<Trainer> findAllTrainers(){
		List<Trainer> trainers = TrainerDao.findAll();
		
		return trainers;
	}

	//Retrieves all batch data
	public List<Batch> findAllBatches(){
		List<Batch> batches = BatchDao.findAll();
		
		
		return batches;
	}
	
	//Retrieves all Room data
	@Override
	public List<Room> getAllRooms() {
		List<Room> rooms = RoomDao.findAll();
		
		for (Room room : rooms) {

			if(room.getLocationID() != null){
				room.setLocationID(null);
			}

			if(room.getRoomStatusID() != null){
				room.setRoomStatusID(null);
			}// set redundant to null
			
			if(room.getUnavailable() != null){
				room.setUnavailable(null);
			}
		}
		
		return rooms;
		
	}

	//Retrieves all Curriculum data
	@Override
	public List<Curriculum> getAllCurriculums() {
		List<Curriculum> cs = CurriculumDao.findAll();
		
		for (Curriculum c : cs) {

			if(c.getSkill() != null){
				c.setSkill(null);
			}

		}
		
		return cs;
		
	}

	//Finds a trainer by their first name
	public Trainer findByTrainerFirstName(String trainerFirstName){
		Trainer t = TrainerDao.findByTrainerFirstName(trainerFirstName);

		return t;
	}

	//Finds a room by room name
	public Room findRoomByRoomName(String roomName){
		return RoomDao.findRoomByRoomName(roomName);
	}
	
	//Finds a Curriculum by name
	public Curriculum findCurriculumByCurriculumName(String curriculumName){
		return CurriculumDao.findCurriculumByCurriculumName(curriculumName);
	}
	
	//Finds a topic name
	public Topic findTopicByTopicName(String topicName){
		return TopicDao.findTopicByTopicName(topicName);
	}
	
	//Finds a batch by ID
	@Override
	public Batch findByBatchID(int BatchID){
		return BatchDao.findByBatchID(BatchID);
	}

	//Get a sorted order of Batch start date data in
	//descending data
	@Override
	public List<Batch> findAllBatchStartDate() {
		List<Batch> b = BatchDao.findAllByOrderByBatchStartDateDesc();
		System.out.println("ordered Batch List");
		System.out.println(b);
		return b;
	}

	
}
