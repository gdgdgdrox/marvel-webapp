package server.marvel.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import server.marvel.Model.MarvelCharacter;

@Repository
public interface MarvelCharacterRepository extends CrudRepository<MarvelCharacter,Integer>{

    
    
}
