package server.marvel.Service;

import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import server.marvel.DTO.MarvelCharacterNameDTO;
import server.marvel.Model.MarvelCharacter;
import server.marvel.Repository.MarvelCharacterRepository;

@Service
public class MarvelService {
    
    @Value("${marvel.api.characters}")
    private String marvelCharactersAPI;

    @Value("${marvel.api.character.info}")
    private String marvelCharacterInfoAPI;

    @Value("${marvel.public.api.key}")
    private String publicApikey;

    @Value("${marvel.private.api.key}")
    private String privateApiKey;



    @Autowired
    private MarvelCharacterRepository repo;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<MarvelCharacterNameDTO> getMarvelCharacters(String nameStartsWith){
        String fullURL = UriComponentsBuilder.fromUriString(marvelCharactersAPI)
                .queryParam("nameStartsWith", nameStartsWith)
                .queryParam("apikey", publicApikey)
                .queryParam("ts",   System.currentTimeMillis())
                .queryParam("hash", generateHash())
                .toUriString();

        String response = restTemplate.getForObject(fullURL, String.class);
        List<MarvelCharacterNameDTO> characterNames = mapResponseToDTO(response);
        System.out.println("size is " + characterNames.size());
        return characterNames;
    }

    private List<MarvelCharacterNameDTO> mapResponseToDTO(String response){
        List<MarvelCharacterNameDTO> characterNames = new LinkedList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode arrayNode = rootNode.get("data").get("results");
            for (JsonNode node : arrayNode){
                int id = node.get("id").asInt();
                String name = node.get("name").asText();
                MarvelCharacterNameDTO dto = new MarvelCharacterNameDTO(id,name);
                characterNames.add(dto);

            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return characterNames;
    }

    private String generateHash(){
        Long ts = System.currentTimeMillis();
        String signature = "%d%s%s".formatted(ts, privateApiKey, publicApikey);
        String hash = "";

        try {
            // Message digest = md5, sha1, sha512
            // Get an instance of MD5
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            // Calculate our hash
            // Update our message digest
            md5.update(signature.getBytes());
            // Get the MD5 digest
            byte[] h = md5.digest();
            // Stringify the MD5 digest
            hash = HexFormat.of().formatHex(h);
        } catch (Exception ex) { }
        return hash;
    }

    private Optional<MarvelCharacter> findById(Integer id){
        return repo.findById(id);
    }

    public MarvelCharacter getMarvelCharacter(Integer id){
        Optional<MarvelCharacter> optChar = findById(id);
        if (optChar.isPresent()){
            MarvelCharacter mc = optChar.get();
            System.out.printf("Found %s in database",mc.getName());
            return optChar.get();
        }
        else{
            MarvelCharacter mc = getMarvelCharacterFromAPI(id);
            repo.save(mc);
            return mc;
        }
    }

    private MarvelCharacter getMarvelCharacterFromAPI(Integer id){
        System.out.println("Getting character info from Marvel API..");
        String fullURL = UriComponentsBuilder.fromUriString(marvelCharacterInfoAPI).pathSegment(id.toString())
        .queryParam("apikey", publicApikey)
        .queryParam("ts",   System.currentTimeMillis())
        .queryParam("hash", generateHash())
        .toUriString();

        String response = restTemplate.getForObject(fullURL, String.class);
        MarvelCharacter mc = mapResponseToMarvelCharacter(response);
        return mc;
    }

    private MarvelCharacter mapResponseToMarvelCharacter(String response){
        MarvelCharacter marvelCharacter = new MarvelCharacter();
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode marvelCharacterInfoNode = rootNode.get("data").get("results").get(0);
            marvelCharacter.setId(marvelCharacterInfoNode.get("id").asInt());
            marvelCharacter.setName(marvelCharacterInfoNode.get("name").asText());
            marvelCharacter.setDescription(marvelCharacterInfoNode.get("description").asText());
            String thumbnail = marvelCharacterInfoNode.get("thumbnail").get("path").asText();
            String extension = marvelCharacterInfoNode.get("thumbnail").get("extension").asText();
            marvelCharacter.setImageURL(String.format("%s.%s",thumbnail,extension));
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
            return marvelCharacter;
    }
}
