package pl.pwr.ite.dynak.requester.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.pwr.ite.dynak.requester.enums.RequestType;
import pl.pwr.ite.dynak.requester.interfaces.IRequester;
import pl.pwr.ite.dynak.requester.models.CharacterData;
import pl.pwr.ite.dynak.requester.models.PlanetData;
import pl.pwr.ite.dynak.requester.models.StarshipData;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

import static pl.pwr.ite.dynak.requester.util.RequestBuilder.buildInfoURL;

public class Requester implements IRequester {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Random random = new Random();

    @Override
    public StarshipData getRandomStarshipData() {
        int randomId = random.nextInt(RequestBuilder.MAX_ID);

        StarshipData starshipData = null;
        URL url = null;

        while (starshipData == null) {
            url = buildInfoURL(randomId, RequestType.STARSHIPS);
            try {
                starshipData = mapper.readValue(url, StarshipData.class);
            } catch (IOException e) {
                randomId = random.nextInt(RequestBuilder.MAX_ID);
            }
        }

        return starshipData;
    }

    @Override
    public PlanetData getRandomPlanetData() {
        int randomId = random.nextInt(RequestBuilder.MAX_ID);

        PlanetData planetData = null;
        URL url = null;

        while (planetData == null) {
            url = buildInfoURL(randomId, RequestType.PLANETS);
            try {
                planetData = mapper.readValue(url, PlanetData.class);
            } catch (IOException e) {
                randomId = random.nextInt(RequestBuilder.MAX_ID);
            }
        }

        return planetData;
    }

    @Override
    public CharacterData getRandomCharacterData() {
        int randomId = random.nextInt(RequestBuilder.MAX_ID);

        CharacterData characterData = null;
        URL url = null;

        while (characterData == null) {
            url = buildInfoURL(randomId, RequestType.PEOPLE);
            try {
                characterData = mapper.readValue(url, CharacterData.class);
            } catch (IOException e) {
                randomId = random.nextInt(RequestBuilder.MAX_ID);
            }
        }

        return characterData;
    }
}
