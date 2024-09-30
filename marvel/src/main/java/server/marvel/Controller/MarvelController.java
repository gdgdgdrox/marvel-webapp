package server.marvel.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import server.marvel.DTO.MarvelCharacterNameDTO;
import server.marvel.Model.MarvelCharacter;
import server.marvel.Service.MarvelService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class MarvelController{

    @Autowired
    private MarvelService marvelService;

    @GetMapping(value = "/characters", produces = "application/json")
    public ResponseEntity<List<MarvelCharacterNameDTO>> getMarvelCharacterNames(@RequestParam(name="nameStartsWith", required=true) String name){        
        List<MarvelCharacterNameDTO> characterNames = marvelService.getMarvelCharacters(name);
        return ResponseEntity.ok(characterNames);
        
    }

    // @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/character/{characterID}", produces="application/json")
    public ResponseEntity<MarvelCharacter> getMarvelCharacterInfo(@PathVariable(name="characterID") Integer id){
        System.out.println("ID is " + id);
        MarvelCharacter mc = marvelService.getMarvelCharacter(id);
        return ResponseEntity.ok(mc);

    }



}